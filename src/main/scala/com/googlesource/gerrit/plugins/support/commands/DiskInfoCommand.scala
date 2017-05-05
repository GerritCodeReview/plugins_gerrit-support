package com.googlesource.gerrit.plugins.support.commands

import java.nio.file.{Files, Path}

import com.google.inject.Inject
import com.googlesource.gerrit.plugins.support.annotations.DataPath
import com.googlesource.gerrit.plugins.support.{CommandResult, GerritSupportCommand}

class DiskInfoCommand @Inject()(@DataPath val dataPath: Path) extends GerritSupportCommand {

  case class DiskInfo(path: String, diskFree: Long, diskUsable: Long, diskTotal: Long)

  def getResult = {
    val store = Files.getFileStore(dataPath)
    DiskInfo(dataPath.toString, store.getUnallocatedSpace,
      store.getUsableSpace, store.getTotalSpace
    )
  }

}