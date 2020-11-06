import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.http.scaladsl.server.{Directive, Directive1, Directives, StandardRoute}
import akka.http.scaladsl.server.Route


import scala.concurrent.Future


trait TodoDirectives extends Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  def handle[T](f: Future[T])(e: Throwable => ApiError): Directive1[T] =
    onComplete(f) flatMap  {
    case Success(t) => provide(t)
    case Failure(error) =>
      val apiError = e(error)
      complete(apiError.statusCode, apiError.message)
  }

  def handleWithGeneric[T](f: Future[T]): Directive1[T] =
    handle[T](f)(_ => ApiError.generic)

}

//1. create new exception type
//2. handle in function with signature e: Throwable => ApiError
//3. complete update endpoint from tutorial