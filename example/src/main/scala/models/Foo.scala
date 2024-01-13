package models

import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Foo(foo: Int)

object Foo {
  implicit val codec: JsonCodec[Foo] = DeriveJsonCodec.gen[Foo]
}
