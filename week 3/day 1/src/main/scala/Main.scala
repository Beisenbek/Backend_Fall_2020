trait TailWagger {
  def startTail(): Unit

  def stopTail(): Unit
}

trait Pet {
  def speak = println("Yo")     // concrete implementation of a speak method
  def comeToMaster(): Unit      // abstract
}

class Dog extends Pet with TailWagger {
  // the implemented methods
  def startTail(): Unit = println("tail is wagging")

  def stopTail(): Unit = println("tail is stopped")

  override def speak: Unit = println("Yo!")

  def comeToMaster(): Unit = {
    println("comeToMaster!")
  }
}

object Main extends  App {

  def f1() = {
    val d = new Dog()
    d.startTail()
    d.stopTail()
    d.comeToMaster()
    d.speak
  }

  def f2() = {
    val ints = List(1, 2, 3)
    val ints2 = ints :+ 4
    val ints3 = 0 +: ints2

    println(ints)
    println(ints2)
    println(ints3)

    //print(ints.tail.tail.tail.tail)
    //print(ints.head)

    for(item <- ints3) println(item)
  }

  f2()

}
