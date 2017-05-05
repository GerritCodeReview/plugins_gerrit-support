package com.googlesource.gerrit.plugins.support.commands

import java.nio.file.Files

import com.google.inject.Inject
import com.googlesource.gerrit.plugins.support.{SitePathsWrapper, GerritSupportCommand}

class DiskInfoCommand @Inject()(val sitePathsFolder: SitePathsWrapper) extends GerritSupportCommand {

  case class DiskInfo(path: String, diskFree: Long, diskUsable: Long, diskTotal: Long)

  def getResult = {
    val dataPath = sitePathsFolder.getAsPath("data_dir")
    val store = Files.getFileStore(dataPath)
    DiskInfo(dataPath.toString, store.getUnallocatedSpace,
      store.getUsableSpace, store.getTotalSpace
    )
  }

}