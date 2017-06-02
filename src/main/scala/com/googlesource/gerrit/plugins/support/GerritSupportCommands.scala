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

import com.google.gson.{Gson, JsonElement, JsonObject}
import com.google.inject._
import org.slf4j.LoggerFactory

import scala.util.Try

// allows returning a pure Json result or a textual file content
case class CommandResult(entryName: String, content: ResultType)

trait ResultType
case class JsonResult(x:JsonElement) extends ResultType
case class TextResult(x:String) extends ResultType

abstract class GerritSupportCommand {
  val log = LoggerFactory.getLogger(classOf[GerritSupportCommand])

  // simplifies the return from commands with automatic wrapping in
  // Json/TextResult/Seq
  implicit def convertAny2JsonElement(x:Any):JsonResult = JsonResult(gson.toJsonTree(x))
  implicit def convertString2TextResult(x:String) = TextResult(x)
  implicit def convertCommandResult2Seq(x:CommandResult) = Seq(x)

  implicit val gson = new Gson
  val name = camelToUnderscores(this.getClass.getSimpleName.stripSuffix("Command"))
    .stripPrefix("_")

  val nameJson = s"$name.json"

  def getResults: Seq[CommandResult]

  def execute: Seq[CommandResult] = {
    Try {
      getResults
    } getOrElse {
      val error = s"${name} not available on ${System.getProperty("os.name")}"
      log.error(error);
      CommandResult(name, ErrorInfo("error" -> error))
    }
  }

  private def camelToUnderscores(name: String) = "[A-Z\\d]".r.replaceAllIn(name, { m =>
    "_" + m.group(0).toLowerCase()
  })
}

@Singleton
class GerritSupportCommandFactory @Inject()(val injector: Injector) {

  def apply(name: String): GerritSupportCommand =
    injector.getInstance(
      Class.forName(s"com.googlesource.gerrit.plugins.support.commands.${name.capitalize}Command")
        .asInstanceOf[Class[_ <: GerritSupportCommand]])

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