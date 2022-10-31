// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.meta.*;
import edu.berkeley.sbp.chr.*;
import edu.berkeley.sbp.util.*;

public class RegressionTests {

    public static boolean yes = false;
    public static boolean graph = false;
    public static GrammarAST.ImportResolver resolver = new GrammarAST.ImportResolver() {
            public InputStream getImportStream(String importname) {
                try {
                    return new FileInputStream("tests/"+importname);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

    public static void main() throws Exception {
        main(new String[] { null, "tests/testcase.g", "tests/regression.tc" });
    }

    public static void main(String[] s) throws Exception {
        try {
            boolean profile = false;
            if (s[0].equals("-graph")) {
                graph = true;
                String[] s2 = new String[s.length-1];
                System.arraycopy(s, 1, s2, 0, s2.length);
                s = s2;
            }
            if (s[0].equals("-profile")) {
                profile = true;
                String[] s2 = new String[s.length-1];
                System.arraycopy(s, 1, s2, 0, s2.length);
                s = s2;
            }

            CharParser cp;
            Tree<String> res;

            InputStream metaGrammarStream =
                s[0] == null
                ? RegressionTests.class.getClassLoader().getResourceAsStream("edu/berkeley/sbp/meta/meta.g")
                : new FileInputStream(s[0]);
            res = new CharParser(GrammarAST.getMetaGrammar()).parse(metaGrammarStream).expand1();
            Union meta = GrammarAST.buildFromAST(res, "s", resolver);
            cp = new CharParser(meta);

            System.err.println("serializing grammar to grammar.ser...");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("grammar.ser"));
            oos.writeObject(cp);
            oos.close();

            System.err.println("deserializing grammar from grammar.ser...");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("grammar.ser"));
            cp = (CharParser)ois.readObject();
            ois.close();

            System.err.println("parsing " + s[1]);
            res = new CharParser(meta).parse(new FileInputStream(s[1])).expand1();

            Union testcasegrammar = GrammarAST.buildFromAST(res, "s", resolver);
            if (testcasegrammar==null) return;
            CharParser parser = new CharParser(testcasegrammar);

            if (profile) {
                System.out.println("\nready...");
                System.in.read();
            }
            System.gc();
            long now = System.currentTimeMillis();
            System.err.println("parsing " + s[2]);
            Forest<String> r2 = parser.parse(new FileInputStream(s[2]));
            System.out.println();
            System.out.println("elapsed = " + (System.currentTimeMillis()-now) + "ms");
            if (profile) {
                System.out.println("\ndone");
                System.in.read();
                System.exit(0);
            }
            System.err.println("expanding...");

            ArrayList<TestCase> cases = new ArrayList<TestCase>();
            Tree tt = r2.expand1();
            for(int i=0; i<tt.size(); i++) {
                Tree t = tt.child(i);
                String[] expect = !"ignore output;".equals(t.child(2).head()) ? new String[t.child(2).size()] : null;
                if (expect != null)
                    for(int j=0; j<t.child(2).size(); j++)
                        expect[j] = stringifyChildren(t.child(2).child(j));
                cases.add(new TestCase(stringifyChildren(t.child(0)),
                                       stringifyChildren(t.child(1)),
                                       expect,
                                       GrammarAST.buildFromAST(t.child(3), "s", resolver),
                                       false,
                                       false));
                
            }
            TestCase[] expanded = new TestCase[cases.size()];
            for(int i=0; i<expanded.length; i++)
                expanded[i] = cases.get(i);
            for(TestCase tc : expanded)
                tc.execute();

        } catch (Throwable t) {
            System.err.println("\n\nexception thrown, class == " + t.getClass().getName());
            System.err.println(t);
            System.err.println();
            t.printStackTrace();
            System.err.println();
        }
    }

    private static String stringifyChildren(Tree t) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<t.size(); i++) {
            sb.append(t.child(i).head());
            sb.append(stringifyChildren(t.child(i)));
        }
        return sb.toString();
    }

    public static class TestCase {
        private final boolean jav;
        public /*final*/ String input;        
        public final String[] output;
        public final Union grammar;
        public final String name;

        public TestCase(String name, String input, String[] output,
                        Union grammar, boolean tib, boolean jav) {
            this.name = name;
            this.jav = jav;
            this.input = input;
            this.output = output;
            this.grammar = grammar;
        }
        public String toString() {
            String ret = "testcase {\n" + "  input \""+input+"\";\n";
            if (output != null)
                for(String s : output) ret += "  output \""+s+"\";\n";
            ret += grammar +"\n}\n";
            return ret;
        }
        public boolean execute() throws Exception {
            Forest<String> res = null;
            ParseFailed pfe = null;
            CharParser parser = new CharParser(grammar);
            System.out.print("     "+name+"\r");
            try {
                res = parser.parse(new StringReader(input));
            } catch (ParseFailed pf) { pfe = pf; }

            if (graph) {
                FileOutputStream fos = new FileOutputStream("out.dot");
                PrintWriter p = new PrintWriter(new OutputStreamWriter(fos));
                GraphViz gv = new GraphViz();
                res.toGraphViz(gv);
                gv.dump(p);
                p.flush();
                p.close();
                System.out.println(parser);
            }

            if (output==null) {
                System.out.println("\r\033[32mDONE\033[0m "+name);
                return true;
            }
            Iterable<Tree<String>> results =
                res==null ? new HashSet<Tree<String>>() : res.expand();

            System.out.print("\r");
            if (results == null || (!results.iterator().hasNext() && (output!=null && output.length > 0))) {
                System.out.print("\033[31m");
                System.out.print("FAIL ");
                System.out.println("\033[0m "+name);
                if (pfe != null) pfe.printStackTrace();
            } else {
                System.out.print("\r                                                                                                              \r");
            }
            HashSet<String> outs = new HashSet<String>();
            if (output != null) for(String s : output) outs.add(s.trim());
            boolean bad = false;
            for (Tree<String> r : results) {
                String s = r.toString().trim();
                if (outs.contains(s)) { outs.remove(s); continue; }
                if (!bad) System.out.println(input);
                System.out.print("\033[33m");
                System.out.println("     GOT: " + s);
                bad = true;
            }
            for(String s : outs) {
                if (!bad) System.out.println(input);
                System.out.print("\033[31m");
                System.out.println("EXPECTED: " + s);
                bad = true;
            }
            if (bad) {
                System.out.println("\033[0m");
                return true;
            }             
            System.out.println("\r\033[32mPASS\033[0m "+name);

            return false;
        }
    }

    private static String pad(int i,String s) { return s.length() >= i ? s : pad(i-1,s)+" "; }
    public static String string(Tree<String> tree) {
        String ret = "";
        if (tree.head()!=null) ret += tree.head();
        ret += string(tree.children());
        return ret;
    }
    public static String string(Iterable<Tree<String>> children) {
        String ret = "";
        for(Tree<String> t : children) ret += string(t);
        return ret;
    }
}
