// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

// Copyright 2006-2007 the Contributors, as shown in the revision logs.
// Licensed under the Apache Public Source License 2.0 ("the License").
// You may not use this file except in compliance with the License.

package edu.berkeley.sbp.misc;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.misc.*;
import edu.berkeley.sbp.meta.*;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.chr.*;
import java.util.*;
import java.io.*;

public class Java15 {
    /*
    public static void main(String[] s) throws Exception {

        try {
            Tree<String> res = new CharParser(MetaGrammar.newInstance()).parse(new FileInputStream(s[0])).expand1();
            
            //AnnotationGrammarBindings resolver = new AnnotationGrammarBindings(Java15.class);
            Grammar.Bindings resolver = new Grammar.Bindings() {
                    public Object repeatTag() { return ""; }
                };
            Union javaGrammar = Grammar.create(res, "s", resolver);

            System.err.println("parsing " + s[1]);
            Tree t = new CharParser(javaGrammar).parse(new FileInputStream(s[1])).expand1();

            System.out.println("tree:\n" + t.toPrettyString());

        } catch (Ambiguous a) {
            FileOutputStream fos = new FileOutputStream("/Users/megacz/Desktop/out.dot");
            PrintWriter p = new PrintWriter(new OutputStreamWriter(fos));
            GraphViz gv = new GraphViz();
            a.getAmbiguity().toGraphViz(gv);
            gv.dump(p);
            p.flush();
            p.close();
            a.printStackTrace();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    */
}
