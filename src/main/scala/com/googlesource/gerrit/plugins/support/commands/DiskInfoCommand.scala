package com.googlesource.gerrit.plugins.support.commands

import java.nio.file.Files

import com.google.inject.Inject
import com.googlesource.gerrit.plugins.support.{DataDirPathProvider, GerritSupportCommand}

class DiskInfoCommand @Inject()(val dataPathProvider: DataDirPathProvider) extends GerritSupportCommand {

  case class DiskInfo(path: String, diskFree: Long, diskUsable: Long, diskTotal: Long)

  def getResult = {
    val dataPath = dataPathProvider.get
    val store = Files.getFileStore(dataPath)
    DiskInfo(dataPath.toString, store.getUnallocatedSpace,
      store.getUsableSpace, store.getTotalSpace
    )
  }

}