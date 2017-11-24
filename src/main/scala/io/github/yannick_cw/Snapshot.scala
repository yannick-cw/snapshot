package io.github.yannick_cw

import java.io.{ByteArrayInputStream, File}

import io.github.yannick_cw.FileAccess._
import io.github.yannick_cw.Messages._
import io.github.yannick_cw.Snapshot.Serializer
import org.scalatest.matchers.{MatchResult, Matcher}
import org.scalatest.{Matchers, SequentialNestedSuiteExecution, TestSuite}

import scala.io.StdIn
import scala.util.Try

trait Snapshot extends Matchers with SequentialNestedSuiteExecution { self: TestSuite =>
  val basePath = "./src/test/scala/"

  private val folderPath     = basePath + getClass.getName.replaceAll("""\.""", """/""")
  private lazy val className = getClass.getSimpleName
  new File(folderPath).mkdir()

  private def updateSnapshot[T](id: Int, left: String, right: String)(implicit s: Serializer[T]): MatchResult =
    if (StdIn.readLine(updateQuestion(left, right, className)) == "y") {
      writeFile(id, left, basePath)
      MatchResult(true, "", "Updated the snapshot file")
    } else be(right)(left)

  private def checkSnapshot[T](id: Int)(snapshot: String)(implicit s: Serializer[T]): Matcher[T] =
    (left: T) =>
      s.read(snapshot) match {
        case Right(right) if left == right => MatchResult(true, "", "Snapshot matches")
        case Right(right)                  => updateSnapshot(id, s.print(left), s.print(right))
        case _                             => updateSnapshot(id, s.print(left), snapshot)
    }

  private def initializeSnapshot[T](id: Int)(implicit s: Serializer[T]): Matcher[T] =
    (left: T) =>
      if (StdIn.readLine(createQuestion(s.print(left), className)) == "y") {
        writeFile(id, s.print(left), folderPath)
        MatchResult(true, "", "Created new snapshot file")
      } else MatchResult(false, s"Did not accept ${s.print(left)} as valid result", "")

  def matchSnapshot[T](id: Int)(implicit s: Serializer[T]): Matcher[T] =
    readSnapshot(id, folderPath).fold(_ => initializeSnapshot(id), checkSnapshot(id))

  def autoAccept[T](thunk: => T): T = Console.withIn(new ByteArrayInputStream("y".getBytes))(thunk)
  def autoReject[T](thunk: => T): T = Console.withIn(new ByteArrayInputStream("n".getBytes))(thunk)
}

object Snapshot {

  trait Serializer[T] {
    def print(t: T): String
    def read(s: String): Either[String, T]
  }

  object Implicits {
    import io.circe.{Decoder, Encoder, parser}

    implicit def fromCirce[T](implicit decoder: Decoder[T], encoder: Encoder[T]): Serializer[T] = new Serializer[T] {
      def read(s: String): Either[String, T] = parser.parse(s).flatMap(decoder.decodeJson).left.map(_.getMessage)
      def print(t: T): String                = encoder(t).spaces2
    }

    implicit val intSerializer: Serializer[Int] = new Serializer[Int] {
      def read(s: String): Either[String, Int] = Try(s.toInt).toEither.left.map(_.getMessage)
      def print(t: Int): String                = t.toString
    }
  }
}
