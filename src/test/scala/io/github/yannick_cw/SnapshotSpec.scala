package io.github.yannick_cw

class SnapshotSpec extends SnapshotTesting with Snapshot {

  import Snapshot.Implicits._

  behavior of "Snapshot"

  it should "generate a snapshot file for integers" in autoAccept {
    val number = 24

    number should matchSnapshot[Int](1)
    testFile(1) shouldBe 24.toString
  }

  it should "generate a snapshot file with circe" in autoAccept {
    case class Tester(a: String, b: Int)
    import io.circe.generic.auto._
    import io.circe.parser.parse

    val t = Tester("name", 25)
    t should matchSnapshot[Tester](2)

    parse(testFile(2)).flatMap(_.as[Tester]) shouldBe Right(t)
  }

  it should "work with multiple snapshots in a single block" in {
    case class Tester(a: String, b: Int)
    import io.circe.generic.auto._

    autoAccept(Tester("name", 25) should matchSnapshot[Tester](3))
    autoAccept(Tester("another name", 99) should matchSnapshot[Tester](4))
  }

  it should "not match snapshot for different result" in {
    autoAccept(5 should matchSnapshot[Int](5))
    autoReject(4 shouldNot matchSnapshot[Int](5))
  }

  it should "fail if snapshot creation is rejected" in autoReject {
    5 shouldNot matchSnapshot[Int](6)
  }

  it should "be able to update a snapshot" in {
    autoAccept(5 should matchSnapshot[Int](7))
    autoAccept(4 should matchSnapshot[Int](7))
  }

  it should "be able to change type of a snapshot" in {
    case class Tester(a: String, b: Int)
    import io.circe.generic.auto._

    autoAccept(5 should matchSnapshot[Int](8))
    autoAccept(Tester("a", 22) should matchSnapshot[Tester](8))
  }
}
