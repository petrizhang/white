import white._

object Main {

  def main(args: Array[String]) {
    var a =
      """if (a == 1) {
      a == 1
    } else if(a == 2) {
      a == 2
    } else if(a == 3) {
      a == 3
    }"""

    var b = "a+b == true"
    var c =
      """if (a==1) 1
        else if(a==2) a==2
        else if(a==3) a==3
        else if(a==4) a==4"""

    val tokens = white.WhiteScanner(a)
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
  }
}
