// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;

import edu.berkeley.sbp.*;

public class Demo2 {

    private static Atom atom(char c) {
        return new edu.berkeley.sbp.chr.CharAtom(c); }
    private static Atom atom(char c1, char c2) {
        return new edu.berkeley.sbp.chr.CharAtom(c1, c2); }

    public static void main(String[] s) throws Exception {

        Union expr = new Union("Expr");

        Element[] add   = new Element[] { expr, atom('+'), expr };
        Element[] mult  = new Element[] { expr, atom('*'), expr };
        Element[] paren = new Element[] { atom('('), expr, atom(')') };
        
        Sequence addSequence = Sequence.create("add", add, null);
        Sequence multSequence = Sequence.create("mult", mult, null);

        // uncomment this line to disambiguate
        //multSequence = multSequence.andnot(Sequence.create("add", add, null, false));

        expr.add(Sequence.create(paren, 1));
        expr.add(addSequence);
        expr.add(multSequence);
        expr.add(Sequence.create(atom('0', '9')));

        edu.berkeley.sbp.chr.CharInput input = new edu.berkeley.sbp.chr.CharInput("(1+3*8)*7");

        System.out.println("input:  \""+input+"\"");

        StringBuffer sb = new StringBuffer();
        expr.toString(sb);
        System.out.println("grammar: \n"+sb);

        Forest f = new edu.berkeley.sbp.chr.CharParser(expr).parse(input);
        try {
            System.out.println("output: "+f.expand1().toPrettyString());
        } catch (Ambiguous a) {
            System.err.println(a.toString());
            System.err.println(" ambiguous text: " + input.showRegion(a.getForest().getRegion()));
        }
    }

}
