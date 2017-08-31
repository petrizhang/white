package white {

  class W_Error

  case class W_ScannerError(msg: String) extends W_Error

  case class W_ParserError(msg: String) extends W_Error

  case class W_SemanticError(msg: String) extends W_Error

  object W_Error {
    def unSupportedOp(op: W_Token, l_type: String, r_type: String): Unit = {
      System.err.println(W_SemanticError(s"unsupported operation '$op' for type '$l_type' and '$r_type'"))
    }

    def wrongParams(func_name: String, expect: Int, given: Int): Unit = {
      System.err.println(W_SemanticError(s"'$func_name'() takes exactly $expect arguments ($given given)"))
    }

    def varReDefine(var_name: String): Unit = {
      System.err.println(W_SemanticError(s"variable redefine '$var_name'"))
    }

    def undefinedVariable(var_name: String): Unit = {
      System.err.println(W_SemanticError(s"using undefined variable '$var_name'"))
    }

    def undefinedFunction(func_name: String): Unit = {
      System.err.println(W_SemanticError(s"using undefined function '$func_name'"))
    }
  }

}