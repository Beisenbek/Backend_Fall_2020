object Main extends App {
  println(Solution.busyStudent(Array(1,2,3),Array.empty,0))

}

object Solution {
  def busyStudent(startTime: Array[Int], endTime: Array[Int], queryTime: Int): Int = {
    (startTime zip endTime).count(e => {
      (e._1 <= queryTime && e._2 >= queryTime)
    })
  }
}