s = Expr [\n ]!*

Expr = Expr ^"+" Expr /ws
     | Expr ^"-" Expr /ws
     >
       Expr ^"*" Expr /ws
     | Expr ^"/" Expr /ws

     | ^"(" Expr  ")" /ws

     | numeric::[0-9]++

ws = [ \r\n]!**



