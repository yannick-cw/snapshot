package io.github.yannick_cw.nested

import io.github.yannick_cw.{Snapshot, SnapshotTesting}

class NestedSnapshotSpec extends SnapshotTesting with Snapshot {

  import Snapshot.Implicits._
  import Snapshot._

  behavior of "NestedSnapshot"

  it should "work in nested packages" in { implicit n =>
    val number = 24
    number should matchSnapshot[Int]
    autoAccept(number should matchSnapshot[Int])
  }
}
