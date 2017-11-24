package io.github.yannick_cw

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.Try

object FileAccess {

  private def cleanId: String => String = _.replaceAll("[^A-Za-z0-9]", "_").toLowerCase()

  def writeFile(id: String, left: String, folderPath: String): Unit = {
    val writer = new PrintWriter(new File(s"$folderPath/snapshot-${cleanId(id)}"))
    writer.write(left)
    writer.close()
  }

  def readSnapshot[T](id: String, folderPath: String): Try[String] =
    Try(
      Source
        .fromFile(s"$folderPath/snapshot-${cleanId(id)}")
        .getLines
        .mkString("\n"))
}
