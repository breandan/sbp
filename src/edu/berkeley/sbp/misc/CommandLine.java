// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;

import edu.berkeley.sbp.*;
import edu.berkeley.sbp.misc.*;
import edu.berkeley.sbp.meta.*;
import edu.berkeley.sbp.chr.*;
import java.io.*;

public class CommandLine {
    public static void main(String[] argv) throws Throwable {
        String grammarFile = argv[0];
        String targetFile = argv[1];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setErr(new PrintStream(baos));
            Tree<String> res = new CharParser(MetaGrammar.getMetaGrammar()).parse(new FileInputStream(grammarFile)).expand1();
            Union meta = GrammarAST.buildFromAST(res, "s", null);
            CharInput input = new CharInput(new FileInputStream(targetFile), "", true);
            Tree ret = new CharParser(meta).parse(input).expand1();
            if (ret==null) throw new NullPointerException("CharParser returned null");
            System.out.println(ret);
            System.exit(0);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(new String(baos.toByteArray()));
            System.exit(-1);
        }
    }
}
