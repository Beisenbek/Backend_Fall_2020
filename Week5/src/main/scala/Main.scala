import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

object  Main extends App {

  def sample7() ={
    def subarraySum(nums: Array[Int], k: Int): Int = {
      var res = 0

      nums.map{
        //
      }
      for(i <- nums.indices) {
        var sum = 0
        for(j <- i until nums.length) {
          sum += nums(j)
          if(sum == k) {
            res += 1
          }
        }
      }
      res
    }
  }

  sample7()

  def sample6() = {

    sealed trait Foo
    case class Bar(xs: Vector[String]) extends Foo
    case class Qux(i: Int, d: Option[Double]) extends Foo

    val foo: Foo = Qux(13, Some(14.0))

    val json = foo.asJson.noSpaces

    //println(json.hcursor.downField("Qux"))

    println(json)

    val decodedFoo = decode[Foo](json)
    println(decodedFoo)
  }




  def sample5() = {
    val x: Option[Int] = None

    x match {
      case Some(value) => println(value)
      case None => println("none!")
    }

    println(x.getOrElse("None"))
  }



  def sample4() = {
    val n1 = Future[Int] { 42}

    n1.onComplete {
      case Success(value) => {
        implicit val v = value
        println(scale(5))
      }
    }
    //implicit val n2: Int = 5

    def f(x: Int) = x

    def scale(x: Int)(implicit k: Int) = k * f(x)

    def s(x: Int, k: Int) = k * f(x)


  }


  def sample3() = {
    val s: String = "abc"
    s.map(_.toInt)
  }



  def sample2() = {

    val f1 = Future[Int] {
      Thread.sleep(2 * 1000)
      10
    }
    val f2 = Future[Int] {
      Thread.sleep(1 * 1000)
      20
    }

    val f3 = Future[Int] {
      Thread.sleep(3 * 1000)
      30
    }

    val sum = for (
      r1 <- f1;
      r2 <- f2;
      r3 <- f3
    ) yield (r1 + r2 + r3)

    sum.onComplete {
      case Success(value) =>
        println(s"Got the callback, value = $value")
      case Failure(e) =>
        e.printStackTrace
    }

    Thread.sleep(5 * 1000)

  }

  def sample1() = {

    val a = Future[Int] {
      Thread.sleep(3 * 1000)
      42
    }

    a.onComplete {
      case Success(value) =>
        println(s"Got the callback, value = $value")
      case Failure(e) =>
        e.printStackTrace
    }

    Thread.sleep(5 * 1000)
    print("finished!")
  }
}