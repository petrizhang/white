package white {

  /// 检查项目
  /// - 运算是否合法(类型)
  /// - 函数调用参数数量是否正确
  /// - 变量引用是否合法(变量未定义)
  /// - 变量名是否合法(函数名被覆盖)
  /// 以及生成作用域树
  class W_SemanticChecker {
    var curScope: Scope = new Scope(None)

    var occurErrors = false

    /// 生成作用域,同时检查部分语义错误
    def check(ast: W_AST): Unit = {
      ast match {
        case m@Module(list, scope) => {
          m.scope = this.curScope
          list.foreach(check(_))
          this.goParent()
        }

        case b@Block(list, scope) => {
          this.curScope = new Scope(Some(curScope))
          b.scope = this.curScope
          list.foreach(check(_))
          this.goParent()
        }

        case VariableDef(name, initValue) => {
          check(initValue)
          // 向符号表添加一个变量
          if (!this.curScope.add(name)) {
            W_Error.varReDefine(name)
          }
        }

        case f@FunctionDef(name, params, body) => {
          // 向符号表添加此函数
          this.curScope.add(name, W_Function(f))
          // 将参数添加到函数的作用域
          params.foreach(x => body.scope.add(x))
        }

        case VariableRef(name) => {
          if (!this.curScope.contains(name)) {
            W_Error.undefinedVariable(name)
          }
        }

        // 赋值
        case AssignExpr(variable: VariableRef, value: Expr) => {
          check(value)
        }

        // 二元运算
        case BinaryExpr(operator: W_Token, lhs: Expr, rhs: Expr) => {
          check(lhs)
          check(rhs)
        }

        // 单目运算
        case UnaryExpr(operator: W_Token, hs: Expr) => check(hs)

        // 完整if表达式
        case IfExpr(cond: Expr, body: Expr, else_body: Option[Expr]) => {
          check(cond)
          check(body)
          if (else_body.isDefined) {
            check(else_body.get)
          }
        }

        // while表达式
        case WhileExpr(condition: Expr, body: Expr) => {
          check(condition)
          check(body)
        }

        // 函数调用
        case FunctionCall(func: Expr, args: List[Expr]) => {
          check(func)
          // 检查参数
          func match {
            case VariableRef(name: String) => {
              val value = curScope.get(name).get
              value match {
                case W_Function(fdef) => {
                  val expect = fdef.params.length
                  val given = args.length
                  if (expect != given) {
                    W_Error.wrongParams(name, expect, given)
                  }
                }
                case _ => None
              }
            }
            case _ => None
          }
          args.foreach(check(_))
        }

        case _ => None
      }
    }

    def goParent(): Unit = {
      val parent = this.curScope.getParent()
      if (parent.isDefined) {
        this.curScope = parent.get
      }
    }
  }


}