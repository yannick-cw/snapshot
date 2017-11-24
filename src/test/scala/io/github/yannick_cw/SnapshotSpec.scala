package io.github.yannick_cw

class SnapshotSpec extends SnapshotTesting with Snapshot {

  import Snapshot.Implicits._
  import Snapshot._

  behavior of "Snapshot"

  it should "generate a snapshot file for integers" in { implicit n =>
    val number = 24

    number should matchSnapshot[Int]
  }

  it should "generate a snapshot file with circe" in { implicit n =>
    case class Tester(a: String, b: Int)
    import io.circe.generic.auto._

    val t = Tester("name", 25)
    t should matchSnapshot[Tester]
  }

  it should "work with multiple snapshots in a single block" in { implicit n =>
    case class Tester(a: String, b: Int)
    import io.circe.generic.auto._

    Tester("name", 25) should matchSnapshot[Tester](1)
    autoAccept(Tester("another name", 99) should matchSnapshot[Tester](2))
  }

  it should "not match snapshot for different result" in { implicit n =>
    5 should matchSnapshot[Int]
    autoReject(4 shouldNot matchSnapshot[Int])
  }

  it should "fail if snapshot creation is rejected" in { implicit n =>
    autoReject(5 shouldNot matchSnapshot[Int])
  }

  it should "be able to update a snapshot" in { implicit n =>
    5 should matchSnapshot[Int]
    autoAccept(4 should matchSnapshot[Int])
  }

  it should "be able to change type of a snapshot" in { implicit n =>
    case class Tester(a: String, b: Int)
    import io.circe.generic.auto._

    5 should matchSnapshot[Int]
    autoAccept(Tester("a", 22) should matchSnapshot[Tester])
  }
}
