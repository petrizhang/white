import white._

object Main {

  def main(args: Array[String]) {
    val intp = new WhiteInterpreter()
    val bin = BinaryExpr(
      PLUS,
      BinaryExpr(MUL, NumberLiteral(1), NumberLiteral(2)),
      BinaryExpr(DIV, NumberLiteral(3), NumberLiteral(4))
    )
    intp.visit(bin)
  }

  def parserTest = {
    var a =
      """
    def add(a,b) {
       a+b;
    };

    var a = (a+b)(1,2)(3,4)(5,6);

    var b = 0;

    while (a > 0) {
       a = a - 1;
       b = b + 1;
    };
"""


    val c = "(a+b)(1,2)(3,4)(5,6);"
    val before = System.currentTimeMillis()

    val tokens = white.W_Scanner(a)
    println(s"tokens: $tokens")
    if (tokens.isRight) {
      val ast = white.WhiteParser(tokens.right.get)
      if (ast.isRight) {
        println(ast.right.get)
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
