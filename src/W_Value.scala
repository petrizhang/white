package white {

  abstract class W_Value

  class W_Primitive extends W_Value

  class W_Object extends W_Value

  case class W_Boolean(ivalue: Boolean = false) extends W_Primitive {
    var value: Boolean = ivalue
  }

  case class W_Number(ivalue: Double = 0.0) extends W_Primitive {
    var value: Double = ivalue
  }

  case class W_String(ivalue: String = "") extends W_Primitive {
    var value: String = ivalue
  }

}
