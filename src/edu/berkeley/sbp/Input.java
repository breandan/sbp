// (C) 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import java.io.*;
import java.util.*;
import edu.berkeley.sbp.util.*;

// FEATURE: Region implements Topology<Location<Tok>>

/** <font color=purple>a stream of <tt>Token</tt>s to be parsed</font> */
public interface Input<Token> {

    /** the current location within the input stream */
    public Location<Token> getLocation();

    /** returns the token just beyond the current location and advances beyond it */
    public Token next() throws IOException;
   
    /** a short string describing where the input is coming from, such as a filename */
    public String getName();

    /** might called by Parser when it is done with the input */
    public void close();

    /**
     *  <b>Optional:</b> <i>If possible</i>, this method will return a
     *  rendering of the input region (for example, if the input is a
     *  region of characters, it would be those characters) --
     *  otherwise, returns null.  In any case, the string returned
     *  will be no more than <tt>maxLength</tt> characters long;
     *  typically ellipses will be inserted to perform truncation.
     */
    public abstract String showRegion(Region<Token> r, int maxLength);

    /** <font color=purple>a location (position) in the input stream <i>between tokens</i></font> */
    public static interface Location<Token> extends Comparable<Location> {

        /** return the region between this location and <tt>loc</tt> */
        public Region<Token> createRegion(Location<Token> loc);

        public String toString();

        /** the location following this one */
        public Location next();

        /** the location preceding this one */
        public Location prev();
    }

    /** <font color=purple>a contiguous set of <tt>Location</tt>s</font> */
    public static interface Region<Token> {

        /** should return less than 80 chars if possible */
        public abstract String toString();
        
        /** The location of the start of this region */
        public abstract Location<Token> getStart();

        /** The location of the end of this region */
        public abstract Location<Token> getEnd();

    }

}


