# white
Modern script language based on scala and JVM.

## Overview
White's design goal is to be a mordern and flexible script lauguage.

I'm inspired by several excellent language like scala,ocaml and ruby to improve this language continuously. 

Currently, white's development is staying on parser stage and have simple semantic checker, because the language features are not fix yet.

You could get the ast output for valid code by white parser now.

Below is sample code and it's AST output:

```python
    def add(a,b) {
       def temp(c){
         a+b+c;
       };
       temp;
    };

    var a = 2;
    var b = 0;

    add(a,b);

    var m = 3;

    while (a > 0) {
       var s = 2;
       a = a - 1;
       b = b + 1;
       s = s + 2;
       m = 1;
    };
```

```js
*ast.Module (len=6) {
  scope:{
    add -> W_Function
    a -> W_Null
    b -> W_Null
    m -> W_Null
  }
  0: *ast.FunctionDef {
    name: add
    params: a,b
    body: *ast.Block (len=2) {
      scope:{
        a -> W_Null
        b -> W_Null
      }
      0: *ast.FunctionDef {
        name: temp
        params: c
        body: *ast.Block (len=1) {
          0: *ast.BinaryExpr {
            operator: +
            lhs: *ast.BinaryExpr {
              operator: +
              lhs: *ast.VariableRef { a }
              rhs: *ast.VariableRef { b }
            }
            rhs: *ast.VariableRef { c }
          }
        }
      }
      1: *ast.VariableRef { temp }
    }
  }
  1: *ast.VariableDef {
    name: a
    initValue: 2.0
  }
  2: *ast.VariableDef {
    name: b
    initValue: 0.0
  }
  3: *ast.FunctionCall {
    func: *ast.VariableRef { add }
    args: {
      0: *ast.VariableRef { a }
      1: *ast.VariableRef { b }
    }
  }
  4: *ast.VariableDef {
    name: m
    initValue: 3.0
  }
  5: *ast.WhileExpr {
    cond: *ast.BinaryExpr {
      operator: >
      lhs: *ast.VariableRef { a }
      rhs: 0.0
    }
    body: *ast.Block (len=5) {
      scope:{
        s -> W_Null
      }
      0: *ast.VariableDef {
        name: s
        initValue: 2.0
      }
      1: *ast.AssignExpr {
        variable: a
        value: *ast.BinaryExpr {
          operator: -
          lhs: *ast.VariableRef { a }
          rhs: 1.0
        }
      }
      2: *ast.AssignExpr {
        variable: b
        value: *ast.BinaryExpr {
          operator: +
          lhs: *ast.VariableRef { b }
          rhs: 1.0
        }
      }
      3: *ast.AssignExpr {
        variable: s
        value: *ast.BinaryExpr {
          operator: +
          lhs: *ast.VariableRef { s }
          rhs: 2.0
        }
      }
      4: *ast.AssignExpr {
        variable: m
        value: 1.0
      }
    }
  }
}
```

