package white {

  trait W_Error

  case class W_ScannerError(msg: String) extends W_Error

  case class W_ParsrError(msg: String) extends W_Error
}