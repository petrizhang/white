import white._

object Main {

  def main(args: Array[String]) {
    parserTest()
  }

  def parserTest(): Unit = {
    var a =
      """
    def add(a,b) {
       var m = 0;
       a+b;
    };

    var a = 2;

    var b = 0;

    while (a > 0) {
       var s = 2;
       a = a - 1;
       b = b + 1;
       s = s + 2
    };
"""


    val c = "(a+b)(1,2)(3,4)(5,6);"
    val before = System.currentTimeMillis()

    val tokens = white.W_Scanner(a)
    println(s"tokens: $tokens")
    if (tokens.isRight) {
      val ast = white.W_Parser(tokens.right.get)
      if (ast.isRight) {
        val result = ast.right.get
        println(result)
        println("--------------------------------")
        W_SemanticChecker.genScope(result)
        println(result)
      } else {
        println(ast.left.get)
      }
    } else {
      println(tokens.left.get)
    }

    val after = System.currentTimeMillis()
    println(after - before, "ms")
  }
}
