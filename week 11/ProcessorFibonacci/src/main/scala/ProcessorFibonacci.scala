import Processor.{ProcessorMessage, ProcessorResponse}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object ProcessorFibonacci {

  sealed trait ProcessorFibonacciMessage

  case class Compute(n: Int, replyTo: ActorRef[ProcessorMessage]) extends ProcessorFibonacciMessage


  def fibonacci(x: Int): BigInt = {
    def fibHelper(x: Int, prev: BigInt = 0, next: BigInt = 1): BigInt = x match {
      case 0 => prev
      case 1 => next
      case _ => fibHelper(x - 1, next, next + prev)
    }

    fibHelper(x)
  }

  def apply(): Behavior[ProcessorFibonacciMessage] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage { message =>
      message match {
        case Compute(value, replyTo) => replyTo ! ProcessorResponse("nodeId", fibonacci(value))
      }
      Behaviors.same
    }
  }
}
