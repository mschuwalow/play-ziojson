# play-ziojson

Adds support for zio-json encoding/decoding to play.

![CI Badge](https://github.com/mschuwalow/play-ziojson/actions/workflows/ci.yml/badge.svg?branch=main) [![Sonatype Releases](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.schuwalow/play-ziojson_2.13.svg?label=Sonatype%20Release)](https://oss.sonatype.org/content/repositories/releases/com/schuwalow/play-ziojson_2.13/) [![Sonatype Snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.schuwalow/play-ziojson_2.13.svg?label=Sonatype%20Snapshot)](https://oss.sonatype.org/content/repositories/snapshots/com/schuwalow/play-ziojson_2.13/) [![play-ziojson](https://img.shields.io/github/stars/mschuwalow/play-ziojson?style=social)](https://github.com/mschuwalow/play-ziojson)

## Installation

Add the dependency to your build.sbt:

```scala
libraryDependencies += "com.schuwalow" %% "play-ziojson" % "0.1.0"
```

## Example controller

Define your model:

```scala
package models

import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Foo(foo: Int)

object Foo {
  implicit val codec: JsonCodec[Foo] = DeriveJsonCodec.gen[Foo]
}
```

Then encode and decode it in your controller!

```scala
package controllers

import com.schuwalow.play.ziojson.ZioJsonSupport
import models.Foo
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class FooController @Inject() (val controllerComponents: ControllerComponents)(implicit val ec: ExecutionContext)
    extends BaseController
    with ZioJsonSupport {

  def getFoo() = Action {
    Ok(ziojson.asJson(Foo(123)))
  }

  def postFoo() = Action(ziojson.json[Foo]) { req =>
    Ok(s"Got a Foo: ${req.body.foo}")
  }
}

```

## License[License](LICENSE)
