import Dependencies._

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "io.github.yannick_cw",
      scalaVersion := "2.12.4",
      version := "0.1.0"
    )),
  name := "snapshot",
  libraryDependencies ++= Seq(scalaTest) ++ circe
)
