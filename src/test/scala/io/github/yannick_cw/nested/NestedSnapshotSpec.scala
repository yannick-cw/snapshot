package io.github.yannick_cw.nested

import io.github.yannick_cw.{Snapshot, SnapshotTesting}

class NestedSnapshotSpec extends SnapshotTesting with Snapshot {

  import Snapshot.Implicits._

  behavior of "NestedSnapshot"

  it should "work in nested packages" in autoAccept {
    val number = 24
    number should matchSnapshot[Int](1)
    testFile(1) shouldBe 24.toString
    number should matchSnapshot[Int](1)
  }
}
