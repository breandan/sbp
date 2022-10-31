//funkanomitron
s             = ws Grammar ws

Grammar::     = Declaration +/ ws

Declaration   = NonTerminal
              | ^"#import" FileName            /ws
              | ^"#import" FileName "as" Word  /ws

FileName::    = (~[\r\n ] | escaped)+ -> [\r\n ]

NonTerminal   = NonTerminal:: Word            ws  "=" ws RHS
              | DropNT::      Word  "!"       ws  "=" ws RHS
              | Colons::      Word  "::"      ws  "=" ws RHS
              |               Word  "*"       ws ^"=" ws RHS
              |               Word  "*/" Word ws ^"=" ws RHS
       
RHS::         = ("|":: Sequence +/ (ws "|" ws)) +/ (ws (">" -> ~">") ws)

Elements::    = e*/ws

PreSequence   = Elements
              | (Quoted|Word)   ^"::" PreSequence /ws
              > PreSequence     ^"/"      e       /ws
              | PreSequence     ^"->"     e       /ws

Sequence      = PreSequence
              | Sequence ^"&"  Elements /ws
              | ^"~~"  Elements /ws
              | Sequence ^"&~" Elements /ws

e             =                                   e ^"!"
              >                (Quoted|Word) ^":" e
              >                NonTerminalReference:: Word
              |                Literal:: Quoted
              |                  ^"()"
              |                  ^"["  Range* "]"
              |                e ^"++"                   /ws -> ~[/]
              |                e ^"+"                    /ws -> ~[+]
              |                e ^"++/" e                /ws        
              |                e ^"+/"  e                /ws        
              |                e ^"**"                   /ws -> ~[/]
              |                e ^"*"                    /ws -> ~[*]
              |                e ^"**/" e                /ws        
              |                e ^"*/"  e                /ws        
              |                e ^"?"                    /ws
              |                  ^"^"   Quoted
              |                  ^"`"   e
              |                  ^"..."
              |                   "(" Word  ^")"
              >                  ^"(" RHS  ")"           /ws
              |                 "~":: ("~" -> ~"~")!  e
              |                  ^">>"
              |                  ^"<<"

Word::        = [.a-zA-Z0-9_]++ &~ "."+
Quoted::      = "\"" (~[\"\\] | escaped)+ "\""
              | "\"\""

Range::       = ec
              | ec "-" ec
              | "<<":: [<][<]
              | ">>":: [>][>]

ec            = ~[\-\]\\<>]
              | [>] -> ~[>]
              | [<] -> ~[<]
              | escaped
escaped       = "\n"::  "\\n"
              | "\r"::  "\\r"
              | "\t"::  "\\t"
              |         "\\" ~[nrt]

ws!          = [ \r\t\n]**
             | [ \r\t\n]** "//" ~[\n]* "\n" ws
