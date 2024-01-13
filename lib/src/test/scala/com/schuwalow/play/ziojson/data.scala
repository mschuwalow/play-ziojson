package com.schuwalow.play.ziojson

import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Foo(foo: String, bar: Bar)
object Foo {
  implicit val codec: JsonCodec[Foo] = DeriveJsonCodec.gen[Foo]
}

final case class Bar(bar: Int)
object Bar {
  implicit val codec: JsonCodec[Bar] = DeriveJsonCodec.gen[Bar]
}

object Data {
  val bar           = Bar(1)
  val foo           = Foo("foo", bar)
  val fooJsonString = """{"foo":"foo","bar":{"bar":1}}"""
}
