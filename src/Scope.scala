package white {

  class Scope(iparent: Option[Scope]) {

    private var parent: Option[Scope] = iparent

    var table: Map[String, W_Value] = Map[String, W_Value]()

    def setParent(parent: Option[Scope]): Unit = {
      this.parent = parent
    }

    def getParent(): Option[Scope] = this.parent

    def add(name: String, value: W_Value = W_Null): Boolean = {
      if (table.contains(name)) {
        return false
      }
      table += (name -> value)
      return true
    }

    def set(name: String, value: W_Value): Unit = {
      table.updated(name, value)
    }

    def get(name: String): Option[W_Value] = {
      table.get(name)
    }

    def contains(name: String): Boolean = {
      if (table.contains(name)) {
        return true
      }
      if (parent.isEmpty) {
        return false
      }
      return parent.get.contains(name)
    }

    override def toString = table.toString
  }

}
