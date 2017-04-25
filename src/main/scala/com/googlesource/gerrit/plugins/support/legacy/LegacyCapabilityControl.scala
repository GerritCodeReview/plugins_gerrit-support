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

package com.googlesource.gerrit.plugins.support.legacy

import com.google.gerrit.server.account.CapabilityControl
import com.googlesource.gerrit.plugins.support.GerritFacade.PluginName
import com.googlesource.gerrit.plugins.support.TryAll

import scala.util.Try

class LegacyCapabilityControl(val capabilityControl: CapabilityControl)(implicit pluginName: PluginName) {

  import LegacyCapabilityControl._

  def canPerform(operation: String): Try[Boolean] = TryAll {
    canPerformMethod.map(_.invoke(capabilityControl, s"${pluginName.value}-$operation").asInstanceOf[Boolean])
  }.flatten
}

object LegacyCapabilityControl {
  lazy val canPerformMethod = TryAll {
    classOf[CapabilityControl].getMethod("canPerform", classOf[String])
  }

  def apply(cc: CapabilityControl)(implicit pluginName: PluginName) = new LegacyCapabilityControl(cc)
}
