import sbt._

object Dependencies {
  private val circeVersion = "0.9.0-M2"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val circe = Seq("io.circe" %% "circe-core" % circeVersion,
                       "io.circe" %% "circe-parser"  % circeVersion,
                       "io.circe" %% "circe-generic" % circeVersion % Test)
}
