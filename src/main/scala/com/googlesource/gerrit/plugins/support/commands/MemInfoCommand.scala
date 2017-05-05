package com.googlesource.gerrit.plugins.support.commands

import com.google.gson.Gson
import com.googlesource.gerrit.plugins.support.{CommandResult, ErrorInfo, GerritSupportCommand}
import org.jutils.jhardware.HardwareInfo.getMemoryInfo

import scala.util.Try

class MemInfoCommand extends GerritSupportCommand {
  implicit val gson = new Gson

  def execute = CommandResult("mem-info.json",
    gson.toJsonTree(
      Try {
        getMemoryInfo
      } getOrElse {
        ErrorInfo("error" -> s"Memory info not available on ${System.getProperty("os.name")}")
      }))
}