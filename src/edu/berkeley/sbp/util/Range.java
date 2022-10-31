// Taken from com.onionnetworks.util (BSD license)
// Many thanks, Justin and Ry4an!

/*
 * Common Java Utilities
 *
 * Copyright (C) 2000-2001 Justin Chapweske (justin@chapweske.com)
 * Copyright (C) 2000-2001 Ry4an Brase (ry4an@ry4an.org) 
 * Copyright (C) 2001 Onion Networks
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials
 *    provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package edu.berkeley.sbp.util;
import java.util.*;
import java.text.*;
import java.io.*;

/**
 * This class represents a range of integers (incuding positive and negative 
 * infinity).
 */
public class Range implements Serializable {

    private boolean negInf, posInf;
    private long min,max;
   
    /**
     * Creates a new Range that is only one number, both min and max will
     * equal that number.
     * @param num The number that this range will encompass.
     */
    public Range(long num) {
        this(num,num,false,false);
    }

    /**
     * Creates a new Range from min and max (inclusive)
     * @param min The min value of the range.
     * @param max The max value of the range.
     */
    public Range(long min, long max) {
        this(min,max,false,false);
    }

    /**
     * Creates a new Range from min to postive infinity
     * @param min The min value of the range.
     * @param posInf Must be true to specify max == positive infinity
     */
    public Range(long min, boolean posInf) {
        this(min,Long.MAX_VALUE,false,posInf);
        if (!posInf) {
            throw new IllegalArgumentException("posInf must be true");
        }
    }

    /**
     * Creates a new Range from negative infinity to max.
     * @param negInf Must be true to specify min == negative infinity
     * @param max The max value of the range.
     */
    public Range(boolean negInf, long max) {
        this(Long.MIN_VALUE,max,negInf,false);
        if (!negInf) {
            throw new IllegalArgumentException("negInf must be true");
        }
    }

    /**
     * Creates a new Range from negative infinity to positive infinity.
     * @param negInf must be true.
     * @param posInf must be true.
     */
    public Range(boolean negInf, boolean posInf) {
        this(Long.MIN_VALUE,Long.MAX_VALUE,negInf,posInf);
        if (!negInf || !posInf) {
            throw new IllegalArgumentException
                ("negInf && posInf must be true");
        }
    }

    private Range(long min, long max, boolean negInf, boolean posInf) {
	if (min > max) {
	    throw new IllegalArgumentException
	      ("min cannot be greater than max");
	}
	// very common bug, its worth reporting for now.
	if (min == 0 && max == 0) {
	    System.err.println("Range.debug: 0-0 range detected. "+
			       "Did you intend to this? :");
	    new Exception().printStackTrace();
	}
        this.min = min;
        this.max = max;
        this.negInf = negInf;
        this.posInf = posInf;
    }
    
    /**
     * @return true if min is negative infinity.
     */
    public boolean isMinNegInf() {
        return negInf;
    }

    /**
     * @return true if max is positive infinity.
     */
    public boolean isMaxPosInf() {
        return posInf;
    }

    /**
     * @return the min value of the range.
     */
    public long getMin() {
	return min;
    }
    
    /**
     * @return the max value of the range.
     */
    public long getMax() {
	return max;
    }
    
    /**
     * @return The size of the range (min and max inclusive) or -1 if the range
     * is infinitly long.
     */
    public long size() {
        if (negInf || posInf) {
            return -1;
        }
        return max-min+1;
    }

    /**
     * @param i The integer to check to see if it is in the range.
     * @return true if i is in the range (min and max inclusive)
     */
    public boolean contains(long i) {
	return i >= min && i <= max;
    }
    
    /**
     * @param r The range to check to see if it is in this range.
     * @return true if this range contains the entirety of the passed range.
     */
    public boolean contains(Range r) {
	return r.min >= min && r.max <= max;
    }
    
    
    public int hashCode() {
	return (int) (min + 23 * max);
    }
    
    public boolean equals(Object obj) {
	if (obj instanceof Range &&
	    ((Range) obj).min == min && ((Range) obj).max == max &&
            ((Range) obj).negInf == negInf && ((Range) obj).posInf == posInf) {
	    return true;
	} else {
	    return false;
	}
    }
    
    public String toString() {
	if (!negInf && !posInf && min == max) {
	    return new Long(min).toString();
	} else {
	    return (negInf ? "(" : ""+min) + "-" + (posInf ? ")" : ""+max);
	}
    }
    
    /**
     * This method creates a new range from a String.
     * Allowable characters are all integer values, "-", ")", and "(".  The
     * open and closed parens indicate positive and negative infinity.
     * <pre>
     * Example strings would be:
     * "11" is the range that only includes 11
     * "-6" is the range that only includes -6
     * "10-20" is the range 10 through 20 (inclusive)
     * "-10--5" is the range -10 through -5
     * "(-20" is the range negative infinity through 20
     * "30-)" is the range 30 through positive infinity.
     * </pre>
     * @param s The String to parse
     * @return The resulting range
     * @throws ParseException, 
     */

    public static final Range parse(String s) throws ParseException {
        try {
            long min=0,max=0;
            boolean negInf=false,posInf=false;
            // search from the 1 pos because it may be a negative number.
            int dashPos = s.indexOf("-",1);
            if (dashPos == -1) { // no dash, one value.
                min = max = Long.parseLong(s);
            } else {
                if (s.indexOf("(") != -1) {
                    negInf = true;
                } else {
                    min = Long.parseLong(s.substring(0,dashPos));
                }
                if (s.indexOf(")") != -1) {
                    posInf = true;
                } else {
                    max = Long.parseLong(s.substring(dashPos+1,s.length()));
                }
            }
            if (negInf) {
                if (posInf) {
                    return new Range(true,true);
                } else {
                    return new Range(true,max);
                }
            } else if (posInf) {
                return new Range(min,true);
            } else {
                return new Range(min,max);
            }
        } catch (RuntimeException e) {
            throw new ParseException(e.getMessage(),-1);
        }
    }    


    /**
     * This class represents a set of integers in a compact form by using ranges.
     * This is essentially equivilent to run length encoding of a bitmap-based
     * set and thus works very well for sets with long runs of integers, but is
     * quite poor for sets with very short runs.
     *
     * This class is similar in flavor to Perl's IntSpan at 
     * http://world.std.com/~swmcd/steven/perl/lib/Set/IntSpan/
     *
     * The Ranges use negative and positive infinity so there should be no
     * border issues with standard set operations and they should behave
     * exactly as you'd expect from your set identities.
     *
     * While the data structure itself should be quite efficient for its intended
     * use, the actual implementation could be heavily optimized beyond what I've 
     * done, feel free to improve it.
     *
     * @author Justin F. Chapweske
     */
    public static class Set implements Iterable<Range>, Serializable {
 
        public static final int DEFAULT_CAPACITY = 16;
    
        boolean posInf, negInf;
        int rangeCount;
        long[] ranges;
    
        /**
         * Creates a new empty Range.Set
         */
        public Set() {
            ranges = new long[DEFAULT_CAPACITY * 2];
        }

        /**
         * Creates a new Range.Set and adds the provided Range
         * @param r The range to add.
         */
        public Set(Range r) {
            this();
            add(r);
        }

        public Set(Range[] ranges) {
            this();
            for(Range r : ranges) add(r);
        }
    
        public Set(Iterable<Range> it) {
            this();
            for(Range r : it) add(r);
        }
    
        /**
         * @param rs The set with which to union with this set.
         * @return A new set that represents the union of this and the passed set.
         */
        public Range.Set union(Range.Set rs) {
            // This should be rewritten to interleave the additions so that there 
            // is fewer midlist insertions.
            Range.Set result = new Range.Set();
            result.add(this);
            result.add(rs);
            return result;
        }
    
        /**
         * @param rs The set with which to intersect with this set.
         * @return new set that represents the intersct of this and the passed set.
         */
        public Range.Set intersect(Range.Set rs) {
            Range.Set result = complement();
            result.add(rs.complement());
            return result.complement();
        }
    
        /**
         * @return The complement of this set, make sure to check for positive
         * and negative infinity on the returned set.
         */
        public Range.Set complement() {
            Range.Set rs = new Range.Set();
            if (isEmpty()) {
                rs.add(new Range(true,true));
            } else {
                if (!negInf) {
                    rs.add(new Range(true,ranges[0]-1));
                }
                for (int i=0;i<rangeCount-1;i++) {
                    rs.add(ranges[i*2+1]+1,ranges[i*2+2]-1);
                }
                if (!posInf) {
                    rs.add(new Range(ranges[(rangeCount-1)*2+1]+1,true));
                }
            }
            return rs;
        }
	
        /**
         * @param i The integer to check to see if it is in this set..
         * @return true if i is in the set.
         */
        public boolean contains(long i) {
            int pos = binarySearch(i);
            if (pos > 0) {
                return true;
            }
            pos = -(pos+1);
            if (pos % 2 == 0) {
                return false;
            } else {
                return true;
            }
        }
 
        /**
         * //FIX unit test
         * Checks to see if this set contains all of the elements of the Range.
         *
         * @param r The Range to see if this Range.Set contains it.
         * @return true If every element of the Range is within this set.
         */
        public boolean contains(Range r) {
            Range.Set rs = new Range.Set();
            rs.add(r);
            return intersect(rs).equals(rs);
        }

        /**
         * Add all of the passed set's elements to this set.
         *
         * @param rs The set whose elements should be added to this set.
         */
        public void add(Range.Set rs) {
            for (Iterator it=rs.iterator();it.hasNext();) {
                add((Range) it.next());
            }
        }
    

        /**
         * Add this range to the set.
         *
         * @param r The range to add
         */
        public void add(Range r) {
            if (r.isMinNegInf()) {
                negInf = true;
            }
            if (r.isMaxPosInf()) {
                posInf = true;
            }
            add(r.getMin(),r.getMax());
        }

        /**
         * Add a single integer to this set.
         *
         * @param i The int to add.
         */
        public void add(long i) {
            add(i,i);
        }
    
        /**
         * Add a range to the set.
         * @param min The min of the range (inclusive)
         * @param max The max of the range (inclusive)
         */
        public void add(long min, long max) {

            if (min > max) {
                throw new IllegalArgumentException
                    ("min cannot be greater than max");
            }

            if (rangeCount == 0) { // first value.
                insert(min,max,0);
                return;
            }
	
            // This case should be the most common (insert at the end) so we will
            // specifically check for it.  Its +1 so that we make sure its not
            // adjacent.  Do the MIN_VALUE check to make sure we don't overflow
            // the long.
            if (min != Long.MIN_VALUE && min-1 > ranges[(rangeCount-1)*2+1]) {
                insert(min,max,rangeCount);
                return;
            } 

            // minPos and maxPos represent inclusive brackets around the various
            // ranges that this new range encompasses.  Anything within these
            // brackets should be folded into the new range.
            int minPos = getMinPos(min);
            int maxPos = getMaxPos(max);
	
            // minPos and maxPos will switch order if we are either completely
            // within another range or completely outside of any ranges.
            if (minPos > maxPos) { 
                if (minPos % 2 == 0) {
                    // outside of any ranges, insert.
                    insert(min,max,minPos/2);
                } else {
                    // completely inside a range, nop
                }
            } else {
                combine(min,max,minPos/2,maxPos/2);
            }

        }
    
        public void remove(Range.Set r) {
            for (Iterator it=r.iterator();it.hasNext();) {
                remove((Range) it.next());
            }
        }

        public void remove(Range r) {
            //FIX absolutely horrible implementation.
            Range.Set rs = new Range.Set();
            rs.add(r);
            rs = intersect(rs.complement());
            ranges = rs.ranges;
            rangeCount = rs.rangeCount;
            posInf = rs.posInf;
            negInf = rs.negInf;
        }

        public void remove(long i) {
            remove(new Range(i,i));
        }

        public void remove(long min, long max) {
            remove(new Range(min,max));
        }

        /**
         * @return An iterator of Range objects that this Range.Set contains.
         */
        public Iterator<Range> iterator() {
            ArrayList l = new ArrayList(rangeCount);
            for (int i=0;i<rangeCount;i++) {
                if (rangeCount == 1 && negInf && posInf) {
                    l.add(new Range(true,true));
                } else if (i == 0 && negInf) {
                    l.add(new Range(true,ranges[i*2+1]));
                } else if (i == rangeCount-1 && posInf) {
                    l.add(new Range(ranges[i*2],true));
                } else {
                    l.add(new Range(ranges[i*2],ranges[i*2+1]));
                }
            }
            return l.iterator();
        }

        /**
         * @return The number of integers in this set, -1 if infinate.
         */
        public long size() {
            if (negInf || posInf) {
                return -1;
            }
            long result = 0;
            for (Iterator it=iterator();it.hasNext();) {
                result+=((Range) it.next()).size();
            }
            return result;
        }

        /**
         * @return true If the set doesn't contain any integers or ranges.
         */
        public boolean isEmpty() {
            return rangeCount == 0;
        }
    
        /**
         * Parse a set of ranges seperated by commas.
         *
         * @see Range
         *
         * @param s The String to parse
         * @return The resulting range
         * @throws ParseException
         */

        public static Range.Set parse(String s) throws ParseException {
            Range.Set rs = new Range.Set();
            for (StringTokenizer st = new StringTokenizer(s,",");
                 st.hasMoreTokens();) {
                rs.add(Range.parse(st.nextToken()));
            }
            return rs;
        }

        public int hashCode() {
            int result = 0;
            for (int i = 0; i < rangeCount*2; i++) {
                result = (int) (91*result + ranges[i]);
            }
            return result;
        }

        public boolean containsAll(Range.Set rs) {
            for(Range r : rs)
                if (!contains(r)) return false;
            return true;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Range.Set) {
                Range.Set rs = (Range.Set) obj;
                if (negInf == rs.negInf &&
                    posInf == rs.posInf &&
                    rangeCount == rs.rangeCount &&
                    arraysEqual(ranges,0,rs.ranges,0,rangeCount*2)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean arraysEqual(int[] arr1, int start1, 
                                          int[] arr2, int start2, int len) {
            if (arr1 == arr2 && start1 == start2) {
                return true;
            }
            for (int i=len-1;i>=0;i--) {
                if (arr1[start1+i] != arr2[start2+i]) {
                    return false;
                }
            }
            return true;
        }    

        public static boolean arraysEqual(long[] arr1, int start1, 
                                          long[] arr2, int start2, int len) {
            if (arr1 == arr2 && start1 == start2) {
                return true;
            }
            for (int i=len-1;i>=0;i--) {
                if (arr1[start1+i] != arr2[start2+i]) {
                    return false;
                }
            }
            return true;
        }    

        public static boolean arraysEqual(char[] arr1, int start1, 
                                          char[] arr2, int start2, int len) {
            if (arr1 == arr2 && start1 == start2) {
                return true;
            }
            for (int i=len-1;i>=0;i--) {
                if (arr1[start1+i] != arr2[start2+i]) {
                    return false;
                }
            }
            return true;
        }

        public static boolean arraysEqual(byte[] arr1, int start1, 
                                          byte[] arr2, int start2, int len) {
            if (arr1 == arr2 && start1 == start2) {
                return true;
            }
            for (int i=len-1;i>=0;i--) {
                if (arr1[start1+i] != arr2[start2+i]) {
                    return false;
                }
            }
            return true;
        }

            
        /**
         * Outputs the Range in a manner that can be used to directly create
         * a new range with the "parse" method.
         */
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Iterator it=iterator();it.hasNext();) {
                sb.append(it.next().toString());
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }

        public Object clone() {
            Range.Set rs = new Range.Set();
            rs.ranges = new long[ranges.length];
            System.arraycopy(ranges,0,rs.ranges,0,ranges.length);
            rs.rangeCount = rangeCount;
            rs.posInf = posInf;
            rs.negInf = negInf;
            return rs;
        }

        private void combine(long min, long max, int minRange, int maxRange) {
            // Fill in the new values into the "leftmost" range.
            ranges[minRange*2] = Math.min(min,ranges[minRange*2]);
            ranges[minRange*2+1] = Math.max(max,ranges[maxRange*2+1]);
	
            // shrink if necessary.
            if (minRange != maxRange && maxRange != rangeCount-1) {
                System.arraycopy(ranges,(maxRange+1)*2,ranges,(minRange+1)*2,
                                 (rangeCount-1-maxRange)*2);
            }
	
            rangeCount -= maxRange-minRange;
        }
    
    
        /**
         * @return the position of the min element within the ranges.
         */
        private int getMinPos(long min) {
            // Search for min-1 so that adjacent ranges are included.
            int pos = binarySearch(min == Long.MIN_VALUE ? min : min-1);
            return pos >= 0 ? pos : -(pos+1);
        }
    
        /**
         * @return the position of the max element within the ranges.
         */
        private int getMaxPos(long max) {
            // Search for max+1 so that adjacent ranges are included.
            int pos = binarySearch(max == Long.MAX_VALUE ? max : max+1);
            // Return pos-1 if there isn't a direct hit because the max
            // pos is inclusive.
            return pos >= 0 ? pos : (-(pos+1))-1;
        }
    
        /**
         * @see java.util.Arrays#binarySearch
         */
        private int binarySearch(long key) {
            int low = 0;
            int high = (rangeCount*2)-1;
	
            while (low <= high) {
                int mid =(low + high)/2;
                long midVal = ranges[mid];
	    
                if (midVal < key) {
                    low = mid + 1;
                } else if (midVal > key) {
                    high = mid - 1;
                } else {
                    return mid; // key found
                }
            }
            return -(low + 1);  // key not found.
        }
    
        private void insert(long min, long max, int rangeNum) {
	
            // grow the array if necessary.
            if (ranges.length == rangeCount*2) {
                long[] newRanges = new long[ranges.length*2];
                System.arraycopy(ranges,0,newRanges,0,ranges.length);
                ranges = newRanges;
            }
	
            if (rangeNum != rangeCount) {
                System.arraycopy(ranges,rangeNum*2,ranges,(rangeNum+1)*2,
                                 (rangeCount-rangeNum)*2);
            }
            ranges[rangeNum*2] = min;
            ranges[rangeNum*2+1] = max;
            rangeCount++;
        }

        /*
        public static final void main(String[] args) throws Exception {
            Range.Set rs = Range.Set.parse("5-10,15-20,25-30");
            BufferedReader br = new BufferedReader(new InputStreamReader
                                                   (System.in));
            while (true) {
                System.out.println(rs.toString());
                String s = br.readLine();
                if (s.charAt(0) == '~') {
                    rs = rs.complement();
                } else if (s.charAt(0) == 'i') {
                    rs = rs.intersect(Range.Set.parse(br.readLine()));
                } else {
                    rs.add(Range.Set.parse(s));
                }
            }
        }
        */
    }
}

