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

import java.io.PrintWriter
import java.nio.file.Paths

import com.google.gerrit.server.plugins.{ListPlugins, PluginsCollection}
import com.google.gson.JsonObject
import com.googlesource.gerrit.plugins.support.GerritSupportCommand._
import com.googlesource.gerrit.plugins.support.commands._
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.scalatest.{FlatSpec, Matchers}

class GerritSupportTest extends FlatSpec with Matchers
  with JsonMatcher with TmpPath {

  "version command" should "return a non-empty version string" in {
    val Seq(CommandResult(name, TextResult(version))) = new GerritVersionCommand().execute

    name.value should be ("gerrit_version")
    version should not be empty
  }

  "cpu-info command" should "return a json object with some fields" in {
    val Seq(CommandResult(name, JsonResult(cpuInfo))) = new CpuInfoCommand().execute

    name.value should be ("cpu_info")
    cpuInfo should not be null
    cpuInfo.getAsJsonObject should haveValidFields
  }

  "mem-info command" should "return a json object with some fields" in {
    val Seq(CommandResult(name, JsonResult(memInfo))) = new MemInfoCommand().execute

    name.value should be ("mem_info")
    memInfo should not be null
    memInfo.getAsJsonObject should haveValidFields
  }

  "disk-info command" should "return a json object with some fields" in {
    val wrapper = Mockito.mock(classOf[SitePathsWrapper])
    Mockito.when(wrapper.getAsPath(any[String])).thenReturn(Paths.get("/tmp"))

    val Seq(CommandResult(name, JsonResult(diskInfo))) = new DiskInfoCommand(wrapper).execute

    name.value should be ("disk_info")
    diskInfo should not be null
    diskInfo.getAsJsonObject should haveValidFields
  }

  "config-info command" should "return some binary content" in {
    val wrapper = Mockito.mock(classOf[SitePathsWrapper])
    Mockito.when(wrapper.getAsPath(any[String])).thenReturn(tmpPath.toPath)
    tmpConfigFile
    val commands = new ConfigInfoCommand(wrapper).execute
    commands should not be empty
    val BinResult(content) = commands.head.content

    content should not be empty
  }

  "plugins-info command" should "return some data" in {
    // returns a valid tmp path for any path requested
    val mockedWrapper = {
      val mock = Mockito.mock(classOf[SitePathsWrapper])
      Mockito.when(mock.getAsPath(any[String])).thenReturn(tmpPath.toPath)
      mock
    }

    // returns a JsonObject for any calls
    val mockedPluginsCollection = {
      val mockedListPlugins = {
        val mock = Mockito.mock(classOf[ListPlugins])
        Mockito.when(mock.display(any[PrintWriter]))
          .thenReturn(new JsonObject)
        mock
      }

      val mock = Mockito.mock(classOf[PluginsCollection])
      Mockito.when(mock.list()).thenReturn(mockedListPlugins)
      mock
    }

    // assert we returns three entries for plugins_dir, lib_dir and
    // plugins_version
    val Seq(plugins_dir,lib_dir,plugins_versions) =
      new PluginsInfoCommand(
      mockedWrapper,
      mockedPluginsCollection).execute

    plugins_dir.entryName.value should be("plugins_dir")
    val JsonResult(plugins_dir_result) = plugins_dir.content
    plugins_dir_result.getAsJsonArray.size should not be 0

    lib_dir.entryName.value should be("lib_dir")
    val JsonResult(lib_dir_result) = lib_dir.content
    lib_dir_result.getAsJsonArray.size should not be 0

    plugins_versions.entryName.value should be("plugins_versions")
    val TextResult(plugins_versions_result) = plugins_versions.content

  }

  private def checkValidJsonArray(result: CommandResult, name: String) = {
    result.entryName should be(name)
    val JsonResult(jsonArray) = result.content
    jsonArray.isJsonArray should be(true)
  }
  private def checkValidTextResult(result: CommandResult, name: String) = {
    result.entryName should be(name)
    val TextResult(text) = result.content
    text should not be null
  }

}

