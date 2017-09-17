
package white {

  object outer {

    implicit def convert[T](value: T): W_Object[T] = {
      new W_Object[T](value)
    }

    implicit def rconvert[T](obj: W_Object[T]): T = {
      obj.value
    }

    class W_Object[T](ivalue: T) {
      var value: T = ivalue

      def +(rhs: W_Object[T]): W_Object[T] = {
        (value, rhs.value) match {
          case (x: Int, y: Int) => (x + y).asInstanceOf[T]
          case (x: String, y: String) => (x + y).asInstanceOf[T]
          case (x, y) => throw new NoSuchMethodError(s"unsupported operand type(s) for +: '${value.getClass()}' and '${rhs.getClass()}'")
        }
      }

      def -(rhs: W_Object[T]): W_Object[T] = {
        throw new NoSuchMethodError
      }

      def *(rhs: W_Object[T]): W_Object[T] = {
        throw new NoSuchMethodError
      }

      def /(rhs: W_Object[T]): W_Object[T] = {
        throw new NoSuchMethodError
      }

      def and(rhs: W_Object[T]): W_Object[T] = {
        throw new NoSuchMethodError
      }

      def or(rhs: W_Object[T]): W_Object[T] = {
        throw new NoSuchMethodError
      }

      def xor(rhs: W_Object[T]): W_Object[T] = {
        throw new NoSuchMethodError
      }

      def getAttr(name: String): W_Object[T] = {
        throw new NoSuchMethodError
      }

      def setAtrr(name: String, value: Object) = {
        throw new NoSuchMethodError
      }
    }


    def add[T](a: W_Object[T], b: W_Object[T]): W_Object[T] = {
      a.getAttr("x") + b.getAttr("x")
    }

    def plus[T](a: W_Object[T], b: W_Object[T]): W_Object[T] = {
      a + b
    }

  }

}
