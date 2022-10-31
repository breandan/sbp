// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.Sequence.Pos;
import edu.berkeley.sbp.Sequence.Pos;
import edu.berkeley.sbp.GSS.Phase;
import edu.berkeley.sbp.StateNode;
import edu.berkeley.sbp.util.*;
import java.io.*;
import java.util.*;

/** thrown when the parser arrives at a state from which it is clear that no valid parse can result */
public class ParseFailed extends Exception {

    private final Input.Location location;
    private final Input.Region region;
    private final Input input;
    private final String message;
    private final GSS gss;
    ParseFailed() { this("", null, null, null); }
    ParseFailed(String message, Input.Region region, Input input, GSS gss) {
        this.region = region;
        this.location = region.getStart();
        this.message = message;
        this.input = input;
        this.gss = gss;
    }
    public Input.Location getLocation() { return location; }
    private Input.Region getRegion() { return region; }
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(message);
        ret.append('\n');
        return ret.toString();
    }

    private static boolean important(Pos p) {
        if (p.isLast()) return false;
        if (p.element() == null) return false;
        if (!(p.element() instanceof Union)) return false;
        Union u = (Union)p.element();
        if (u.isSynthetic()) return false;
        if (u.getName()==null) return false;
        if (u.getName().length() == 0) return false;
        char c = u.getName().charAt(0);
        return (c >= 'A' && c <= 'Z');
    }

    static <Tok> void barf(HashMap<Element,Input.Location> sb, StateNode n, int indent, boolean skip, int count, Input.Location loc) {
        if (count <= 0) {
            barf(sb, n, indent, skip, loc);
        } else {
            /*
              FIXME: removed
            for(StateNode nn : (Iterable<StateNode>)n.parents())
                barf(sb, nn, indent, skip, count-1, n.phase().getLocation());
            */
        }
    }
    static <Tok> void barf(HashMap<Element,Input.Location> sb, StateNode n, int indent, boolean skip, Input.Location loc) {
        if (touched.contains(n)) return;
        touched.add(n);
        String s = "";
        for(int i=0; i< indent; i++) s += " ";
        StateNode parent = n;
        boolean done = false;
        boolean alldone = false;
        boolean go = false;
        boolean force = false;
        for(Pos pp : (Iterable<Pos>)parent.state().positions()) {
            Pos p = (Pos)pp;
            if (skip) p = p.next();
            int raise = 0;
            done = false;
            while(p != null) {
                if (p.isLast()) break;
                if (important(p)) {
                    Input.Location l = sb.get(p.element());
                    if (l == null || l.compareTo(loc) < 0)
                        sb.put(p.element(), loc);
                    done = true;
                    alldone = true;
                }
                /*
               else if (p.pos-raise > 0)
                    barf(sb, n, indent, false, 1);
                if (!new Grammar(null, null).possiblyEpsilon(p.element()))
                    break;
                */
                p = p.next();
                raise++;
                if (p.isLast()) {
                    if (!done) barf(sb, n, indent, true, 1, loc);
                    break;
                }
            }
        }
        if (!alldone) barf(sb, n, indent, false, 1, loc);
    }



    // FIXME
    private static HashSet<StateNode> touched = new HashSet<StateNode>();
    static <Tok> void complain(StateNode n, HashMap<String,HashSet<String>> errors, boolean force, int indent) {
        if (touched.contains(n)) return;
        touched.add(n);
        for(Pos p : (Iterable<Pos>)n.state()) {
            //if (!p.isLast() && !p.next().isLast()) continue;
            if (((p.isFirst() || p.isLast()) && !force)/* || p.owner().name==null*/ ||
                !important(p)) {
            /*
              FIXME: removed
                for(StateNode n2 : n.parents())
                    complain(n2, errors, force
                    //| p.isFirst()
                , indent);
            */
            } else {
                String seqname = p.owner()/*.name*/+"";
                HashSet<String> hs = errors.get(seqname);
                if (hs==null) errors.put(seqname, hs = new HashSet<String>());
                String s = "";
                hs.add(" "+p.element()+"");
            }
        }
    }

    static String el(Object e) {
        String s = e.toString();
        if (s.length()==0 || s.charAt(0)!='\"' || s.charAt(s.length()-1)!='\"') return ANSI.yellow(s);
        s = s.substring(1);
        s = s.substring(0, s.length()-1);
        StringBuffer ret = new StringBuffer();
        for(int i=0; i<s.length(); i++) {
            if (s.charAt(i)=='\\' && i<s.length()-1) ret.append(s.charAt(++i));
            else ret.append(s);
        }
        return ANSI.purple(ret.toString());
    }

    static void error(String message, GSS.Phase phase, Object token, Input.Region region) throws ParseFailed {
        error(message,
              token,
              /*phase*/null,
              region,
              phase.getGSS().getInput(),
              phase.getGSS());
    }
    private static void error(String message,
                              Object token,
                              Iterable<StateNode> nodes,
                              Input.Region region,
                              Input input,
                              GSS gss) throws ParseFailed{
        String lookAhead = token==null ? "<EOF>" : token.toString();
        StringBuffer ret = new StringBuffer();
        ret.append(ANSI.bold(ANSI.red(message)));
        String toks = token+"";
        ret.append(" at ");
        ret.append(ANSI.yellow(region+""));
        if (input != null) {
            ret.append('\n');
            ret.append("     text: ");
            int budget = 60;
            String second = input.showRegion(region, 60);
            budget -= second.length();
            Input.Location after = region.getEnd();
            for(int i=0; i<10; i++) after = after.next() == null ? after : after.next();
            String third = input.showRegion(region.getEnd().createRegion(after), 60);
            budget -= third.length();
            Input.Location before = region.getStart();
            for(int i=0; i<budget; i++) before = before.prev() == null ? before : before.prev();
            String first = input.showRegion(before.createRegion(region.getStart()), 60);
            ret.append(ANSI.cyan(first));
            ret.append(ANSI.invert(ANSI.red(second)));
            ret.append(ANSI.cyan(third));
            /*
            ret.append('\n');
            ret.append("           ");
            for(int i=0; i<first.length(); i++) ret.append(' ');
            for(int i=0; i<second.length(); i++) ret.append(ANSI.red("^"));
            */
        }
        HashMap<Element,Input.Location> hm = new HashMap<Element,Input.Location>();
        if (nodes!=null)
            for(StateNode no : nodes)
                barf(hm, no, 0, false, region.getStart());
        ret.append("\n expected: ");
        Set<Element> hs = hm.keySet();
        if (hs.size() == 1) {
            ret.append(hs.iterator().next());
        } else {
            int i=0;
            for(Element s : hs) {
                Input.Location loc2 = hm.get(s);
                if (i==0) {
                    ret.append("" + ANSI.purple(s));
                } else {
                    ret.append("\n        or " + ANSI.purple(s));
                }
                Input.Region reg = loc2.createRegion(region.getEnd());
                ret.append(" to match \"" + ANSI.cyan(input.showRegion(reg, 60)) + "\" at " + ANSI.yellow(reg));
                i++;
            }
        }
        /*
        ret.append("\n  The author of SBP apologizes for the these nearly-useless error messages:\n\n");
        HashMap<String,HashSet<String>> errors = new HashMap<String,HashSet<String>>();
        for(StateNode n : nodes) {
            //System.out.println(n.state);
            complain(n, errors, false, 0);
        }
        for(String s : errors.keySet()) {
            ret.append("    while parsing " + ANSI.yellow(s));
            HashSet<String> hs = errors.get(s);

            if (hs.size()==1) ret.append("\n      expected " + ANSI.yellow(el(hs.iterator().next())) + "\n\n");
            else {
                ret.append("\n      expected ");
                boolean first = true;
                for(String s2 : hs) {
                    if (!first) ret.append(" or ");
                    first = false;
                    ret.append(ANSI.yellow(el(s2)));
                }
                ret.append("\n\n");
            }
        }
        */
        throw new ParseFailed(ret.toString(), region, input, gss);
    }

}
