package white {

  sealed trait WhiteToken

  /// 标识符
  case class IDENTIFIER(str: String) extends WhiteToken {
    override def toString: String = str
  }

  /// 字符字面量
  case class STR_LITERAL(value: String) extends WhiteToken {
    override def toString: String = value
  }

  case class NUMBER_LITERAL(value: Double) extends WhiteToken {
    override def toString: String = value.toString
  }

  case class BOOL_LITERAL(value: Boolean) extends WhiteToken {
    override def toString: String = value.toString
  }

  case object PLUS extends WhiteToken {
    override def toString: String = "+"
  }

  case object MINUS extends WhiteToken {
    override def toString: String = "-"
  }

  case object MUL extends WhiteToken {
    override def toString: String = "*"
  }

  case object DIV extends WhiteToken {
    override def toString: String = "/"
  }

  /// 小于 <
  case object L extends WhiteToken {
    override def toString: String = "<"
  }

  /// 大于 >
  case object G extends WhiteToken {
    override def toString: String = ">"
  }

  /// 等于 ==
  case object E extends WhiteToken {
    override def toString: String = "=="
  }

  /// 小于等于 <=
  case object LE extends WhiteToken {
    override def toString: String = "<="
  }

  /// 大于等于 》=
  case object GE extends WhiteToken {
    override def toString: String = ">="
  }

  /// 大于等于 》=
  case object ASSIGN extends WhiteToken {
    override def toString: String = "="
  }

  /// and
  case object AND extends WhiteToken {
    override def toString: String = "and"
  }

  /// or
  case object OR extends WhiteToken {
    override def toString: String = "or"
  }

  /// xor
  case object XOR extends WhiteToken {
    override def toString: String = "xor"
  }

  /// not
  case object NOT extends WhiteToken {
    override def toString: String = "not"
  }

  /// if
  case object IF extends WhiteToken {
    override def toString: String = "IF"
  }

  /// if
  case object ELSE extends WhiteToken {
    override def toString: String = "ELSE"
  }

  /// while
  case object WHILE extends WhiteToken {
    override def toString: String = "WHILE"
  }

  /// (
  case object L_BRACKET extends WhiteToken {
    override def toString: String = "("
  }

  /// )
  case object R_BRACKET extends WhiteToken {
    override def toString: String = ")"
  }

  /// {
  case object L_BRACE extends WhiteToken {
    override def toString: String = "{"
  }

  /// {
  case object R_BRACE extends WhiteToken {
    override def toString: String = "}"
  }

  /// ;
  case object SEM extends WhiteToken {
    override def toString: String = ";"
  }
}