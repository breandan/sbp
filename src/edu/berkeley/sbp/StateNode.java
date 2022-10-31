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

/** a node in the GSS */
final class StateNode
    extends Node<ResultNode>
    implements Invokable<Pos, ResultNode, Object> {

    private final Parser.Table.State state;

    /** which GSS.Phase this StateNode belongs to */
    public Parser.Table.State state() { return state; }
    public boolean isDoomedState() { return state.doomed; }
    public boolean hasPathToRoot() {
        if (state.doomed) return false;
        return super.hasPathToRoot();
    }

    public void check() {
        if (destroyed) return;
        boolean dead = true;
        // - all nodes must have a predecessor
        // - non-doomed nodes must either:
        //      - be on the frontier or
        //      - have a non-doomed node closer to the frontier than themself
        if (phase().isFrontier()) dead = false;
        else for(ResultNode r : successors())
                 if (state.doomed) { if (r.hasSuccessors()) { dead = false; break; } }
                 else              { if (r.hasNonDoomedSuccessors()) { dead = false; break; } }
        dead |= !hasPredecessors();
        if (!dead) return;
        if (phase() != null && phase().hash != null)
            phase().hash.remove(state, predecessorPhase());
        destroy();
    }

    public final void invoke(Pos r, ResultNode only, Object o) {
        boolean emptyProductions = only==null;
        if (emptyProductions != (r.numPops()==0)) return;
        if (r.numPops()==0) {
            Input.Region region = phase().getLocation().createRegion(phase().getLocation());
            phase().newNodeFromReduction(r.rewrite(region), r, this);
        } else {
            // never start reductions at a node that was created via a right-nulled rule
            if (only.reduction() != null && only.reduction().numPops()==0) return;
            reduce(r, r.numPops()-1, phase(), only);
        }
    }

    private void reduce(Pos r, int pos, GSS.Phase target, ResultNode only) {
        for(ResultNode res : this)
            if (only == null || res == only)
                for(StateNode pred : res) {
                    Forest[] holder = r.holder;
                    Forest old = pos >= holder.length ? null : holder[pos];
                    if (pos < holder.length) holder[pos] = res.getForest();
                    if (pos>0)  pred.reduce(r, pos-1, target, null);
                    else new Reduction(pred, r,
                                       pred.hasPathToRoot()              
                                       ? r.rewrite(pred.phase().getLocation().createRegion(target.getLocation()))
                                       : null,
                                       target);
                    if (pos < holder.length) holder[pos] = old;
                }
    }

    StateNode(GSS.Phase phase, ResultNode pred, State state) {
        super(phase, pred.phase());
        this.state = state;
        if (phase.hash.get(state, pred.phase()) != null) throw new Error("severe problem!");
        phase.hash.put(state, pred.phase(), this);
        addPred(pred);
        state.invokeEpsilonReductions(phase().token, this);
    }

    // Add/Remove Successors/Predecessors //////////////////////////////////////////////////////////////////////////////

    public void addPred(Forest f, Pos reduction, StateNode pred) {
        for(ResultNode r : this)
            if (r.predecessorsContains(pred)) {
                r.merge(f);
                return;
            }
        addPred(new ResultNode(f, reduction, pred));
    }
    protected void addPred(ResultNode result) {
        super.addPred(result);
        state.invokeReductions(phase().getToken(), this, result);
    }
}
