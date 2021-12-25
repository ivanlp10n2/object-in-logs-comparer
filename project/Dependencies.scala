import sbt._
import sbt.librarymanagement.DependencyBuilders.OrganizationArtifactName

object Dependencies {
  object V{
    val circeGeneric = "0.14.1"

    val fs2 = "3.2.3"
    val cats = "2.7.0"
    val catsEffect = "2.2.0"

    val circeCore = "0.14.1"
    val circeFs2 = "0.14.0"

    val refined = "0.9.28"
    val log4cats = "1.4.0"
  }

  object Libraries{
    import LazyConstructors._
    val catsCore = cats("core") % V.cats
    val catsEffect = cats("effect") % V.catsEffect
    val fs2Core = fs2("core") % V.fs2
    val fs2IO   = fs2("io") % V.fs2

    val circe = circeLib("core") % V.circeCore
    val circeFs2 = circeLib("fs2") % V.circeFs2
    val circeGeneric = circeLib("generic") % V.circeGeneric

    lazy val refinedcore = "eu.timepit" %% "refined"      % V.refined
    lazy val refinedCats = "eu.timepit" %% "refined-cats" % V.refined

    lazy val catsLogging = log4cats("core") % V.log4cats
    lazy val sl4jLogging = log4cats("slf4j") % V.log4cats
  }

  object LazyConstructors{
    def cats(artifact: String): OrganizationArtifactName = "org.typelevel" %% s"cats-$artifact"
    def fs2(artifact: String): OrganizationArtifactName = "co.fs2" %% s"fs2-$artifact"
    def circeLib(artifact: String): OrganizationArtifactName = "io.circe" %% s"circe-$artifact"
    def log4cats(artifact: String): OrganizationArtifactName = "org.typelevel" %% s"log4cats-$artifact"
  }
}
