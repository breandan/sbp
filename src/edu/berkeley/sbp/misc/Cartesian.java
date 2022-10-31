// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.Input.Location;
import edu.berkeley.sbp.util.*;

public class Cartesian {

    public static abstract class Input<Token> implements edu.berkeley.sbp.Input<Token> {

        public abstract Token   _next() throws IOException;
        public abstract boolean isCR();

        long then = 0;
        private Cartesian.Location location = new Cartesian.Location();
        public  edu.berkeley.sbp.Input.Location    getLocation() { return location; }

        public Token next() throws IOException {
            int line   = location.getRow();
            int col    = location.getCol();
	    int scalar = location.getScalar();
            Token t = _next();
            if (t==null) return null;
            if (isCR()) { 
                line++;
                col = 1;
            } else {
                col++;
            }
            location.next = new Cartesian.Location(col, line, scalar+1);
            location.next.prev = location;
            location = location.next;
            return t;
        }

        public String showRegion(Input.Region<Token> region) {
            return null;
        }
    }

    /** an implementation of Location for a cartesian grid (row, col) */
    public static class Location<Tok> implements Input.Location<Tok>, Comparable<Input.Location> {
        protected final int row;
        protected final int col;
        protected final int scalar;
        Location<Tok> next = null;
        Location<Tok> prev = null;
        public Location<Tok> next() { return next; }
        public Location<Tok> prev() { return prev; }
        public String toString() { return row+":"+col; }
        public int getCol() { return col; }
        public int getRow() { return row; }
        public int getScalar() { return scalar; }
        public Location() { this(-1, 1, 0); }
        public Location(int col, int row, int scalar) { this.row = row; this.col = col; this.scalar = scalar; }
        public int compareTo(Input.Location loc) throws ClassCastException {
            if (!(loc instanceof Cartesian.Location)) throw new ClassCastException(loc.getClass().getName());
            Location<Tok> c = (Location<Tok>)loc;
            if (row < c.row) return -1;
            if (row > c.row) return  1;
            if (col < c.col) return -1;
            if (col > c.col) return  1;
            return 0;
        }
        public Input.Region<Tok> createRegion(Input.Location<Tok> loc) {
            return new Region<Tok>(this, (Cartesian.Location<Tok>)loc); }
    }

    public static class Region<Tok> implements Input.Region<Tok> {
        public final Location<Tok> start;
        public final Location<Tok> end;
        public Location<Tok> getStart() { return start; }
        public Location<Tok> getEnd() { return end; }
        public String toString() {
            if (start.row==end.row && start.col==end.col) return start+"";
            if (start.row==end.row) return start.row+":"+(start.col+"-"+end.col);
            return start+"-"+end;
        }
        public Region(Location<Tok> a, Location<Tok> b) {
            switch(a.compareTo(b)) {
                case -1:
                case  0: start=a; end=b; return;
                case  1: start=b; end=a; return;
                default: throw new Error("impossible");
            }
        }
    }
}
