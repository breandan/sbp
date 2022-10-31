// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.meta;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.chr.*;
import edu.berkeley.sbp.misc.*;
import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.io.*;

public class MetaGrammar {

    /** Used to rebuild MetaGrammar.java, and not for much else */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: java " + MetaGrammar.class.getName() + " grammarfile.g com.yourdomain.package.ClassName");
            System.exit(-1);
        }
        String className   = args[1].substring(args[1].lastIndexOf('.')+1);
        String packageName = args[1].substring(0, args[1].lastIndexOf('.'));
        String fileName    = packageName.replace('.', '/') + "/" + className + ".java";

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        StringBuffer out = new StringBuffer();

        boolean skip = false;
        for(String s = br.readLine(); s != null; s = br.readLine()) {
            if (s.indexOf("DO NOT EDIT STUFF BELOW: IT IS AUTOMATICALLY GENERATED") != -1 && s.indexOf("\"")==-1) skip = true;
            if (s.indexOf("DO NOT EDIT STUFF ABOVE: IT IS AUTOMATICALLY GENERATED") != -1 && s.indexOf("\"")==-1) break;
            if (!skip) out.append(s+"\n");
        }

        out.append("\n        // DO NOT EDIT STUFF BELOW: IT IS AUTOMATICALLY GENERATED\n");

        Union u = getMetaGrammar();
        Tree t = new CharParser((Union)u).parse(new FileInputStream(args[0])).expand1();

        t.toJava(out);
        out.append("\n        // DO NOT EDIT STUFF ABOVE: IT IS AUTOMATICALLY GENERATED\n");

        for(String s = br.readLine(); s != null; s = br.readLine()) out.append(s+"\n");
        br.close();

        OutputStream os = new FileOutputStream(fileName);
        PrintWriter p = new PrintWriter(new OutputStreamWriter(os));
        p.println(out.toString());
        p.flush();
        os.close();
    }

    static final Tree meta;
    public static Union getMetaGrammar() { return GrammarAST.buildFromAST(meta, "s", null); }

    static {
        Tree t = null;
        try {
            t = 





























































































































































        // DO NOT EDIT STUFF BELOW: IT IS AUTOMATICALLY GENERATED
new edu.berkeley.sbp.Tree(null, "Grammar", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "G", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Colons", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "G", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "D", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "D", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "T", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "#", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "p", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "F", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "#", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "p", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "F", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Colons", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "F", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\r", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\n", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, " ", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "p", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\r", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\n", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, " ", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "T", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "T", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "=", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "H", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "D", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "p", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "T", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "!", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "=", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "H", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "C", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ":", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ":", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "=", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "H", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "=", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "H", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "=", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "H", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Colons", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "H", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Colons", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "E", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "P", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "E", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ":", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ":", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "P", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "P", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "P", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "-", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "P", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "&", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "E", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "E", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "&", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "E", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "!", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ":", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "N", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "T", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "m", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "f", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "L", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "i", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "l", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ")", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "g", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "]", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "?", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "`", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ".", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ".", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ".", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ")", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "H", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "S", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ")", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "!", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { })})})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "^", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Colons", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "W", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "&~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "++", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ".", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "z", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "A", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "Z", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "0", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "9", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "_", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ".", new edu.berkeley.sbp.Tree[] { })})})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Colons", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Q", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "u", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "o", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\"", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "+", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "(", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\"", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\\", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "p", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\"", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\"", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "\"", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "Colons", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "R", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "g", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "-", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "-", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "]", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\\", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, ">", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "->", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "<", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "p", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })})})})})})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "c", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "a", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "p", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "e", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "d", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\n", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\\", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\r", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\\", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "::", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\t", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\\", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\\", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "n", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "r", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "t", new edu.berkeley.sbp.Tree[] { })})})})})})})}),
        new edu.berkeley.sbp.Tree(null, "DropNT", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "RHS", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "|", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "**", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, " ", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\r", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\t", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\n", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Elements", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "**", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, " ", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\r", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\t", new edu.berkeley.sbp.Tree[] { })}),
        new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\n", new edu.berkeley.sbp.Tree[] { })})})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "/", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "*", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "~", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "[", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Range", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\n", new edu.berkeley.sbp.Tree[] { })})})})}),
        new edu.berkeley.sbp.Tree(null, "Literal", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Quoted", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "\n", new edu.berkeley.sbp.Tree[] { })})}),
        new edu.berkeley.sbp.Tree(null, "NonTerminalReference", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "Word", new edu.berkeley.sbp.Tree[] { new edu.berkeley.sbp.Tree(null, "w", new edu.berkeley.sbp.Tree[] { }),
        new edu.berkeley.sbp.Tree(null, "s", new edu.berkeley.sbp.Tree[] { })})})})})})})})
        // DO NOT EDIT STUFF ABOVE: IT IS AUTOMATICALLY GENERATED
                ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        meta = t;
    }
}
































































































































































