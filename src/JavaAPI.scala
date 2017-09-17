package white {

  import scala.reflect._

  object JavaAPI {

    def load(): Unit = {
      val code ="""
import System;

var a = new String("zhang");

System.out.println(a);

def add(a,b){
  return a.a + b.b;
};
"""
      val a = new java.lang.String
      val SystemClass = Class.forName("java.lang.String")
      var s = SystemClass.getConstructors
      println(s(0))
      println(s)
    }


  }

}
