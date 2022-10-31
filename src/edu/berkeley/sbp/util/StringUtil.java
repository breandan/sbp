// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;

/** miscellaneous string utilities */
public class StringUtil {

    public static String pad(int i,String s) {
        return s.length() >= i ? s : pad(i-1,s)+" ";
    }

    public static String join(String[] s, String sep) {
        StringBuffer ret = new StringBuffer();
        for(int i=0; i<s.length; i++) {
            if (i>0) ret.append(sep);
            ret.append(s[i]);
        }
        return ret.toString();
    }
    public static String toJavaString(String s) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            switch(c) {
                case '\\': sb.append("\\\\");  break;
                case '\"': sb.append("\\\"");  break;
                case '\'': sb.append("\\\'");  break;
                case '\n':  sb.append("\\n");  break;
                case '\r':  sb.append("\\r");  break;
                case '\t':  sb.append("\\t");  break;
                default: sb.append(c); break;
            }
        }
        return sb.toString();
    }

    public static String unescapify(String s) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (c=='\\') {
                c = s.charAt(++i);
                switch(c) {
                    case 'r': c = '\r'; break;
                    case 'n': c = '\n'; break;
                    case 't': c = '\t'; break;
                    default: break;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
    /*
                case ' ':  sb.append("\\ ");  break;
                case '{':  sb.append("\\{");  break;
                case '}':  sb.append("\\}");  break;
                case ':':  sb.append("\\:");  break;
    */
    public static String escapify(String s) { return escapify(s, "\\\n\r\t"); }
    public static String escapify(String s, String illegal) {
        if (s==null) return null;
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (illegal.indexOf(c) != -1)
                switch(c) {
                    case '\n':  sb.append("\\n"); continue;
                    case '\r':  sb.append("\\r"); continue;
                    case '\t':  sb.append("\\t"); continue;
                    default:    sb.append('\\');  break;
                }
            else if (c < 32 || c >= 127) {
                sb.append("\\x"+((int)c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String indent(String s, int indent) {
        if (s.indexOf('\n')==-1) return s;
        StringBuffer ret = new StringBuffer();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            ret.append(c);
            if (c=='\n')
                for(int j=0; j<indent; j++)
                    ret.append(' ');
        }
        return ret.toString();
    }

    public static String uncapitalize(String s) {
        if (s==null) return null;
        return (""+s.charAt(0)).toUpperCase()+s.substring(1);
    }
}
