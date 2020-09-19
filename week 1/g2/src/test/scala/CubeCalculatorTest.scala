import org.scalatest.FunSuite

class CubeCalculatorTest extends FunSuite {
  test("CubeCalculator.cube") {
    assert(Main.cube(3) === 27)
  }
  test("CubeCalculator.cube2") {
    assert(Main.cube(3) === 271)
  }
}