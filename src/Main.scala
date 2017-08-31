import white._

object Main {

  def main(args: Array[String]) {
    parserTest()
  }

  def dumpTest(): Unit ={
    val scope = new Scope(None)
    scope.add("a")
    scope.add("b")
    scope.add("c")
    val vref = VariableRef("zhang")
    val vdef = VariableDef("zhang",StringLiteral("true"))
    val block = Block(List(vdef,vdef,vdef),scope)
    val bin = BinaryExpr(PLUS,block,block)
    val una = UnaryExpr(MINUS,block)
    val assign = AssignExpr(vref,block)
    val ife = IfExpr(vref,block,Some(bin))
    val whe = WhileExpr(vref,block)
    val fdef = FunctionDef("add",List("a","b"),block)
    val fcall = FunctionCall(vref,List(bin,vdef))
    dump.dump(fcall)
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
       s = s + 2;
    };
"""


    val c = "(a+b)(1,2)(3,4)(5,6);"
    val before = System.currentTimeMillis()

    val tokens = white.W_Scanner(c)
    println(s"tokens: $tokens")
    if (tokens.isRight) {
      val ast = white.W_Parser(tokens.right.get)
      if (ast.isRight) {
        val result = ast.right.get
        dump.dump(result)
        println("--------------------------------")
        W_SemanticChecker.genScope(result)
        dump.dump(result)
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
