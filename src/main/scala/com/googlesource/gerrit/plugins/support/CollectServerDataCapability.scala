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

import com.google.gerrit.extensions.api.access.PluginPermission
import com.google.gerrit.extensions.config.CapabilityDefinition
import com.google.gerrit.server.account.CapabilityControl

import scala.util.Try

class CollectServerDataCapability extends CapabilityDefinition {
  override def getDescription: String = "Collect Server Data"
}

object CollectServerDataCapability {
  val COLLECT_SERVER_DATA = "collectServerData"

  val pluginName = "gerrit-support"

  implicit class PimpedCapabilityControl(val capabilityControl: CapabilityControl) extends AnyVal {

    def legacyCanPerform(operation: String): Try[Boolean] = Try {
      val canPerform = capabilityControl.getClass.getMethod("canPerform", classOf[String])
      canPerform.invoke(capabilityControl, operation).asInstanceOf[Boolean]
    }

    implicit def stringToGlobalOrPluginPermissions(operation: String) = new PluginPermission(pluginName, operation)

    def canDo(operation: String): Boolean =
      legacyCanPerform(operation).getOrElse {
        capabilityControl.doCanForDefaultPermissionBackend(operation)
      }
  }
}
