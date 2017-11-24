package io.github.yannick_cw

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.Try

object FileAccess {

  def writeFile(id: Int, left: String, folderPath: String): Unit = {
    val writer = new PrintWriter(new File(s"$folderPath/snapshot-$id"))
    writer.write(left)
    writer.close()
  }

  def readSnapshot[T](id: Int, folderPath: String): Try[String] =
    Try(
      Source
        .fromFile(s"$folderPath/snapshot-$id")
        .getLines
        .mkString("\n"))
}
