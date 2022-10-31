// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.io.*;
import java.util.*;

public interface ToHTML {
    public void toHTML(HTML h);

    public static class HTML {
        private final StringBuffer sb;
        public HTML(StringBuffer sb) { this.sb = sb; }

        public void appendLiterally(String s) { sb.append(s); }
        public void appendLiterally(char c) { sb.append(c); }

        public void appendText(String s) {
            sb.append(escapify(s));
        }

        public String escapify(String s) {
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<s.length(); i++) {
                char c = s.charAt(i);
                switch(c) {
                    case '&':  sb.append("&amp;"); break;
                    case '<':  sb.append("&lt;"); break;
                    case '>':  sb.append("&gt;"); break;
                    case '\'': sb.append("&apos;"); break;
                    case '\"': sb.append("&quot;"); break;
                    default:
                        if (c < 32 || c >= 127) {
                            sb.append("&#x" + Integer.toString((int)(c & 0xffff), 16) + ";");
                        } else {
                            sb.append(c);
                        }
                }
            }
            return sb.toString();
        }

        public void entity(int entity) { appendLiterally("&#"+entity+";"); }
        public void entity(String entity) { appendLiterally("&"+entity+";"); }

        public void append(Object o) {
            if (o==null)                    appendLiterally("<tt><font color=red>null</font></tt>");
            else if (o instanceof ToHTML)   ((ToHTML)o).toHTML(this);
            else if (o instanceof Object[]) append((Object[])o);
            else                            appendText(o.toString());
        }
        public void append(int i) { sb.append(i); }
        public void append(char c) { append(""+c); }

        public void append(Object[] o) {
            for(int i=0; i<o.length; i++) {
                if (i>0) append(' ');
                append(o[i]);
            }
        }

        public void tag(String s) {
            appendLiterally("<");
            appendLiterally(s);
            appendLiterally("/>");
        }
        public void openTag(String s) { openTag(s, null); }
        public void openTag(String s, Object[] attrs) {
            appendLiterally("<");
            appendLiterally(s);
            if (attrs != null)
                for(int i=0; i<attrs.length; i+=2) {
                    appendLiterally(' ');
                    append(attrs[i]);
                    appendLiterally("=\'");
                    append(attrs[i+1]);
                    appendLiterally("\'");
                }
            appendLiterally(">");
        }
        public void closeTag(String s) {
            appendLiterally("</");
            appendLiterally(s);
            appendLiterally(">");
        }
        public void tag(String s, Object o) { tag(s, null, o); }
        public void tag(String s, Object[] attrs, Object o) {
            if (s != null) openTag(s, attrs);
            append(o);
            if (s != null) {
                appendLiterally("</");
                appendLiterally(s);
                appendLiterally(">");
            }
        }

    }
}
