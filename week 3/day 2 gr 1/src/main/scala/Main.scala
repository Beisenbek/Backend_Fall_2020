object Main extends  App{
  problem2()

  def problem2() = {
    //https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/

    class ListNode(_x: Int = 0, _next: ListNode = null) {
      var next: ListNode = _next
      var x: Int = _x
    }

    def prepare(array: Array[Int]): ListNode = {
      var node: ListNode = null
      for (x <- array.reverse) {
        val curNode = new ListNode(x)
        curNode.next = node
        node = curNode
      }
      node
    }

    def traverse(head: ListNode, res: String): String = {
      if (head.next == null) return res + " " + head.x
      traverse(head.next, res + " " + head.x)
    }

    def getDecimalValue(head: ListNode): Int = {
      def rec(head: ListNode, res: String): String = {
        if (head.next == null) return res + head.x
        rec(head.next, res + head.x)
      }

      Integer.parseInt(rec(head, ""), 2)
    }

    def test1() = {
      val rawData = Array(1, 0, 1)
      val head: ListNode = prepare(rawData)
      println(getDecimalValue(head))
      //println(traverse(head, ""))
    }

    def test2() = {
      val rawData = Array(1,0,0,1,0,0,1,1,1,0,0,0,0,0,0)
      val head: ListNode = prepare(rawData)
      println(getDecimalValue(head))
      //println(traverse(head, ""))
    }

    test2()
  }
  def problem1() = {

    //https://leetcode.com/problems/kids-with-the-greatest-number-of-candies/

    def kidsWithCandies(candies: Array[Int], extraCandies: Int): Array[Boolean] = {
      val res = Array.ofDim[Boolean](candies.length)
      //val max = candies.reduce(_ max _)
      val max = candies.max
      for ((n, i) <- candies.zipWithIndex)
        res(i) = (extraCandies + n) >= max
      res
    }

    def test1() = {
      val candies: Array[Int] = Array[Int](2, 3, 5, 1, 3)
      val extraCandies: Int = 3
      println(kidsWithCandies(candies, extraCandies).foreach(x => println(x)) + "\n")
    }

    def test2() = {
      val candies: Array[Int] = Array[Int](4,2,1,1,2)
      val extraCandies: Int = 1
      println(kidsWithCandies(candies, extraCandies).foreach(x => println(x)) + "\n")
    }

    def test3() = {
      val candies: Array[Int] = Array[Int](12, 1, 12)
      val extraCandies: Int = 10
      println(kidsWithCandies(candies, extraCandies).foreach(x => println(x)) + "\n")
    }

    test1()
    test2()
    test3()
  }

}
