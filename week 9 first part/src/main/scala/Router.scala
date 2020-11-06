import akka.actor.typed.ActorSystem
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import  io.circe.generic.auto._
import akka.http.scaladsl.server.{Directives, Route}

import scala.concurrent.ExecutionContext

trait Router {
  def route: Route
}

class MyRouter(val todoRepository: TodoRepository)(implicit system: ActorSystem[_],  ex:ExecutionContext)
  extends  Router
    with  Directives
    with HealthCheckRoute
    with ValidatorDirectives
    with TodoDirectives {

  def todo = {
    pathPrefix("todos") {
      concat(
        pathEndOrSingleSlash {
          concat(
            get {
              complete(todoRepository.all())
            },
            post {
              entity(as[CreateTodo]) { createTodo =>
                validateWith(CreateTodoValidator)(createTodo) {
                  handleWithGeneric(todoRepository.create(createTodo)) { todo =>
                    complete(todo)
                  }
                }
              }x
            }
          )
        },
        path("done") {
          get {
            complete(todoRepository.done())
          }
        },
        path("pending") {
          get {
            complete(todoRepository.pending())
          }
        }
      )
    }
  }

  override def route = {
    concat(
      healthCheck,
      todo
    )
  }
}




