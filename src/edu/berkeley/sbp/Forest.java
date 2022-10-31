// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;
import java.io.*;
import java.util.*;

/**
 *   <font color=blue>
 *   An efficient representation of a collection of trees (Tomita's
 *   shared packed parse forest).
 *   </font>
 */
public abstract class Forest<NodeType> implements GraphViz.ToGraphViz {

    /** assume that this forest contains exactly one tree and return it; otherwise throw an exception */
    public abstract Tree<NodeType> expand1() throws Ambiguous;

    /** expand this forest into a set of trees */
    public Iterable<Tree<NodeType>> expand() {
        HashSet<Tree<NodeType>> ht = new HashSet<Tree<NodeType>>();
        expand(ht, new HashSet<Forest<NodeType>>(), null);
        return ht;
    }

    /** returns the input Region which this Forest was parsed from */
    public abstract Input.Region getRegion();

    // Package-Private //////////////////////////////////////////////////////////////////////////////

    public static <NodeType> Forest<NodeType> create(Input.Region region, NodeType head, Forest<NodeType>[] children) {
        return create(region, head, children, new boolean[children==null ? 0 : children.length]); }
    public static <NodeType> Forest<NodeType> create(Input.Region region, NodeType head, Forest<NodeType>[] children, boolean[] lifts) {
        if (region == null) throw new RuntimeException("invoked Forest.create(region=null) -- this should never happen");
        return new One<NodeType>(region, head, children, lifts);
    }

    abstract void expand(HashSet<Tree<NodeType>> ht, HashSet<Forest<NodeType>> ignore, Tree<NodeType> bogus);
    abstract void gather(HashSet<Forest<NodeType>> ignore);
    abstract void edges(GraphViz.StateNode n);
    boolean ambiguous() { return false; }
    
    // One //////////////////////////////////////////////////////////////////////////////

    /** A "single" forest with a head and child subforests */    
    private static class One<NodeType> extends Forest<NodeType> {

        private final Input.Region      location;
        private final NodeType                head;
        private final Forest<NodeType>[]       children;

        /** if true, the last child's children are considered children of this node */
        private final boolean[]         lifts;

        public Input.Region getRegion() { return location; }

        private One(Input.Region loc, NodeType head, Forest<NodeType>[] children, boolean[] lifts) {
            this.location = loc;
            this.head = head;
            if (head==null) throw new RuntimeException("invoked Forest.create(,null,,,) -- this should never happen");
            this.children = children==null ? emptyForestArray : new Forest[children.length];
            if (children != null) System.arraycopy(children, 0, this.children, 0, children.length);
            if (children != null) for(int i=0; i<children.length; i++) if (children[i]==null) throw new Error(i+"");
            this.lifts = lifts;
        }

        public Tree<NodeType> expand1() throws Ambiguous {
            if (children.length == 0)
                return new Tree<NodeType>(location, head);
            else
                return expand1ForOneAndMany((Forest<NodeType>)this);
        }

        void gather(HashSet<Forest<NodeType>> hf) {
            hf.add(this);
            for(Forest<NodeType> f : children) f.gather(hf);
        }
        void expand(HashSet<Tree<NodeType>> ht, HashSet<Forest<NodeType>> ignore, Tree<NodeType> bogus) {
            if (ignore.contains(this)) { ht.add(bogus); return; }
            expand(0, new Tree[children.length], ht, ignore, bogus);
        }
        private void expand(final int i, Tree<NodeType>[] ta, HashSet<Tree<NodeType>> ht, HashSet<Forest<NodeType>> ignore,
                            Tree<NodeType> bogus) {
            if (i==children.length) {
                ht.add(new Tree<NodeType>(location, head, ta, lifts));
            } else {
                HashSet<Tree<NodeType>> ht2 = new HashSet<Tree<NodeType>>();
                children[i].expand(ht2, ignore, bogus);
                for(Tree<NodeType> tc : ht2) {
                    ta[i] = tc;
                    expand(i+1, ta, ht, ignore, bogus);
                    ta[i] = null;
                }
            }
        }

        // GraphViz, ToInt //////////////////////////////////////////////////////////////////////////////

        public boolean isTransparent() { return false; }
        public boolean isHidden() { return false; }
        public GraphViz.StateNode toGraphViz(GraphViz gv) {
            if (gv.hasNode(this)) return gv.createNode(this);
            GraphViz.StateNode n = gv.createNode(this);
            n.label = headToString()==null?"":headToString();
            n.directed = true;
            edges(n);
            return n;
        }
        boolean edges = false; // FIXME ??
        public void edges(GraphViz.StateNode n) {
            if (edges) return;
            edges = true;
            for(int i=0; i<children.length; i++) {
                if (lifts[i] && !children[i].ambiguous()) {
                    children[i].edges(n);
                } else {
                    n.edge(children[i], null);
                }
            }
        }

        protected String  headToString()         { return head==null?null:head.toString(); }
        protected String  headToJava()           { return "null"; }
        protected String  left()                 { return "{"; }
        protected String  right()                { return "}"; }
        protected boolean ignoreSingleton()      { return false; }
    }


    // Many //////////////////////////////////////////////////////////////////////////////

    /** An "ambiguity node"; this is immutable once it has been "looked at" */
    static class Many<NodeType> extends Forest<NodeType> {

        private FastSet<Forest<NodeType>> hp = new FastSet<Forest<NodeType>>();
        private boolean touched = false;

        public Many() { }

        public Input.Region getRegion() { return hp.iterator().next().getRegion(); } // all should be identical

        Forest<NodeType> simplify() throws Ambiguous {
            touched();
            if (hp.size() > 1) {
                HashSet<Forest<NodeType>> hf0 = new HashSet<Forest<NodeType>>();
                Iterator<Forest<NodeType>> ih = hp.iterator();
                ih.next().gather(hf0);
                for(Forest<NodeType> f : hp) {
                    HashSet<Forest<NodeType>> hf1 = new HashSet<Forest<NodeType>>();
                    f.gather(hf1);
                    hf0.retainAll(hf1);
                }
                HashSet<Tree<NodeType>> ht = new HashSet<Tree<NodeType>>();
                expand(ht, hf0, new Tree(null, "*"));
                throw new Ambiguous((Forest<?>)this,
                                    (HashSet<Tree<?>>)(Object)ht);
            }

            return hp.iterator().next();
        }

        public Tree<NodeType> expand1() throws Ambiguous {
            return simplify().expand1();
        }
        
        void gather(HashSet<Forest<NodeType>> ht) {
            touched();

            // FIXME: do something more sensible here
            if (ht.contains(this)) {
                System.err.println("WARNING: grammar produced a circular forest\n" + this);
                //throw new Error("grammar produced a circular forest:\n" + this);
                return;
            }

            ht.add(this);
            for(Forest<NodeType> f : hp) f.gather(ht);
        }

        private void touched() {
            if (touched) return;
            touched = true;
            /*
            FastSet<Forest<NodeType>> f2 = new FastSet<Forest<NodeType>>();
            for(Forest f : hp)
                if (f instanceof Forest.One) f2.add(f);
                else for(Forest ff : ((Forest.Many<NodeType>)f))
                    f2.add(ff);
            hp = f2;
            */
        }
        public boolean contains(Forest f) {
            touched();
            return hp.contains(f);
        }
        public void merge(Forest p) { 
            if (touched) throw new RuntimeException("attempt to merge() on a Forest.Many that has already been examined");
            if (p==this) throw new RuntimeException("attempt to merge() a Forest.Many to itself!");
            hp.add(p, true);
        }
        boolean ambiguous() {
            touched();
            if (hp.size()==0) return false;
            if (hp.size()==1) return hp.iterator().next().ambiguous();
            return true;
        }

        void expand(HashSet<Tree<NodeType>> ht, HashSet<Forest<NodeType>> ignore, Tree<NodeType> bogus) {
            touched();
            if (ignore.contains(this)) { ht.add(bogus); return; }
            for (Forest<NodeType> f : hp) f.expand(ht, ignore, bogus);
        }


        // GraphViz, ToInt //////////////////////////////////////////////////////////////////////////////

        public boolean isTransparent() { return hp.size()==1; }
        public boolean isHidden() { return hp.size()==0; }
        public void edges(GraphViz.StateNode n) {
            if (hp.size()==1) { hp.iterator().next().edges(n); return; }
            for(Forest f : hp) f.edges(n);
        }
        public GraphViz.StateNode toGraphViz(GraphViz gv) {
            if (hp.size()==1) return hp.iterator().next().toGraphViz(gv);
            if (gv.hasNode(this)) return gv.createNode(this);
            GraphViz.StateNode n = gv.createNode(this);
            n.label = "?";
            n.color = "red";
            for(Forest f : hp) n.edge(f, null);
            return n;
        }
    }


    // Statics //////////////////////////////////////////////////////////////////////////////

    /** Depth-first expansion, handling .One and .Many without recursion. */
    private static <NodeType> Tree<NodeType> expand1ForOneAndMany(
            Forest<NodeType> forest) throws Ambiguous {
        List<Tree<NodeType>> nodes = new ArrayList<Tree<NodeType>>();
        List<Tree<NodeType>> build = new ArrayList<Tree<NodeType>>();

        // path stack
        List<Forest.One<NodeType>> path = new ArrayList<Forest.One<NodeType>>();
        List<Integer> sizes = new ArrayList<Integer>(),
                      poss  = new ArrayList<Integer>();

        // handle the left-most leaf
        expand1Descend(path, nodes, sizes, poss, forest);

        for (Forest.One<NodeType> f; path.size() > 0;) {
            // up one
            f = pop(path);
            int size = pop(sizes) + 1;
            int pos = pop(poss) + 1;
            Forest<NodeType>[] children = f.children;
            if (pos < children.length) {
                // down the next branch
                path.add(f);
                sizes.add(new Integer(size));
                poss.add(new Integer(pos));

                // handle the left-most leaf
                expand1Descend(path, nodes, sizes, poss, children[pos]);
            } else {
                if (path.size() > 0 && peek(path).lifts[peek(poss)]) {
                    // skip assembling this node, lift children
                    sizes.add(pop(sizes) + size - 1);
                    continue;
                }
                // assemble this node
                for (int i=size; i > 0; i--)
                    build.add(nodes.remove(nodes.size() - i));
                nodes.add(new Tree<NodeType>(f.location, f.head, build));
                build.clear();
            }
        }

        return pop(nodes);
    }

    /** Descend to the left-most leaf, building the path. */
    private static <NodeType> void expand1Descend(
            List<Forest.One<NodeType>> path, List<Tree<NodeType>> nodes,
            List<Integer> sizes, List<Integer> poss, Forest<NodeType> f)
            throws Ambiguous {
        while (true) {
            if (f instanceof Forest.Many)
                f = ((Forest.Many<NodeType>)f).simplify();
            else if (f instanceof Forest.One) {
                Forest.One<NodeType> one = (Forest.One<NodeType>)f;
                if (one.children.length == 0)
                    break;
                path.add(one);
                sizes.add(0);
                poss.add(0);
                f = one.children[0];
            } else {
                nodes.add(f.expand1());
                return;
            }
        }

        if (path.size() > 0 && peek(path).lifts[peek(poss)])
            sizes.add(pop(sizes) - 1); // ignore lifted leafs
        else
            nodes.add(f.expand1());
    }

    private static <T> T pop(List<T> list) {
        return list.remove(list.size() - 1); }
    private static <T> T peek(List<T> list) {
        return list.get(list.size() - 1); }

    private static Tree[] tree_hint = new Tree[0];
    private static String[] string_hint = new String[0];
    private static final Forest[] emptyForestArray = new Forest[0];

    protected String  headToString()    { return null; }
    protected String  headToJava()      { return "null"; }
    protected String  left()            { return "<?"; }
    protected String  right()           { return "?>"; }
    protected boolean ignoreSingleton() { return true; }
}
