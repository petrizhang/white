package white {

  /// 检查项目
  /// - 运算是否合法(类型)
  /// - 函数调用参数数量是否正确
  /// - 变量引用是否合法(变量未定义)
  /// - 变量名是否合法(函数名被覆盖)
  /// 以及生成作用域树
  object W_SemanticChecker {

    def entry(module: Module): Unit = {

    }

    def check(ast: W_AST): Unit = {
      ast match {
        // 变量引用
        case VariableRef(name: String) => None

        // 变量声明
        case VariableDef(name: String, initValue: Expr) => None

        // 赋值
        case AssignExpr(variable: VariableRef, value: Expr) => None

        // 代码块
        case Block(block: List[Expr], scope: Scope) => None

        // 数字字面量
        case NumberLiteral(value: Double) => None

        // 字符串字面量
        case StringLiteral(value: String) => None

        // 布尔字面量
        case BoolLiteral(value: Boolean) => None

        // 二元运算
        case BinaryExpr(operator: W_Token, lhs: Expr, rhs: Expr) => None

        // 单目运算
        case UnaryExpr(operator: W_Token, hs: Expr) => None

        // 完整if表达式
        case IfExpr(cond: Expr, body: Expr, else_body: Option[Expr]) => None

        // while表达式
        case WhileExpr(condition: Expr, body: Expr) => None

        // 函数定义
        case FunctionDef(name: String, params: List[String], body: Block) => None

        // 函数调用
        case FunctionCall(func: Expr, args: List[Expr]) => None

        // 模块
        case Module(body: List[Expr], scope: Scope) => None
      }
    }

    /// 生成作用域,同时检查变量引用情况
    def genScope(ast: W_AST): Unit = {
      ast match {
        case m@Module(list, scope) => {
          m.scope = curScope
          list.foreach(genScope(_))
        }

        case Block(list, scope) => {
          this.curScope = new Scope(Some(curScope))
          list.foreach(genScope(_))
        }

        case VariableDef(name, initValue) => {
          genScope(initValue)
          // 向符号表添加一个变量
          if (!this.curScope.add(name)) {
            W_Error.varReDefine(name)
          }
        }

        case f@FunctionDef(name, params, body) => {
          // 向符号表添加一个函数
          curScope.add(name, W_Function(f))
          params.foreach(x => body.scope.add(name))
        }

        case VariableRef(name) => {
          if (!this.curScope.contains(name)) {
            W_Error.undefinedVariable(name)
          }
        }

        // 赋值
        case AssignExpr(variable: VariableRef, value: Expr) => {
          genScope(value)
        }

        // 二元运算
        case BinaryExpr(operator: W_Token, lhs: Expr, rhs: Expr) => {
          genScope(lhs)
          genScope(rhs)
        }

        // 单目运算
        case UnaryExpr(operator: W_Token, hs: Expr) => genScope(hs)

        // 完整if表达式
        case IfExpr(cond: Expr, body: Expr, else_body: Option[Expr]) => {
          genScope(cond)
          genScope(body)
          if (else_body.isDefined) {
            genScope(else_body.get)
          }
        }

        // while表达式
        case WhileExpr(condition: Expr, body: Expr) => {
          genScope(condition)
          genScope(body)
        }

        // 函数调用
        case FunctionCall(func: Expr, args: List[Expr]) => args.foreach(genScope(_))

        case _ => return
      }

      val parent = this.curScope.getParent()
      if (parent.isDefined) {
        this.curScope = parent.get
      }
    }

    var curScope: Scope = new Scope(None)
  }

}