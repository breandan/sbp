#import ../src/edu/berkeley/sbp/meta/meta.g as grammar
// this is a testx
s          = ws! (TestCases:: TestCase */ ws) ws!
Input      = "input"  grammar.Quoted ";" /ws
Output     = "output" grammar.Quoted ";" /ws
Outputs::  = Output */ ws
TestCase:: = "testcase" grammar.Quoted "{"
                     Input
                     (^"ignore output;" | Outputs)
                     (SubGrammar:: grammar.Grammar)
                "}" /ws
ws         = grammar.ws
