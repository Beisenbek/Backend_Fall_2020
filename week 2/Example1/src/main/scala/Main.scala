import java.time.DayOfWeek

object Main extends  App {

  def f1() = {
    val count = 1

    val r = count match {
      case 1 => 1
      case _ => 2
    }

    print(r)
  }

  def f2() = {
    val count = 2

    val r = count match {
      case 1 => 1
      case _ => 2
    }

    print(r)
  }

  def action2(x:Int):Int ={
    val y = x + 10
    y
  }

  def f3() = {
    val count = 3

    val r = count match {
      case 1 => 1
      case x if x % 2 == 1 => action2(x)
      case _ => 0
    }

    print(r)
  }

  def f4()= {

    class Person(var firstName: String, var lastName: String)

    val p = new Person("Bill", "Panner")
    println(p.firstName + " " + p.lastName)
  }

  def f5()= {

    class Person(var firstName: String, var lastName: String) {

      println("the constructor begins")

      // 'public' access by default
      var age = 0

      // some class fields
      private val HOME = System.getProperty("user.home")

      // some methods
      override def toString(): String = s"$firstName $lastName is $age years old"

      def printHome(): Unit = println(s"HOME = $HOME")
      def printFullName(): Unit = println(this)

      printHome()
      printFullName()
      println("you've reached the end of the constructor")

    }

    val p = new Person("Kim", "Carnes")
  }

  def f6(): Unit = {
    sealed trait DayOfWeek
    case object Sunday extends DayOfWeek
    case object Monday extends DayOfWeek
    case object Tuesday extends DayOfWeek
    case object Wednesday extends DayOfWeek
    case object Thursday extends DayOfWeek
    case object Friday extends DayOfWeek
    case object Saturday extends DayOfWeek

    val someDay = DayOfWeek.FRIDAY

    print(someDay)
  }

  f6()

}