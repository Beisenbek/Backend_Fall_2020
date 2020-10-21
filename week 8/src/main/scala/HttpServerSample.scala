import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.{Logger, LoggerFactory}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import akka.routing.Router
import akka.stream.Supervision.Directive
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


object  MyServer{
   def startHttpServer(routes: Route)(implicit system: ActorSystem[_],  ex:ExecutionContext): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
     val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
}

trait  Router {
  def route:Route
}
class MyRouter(val todoRepository: TodoRepository)(implicit system: ActorSystem[_],  ex:ExecutionContext) extends  Router with  Directives{

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import  io.circe.generic.auto._

  override def route = {
    concat(
      path("ping") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong"))
        }
      },
      pathPrefix("todos") {
        pathEndOrSingleSlash {
          get {
            complete(todoRepository.all())
            //complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
          }
        }
      },
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      })
  }
}


object HttpServerSample {

  def main(args: Array[String]): Unit = {

    implicit val log: Logger = LoggerFactory.getLogger(getClass)

    val rootBehavior = Behaviors.setup[Nothing] { context =>

      val todos:Seq[Todo] = Seq(
        Todo("1","title1","description1",true),
        Todo("2","title2","description2",false)
      )

      val todoRepository = new InMemoryTodoRepository(todos)(context.executionContext)
      val router = new MyRouter(todoRepository)(context.system, context.executionContext)

      MyServer.startHttpServer(router.route)(context.system, context.executionContext)
      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
  }

  /*
  *
  *
  * def main(args: Array[String]): Unit = {
  * val log: Logger = LoggerFactory.getLogger(getClass)
    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext = system.executionContext


    val route = {
      concat(
        path("ping") {
          get {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong"))
          }
        },
        path("hello") {
          get {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
          }
        })
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    bindingFuture.onComplete{
      case Success(_) => log.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      case Failure(exception) => log.error(exception.toString)
    }
    * }
  *
  * */

}
