package white {

  import scala.collection.mutable.Map

  object W_Operator {

    import W_Type._

    var opmap = Map(
      (AND, TypeBoolean, TypeBoolean) -> TypeBoolean,
      (OR, TypeBoolean, TypeBoolean) -> TypeBoolean,
      (XOR, TypeBoolean, TypeBoolean) -> TypeBoolean,
      (PLUS, TypeNumber, TypeNumber) -> TypeNumber,
      (MINUS, TypeNumber, TypeNumber) -> TypeNumber,
      (MUL, TypeNumber, TypeNumber) -> TypeNumber,
      (DIV, TypeNumber, TypeNumber) -> TypeNumber,
      (L, TypeNumber, TypeNumber) -> TypeBoolean,
      (LE, TypeNumber, TypeNumber) -> TypeBoolean,
      (E, TypeNumber, TypeNumber) -> TypeBoolean,
      (GE, TypeNumber, TypeNumber) -> TypeBoolean,
      (G, TypeNumber, TypeNumber) -> TypeBoolean
    )
  }

}
