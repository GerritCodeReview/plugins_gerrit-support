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

package com.googlesource.gerrit.plugins.support.latest

import com.google.gerrit.server.account.CapabilityControl
import com.googlesource.gerrit.plugins.support.GerritFacade.PluginName

import scala.util.Try
import com.googlesource.gerrit.plugins.support.TryClass

class LatestCapabilityControl(val capabilityControl: CapabilityControl)(implicit pluginName: PluginName) {

  import LatestCapabilityControl._

  def canPerform(operation: String): Try[Boolean] = TryClass {
    for (
      permissionConstructor <- CollectServerDataPermissionConstructor;
      checkMedhod <- checkPermissionMethod
    ) yield {
      val permission = permissionConstructor.newInstance(pluginName.value).asInstanceOf[Object]
      val result: Object = checkMedhod.invoke(capabilityControl, permission)
      result.asInstanceOf[Boolean]
    }
  }.flatten
}

object LatestCapabilityControl {
  lazy val GlobalOrPluginPermissionClass = TryClass {
    Class.forName("com.google.gerrit.extensions.api.access.GlobalOrPluginPermission")
  }

  lazy val CollectServerDataPermissionClass = TryClass {
    Class.forName("com.googlesource.gerrit.plugins.support.latest.CollectServerDataPermission")
  }

  lazy val CollectServerDataPermissionConstructor = TryClass {
    CollectServerDataPermissionClass.map(_.getConstructor(classOf[String]))
  }.flatten

  lazy val checkPermissionMethod = TryClass {
    GlobalOrPluginPermissionClass.map(classOf[CapabilityControl].getMethod("doCanForDefaultPermissionBackend", _))
  }.flatten

  def apply(cc: CapabilityControl)(implicit pluginName: PluginName) = new LatestCapabilityControl(cc)
}
