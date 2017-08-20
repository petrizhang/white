import java.time.LocalDateTime

import white._

object Main {

  def main(args: Array[String]) {
    var a =
      """
{
    if (a == 1 and a > b) {
      a = 1;
      a = a + 2;
      c;
    } else if(a == 2) {
      a = 2;
    } else if(a == 3) {
      a = 3;
    } else {
      a==2;
    };

    while(a == 2) {
      a = 2;
    };

}"""

    var c =
"""
{
if (a==1) {
  a=1
};

while(a == 2) {
  a = 2;
};
}
"""

    val before = System.currentTimeMillis()

    val tokens = white.WhiteScanner(a)
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
    println(after - before,"ms")
  }
}
