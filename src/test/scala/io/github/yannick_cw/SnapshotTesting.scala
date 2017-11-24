package io.github.yannick_cw

import java.io.File

import org.scalatest._

import scala.io.Source
import scala.util.Try

trait SnapshotTesting
    extends fixture.FlatSpec
    with Matchers
    with OptionValues
    with BeforeAndAfterEach
    with BeforeAndAfterAll {

  override protected def withFixture(test: NoArgTest) = {
    Snapshot.autoAccept(super.withFixture(test))
  }

  override protected def beforeEach(): Unit = {
    Try(new File("./src/test/scala/" + getClass.getName.replaceAll("""\.""", """/""")).listFiles().foreach(_.delete))
    super.beforeEach()
  }

  override protected def afterAll(): Unit = {
    Try(new File("./src/test/scala/" + getClass.getName.replaceAll("""\.""", """/""")).listFiles().foreach(_.delete))
    super.afterAll()
  }

  def testFile(id: Int): String =
    Source
      .fromFile(s"./src/test/scala/" + getClass.getName.replaceAll("""\.""", """/""") + s"/snapshot-$id")
      .getLines
      .mkString("\n")
}
