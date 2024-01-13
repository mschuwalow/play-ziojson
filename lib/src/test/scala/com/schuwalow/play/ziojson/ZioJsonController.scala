package com.schuwalow.play.ziojson

import play.api.mvc._
import zio.json._
import zio.json.ast.Json

import scala.concurrent.ExecutionContext

class ZioJsonController(val controllerComponents: ControllerComponents) extends BaseController with ZioJsonSupport {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  def get: Action[AnyContent] = Action {
    Ok(ziojson.asJson(Data.foo))
  }

  def post = Action(ziojson.json[Foo]) { (request: Request[Foo]) =>
    val isEqual = request.body == Data.foo
    Ok(isEqual.toString)
  }

  def postJson = Action(ziojson.json) { (request: Request[Json]) =>
    val isEqual = request.body == Data.foo.toJsonAST.toOption.get
    Ok(isEqual.toString)
  }
}
