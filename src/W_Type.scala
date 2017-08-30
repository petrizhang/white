package white {

  class W_Type {
  }

  object W_Type {
    val TypeNull = 0
    val TypeBoolean = 1
    val TypeNumber = 2
    val TypeString = 3
    val TypeFunction = 4

    var nextTypeId = 5

    var typeMap = Map(
      TypeBoolean -> ("Boolean", W_Boolean()),
      TypeNumber -> ("Number", W_Number()),
      TypeString -> ("String", W_String())
    )
  }

}
