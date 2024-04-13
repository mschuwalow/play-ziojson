import sbt._

object Dependencies {

  object Versions {
    val play    = "3.0.1"
    val zio     = "2.0.22"
    val zioJson = "0.6.2"
  }

  import Versions._

  val Lib = Seq(
    "org.playframework" %% "play"         % play % Provided,
    "org.playframework" %% "play-guice"   % play % Provided,
    "dev.zio"           %% "zio-json"     % zioJson,
    "dev.zio"           %% "zio-test"     % zio  % Test,
    "dev.zio"           %% "zio-test-sbt" % zio  % Test
  )
}
