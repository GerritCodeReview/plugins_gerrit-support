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

import java.nio.file.Paths

import com.googlesource.gerrit.plugins.support.commands._
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.scalatest.{FlatSpec, Matchers}

class GerritSupportTest extends FlatSpec with Matchers
  with JsonMatcher with TmpPath {

  "version command" should "return a non-empty version string" in {
    val Seq(CommandResult(name, TextResult(version))) = new GerritVersionCommand().execute

    version should not be null
  }

  "cpu-info command" should "return a json object with some fields" in {
    val Seq(CommandResult(name, JsonResult(cpuInfo))) = new CpuInfoCommand().execute

    cpuInfo should not be null
    cpuInfo.getAsJsonObject should haveValidFields
  }

  "mem-info command" should "return a json object with some fields" in {
    val Seq(CommandResult(name, JsonResult(memInfo))) = new MemInfoCommand().execute

    memInfo should not be null
    memInfo.getAsJsonObject should haveValidFields
  }

  "disk-info command" should "return a json object with some fields" in {
    val wrapper = Mockito.mock(classOf[SitePathsWrapper])
    Mockito.when(wrapper.getAsPath(any[String])).thenReturn(Paths.get("/tmp"))

    val Seq(CommandResult(name, JsonResult(diskInfo))) = new DiskInfoCommand(wrapper).execute

    diskInfo should not be null
    diskInfo.getAsJsonObject should haveValidFields
  }

}

