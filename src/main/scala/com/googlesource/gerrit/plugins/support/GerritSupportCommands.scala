/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlesource.gerrit.plugins.support

import java.io.File
import java.nio.file.Files

import com.google.gerrit.common.Version
import com.google.gerrit.server.config.SitePaths
import com.google.gson.{Gson, JsonElement, JsonObject, JsonPrimitive}
import com.google.inject._
import org.jutils.jhardware.HardwareInfo.{getMemoryInfo, getProcessorInfo}

import scala.util.Try

case class CommandResult(entryName: String, content: JsonElement)

trait GerritSupportCommand {
  def execute: CommandResult
}

@Singleton
class GerritSupportCommandFactory @Inject()(val injector: Injector) {
  def apply(name: String): GerritSupportCommand =
    injector.getInstance(
      Class.forName(s"com.googlesource.gerrit.plugins.support.${name.capitalize}Command")
        .asInstanceOf[Class[_ <: GerritSupportCommand]])
}

class GerritVersionCommand extends GerritSupportCommand {
  def execute = CommandResult("version.json", new JsonPrimitive(Version.getVersion))
}

class CpuInfoCommand extends GerritSupportCommand {
  implicit val gson = new Gson

  def execute = CommandResult("cpu-info.json",
    gson.toJsonTree(
      Try {
        getProcessorInfo
      } getOrElse {
        ErrorInfo("error" -> s"CPU info not available on ${System.getProperty("os.name")}")
      }))
}

class MemInfoCommand extends GerritSupportCommand {
  implicit val gson = new Gson

  def execute = CommandResult("mem-info.json",
    gson.toJsonTree(
      Try {
        getMemoryInfo
      } getOrElse {
        ErrorInfo("error" -> s"Memory info not available on ${System.getProperty("os.name")}")
      }))
}

class DiskInfoCommand @Inject() (sitePaths:SitePaths) extends GerritSupportCommand {
  implicit val gson = new Gson
  case class DiskInfoBean(path: String, diskFree: Long, diskUsable: Long, diskTotal: Long)

  def execute = {
    val path = sitePaths.site_path
    val store = Files.getFileStore(path)
    CommandResult("disk-info.json",
      gson.toJsonTree(DiskInfoBean(path.toString, store.getUnallocatedSpace,
        store.getUsableSpace, store.getTotalSpace))
    )
  }

}
object ErrorInfo {
  def apply[T](attributes: (String, T)*)(implicit gson: Gson): JsonObject =
    attributes.foldLeft(new JsonObject) {
      (json, pair) => {
        json.add(pair._1, gson.toJsonTree(pair._2))
        json
      }
    }
}