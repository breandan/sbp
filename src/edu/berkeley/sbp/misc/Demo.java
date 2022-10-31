// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;

import edu.berkeley.sbp.*;
import edu.berkeley.sbp.chr.*;
import edu.berkeley.sbp.misc.*;
import edu.berkeley.sbp.meta.*;
import edu.berkeley.sbp.util.*;
import java.util.*;
import java.io.*;

public class Demo {
    /*
    // our grammar class
    public static class Math {

        public static @bind.as("(") Expr parenthesis(Expr e) { return e; }
        public static class Expr implements Reflection.Show { }

        public static @bind.as("Expr") class Numeric extends Expr {
            public @bind.arg String numeric;
        }

        public static class BinOp extends Expr {
            public @bind.arg Expr left;
            public @bind.arg Expr right;
        }

        public static @bind.as("*") class Multiply extends BinOp { }
        public static @bind.as("/") class Divide   extends BinOp { }
        public static @bind.as("+") class Add      extends BinOp { }
        public static @bind.as("-") class Subtract extends BinOp { }
    }


    // invoke "java -jar edu.berkeley.sbp.jar edu.berkeley.sbp.misc.Demo tests/demo.g <expr>"
    public static void main(String[] s) throws Exception {

        Parser metaGrammarParser   = new CharParser(MetaGrammar.newInstance());
        Tree<String> parsedGrammar = metaGrammarParser.parse(new CharInput(new FileInputStream(s[0]))).expand1();
        Grammar.Bindings gbr       = new AnnotationGrammarBindings(Math.class);
        Union   mathGrammar        = Grammar.create(parsedGrammar, "Expr", gbr);
        Parser  mathParser         = new CharParser(mathGrammar);

        System.out.println("about to parse: \""+s[1]+"\"");
        Tree tree = mathParser.parse(new CharInput(new StringReader(s[1]))).expand1();

        // below is ugly voodoo which will go away very soon.  ignore it.
        TreeFunctor tf = (TreeFunctor)tree.head();
        Math.Expr e = (Math.Expr)tf.invoke(tree);
        // above is ugly voodoo which will go away very soon.  ignore it.

        System.out.println("done!");
        System.out.println(Reflection.show(e));
    }
    */
}
