s = Expr [\n ]!*

Expr = Plus::  Expr "+" Expr
     | Minus:: Expr "-" Expr
     | Num

Num:: = [0-9]++
