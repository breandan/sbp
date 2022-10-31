// draft grammar for [La]TeX
// note that this is approximate; in reality TeX has no grammar

Text            = ()
                | Token
                | Text:: `Text                  Token
                | Text:: `Text (WS:: ws)        Token
                | Text:: `Text (BlankLine:: br) Token

Token           = Command
                | Comment::     "%" ~[\n]* "\n"
                | EscapedChar:: "\\" ~[a-zA-Z\\]
                | Environment:: "\\begin{" EnvironmentName "}" Text "\\end{" EnvironmentName! "}"
                | Word::        ~[\\\r\n\ %{}]++
                | LineBreak::   "\\\\"
                | "$"  (Math:: ~[$]+) "$"
                | "$$" (Math:: ~[$]+) "$$"
                | Verbatim
                | Table
             // | WiX

Command::       = "\\" CommandName         ws              -> ~[{]
                | "\\" CommandName OptArgs ws "{" Text "}"
CommandName     = [A-Za-z0-9]++ &~ "begin" | "end"
OptArgs::       = ws ("[" Arg "]") +/ ws
                | ()

Table           = "\\begin{tabular}"
                  TableRow +/ (ws "\\\\" ws)
                  "\\end{tabular}"
TableRow        = TableCol +/ (ws "&" ws)
TableCol        = Text &~ ... "\\\\" ...

EnvironmentName = [A-Za-z0-9]++ &~ "verbatim" | "tabular"

Verbatim        = "\\begin{verbatim}"
                  (Verb:: ... &~ ... "\\end{verbatim}")
                  "\\end{verbatim}"

hws!            = [ \r]
ws!             = (hws* "\n")? hws*                   -> ~[ \n\r]
nl!             = [ \r]* "\n"  [ \r]*                 -> ~[ \n\r]
br!             = [ \r]* "\n"  [ \r]*  "\n" [ \r\n]*  -> ~[ \n\r]

// import wix.g as wix
// WiX       = "\\begin{wix}\n"
//             wix.Pars
//             "\n\\end{verbatim}"




