// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.chr;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.misc.*;
import edu.berkeley.sbp.Input.Location;

public class CharInput extends Cartesian.Input<Character> {

    private static class RollbackReader extends Reader {
        private final Reader r;
        public RollbackReader(Reader r) { this.r = r; }
        
        private char[] queue = new char[1024];
        private int head = 0;
        private int tail = 0;

        private void unread(char c) {
            if (tail >= queue.length) {
                if (tail - head > queue.length/2) {
                    char[] queue2 = new char[queue.length * 2];
                    System.arraycopy(queue, head, queue2, 0, tail-head);
                } else {
                    System.arraycopy(queue, head, queue, 0, tail-head);
                }
                tail = tail-head;
                head = 0;
            }
            queue[tail++] = c;
        }

        public void close() throws IOException { r.close(); }
        public int read() throws IOException {
            if (tail>head)
                return queue[head++];
            return r.read();
        }
        public int read(char cbuf[]) throws IOException { return read(cbuf, 0, cbuf.length); }
        public int read(char cbuf[], int off, int len) throws IOException {
            if (tail>head) {
                int count = Math.min(len, tail-head);
                System.arraycopy(queue, head, cbuf, off, count);
                head += count;
                return count;
            }
            return r.read(cbuf, off, len);
        }
        public long skip(long n) throws IOException { return r.skip(n); }
        public boolean ready() throws IOException { return true; }
        public boolean markSupported() { return false; }
        public void mark(int readAheadLimit) throws IOException { throw new IOException("not supported"); }
        public void reset() throws IOException { throw new IOException("not supported"); }
    }

    private final RollbackReader r;
    
    public CharInput(Reader r,      String s) {
        this.name = s;
        this.r = new RollbackReader(new BufferedReader(r));
    }
    public CharInput(String s)                  { this(new StringReader(s)); }
    public CharInput(Reader r)                  { this(r, null); }
    public CharInput(InputStream i)             { this(i, null); }
    public CharInput(InputStream i, String s)   { this(new InputStreamReader(i), s); }
    public CharInput(File f) throws IOException { this(new FileInputStream(f), f.getName()); }

    public CharInput(InputStream i, String s, boolean indent) {
        this(new InputStreamReader(i), s);
        this.indent = indent;
    }
    public String getName() { return name; }

    private String name;
    boolean cr = false;
    boolean indent = false;
    private int count = 0;
    private StringBuilder cache = new StringBuilder();
    
    public void setCacheEnabled(boolean enabled) {
        if (!enabled) cache = null;
        else if (cache == null) cache = new StringBuilder();
    }

    int indentation = -1;
    int lastIndentation = 0;
    int delta = 0;

    public boolean   isCR() { return cr; }
    public Character _next() throws IOException {
        Character ret = __next();
        if (ret==null) return null;
        char c = ret.charValue();
        return ret;
    }
    public Character __next() throws IOException {

        cr = false;

        int i = r.read();
        if (i==-1) {
            if (indent && indentation >= 0) {
                redent(indentation - lastIndentation);
                lastIndentation = indentation;
                indentation = -1;
                return __next();
            }
            return null;
        }
        char c = (char)i;
        if (cache != null) cache.append(c);
        cr = c=='\n';

        if (indent)
            if (cr && ignore) {
                ignore = false;
            } else if (cr) {
                while(true) {
                    indentation = 0;
                    do { i = r.read(); if (i==' ') indentation++; } while (i==' ');
                    if (i=='\n') { /* FIXME */ continue; }
                    if (i==-1) { /* FIXME */ }
                     if (indentation - lastIndentation > 0) {
                        r.unread('\n');
                        for(int j=0; j<indentation; j++) r.unread(' ');
                        redent(indentation - lastIndentation);
                    } else {
                        redent(indentation - lastIndentation);
                        r.unread('\n');
                        for(int j=0; j<indentation; j++) r.unread(' ');
                    }
                    if (i != -1) r.unread((char)i);
                    ignore = true;
                    break;
                }
                lastIndentation = indentation;
                indentation = -1;
                return __next();
            }

        return c;
    }

    private boolean ignore = false;

    private void redent(int i) {
        if (i<0) { r.unread(CharAtom.right); redent(i+1); return; }
        if (i>0) { r.unread(CharAtom.left); redent(i-1); return; }
    }

    public String showRegion(Region<Character> rc, int maxLength) {
        if (cache == null) return null;
        Cartesian.Region r = (Cartesian.Region)rc;
        int start = r.getStart().getScalar();
        int end = r.getEnd().getScalar();
        if (start < 0) start = 0;
        if (end < start) end = start;
        if (end > cache.length()) end = cache.length();
        String ret;
        if (end-start < maxLength) ret = cachesubstring(start, end);
        else ret = cachesubstring(start, start+(maxLength/2-5)) +
                 "..." +
                 cachesubstring(end-(maxLength/2-5), end);
        return StringUtil.escapify(ret, "\n\r\t");
    }

    private String cachesubstring(int start, int end) {
        if (start < 0) start = 0;
        if (end < 0)   end = 0;
        if (start >= cache.length()) start = cache.length();
        if (end >= cache.length()) end = cache.length();
        return cache.substring(start, end);
    }

    public void close() { }
}
