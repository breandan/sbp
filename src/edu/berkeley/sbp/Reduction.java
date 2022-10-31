// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.Sequence.Pos;
import edu.berkeley.sbp.Sequence.Pos;

final class Reduction implements Comparable<Reduction> {

    private Pos reduction;
    private GSS.Phase phase;
    private Forest forest;
    private StateNode pred;
    
    public Reduction(StateNode pred, Pos reduction, Forest forest, GSS.Phase target) {
        this.reduction = reduction;
        this.forest = forest;
        this.phase = target;
        this.pred = pred;
        target.addReduction(this);
    }

    public int compareTo(Reduction r) {
        if (pred.phase()!=null || r.pred.phase()!=null) {
            if (pred.phase()==null) return 1;
            if (r.pred.phase()==null) return -1;
            if (pred.phase().pos < r.pred.phase().pos) return 1;
            if (pred.phase().pos > r.pred.phase().pos) return -1;
        }
        /*
        int master = Parser.mastercache.comparePositions(reduction(), r.reduction());
        if (master != 0 && master != signum(reduction.ord() - r.reduction.ord()))
            throw new Error("master="+master+" and a="+reduction.ord()+" and b="+r.reduction.ord()+"\n"+reduction+"\n"+r.reduction);
        */
        return reduction.compareTo(r.reduction);
    }

    private int signum(int x) {
        if (x==0) return 0;
        if (x<0) return -1;
        return 1;
    }

    public void perform() {
        if (reduction==null) return;
        phase.newNodeFromReduction(forest, reduction, pred);
    }
    public GSS.Phase predPhase() { return pred.phase(); }
    public Pos reduction() { return reduction; }
    public GSS.Phase targetPhase() { return phase; }
    public String toString() { return (pred.phase()==null ? 0 : pred.phase().pos) + ":"+reduction; }
}
