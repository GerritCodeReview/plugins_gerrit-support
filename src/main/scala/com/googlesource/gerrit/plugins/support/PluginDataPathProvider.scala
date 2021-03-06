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
import java.nio.file.Path

import com.google.gerrit.extensions.annotations.PluginData
import com.google.inject.{Inject, Injector, Key, Provider}

import scala.util.Try

class PluginDataPathProvider @Inject()(val injector: Injector) extends Provider[Path] {
  override def get(): Path = Try {
    injector.getInstance(Key.get(classOf[Path], classOf[PluginData]))
  }.orElse {
    Try {
      injector.getInstance(Key.get(classOf[File], classOf[PluginData]))
    }.map(_.toPath)
  }.get
}
