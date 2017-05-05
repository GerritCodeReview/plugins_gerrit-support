package com.googlesource.gerrit.plugins.support.commands

import com.google.gson.Gson
import com.googlesource.gerrit.plugins.support.{CommandResult, ErrorInfo, GerritSupportCommand}
import org.jutils.jhardware.HardwareInfo.getProcessorInfo

import scala.util.Try

class CpuInfoCommand extends GerritSupportCommand {
  implicit val gson = new Gson

  def execute = CommandResult("cpu-info.json",
    gson.toJsonTree(
      Try {
        getProcessorInfo
      } getOrElse {
        ErrorInfo("error" -> s"CPU info not available on ${System.getProperty("os.name")}")
      }))
}