// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;

import edu.berkeley.sbp.*;
import edu.berkeley.sbp.chr.*;
import edu.berkeley.sbp.misc.*;
import edu.berkeley.sbp.meta.*;
import edu.berkeley.sbp.util.*;
import java.util.*;
import java.io.*;

/** This does not work yet */
public class TestAstGenerator {

    public static GrammarAST.ImportResolver resolver = new GrammarAST.ImportResolver() {
            public InputStream getImportStream(String importname) {
                try {
                    return new FileInputStream("tests/"+importname);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

    public static void main(String[] s) throws Exception {

        Parser metaGrammarParser   = new CharParser(GrammarAST.getMetaGrammar());
        Tree<String> parsedGrammar = metaGrammarParser.parse(new CharInput(new FileInputStream(s[0]))).expand1();
        PrintWriter pw = new PrintWriter(System.out);
        GrammarAST.emitCode(pw, parsedGrammar, "Expr", resolver);
        pw.flush();
    }

}
