// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.Parser.Table.*;
import edu.berkeley.sbp.Sequence.Pos;
import edu.berkeley.sbp.Sequence.Pos;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/** implements Tomita's Graph Structured Stack */
class GSS {

    Input input;
    private Parser parser;
    public GSS(Input input, Parser parser) { this.input = input; this.parser = parser;}
    public Input getInput() { return input; }

    int numNewNodes = 0;
    int numOldNodes = 0;
    int viewPos = 0;
    int numReductions = 0;

    /** corresponds to a positions <i>between tokens</i> the input stream; same as Tomita's U_i's */
    class Phase<Tok> implements Invokable<State, StateNode, Forest>, IntegerMappable, GraphViz.ToGraphViz /*, Iterable<StateNode>*/ {

        // FIXME: right now, these are the performance bottleneck
        private HashMapBag<Integer,Integer>       performed       = new HashMapBag<Integer,Integer>();

        public Forest.Many finalResult;
        private PriorityQueue<Reduction> reductionQueue = new PriorityQueue<Reduction>();

        Parser parser() { return parser; }
        public void addReduction(Reduction r) {
            //System.out.println("+ " + r);
            parser.spin();
            reductionQueue.add(r);
        }

        public void invoke(State st, StateNode pred, Forest f) {
            parser.spin();
            good |= next.newNode(f, null, pred, st);
        }

        /** the token immediately after this phase */
        final Tok token;
        final int pos;
        public IntPairMap<StateNode> hash = new IntPairMap<StateNode>();  /* ALLOC */
        private boolean good = false;
        private Phase next = null;
        private Phase prev;
        private Input.Location location;
        private Input.Location nextLocation;
        
        private Forest forest;

        public Phase(State startState) throws ParseFailed, IOException {
            this(null, null);
            newNode(null, null, null, startState);
        }
        public Phase(Phase prev, Forest forest) throws ParseFailed, IOException {
            this.location = input.getLocation();
            this.token = (Tok)input.next();
            this.nextLocation = input.getLocation();
            this.prev = prev;
            this.forest = forest;
            this.pos = prev==null ? 0 : prev.pos+1;
            if (prev != null) prev.shift(this, forest);
            numReductions = 0;
            /*
            finishedReductions.clear();
            */

            int minPhasePos = Integer.MAX_VALUE;
            Reduction best = null;
            //System.out.println("==============================================================================");
            while(!reductionQueue.isEmpty()) {
                Reduction r = reductionQueue.poll();
                //System.out.println("- " + r);
                if (r.predPhase() != null)
                    if (r.predPhase().pos > minPhasePos)
                        throw new Error();
                r.perform();
                if (r.predPhase() != null) {
                    if (r.predPhase().pos < minPhasePos) {
                        minPhasePos = r.predPhase().pos;
                        best = r;
                    } else if (r.predPhase().pos == minPhasePos) {
                        /*
                        if (best != null && Parser.mastercache.comparePositions(r.reduction(), best.reduction()) < 0)
                            throw new Error("\n"+r+"\n"+best+"\n"+
                                            Parser.mastercache.comparePositions(r.reduction(), best.reduction())+"\n"+r.compareTo(best)+
                                            "\n"+(r.reduction().ord-best.reduction().ord));
                        */
                        best = r;
                    }
                }
                /*
                finishedReductions.add(r);
                */
                numReductions++;
            }
            if (token==null) shift(null, null);
        }

        public boolean isDone() throws ParseFailed {
            if (token != null) return false;
            if (token==null && finalResult==null)
                ParseFailed.error("unexpected end of file", this, null,
                                  getLocation().createRegion(getLocation()));
            return true;
        }

        public Input.Location getLocation() { return location; }
        public Input.Location getNextLocation() { return nextLocation; }
        public boolean        isFrontier() { return hash!=null; }

        /** perform all shift operations, adding promoted nodes to <tt>next</tt> */
        private void shift(Phase next, Forest f) throws ParseFailed {
            this.next = next;
            // this massively improves GC performance
            if (prev != null) {
                IntPairMap<StateNode> h = prev.hash;
                prev.hash = null;
                prev.performed = null;
                for(StateNode n : h) n.check();
            }
            numOldNodes = hash.size();
            for(StateNode n : hash) {
                if (token == null && n.state().isAccepting()) {
                    if (finalResult==null) finalResult = new Forest.Many();
                    for(ResultNode r : n)
                        finalResult.merge(r.getForest());
                }
                if (token == null) continue;
                n.state().invokeShifts(token, this, n, f);
            }
            numNewNodes = next==null ? 0 : next.hash.size();
            viewPos = this.pos;

            if (!good && token!=null) {
                String toks = token+"";
                if (toks.length()==1 && toks.charAt(0) == edu.berkeley.sbp.chr.CharAtom.left) {
                    ParseFailed.error("unexpected increase in indentation", this,
                                      token, getRegionFromThisToNext());
                } else if (toks.length()==1 && toks.charAt(0) == edu.berkeley.sbp.chr.CharAtom.right) {
                    ParseFailed.error("unexpected decrease in indentation", this,
                                      token, getRegionFromThisToNext());
                } else {
                    ParseFailed.error("unexpected character '"+ANSI.cyan(StringUtil.escapify(token+"",
                                                                                             "\\\'\r\n"))+"'",
                                      this, token, getRegionFromThisToNext());
                }
            }
            if (token==null && finalResult==null)
                ParseFailed.error("unexpected end of file", this, null,
                                  getLocation().createRegion(getLocation()));
            for(StateNode n : hash) n.check();
        }

        Input.Region getRegionFromThisToNext() {
            return getLocation().createRegion(getNextLocation());
        }

        void newNodeFromReduction(Forest f, Pos reduction, StateNode pred) {
            int pos = pred.phase().pos;
            for(int s : reduction.hates())
                if (performed.contains(pos, s))
                    return;
            for(int s : reduction.needs())
                if (!performed.contains(pos, s))
                    return;
            if (reduction.owner_needed_or_hated() && !performed.contains(pos, reduction.provides()))
                performed.add(pos, reduction.provides());
            Parser.Table.State state = (Parser.Table.State)pred.state().gotoSetNonTerminals.get(reduction);
            if (state!=null)
                newNode(f, reduction, pred, state);
        }

        /** add a new node (merging with existing nodes if possible)
         *  @param parent             the parent of the new node
         *  @param result             the SPPF result corresponding to the new node
         *  @param state              the state that the new node is in
         *  @param start              the earliest part of the input contributing to this node (used to make merging decisions)
         */
        private boolean newNode(Forest f, Pos reduction, StateNode pred, State state) {
            StateNode p = pred==null ? null : hash.get(state, pred.phase());
            if (p != null) {
                p.addPred(f, reduction, pred);
                return !state.doomed();
            }
            do {
                // optimizations
                if (token != null && state.canShift(token)) break;
                if (state.isAccepting()) break;
                if (token==null) break;
                if (!state.canReduce(token)) return false;
            } while(false);
            StateNode n = new StateNode(Phase.this, new ResultNode(f, reduction, pred), state);  // ALLOC
            /** FIXME: this null-result can be used to notice bogus/dead states */
            for(Object s : state.conjunctStates)
                newNode(null, null, n, (State)s);
            return !n.state().doomed();
        }

        public int toInt() { return pos+1; }
        public int size() { return hash==null ? 0 : hash.size(); }
        public int pos() { return pos; }
        public Tok getToken() { return token; }
        //public Iterator<StateNode> iterator() { return hash.iterator(); }
        public GSS getGSS() { return GSS.this; }

        // GraphViz //////////////////////////////////////////////////////////////////////////////

        public GraphViz.StateNode toGraphViz(GraphViz gv) {
            if (gv.hasNode(this)) return gv.createNode(this);
            GraphViz.Group g = gv.createGroup(this);
            g.label = "Phase " + pos;
            g.color = "gray";
            g.cluster = true;
            return g;
        }
        public boolean isTransparent() { return false; }
        public boolean isHidden() { return false; }

        public void dumpGraphViz(String filename) throws IOException {
            FileOutputStream fos = new FileOutputStream(filename);
            PrintWriter p = new PrintWriter(new OutputStreamWriter(fos));
            GraphViz gv = new GraphViz();
            /*
            for(Object n : this)
                ((StateNode)n).toGraphViz(gv);
            */
            gv.dump(p);
            p.flush();
            p.close();
        }

    }

}
