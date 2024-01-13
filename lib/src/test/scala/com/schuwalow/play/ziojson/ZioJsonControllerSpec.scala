package com.schuwalow.play.ziojson

import zio.ZIO
import zio.json._
import zio.test.Assertion._
import zio.test._

object ZioJsonControllerSpec extends ZIOSpecDefault with PlaySpec {

  val controller = new ZioJsonController(controllerComponents)

  def spec = suite("ZioJsonController")(
    test("serialize case class") {
      ZIO.fromFuture { implicit ec =>
        call(controller.get, FakeReq.get("/get")).map { case Reply(_, ct, bd) =>
          assert(ct)(isSome(equalTo("application/json"))) &&
          assert(bd)(equalTo(Data.fooJsonString))
        }
      }
    },
    test("parse case class") {
      ZIO.fromFuture { implicit ec =>
        call(controller.post, FakeReq.post("/post").withTextBody("application/json", Data.foo.toJson)).map {
          case Reply(_, _, bd) =>
            assert(bd)(equalTo("true"))
        }
      }
    },
    test("parse json") {
      ZIO.fromFuture { implicit ec =>
        call(controller.postJson, FakeReq.post("/postJson").withTextBody("application/json", Data.foo.toJson)).map {
          case Reply(_, _, bd) =>
            assert(bd)(equalTo("true"))
        }
      }
    },
    test("parse `text/json` as json") {
      ZIO.fromFuture { implicit ec =>
        call(controller.post, FakeReq.post("/post").withTextBody("text/json;charset=utf-8", Data.foo.toJson)).map {
          case Reply(_, _, bd) =>
            assert(bd)(equalTo("true"))
        }
      }
    },
    test("invalid content type") {
      ZIO.fromFuture { implicit ec =>
        call(controller.post, FakeReq.post("/post").withTextBody("text/html;charset=utf-8", Data.foo.toJson)).map {
          case Reply(s, _, _) =>
            assert(s)(equalTo(415))
        }
      }
    },
    test("invalid json") {
      ZIO.fromFuture { implicit ec =>
        call(controller.post, FakeReq.post("/post").withTextBody("application/json", "not json")).map {
          case Reply(s, _, _) =>
            assert(s)(equalTo(400))
        }
      }
    },
    test("json not matching schema") {
      ZIO.fromFuture { implicit ec =>
        call(controller.post, FakeReq.post("/post").withTextBody("application/json", "{}")).map { case Reply(s, _, _) =>
          assert(s)(equalTo(400))
        }
      }
    }
  )
}
