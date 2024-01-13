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
