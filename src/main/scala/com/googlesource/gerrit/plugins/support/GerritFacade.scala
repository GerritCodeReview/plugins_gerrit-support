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

import com.google.gerrit.server.account.CapabilityControl
import com.googlesource.gerrit.plugins.support.latest.LatestCapabilityControl
import com.googlesource.gerrit.plugins.support.legacy.LegacyCapabilityControl

import scala.util.{Failure, Success, Try}

object GerritFacade {

  class PluginName(val value: String) extends AnyRef

  implicit class PimpedCapabilityControl(val cc: CapabilityControl) extends AnyVal {

    def canDo(operation: String)(implicit pluginName: PluginName) =
      LatestCapabilityControl(cc).canPerform(operation)
        .orElse(LegacyCapabilityControl(cc).canPerform(operation))
        .get
  }
}

object TryAll {
  def apply[T](block: => T): Try[T] = {
    try {
      val res:T = block
      Success(res)
    } catch {
      case t: Throwable => Failure(t)
    }
  }
}
