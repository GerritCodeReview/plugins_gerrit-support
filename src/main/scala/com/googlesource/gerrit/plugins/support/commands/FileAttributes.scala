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

import java.io.File
import java.nio.file.{Files, LinkOption}
import java.nio.file.attribute.{BasicFileAttributes, PosixFileAttributeView,
PosixFileAttributes, PosixFilePermissions}

case class FileAttributes(name: String, perms: String,
                          owner: String, group: String,
                          date: String, size: Long) {
  def ls = s"$perms\t$owner\t$group\t$date\t$size\t$name"
}

object FileAttributes {
  def from(f: File) = {
    val p = f.toPath

    FileAttributes(
      name = f.getName,
      perms = PosixFilePermissions.toString(
        Files.getFileAttributeView(p, classOf[PosixFileAttributeView])
          .readAttributes.permissions),
      owner = Files.getOwner(p).getName,
      group = Files.readAttributes(p, classOf[PosixFileAttributes])
        .group.getName,
      date =
        Files.readAttributes(p, classOf[BasicFileAttributes])
          .creationTime().toString
      ,
      size = f.length
    )
  }

}