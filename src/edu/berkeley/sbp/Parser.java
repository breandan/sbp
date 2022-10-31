// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.Sequence.Pos;
import edu.berkeley.sbp.Sequence.Pos;
import java.io.*;
import java.util.*;

/** a parser which translates an Input&lt;Token&gt; into a Forest&lt;NodeType&gt; */
public abstract class Parser<Token, NodeType> implements Serializable {

    final Table pt;

    /** create a parser to parse the grammar with start symbol <tt>u</tt> */
    public Parser(Union u)  { this.pt = new Table(u); }

    /** implement this method to create the output forest corresponding to a lone shifted input token */
    public abstract Forest<NodeType> shiftToken(Token t, Input.Region region);

    public abstract Topology<Token> emptyTopology();

    public String toString() { return pt.toString(); }

    /** parse <tt>input</tt>, and return the shared packed parse forest (or throw an exception) */
    public Forest<NodeType> parse(Input<Token> input) throws IOException, ParseFailed {
        long start = System.currentTimeMillis();
        verbose = System.getProperty("sbp.verbose", null) != null;
        spinpos = 0;
        GSS gss = new GSS(input, this);
        int idmax = 0;
        int[][] count = new int[1024*1024][];
        HashMap<Pos,Integer> ids = new HashMap<Pos,Integer>();
        try {
            for(GSS.Phase current = gss.new Phase<Token>(pt.start); ;) {
                if (verbose) debug(current.token, gss, input);
                if (current.isDone()) return (Forest<NodeType>)current.finalResult;
                Input.Region region = current.getLocation().createRegion(current.getNextLocation());
                Forest forest = shiftToken((Token)current.token, region);
                /*
                int maxid = 0;
                for(Reduction r : gss.finishedReductions)
                    if (ids.get(r.reduction())==null)
                        ids.put(r.reduction(), idmax++);
                count[current.pos] = new int[idmax];
                for(Reduction r : gss.finishedReductions)
                    count[current.pos][ids.get(r.reduction())]++;
                */
                current = gss.new Phase<Token>(current, forest);
            }
        } finally {
            if (verbose) {
                long time = System.currentTimeMillis() - start;
                System.err.println("\r parse time: " + time +"ms "+ ANSI.clreol());
                debug(null, gss, input);
            }
            /*
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("out.plot")));
            boolean[] use = new boolean[idmax];
            for(int i=0; i<count.length; i++)
                if (count[i]!=null)
                for(int j=0; j<count[i].length; j++)
                    if (count[i][j]>20)
                        use[j] = true;
            for(int i=0; i<count.length; i++)
                if (count[i]!=null) {
                    int row = 0;
                    for(int j=0; j<use.length; j++)
                        if (use[j]) {
                            row++;
                            pw.println(i+", "+row+", "+(j>=count[i].length ? 0 : count[i][j]));
                        }
                    pw.println();
                }
            pw.close();
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("test.plot")));
            pw.println("set terminal postscript enhanced color");
            pw.println("set output \"out.ps\"");
            pw.println("set pm3d map");
            pw.println("set autoscale");
            pw.println("set view 0,0");
            pw.println("set ytics (\\");
            int q = -1;
            for(int j=0; j<use.length; j++)
                if (use[j]) {
                    q++;
                    for(Pos p : ids.keySet())
                        if (ids.get(p) == j) {
                            String title = p.toString();
                            System.out.println(q + " " + title);
                            pw.println("\""+q+"\"  "+(((double)q)+0.5)+",\\");
                            break;
                        }
                }
            pw.println("\".\"  "+(q+1)+")");
            pw.println("set size square");
            pw.println("splot \"out.plot\"");
            pw.close();
            */
        }
    }

    // Spinner //////////////////////////////////////////////////////////////////////////////

    private boolean verbose = false;
    private static final char[] spin = new char[] { '-', '\\', '|', '/' };
    private int spinpos = 0;
    private long last = 0;
    void spin() {
        if (!verbose) return;
        long now = System.currentTimeMillis();
        if (now-last < 70) return;
        last = now;
        System.err.print("\r  " + spin[spinpos++ % (spin.length)]+"\r");
    }

    private int _last = -1;
    private String buf = "";
    private void debug(Object t, GSS gss, Input input) {
        //FIXME
        int c = t==null ? -1 : ((t+"").charAt(0));
        int last = _last;
        _last = c;
        switch(c) {
            case edu.berkeley.sbp.chr.CharAtom.left:
                buf += "\033[31m>\033[0m";
                break;
            case edu.berkeley.sbp.chr.CharAtom.right:
                buf += "\033[31m<\033[0m";
                break;
            case -1: // FIXME 
            case '\n':
                if (verbose) {
                    if (last==' ') buf += ANSI.blue("\\n");
                    System.err.println("\r"+ANSI.clreol()+"\r"+buf);
                    buf = "";
                }
                break;
            default:
                buf += ANSI.cyan(""+((char)c));
                break;
        }
        if (t==null) return;

        // FIXME: clean this up
        String s;
        s = "  " + spin[spinpos++ % (spin.length)]+" parsing ";
        s += input.getName();
        s += " "+input.getLocation();
        while(s.indexOf(':') != -1 && s.indexOf(':') < 8) s = " " + s;
        String y = "@"+gss.viewPos+" ";
        while(y.length() < 9) y = " " + y;
        s += y;
        s += "   nodes="+gss.numOldNodes;
        while(s.length() < 50) s = s + " ";
        s += " shifted="+gss.numNewNodes;
        while(s.length() < 60) s = s + " ";
        s += " reductions="+gss.numReductions;
        while(s.length() < 78) s = s + " ";
        System.err.print("\r"+ANSI.invert(s+ANSI.clreol())+"\r");
    }

    // Table //////////////////////////////////////////////////////////////////////////////

    /** an SLR(1) parse table which may contain conflicts */
    class Table implements Serializable {

        /** the start state */
        final State<Token>   start;

        /** a dummy state from which no reductions can be performed */
        private final State<Token>   dead_state;

        /** used to generate unique values for State.idx */
        private int master_state_idx = 0;

        /** all the states for this table */
        private transient HashSet<State<Token>>                all_states       = new HashSet<State<Token>>();

        /** all the doomed states in this table */
        private transient HashMap<HashSet<Pos>,State<Token>>   doomed_states    = new HashMap<HashSet<Pos>,State<Token>>();

        /** all the non-doomed states in this table */
        private transient HashMap<HashSet<Pos>,State<Token>>   normal_states    = new HashMap<HashSet<Pos>,State<Token>>();

        /** construct a parse table for the given grammar */
        Table(Union ux) {
            Union rootUnion = new Union("0", Sequence.create(ux), true);
            Grammar<Token> grammar = new Grammar<Token>(rootUnion) {
                public Topology<Token> emptyTopology() { return Parser.this.emptyTopology(); }
            };

            // create the "dead state"
            this.dead_state = new State<Token>(new HashSet<Pos>(), true, grammar);

            // construct the start state; this will recursively create *all* the states
            this.start = new State<Token>(reachable(rootUnion), false, grammar);

            buildReductions(grammar);
            sortReductions(grammar);
        }

        /** fill in the reductions table */
        private void buildReductions(Grammar<Token> grammar) {
            // for each state, fill in the corresponding "row" of the parse table
            for(State<Token> state : all_states)
                for(Pos p : state.hs) {

                    // if the element following this position is an atom, copy the corresponding
                    // set of rows out of the "master" goto table and into this state's shift table
                    if (p.element() != null && p.element() instanceof Atom)
                        state.shifts.addAll(state.gotoSetTerminals.subset(((Atom)p.element()).getTokenTopology()));

                    // RNGLR: we can potentially reduce from any "right-nullable" position -- that is,
                    // any position for which all Elements after it in the Sequence are capable of
                    // matching the empty string.
                    if (!grammar.isRightNullable(p)) continue;
                    Topology<Token> follow = grammar.follow(p.owner());
                    for(Pos p2 = p; p2 != null && p2.element() != null; p2 = p2.next()) {
                        if (!(p2.element() instanceof Union))
                            throw new Error("impossible -- only Unions can be nullable");
                        
                        // interesting RNGLR-followRestriction interaction: we must intersect
                        // not just the follow-set of the last non-nullable element, but the
                        // follow-sets of the nulled elements as well.
                        for(Sequence s : ((Union)p2.element()))
                            follow = follow.intersect(grammar.follow(s));
                        Topology<Token> set = grammar.epsilonFollowSet((Union)p2.element());
                        if (set != null) follow = follow.intersect(set);
                    }
                    
                    // indicate that when the next token is in the set "follow", nodes in this
                    // state should reduce according to Pos "p"
                    state.reductions.put(follow, p);
                    if (grammar.followEof.contains(p.owner())) state.eofReductions.add(p);
                }

            // optimize the reductions table
            if (emptyTopology() instanceof IntegerTopology)
                for(State<Token> state : all_states) {
                    // FIXME: this is pretty ugly
                    state.oreductions = state.reductions.optimize(((IntegerTopology)emptyTopology()).functor());
                    state.oshifts     = state.shifts.optimize(((IntegerTopology)emptyTopology()).functor());
                }
        }

        // FIXME: this method needs to be cleaned up and documented
        private void sortReductions(Grammar<Token> grammar) {
            // crude algorithm to assing an ordinal ordering to every position
            // al will be sorted in DECREASING order (al[0] >= al[1])
            ArrayList<Sequence.Pos> al = new ArrayList<Sequence.Pos>();
            for(State s : all_states) {
                for(Object po : s.positions()) {
                    Sequence.Pos p = (Sequence.Pos)po;
                    if (al.contains(p)) continue;
                    int i=0;
                    for(; i<al.size(); i++) {
                        if (grammar.comparePositions(p, al.get(i)) < 0)
                            break;
                    }
                    al.add(i, p);
                }
            }
            // FIXME: this actually pollutes the "pure" objects (the ones that should not be modified by the Parser)
            // sort in increasing order...
            OUTER: while(true) {
                for(int i=0; i<al.size(); i++)
                    for(int j=i+1; j<al.size(); j++)
                        if (grammar.comparePositions(al.get(i), al.get(j)) > 0) {
                            Sequence.Pos p = al.remove(j);
                            al.add(i, p);
                            continue OUTER;
                        }
                break;
            }

            int j = 1;
            int pk = 0;
            for(int i=0; i<al.size(); i++) {
                boolean inc = false;
                for(int k=pk; k<i; k++) {
                    if (grammar.comparePositions(al.get(k), al.get(i)) > 0)
                        { inc = true; break; }
                }
                inc = true;
                if (inc) {
                    j++;
                    pk = i;
                }
                al.get(i).ord = j;
            }
        }

        /**
         *  A single state in the LR table and the transitions
         *  possible from it
         *
         *  A state corresponds to a set of Sequence.Pos's.  Each
         *  StateNode in the GSS has a State; the StateNode represents a set of
         *  possible parses, one for each Pos in the State.
         *
         *  Every state is either "doomed" or "normal".  If a Pos
         *  is part of a Sequence which is a conjunct (that is, it was
         *  passed to Sequence.{and(),andnot()}), then that Pos
         *  will appear only in doomed States.  Furthermore, any set
         *  of Positions reachable from a doomed State also forms a
         *  doomed State.  Note that in this latter case, a doomed
         *  state might have exactly the same set of Positions as a
         *  non-doomed state.
         *
         *  Nodes with non-doomed states represent nodes which
         *  contribute to actual valid parses.  Nodes with doomed
         *  States exist for no other purpose than to enable/disable
         *  some future reduction from a non-doomed StateNode.  Because of
         *  this, we "garbage-collect" Nodes with doomed states if
         *  there are no more non-doomed Nodes which they could
         *  affect (see ResultNode, Reduction, and StateNode for details).
         *
         *  Without this optimization, many seemingly-innocuous uses
         *  of positive and negative conjuncts can trigger O(n^2)
         *  space+time complexity in otherwise simple grammars.  There
         *  is an example of this in the regression suite.
         */
        class State<Token> implements IntegerMappable, Serializable {
        
            public  final     int               idx    = master_state_idx++;
            private final  transient   HashSet<Pos> hs;
            public HashSet<State<Token>> conjunctStates = new HashSet<State<Token>>();

            HashMap<Pos,State<Token>>      gotoSetNonTerminals = new HashMap<Pos,State<Token>>();
            private transient TopologicalBag<Token,State<Token>>  gotoSetTerminals    = new TopologicalBag<Token,State<Token>>();

                       TopologicalBag<Token,Pos>      reductions          = new TopologicalBag<Token,Pos>();
                       HashSet<Pos>                   eofReductions       = new HashSet<Pos>();
            private           TopologicalBag<Token,State<Token>>  shifts              = new TopologicalBag<Token,State<Token>>();
            private           boolean                             accept              = false;

            private VisitableMap<Token,State<Token>> oshifts     = null;
            private VisitableMap<Token,Pos>     oreductions = null;
            public  final boolean doomed;

            // Interface Methods //////////////////////////////////////////////////////////////////////////////

            public boolean doomed() { return doomed; }
            boolean                    isAccepting()           { return accept; }

            Iterable<Pos>  positions()             { return hs; }

            boolean                    canShift(Token t)       { return oshifts!=null && oshifts.contains(t); }
            void                       invokeShifts(Token t, GSS.Phase phase, StateNode pred, Forest f) { oshifts.invoke(t, phase, pred, f); }
            boolean                    canReduce(Token t)        {
                return oreductions != null && (t==null ? eofReductions.size()>0 : oreductions.contains(t)); }
            void          invokeEpsilonReductions(Token t, StateNode node) {
                if (t==null) for(Pos r : eofReductions) node.invoke(r, null, null);
                else         oreductions.invoke(t, node, null, null);
            }
            void          invokeReductions(Token t, StateNode node, ResultNode only) {
                if (t==null) for(Pos r : eofReductions) node.invoke(r, only, null);
                else         oreductions.invoke(t, node, only, null);
            }

            // Constructor //////////////////////////////////////////////////////////////////////////////

            /**
             *  create a new state consisting of all the <tt>Pos</tt>s in <tt>hs</tt>
             *  @param hs           the set of <tt>Pos</tt>s comprising this <tt>State</tt>
             *  @param all the set of all elements (Atom instances need not be included)
             *  
             *   In principle these two steps could be merged, but they
             *   are written separately to highlight these two facts:
             * <ul>
             * <li> Non-atom elements either match all-or-nothing, and do not overlap
             *      with each other (at least not in the sense of which element corresponds
             *      to the last reduction performed).  Therefore, in order to make sure we
             *      wind up with the smallest number of states and shifts, we wait until
             *      we've figured out all the token-to-position multimappings before creating
             *      any new states
             *  
             * <li> In order to be able to run the state-construction algorithm in a single
             *      shot (rather than repeating until no new items appear in any state set),
             *      we need to use the "yields" semantics rather than the "produces" semantics
             *      for non-Atom Elements.
             *  </ul>
             */
            public State(HashSet<Pos> hs, boolean doomed, Grammar<Token> grammar) {
                this.hs = hs;
                this.doomed = doomed;

                // register ourselves so that no two states are ever
                // created with an identical position set (termination depends on this)
                ((HashMap)(doomed ? doomed_states : normal_states)).put(hs, this);
                ((HashSet)all_states).add(this);

                for(Pos p : hs) {
                    // Step 1a: take note if we are an accepting state
                    //          (last position of the root Union's sequence)
                    if (p.next()==null && !doomed && grammar.rootUnion.contains(p.owner()))
                        accept = true;

                    // Step 1b: If any Pos in the set is the first position of its sequence, then this
                    //          state is responsible for spawning the "doomed" states for each of the
                    //          Sequence's conjuncts.  This obligation is recorded by adding the to-be-spawned
                    //          states to conjunctStates.
                    if (!p.isFirst()) continue;
                    for(Sequence s : p.owner().needs())
                        if (!hs.contains(s.firstp()))
                            conjunctStates.add(mkstate(reachable(s.firstp()), true, grammar));
                    for(Sequence s : p.owner().hates())
                        if (!hs.contains(s.firstp()))
                            conjunctStates.add(mkstate(reachable(s.firstp()), true, grammar));
                }

                // Step 2a: examine all Pos's in this state and compute the mappings from
                //          sets of follow tokens (tokens which could follow this position) to sets
                //          of _new_ positions (positions after shifting).  These mappings are
                //          collectively known as the _closure_

                TopologicalBag<Token,Pos> bag0 = new TopologicalBag<Token,Pos>();
                for(Pos position : hs) {
                    if (position.isLast() || !(position.element() instanceof Atom)) continue;
                    Atom a = (Atom)position.element();
                    HashSet<Pos> hp = new HashSet<Pos>();
                    reachable(position.next(), hp);
                    bag0.addAll(a.getTokenTopology(), hp);
                }

                // Step 2b: for each _minimal, contiguous_ set of characters having an identical next-position
                //          set, add that character set to the goto table (with the State corresponding to the
                //          computed next-position set).

                for(Topology<Token> r : bag0) {
                    HashSet<Pos> h = new HashSet<Pos>();
                    for(Pos p : bag0.getAll(r)) h.add(p);
                    ((TopologicalBag)gotoSetTerminals).put(r, mkstate(h, doomed, grammar));
                }

                // Step 3: for every Sequence, compute the closure over every position in this set which
                //         is followed by a symbol which could yield the Sequence.
                //
                //         "yields" [in one or more step] is used instead of "produces" [in exactly one step]
                //         to avoid having to iteratively construct our set of States as shown in most
                //         expositions of the algorithm (ie "keep doing XYZ until things stop changing").

                HashMapBag<Sequence,Pos> move = new HashMapBag<Sequence,Pos>();
                for(Pos p : hs)
                    if (!p.isLast() && p.element() instanceof Union)
                        for(Sequence s : ((Union)p.element())) {
                            HashSet<Pos> hp = new HashSet<Pos>();
                            reachable(p.next(), hp);
                            move.addAll(s, hp);
                        }
                OUTER: for(Sequence y : move) {
                    // if a reduction is "lame", it should wind up in the dead_state after reducing
                    HashSet<Pos> h = move.getAll(y);
                    State<Token> s = mkstate(h, doomed, grammar);
                    for(Pos p : hs)
                        if (p.element() != null && (p.element() instanceof Union))
                            for(Sequence seq : ((Union)p.element()))
                                if (seq.needs.contains(y) || seq.hates.contains(y)) {
                                    // FIXME: assumption that no sequence is ever both usefully (non-lamely) matched
                                    //        and also directly lamely matched
                                    for(Pos pp = y.firstp(); pp != null; pp = pp.next())
                                        ((HashMap)gotoSetNonTerminals).put(pp, dead_state);
                                    continue OUTER;
                                }
                    for(Pos pp = y.firstp(); pp != null; pp = pp.next())
                        gotoSetNonTerminals.put(pp, s);
                }
            }

            private State<Token> mkstate(HashSet<Pos> h, boolean b, Grammar<Token> grammar) {
                State ret = (b?doomed_states:normal_states).get(h);
                if (ret==null) ret = new State<Token>(h,b, grammar);
                return ret;
            }

            public int toInt() { return idx; }
            public String toString() {
                StringBuffer ret = new StringBuffer();
                for(Pos p : hs)
                    ret.append(p+"\n");
                return ret.toString();
            }
        }

    }

    // Helpers //////////////////////////////////////////////////////////////////////////////
    
    private static HashSet<Pos> reachable(Element e) {
        HashSet<Pos> h = new HashSet<Pos>();
        reachable(e, h);
        return h;
    }
    private static void reachable(Element e, HashSet<Pos> h) {
        if (e instanceof Atom) return;
        for(Sequence s : ((Union)e))
            reachable(s.firstp(), h);
    }
    private static void reachable(Pos p, HashSet<Pos> h) {
        if (h.contains(p)) return;
        h.add(p);
        if (p.element() != null) reachable(p.element(), h);
    }
    private static HashSet<Pos> reachable(Pos p) {
        HashSet<Pos> ret = new HashSet<Pos>();
        reachable(p, ret);
        return ret;
    }

}
