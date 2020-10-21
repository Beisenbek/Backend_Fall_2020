object Main extends  App {


  def task4() = {

    def f(a:Int, b: Int): Int = {
      a - b
    }
    def findPairs(nums: Array[Int], k: Int): Int = {
      nums.groupBy(identity).foreach(x => {
        println("group of " + x._1 + ":")
        x._2.foreach(item => println(item))
      })
      0
    }

    val test2 = Array(1, 2, 3, 4, 5)
    val k2 = 1

    println(findPairs(test2, k2))
  }


  task4()

  def task2() = {

    def average(salary:Array[Int]):Double = {

      salary
        .foldLeft((0, Int.MaxValue, Int.MinValue, 0)) {
          (memo, next) =>
            memo match {
              case (sum, min, max, count) =>
                (sum + next, math.min(next, min), math.max(next, max), count + 1)
            }
        } match {
        case (sum, min, max, count) => ((sum - min - max) / (count - 2).toDouble)
      }

      //salary.foldLeft(0)(_ + _).toDouble
      //salary.foldLeft(0)((memo,next) => memo + next)
      /*salary.foldLeft(0) {
        (memo, next) => memo + next
      }*/
    }

    def average2(salary: Array[Int]): Double = {
      1.0 * (salary.sum - salary.min - salary.max) / (salary.length - 2)
    }

    val test1 = Array(4000,3000,1000,2000)

    println(average(test1))
  }


  def task1() = {
    def maxProduct(nums: Array[Int]): Int = {
      nums.sorted.slice(nums.length - 2, nums.length).reduce((a, b) => (a - 1) * (b - 1))

      /*
    val nums2 = nums.sorted
    (nums2(nums2.length - 1) - 1) * (nums2(nums2.length - 2) - 1)
       */
      //nums.sorted.slice(0,nums.length - 1). foreach(x => print(x + " "))
      //println(nums.sorted.reduce((a, b) => a + b))

    }

    val test1 = Array(4, 2, 3, 1)
    val test2 = Array(3, 4, 5, 2)
    val test3: Array[Int] = Array.empty
    println(maxProduct(test3))
  }
}
