import Dependencies._

ThisBuild / organization  := "ar.empanada"
ThisBuild / version       := "0.0.1"
ThisBuild / scalaVersion  := "2.13.6"

lazy val root = (project in file("."))
  .settings(
    name := "compareLogs",
    libraryDependencies ++= Seq(
      Libraries.catsCore,
      Libraries.catsEffect,
      Libraries.fs2Core,
      Libraries.fs2IO,
      Libraries.circe,
      Libraries.circeFs2,
      Libraries.circeGeneric,
      Libraries.refinedcore,
      Libraries.refinedCats,
      Libraries.catsLogging,
      Libraries.sl4jLogging,
    )
  )
