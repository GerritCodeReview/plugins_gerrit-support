package com.googlesource.gerrit.plugins.support.commands

import com.google.gerrit.common.Version
import com.google.gson.JsonPrimitive
import com.googlesource.gerrit.plugins.support.{CommandResult, GerritSupportCommand}

class GerritVersionCommand extends GerritSupportCommand {
  def execute = CommandResult("version.json", new JsonPrimitive(Version.getVersion))
}