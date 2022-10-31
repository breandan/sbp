// Copyright 2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;

import java.util.*;
import java.io.*;

/**
 *  This Reader inserts special characters into the stream to indicate
 *  when the indentation level has increased or decreased.<p>
 *  
 *  Lines are separated by LF characters (ASCII/Unicode 0x0A).  The
 *  indentation of a line is defined to be the number of contiguous
 *  spaces (ASCII/Unicode 0x20).  A blank line is a line consisting
 *  only of zero or more spaces.  <p>
 *
 *  If the indentation of the current line is <i>greater than</i> the
 *  indentation of the most recent non-blank line, the last space
 *  character of the current line's indentation will be <i>replaced
 *  by</i> an <tt>updent</tt> character. (FIXME not quite right)<p>
 *
 *  If the indentation of the next non-blank line is <i>less than</i>
 *  the indentation of the current line, one or more <tt>downdent</tt>
 *  characters will be <i>inserted</i> immediately after the last
 *  non-whitespace character on the current line. <p>
 *
 *  These rules have two goals:
 *
 *  <ul> <li> Whitespace never appears immediately after an
 *            <tt>updent</tt> or immediately before a
 *            <tt>downdent</tt>.  This simplifies grammars which are
 *            based on these characters.
 *
 *       <li> Blank lines have no effect on the placement of
 *            <tt>updent</tt> and <tt>downdent</tt>.
 *
 *       <li> Every non-space character from the original stream
 *            appears in the modified stream.  Furthermore, these
 *            characters appear at exactly the same row and column as
 *            they did in the original stream.  This simplifies
 *            debugging considerably.
 *  </ul>
 */
public class IndentingReader extends Reader {

    private final Reader r;
    private final char updent;
    private final char downdent;

    public IndentingReader(Reader r, char updent, char downdent) {
        this.r = r;
        this.updent = updent;
        this.downdent = downdent;
        istack.add(0);
    }

    private boolean indenting = true;
    private int indentation = 0;
    private ArrayList<Integer> istack = new ArrayList<Integer>();
    private ArrayList<Integer> blanks = new ArrayList<Integer>();
    private int queuedIndentation = 0;
    private int blankIndentation = 0;

    private int _peek = -2;
    public int _peek() throws IOException {
        if (_peek == -2) _peek = r.read();
        return _peek;
    }
    public int _read() throws IOException {
        int ret = _peek();
        _peek = -2;
        return ret;
    }

    public int read() throws IOException {
        while(true) {
            int i = _peek();
            if (i==-1 && istack.size() > 1) {
                istack.remove(istack.size()-1);
                return downdent;
            }
            if (i==-1) return -1;

            char c = (char)i;

            if (c=='\n') {
                if (indenting) {
                    blanks.add(indentation);
                } else {
                    blanks.add(0);
                }
                indenting=true;
                indentation = 0;
                _read();
                continue;
            }

            if (indenting) {

                // more indentation to consume
                if (c==' ') { indentation++; _read(); continue; }

                // reached end of whitespace; line has non-whitespace material
                int last = istack.size()==0 ? -1 : istack.get(istack.size()-1);

                // emit any required close-braces
                if (indentation < last) {
                    istack.remove(istack.size()-1);
                    return downdent;
                }

                // emit \n and any blank lines
                if (blankIndentation > 0) {
                    blankIndentation--;
                    return ' ';
                }
                if (blanks.size() > 0) {
                    blankIndentation = blanks.remove(blanks.size()-1);
                    return '\n';
                }

                if (queuedIndentation < indentation) {
                    queuedIndentation++;

                    // omit the last space of indentation if we're planning on emitting an open-brace
                    if (queuedIndentation < indentation || !(indentation > last))
                        return ' ';
                }

                // emit open-brace
                if (indentation > last) {
                    istack.add(indentation);
                    return updent;
                }

                // done with indentation
                indenting = false;
                queuedIndentation = 0;
            }
            return _read();
        }
    }

    public int read(char[] buf, int off, int len) throws IOException {
        int numread = 0;
        while(len>0) {
            int ret = read();
            if (ret==-1) break;
            buf[off] = (char)ret;
            off++;
            len--;
            numread++;
        }
        return (len>0 && numread==0) ? -1 : numread;
    }
    public void close() throws IOException { r.close(); }
}

