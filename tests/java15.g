
s = ws! CompilationUnit ws!

CompilationUnit =
   CompilationUnit:: Package? Import* (InterfaceDecl|ClassDecl)*  /ws

Package = Annotations?! "package" PackageName ";" /ws

Annotations = ()

Import = "import"           TypeName       ";" /ws
       | "import"          (TypeName ".*") ";" /ws
       | "import" "static"  TypeName       ";" /ws
       | "import" "static" (TypeName ".*") ";" /ws

TypeName      = Identifier +/ "."
PackageName   = Identifier +/ "."

Modifiers     = Modifiers:: ("public" | "protected" | "private") (ws! "abstract")?
              | Modifiers:: "abstract"

ClassDecl     = Class::     Modifiers "class"     TypeDecl ClassBody /ws
InterfaceDecl = Interface:: Modifiers "interface" TypeDecl ClassBody /ws

TypeDecl =                   Identifier
         | GenericTypeDecl:: Identifier "<" (TypeArg +/ comma) ">" /ws

TypeArg =                Identifier
        | Extends::      Identifier "extends" Type  /ws
        | Super::        Identifier "super"   Type  /ws
        | Exist::        "?"
        | ExistExtends:: "?" "extends" Type
        | Intersect::    Identifier "&" Type

ClassBody = "{" (BodyDecl +/ ws) "}" /ws
          | "{"     ws!          "}"

BodyDecl = FieldDecl | MethodDecl | ClassDecl | InterfaceDecl

FieldDecl  = Field::  Modifiers Type Identifier ";" /ws
MethodDecl = Method:: MethodHeader (";" | MethodBody) /ws

MethodHeader  = MethodHeader:: Modifiers Type Identifier Args /ws
MethodBody    = "{" "}" /ws

Args = "(" (Arg+/comma) ")" /ws
     | "(" ws! ")"
Arg = Arg:: Type Identifier /ws

Type        = BareType | GenericType | ArrayType
BareType    = Type::        TypeName | "boolean" | "int" | "double" | "float" | "char" | "short" | "long" | "void"
GenericType = GenericType:: TypeName "<" (Type+/comma) ">" /ws
ArrayType   = ArrayOf::     (BareType | GenericType) "[]" /ws

ws = [\r\n ]**
comma = ws! "," ws!

JavaLetter = [a-zA-Z_$]
Identifier = JavaLetter++
           &~ ([0-9]! ~[]*!)
           &~ Keyword
           &~ BooleanLiteral
           &~ NullLiteral
BooleanLiteral = "true" | "false"
NullLiteral    = "null"

// http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#29542
//
//HexDigit = 
//UnicodeEscape = "\\u" [0-9] [0-9] [0-9] [0-9]      // this is valid even inside strings/comments
//
//// Ctrl-Z
//// Unicode escapes (including \\u garbage)
//
//WhiteSpace = [\r\n\t ]
//Comment = "/*" (~[]* &~ (~[]*! "*/" ~[]*!)) "*/"
//        | "//" [^\r\n]* [\r\n]!
//
//Token   = Identifier | Keyword | Literal | Separator | Operator
//
//
//Literal =
//      | IntegerLiteral
//      | FloatingPointLiteral
//      | BooleanLiteral
//      | CharacterLiteral
//      | StringLiteral
//      | NullLiteral
//
//
//FloatLiteral        = (DecimalFloatLiteral > HexFloatLiteral) [dDfF]?
//DecimalFloatLiteral =             [0-9]++       "." [0-9]++       [ep] [+-]? [0-9]++
//HexFloatLiteral     = ("0x"|"0X") [0-9a-fA-F]++ "." [0-9a-fA-F]++ [ep] [+-]? [0-9a-fA-F]++
//
//CharLiteral   = "'"  ([^\\\'] | "\\" ~[])  "'"
//StringLiteral = "\"" ([^\\\"] | "\\" ~[])* "\""
//
//IntegerLiteral = (DecimalLiteral | OctalLiteral | HexLiteral) [lL]?
//DecimalLiteral = [0-9]++     &~ OctalLiteral
//OctalLiteral   = "0" [0-9]++ &~ "0"
//HexLiteral     = ("0x"|"0X") [0-9a-fA-F]++
//

Keyword = 
         "abstract"   | "continue"   | "for"          | "new"         | "switch"
       | "assert"     | "default"    | "if"           | "package"     | "synchronized"
       | "boolean"    | "do"         | "goto"         | "private"     | "this"
       | "break"      | "double"     | "implements"   | "protected"   | "throw"
       | "byte"       | "else"       | "import"       | "public"      | "throws"
       | "case"       | "enum"       | "instanceof"   | "return"      | "transient"
       | "catch"      | "extends"    | "int"          | "short"       | "try"
       | "char"       | "final"      | "interface"    | "static"      | "void"
       | "class"      | "finally"    | "long"         | "strictfp"    | "volatile"
       | "const"      | "float"      | "native"       | "super"       | "while"

// We don't obey this: 
//
//   "The longest possible translation is used at each step, even if
//   "the result does not ultimately make a correct program while
//   "another lexical translation would. Thus the input characters
//   "a--b are tokenized (-3.5) as a, --, b, which is not part of any
//   "grammatically correct program, even though the tokenization a,
//   "-, -, b could be part of a grammatically correct program.
