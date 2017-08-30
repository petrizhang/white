package white {

  abstract class W_Value {
    val valueType: Int
  }

  abstract class W_Primitive extends W_Value

  abstract class W_Object extends W_Value

  case object W_Null extends W_Primitive{
    override val valueType: Int = W_Type.TypeNull
  }

  case class W_Boolean(ivalue: Boolean = false) extends W_Primitive {
    override val valueType: Int = W_Type.TypeBoolean
    var value: Boolean = ivalue
  }

  case class W_Number(ivalue: Double = 0.0) extends W_Primitive {
    override val valueType: Int = W_Type.TypeNumber
    var value: Double = ivalue
  }

  case class W_String(ivalue: String = "") extends W_Primitive {
    override val valueType: Int = W_Type.TypeString
    var value: String = ivalue
  }

  case class W_Function(ivalue: FunctionDef) extends W_Primitive{
    override val valueType: Int = W_Type.TypeFunction
  }

}
