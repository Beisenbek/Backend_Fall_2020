import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Processor {

  sealed trait ProcessorMessage

  case class ComputeFibonacci(n: Int) extends ProcessorMessage

  case class ProcessorResponse(nodeId: String, res: BigInt) extends ProcessorMessage


  def apply(): Behavior[ProcessorMessage] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage { message =>
      message match {
        case ComputeFibonacci(value: Int) => {
          val fibonacciProcessor = ctx.spawn(ProcessorFibonacci(), "fibonacci")
          fibonacciProcessor ! ProcessorFibonacci.Compute(value, ctx.self)
        }
      }
      Behaviors.same
    }
  }

}
