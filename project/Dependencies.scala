import sbt._

object Dependencies {

  object Versions {
    val play    = "3.0.8"
    val zio     = "2.1.19"
    val zioJson = "0.7.44"
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
