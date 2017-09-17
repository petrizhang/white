import white._

object Main {

  def main(args: Array[String]) {

    var code =
      """
def a(a,b){
  a+b;
};
a(1,2) == 3;
a("z","h") == "zh";
    """
    class x(ivalue:Int){
      var value:Int = ivalue
    }

   testParser()
  }

  def testJavaAPI(): Unit = {
    JavaAPI.load()
  }

  def testDumper(): Unit = {
    val scope = new Scope(None)
    scope.add("a")
    scope.add("b")
    scope.add("c")
    val vref = VariableRef("zhang")
    val vdef = VariableDef("zhang", StringLiteral("true"))
    val block = Block(List(vdef, vdef, vdef), scope)
    val bin = BinaryExpr(PLUS, block, block)
    val una = UnaryExpr(MINUS, block)
    val assign = AssignExpr(vref, block)
    val ife = IfExpr(vref, block, Some(bin))
    val whe = WhileExpr(vref, block)
    val fdef = FunctionDef("add", List("a", "b"), block)
    val fcall = FunctionCall(vref, List(bin, vdef))
    dumper.dump(fcall)
  }

  def testParser(): Unit = {
    var a =
      """
    def add(a,b) {
       def temp(c){
         a+b+c;
       };
       temp;
    };

    var a = 2;
    var b = 0;

    add(a,b);

    var m = 3;

    while (a > 0) {
       var s = 2;
       a = a - 1;
       b = b + 1;
       s = s + 2;
       m = 1;
    };
"""


    val c = "(a+b)(1,2)(3,4)(5,6);"
    val before = System.currentTimeMillis()
    val checker = new W_SemanticChecker

    val tokens = white.W_Scanner(a)
    println(s"tokens: $tokens")
    if (tokens.isRight) {
      val ast = white.W_Parser(tokens.right.get)
      if (ast.isRight) {
        val result = ast.right.get
        println("--------------------------------\n")
        checker.check(result)
        dumper.dump(result)
      } else {
        println(ast.left.get)
      }
    } else {
      println(tokens.left.get)
    }

    val after = System.currentTimeMillis()
    println(after - before, "ms")
  }

  def temp(ast: W_AST): Unit = {
    ast match {
      // 变量引用
      case VariableRef(name: String) => None

      // 变量声明
      case VariableDef(name: String, initValue: Expr) => None

      // 赋值
      case AssignExpr(variable: VariableRef, value: Expr) => None

      // 代码块
      case Block(block: List[Expr], scope: Scope) => None

      // 数字字面量
      case NumberLiteral(value: Double) => None

      // 字符串字面量
      case StringLiteral(value: String) => None

      // 布尔字面量
      case BoolLiteral(value: Boolean) => None

      // 二元运算
      case BinaryExpr(operator: W_Token, lhs: Expr, rhs: Expr) => None

      // 单目运算
      case UnaryExpr(operator: W_Token, hs: Expr) => None

      // 完整if表达式
      case IfExpr(cond: Expr, body: Expr, else_body: Option[Expr]) => None

      // while表达式
      case WhileExpr(condition: Expr, body: Expr) => None

      // 函数定义
      case FunctionDef(name: String, params: List[String], body: Block) => None

      // 函数调用
      case FunctionCall(func: Expr, args: List[Expr]) => None

      // 模块
      case Module(body: List[Expr], scope: Scope) => None
    }
  }
}
