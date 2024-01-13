package com.schuwalow.play.ziojson

import org.apache.pekko.stream.scaladsl.StreamConverters
import org.apache.pekko.util.ByteString
import play.api.http._
import play.api.libs.streams.Accumulator
import play.api.mvc.{BodyParser, PlayBodyParsers, RequestHeader, Results}
import zio.json._
import zio.json.ast.Json

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Using

trait ZioJsonSupport extends Status {
  import ZioJsonSupport._

  def parse: PlayBodyParsers
  implicit def ec: ExecutionContext

  protected implicit val stringifiedJsonContentType: ContentTypeOf[StringifiedJson] = ContentTypeOf(
    Some(ContentTypes.JSON)
  )

  protected implicit val stringifiedJsonWriteable: Writeable[StringifiedJson] =
    Writeable(body => ByteString(body.content))

  object ziojson {

    def asJson[A: JsonEncoder](a: A): StringifiedJson =
      StringifiedJson(a.toJson)

    def json[A: JsonDecoder]: BodyParser[A] = parse.when(
      _.contentType.exists(m => m.equalsIgnoreCase("text/json") || m.equalsIgnoreCase("application/json")),
      parseJson[A],
      _ => Future.successful(Results.UnsupportedMediaType("Expecting text/json or application/json body"))
    )

    val json: BodyParser[Json] = json[Json]

    private def parseJson[A: JsonDecoder]: BodyParser[A] = BodyParser { rh =>
      Accumulator(StreamConverters.asInputStream().mapMaterializedValue(Future.successful)).map { is =>
        val reader        = new java.io.InputStreamReader(is, detectCharset(rh))
        val retractReader = new zio.json.internal.WithRetractReader(reader)
        Using(retractReader)(JsonDecoder[A].unsafeDecode(Nil, _)).fold(
          {
            case JsonDecoder.UnsafeJson(trace) =>
              Left(Results.BadRequest(s"Parsing json failed: ${JsonError.render(trace)}"))
            case other                         =>
              Left(Results.BadRequest(s"Parsing json failed: $other"))
          },
          Right(_)
        )
      }
    }

    private def detectCharset(request: RequestHeader) =
      request.headers.get("Content-Type") match {
        case Some(CharsetPattern(c)) => c
        case _                       => "UTF-8"
      }

  }
}

object ZioJsonSupport {
  private final val CharsetPattern = "(?i)\\bcharset=\\s*\"?([^\\s;\"]*)".r
}
