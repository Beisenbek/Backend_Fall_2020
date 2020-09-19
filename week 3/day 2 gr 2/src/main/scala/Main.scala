object Main extends App {

  def problem4() = {

    def repeatedNTimes(A: Array[Int]): Int = {
      val res = A.map(x => A.count(y => x == y))
      for((value, index) <- res.zipWithIndex)
        if (value == A.length / 2) return  A(index)
      -1
    }

    def test1() = {
      val nums = Array(1,2,3,3)
      println(repeatedNTimes(nums))
    }

    def test2() = {
      val nums = Array(2,1,2,5,3,2)
      println(repeatedNTimes(nums))
    }

    def test3() = {
      val nums = Array(5,1,5,2,5,3,5,4)
      println(repeatedNTimes(nums))
    }

    test1()
    test2()
    test3()
  }

  def problem3() = {

    def smallerNumbersThanCurrent(nums: Array[Int]): Array[Int] = {
      nums.map(x => nums.count(y => x > y))
    }

    def smallerNumbersThanCurrent_1(nums: Array[Int]): Array[Int] = {
      val res = Array.ofDim[Int](nums.length)
      for ((value, index) <- nums.zipWithIndex) {
        var cnt = 0
        nums.foreach(x => if (x < value)
          cnt = cnt + 1
        )
        res(index) = cnt
      }
      res
    }

    def test1() = {
      val nums = Array(8, 1, 2, 2, 3)
      println(smallerNumbersThanCurrent(nums).foreach(x => print(x + " ")))
    }

    def test2() = {
      val nums = Array(6,5,4,8)
      println(smallerNumbersThanCurrent(nums).foreach(x => print(x + " ")))
    }

    def test3() = {
      val nums = Array(7, 7, 7, 7)
      println(smallerNumbersThanCurrent(nums).foreach(x => print(x + " ")))
    }

    test1()
    test2()
    test3()
  }

  def problem5() = {
    def decompressRLElist(nums: Array[Int]): Array[Int] = {
      (for (i <- nums.indices by 2) yield List.fill(nums(i))(nums(i + 1))).flatten.toArray
    }

    def test1() = {
      val nums = Array(1,2,3,4)
      println(decompressRLElist(nums).foreach(x => print(x + " ")))
    }

    def test2() = {
      val nums = Array(1,1,2,3)
      println(decompressRLElist(nums).foreach(x => print(x + " ")))
    }

    def test3() = {
      val nums = Array(1, 2)
      println(decompressRLElist(nums).foreach(x => print(x + " ")))
    }

    test1()
    test2()
    test3()
  }

  problem5()
}
