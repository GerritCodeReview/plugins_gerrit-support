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

package com.googlesource.gerrit.plugins.support.commands

import java.io.{PrintWriter, StringWriter}

import com.google.gerrit.server.plugins.{ListPlugins, PluginsCollection}
import com.google.inject.Inject
import com.googlesource.gerrit.plugins.support.{CommandResult, GerritSupportCommand, SitePathsWrapper}

class PluginsInfoCommand @Inject()(val sitePathsWrapper: SitePathsWrapper,
                                   val listPlugins: ListPlugins)
  extends GerritSupportCommand {

  override def getResults = {
    try {
      Seq(
        listDir("plugins_dir"),
        listDir("lib_dir"),
        listPluginsVersions
      )
    } catch {
      case x: Exception => {
        CommandResult(name, s"Error ${x.getMessage}")
      }
    }
  }

  private def listDir(dir: String): CommandResult = {
    CommandResult(dir,
      sitePathsWrapper
        .getAsPath(dir)
        .toFile
        .listFiles
        .map(FileAttributes.from)
        .map(_.ls)
        .mkString("\n"))
  }

  private def listPluginsVersions = {

    val sw = new StringWriter
    val pw = new PrintWriter(sw)
    listPlugins.display(pw)
    pw.flush()
    CommandResult("plugins-versions",
      sw.toString
    )
  }
}