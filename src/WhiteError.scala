package white {

  trait WhiteError

  case class WhiteScannerError(msg: String) extends WhiteError

  case class WhiteParsrError(msg: String) extends WhiteError
}