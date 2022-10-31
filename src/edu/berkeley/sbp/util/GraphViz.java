// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;

public class GraphViz {

    IdentityHashMap<ToGraphViz,StateNode> ihm = new IdentityHashMap<ToGraphViz,StateNode>();
    HashMap<StateNode,Group> groups = new HashMap<StateNode,Group>();

    public class Group extends StateNode {
        private final int idx = master_idx++;
        public boolean cluster = false;
        public boolean primary = true;
        public Group() { }
        public void add(StateNode n) { groups.put(n, this); }
        public String name() { return cluster?("cluster_"+idx):("subgraph_"+idx); }
        public boolean simple() { return false; }
        public void dump(PrintWriter pw, IdentityHashMap<StateNode,StateNode> done) {
            Group g = this;
            if (done.get(g)!=null) return;
            done.put(g,g);
            pw.println("  subgraph "+name()+" { rank=same;\n");
            pw.println("  label=\""+StringUtil.escapify(label.toString(), "\\\"\r\n")+"\";\n");
            pw.println("  color="+g.color+";\n");
            pw.println("  shape="+g.shape+";\n");
            for(StateNode n : groups.keySet())
                if (groups.get(n)==g)
                    n.dump(pw, done);
            pw.println("  }\n");
        }
    }

    private static int master_idx=0;

    public class StateNode {
        private final int idx = master_idx++;
        public String label;
        public String comment;
        public boolean directed = false;
        public String color="black";
        public String fill="white";
        public String shape="ellipse";
        public ArrayList<StateNode> edges = new ArrayList<StateNode>();
        public ArrayList<Object> labels = new ArrayList<Object>();
        public ArrayList<StateNode> inbound = new ArrayList<StateNode>();
        public void edge(ToGraphViz o, Object label) {
            StateNode n = o.toGraphViz(GraphViz.this);
            if (n==null) return;
            edges.add(n);
            labels.add(label);
            n.inbound.add(this);
        }
        public String getParentName() {
            if (inbound.size()==1 && inbound.get(0).simple())
                return inbound.get(0).getParentName();
            return name();
        }
        public String name() {
            if (inbound.size()==1 && inbound.get(0).simple())
                return inbound.get(0).getParentName()+":node_"+idx;
            return "node_"+idx;
        }
        public void edges(PrintWriter pw) {
            if (simple()) return;
            for(int i=0; i<edges.size(); i++) {
                StateNode n = edges.get(i);
                Object label = labels.get(i);
                pw.println("    "+name()+" -> " + n.name() + " [color="+color+" "
                           +(label==null?"":("label=\""+StringUtil.escapify(label.toString(), "\\\"\r\n")+"\""))+ "];\n");
            }
        }
        public int numEdges() { return edges.size(); }
        public boolean simple() {
            boolean simple = true;
            if (label!=null && !label.equals("")) simple = false;
            if (simple)
                for(StateNode n : edges)
                    //if (n.numEdges()>0) { simple = false; break; }
                    if (n.inbound.size() > 1) { simple = false; break; }
            return simple;
        }
        public void dump(PrintWriter pw, IdentityHashMap<StateNode,StateNode> done) {
            if (done.get(this)!=null) return;
            done.put(this, this);
            if (inbound.size() > 0) {
                boolean good = false;
                for(StateNode n : inbound)
                    if (!n.simple())
                        { good = true; break; }
                if (!good) return;
            }
            pw.print("    "+name());
            pw.print(" [");
            if (directed) pw.print("ordering=out");
            if (simple()) {
                pw.print(" shape=record ");
                pw.print(" label=\"");
                boolean complex = false;
                for(StateNode n : edges)
                    if (n.edges.size()>0)
                        complex = true;
                if (!complex) pw.print("{");
                boolean first = true;
                for(StateNode n : edges) {
                    if (!first) pw.print("|");
                    first = false;
                    pw.print("<node_"+n.idx+">");
                    pw.print(StringUtil.escapify(n.label,"\\\"\r\n"));
                }
                if (!complex) pw.print("}");
                pw.print("\" ");
            } else {
                pw.print(" label=\"");
                pw.print(StringUtil.escapify(label,"\\\"\r\n"));
                pw.print("\" ");
            }
            pw.print("color="+color);
            if (comment!=null) pw.print(" comment=\""+StringUtil.escapify(comment,"\\\"\r\n")+"\" ");
            pw.print("];\n");
        }
    }

    public boolean hasNode(ToGraphViz o) {
        return ihm.get(o)!=null;
    }

    public StateNode createNode(ToGraphViz o) {
        StateNode n = ihm.get(o);
        if (n!=null) return n;
        n = new StateNode();
        ihm.put(o, n);
        return n;
    }

    public Group createGroup(ToGraphViz o) {
        Group n = (Group)ihm.get(o);
        if (n!=null) return n;
        n = new Group();
        ihm.put(o, n);
        return n;
    }

    public static interface ToGraphViz {
        StateNode    toGraphViz(GraphViz gv);
        boolean isTransparent();
        boolean isHidden();
    }

    public void show() throws IOException {
        Runtime.getRuntime().exec(new String[] { "dot", "-Tsvg" });
    }

    public void dump(OutputStream os) { dump(new PrintWriter(new OutputStreamWriter(os))); }
    public void dump(PrintWriter pw) {
        IdentityHashMap<StateNode,StateNode> done = new IdentityHashMap<StateNode,StateNode>();
        pw.println("digraph G { rankdir=LR; ordering=out; compound=true; \n");
        for(Group g : groups.values())
            if (g.primary)
                g.dump(pw, done);
        for(StateNode n : ihm.values()) {
            if (done.get(n)!=null) continue;
            if (n instanceof Group) continue;
            n.dump(pw, done);
        }
        for(StateNode n : ihm.values()) n.edges(pw);
        pw.println("}\n");
        pw.flush();
    }

}
