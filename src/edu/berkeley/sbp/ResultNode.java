// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.Sequence.Pos;
import edu.berkeley.sbp.Sequence.Pos;
import java.util.*;

final class ResultNode
    extends Node<StateNode> {

    private Pos reduction;
    private Forest.Many f = new Forest.Many();

    public void merge(Forest newf) { this.f.merge(newf); }
    public Pos reduction() { return reduction; }
    public boolean isDoomedState() { /* this is irrelevant */ return false; }
    public Forest getForest() { return f; }
    public String toString() { return super.toString()+"->"+phase(); }
    public boolean hasPathToRoot() {
        if (predecessorPhase()==null) return true;
        return super.hasPathToRoot();
    }

    public void check() {
        if (destroyed) return;
        if (!hasSuccessors() || !hasPredecessors()) destroy();
    }
    protected void destroy() {
        if (destroyed) return;
        if (predecessorPhase()==null) return;  // never destroy the "primordeal" result
        super.destroy();
    }

    protected void addPred(StateNode pred) {
        super.addPred(pred);
        // results should have at most one predecessor
        //if (predecessors.size() > 1) throw new Error();
    }
        
    public ResultNode() { this(null, null, null); }
    public ResultNode(Forest f, Pos reduction, StateNode pred) {
        super(pred==null ? null : pred.phase(),
              pred==null ? null : pred.phase());
        this.f.merge(f);
        this.reduction = reduction;
        if (pred != null) addPred(pred);
    }
}