// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/** use of this class is not recommended; it can handle only S-attributed grammars */
public class ReflectiveWalker extends StringWalker {
    public ReflectiveWalker()              { this.target = this; }
    public ReflectiveWalker(Object target) { this.target = target; }
    private final Object target;
    public static String mangle(String s) {
        StringBuffer ret = new StringBuffer();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (i==0 && c >= '0' && c <= '9') { ret.append('_'); ret.append(c); continue; }
            switch(c) {
                case ' ':  ret.append("_space_"); break;
                case '~':  ret.append("_tilde_"); break;
                case ';':  ret.append("_semicolon_"); break;
                case '=':  ret.append("_equals_"); break;
                case '-':  ret.append("_minus_"); break;
                case ')':  ret.append("_rightparen_"); break;
                case '(':  ret.append("_leftparen_"); break;
                case '!':  ret.append("_bang_"); break;
                case '`':  ret.append("_backtick_"); break;
                case '\"': ret.append("_quotes_"); break;
                case '\'': ret.append("_quote_"); break;
                case ',':  ret.append("_comma_"); break;
                case '.':  ret.append("_dot_"); break;
                case '>':  ret.append("_greater_"); break;
                case '<':  ret.append("_less_"); break;
                case '&':  ret.append("_amp_"); break;
                case '@':  ret.append("_at_"); break;
                case '#':  ret.append("_hash_"); break;
                case '|':  ret.append("_pipe_"); break;
                case '^':  ret.append("_caret_"); break;
                case '%':  ret.append("_percent_"); break;
                case '$':  ret.append("_dollar_"); break;
                case ':':  ret.append("_colon_"); break;
                case '?':  ret.append("_question_"); break;
                case '/':  ret.append("_slash_"); break;
                case '\\': ret.append("_backslash_"); break;
                case '*':  ret.append("_star_"); break;
                case '+':  ret.append("_plus_"); break;
                case '_':  ret.append("_underscore_"); break;
                case '[':  ret.append("_leftbracket_"); break;
                case ']':  ret.append("_rightbracket_"); break;
                case '{':  ret.append("_leftbrace_"); break;
                case '}':  ret.append("_rightbrace_"); break;
                default:   ret.append(c); break;
            }
        }
        return ret.toString();
    }
    /*
    public Object walk(Tree<String> tree) {
        if (tree.head()!=null) {
            Member m = member("$"+mangle(tree.head()), 0, false);
            if (m!=null) {
                if ((m instanceof Method) && ((Method)m).getReturnType()==Void.TYPE) {
                    Reflection.fuzzyInvoke(target, m, new Object[0]);
                }
            }
        }
        return super.walk(tree);
    }
    */
    public void walk(String tag) {
        if (tag==null) return;
        Member m = member(mangle(tag), 0, false);
        if (m!=null) Reflection.fuzzyInvoke(target, m);
    }
    protected Object defaultWalk(String tag, Object[] argo) { return super.walk(tag, argo); }
    public Object walk(String tag, Object[] argo) {
        if (argo.length==0) return super.walk(tag, argo);
        if (argo==null) return tag;
        if (tag==null || "".equals(tag)) return argo;
        Member m = tag==null ? null : member(mangle(tag), argo.length, false);
        if (m==null) return defaultWalk(tag, argo);
        //System.out.println("preparing to invoke method " + (m==null ? "null" : (m.toString())) + " for sequence " + (owner()+"."+tag));
        if (m != null) return Reflection.fuzzyInvoke(target, m, argo);
        if (argo.length==0) return null;
        if (argo.length==1) return argo[0];
        Class c = argo[0].getClass();
        for(Object o : argo) c = Reflection.lub(c, o.getClass());
        Object[] ret = Reflection.newArray(c, argo.length);
        System.arraycopy(argo, 0, ret, 0, argo.length);
        return ret;
    }

    public Member member(String methodName, int nonVoid, boolean complain) {
        Class target = this.target.getClass();
        if (methodName == null || methodName.equals("")) return null;
        Member ret = null;
        for (Method m : target.getMethods()) {
            if (!m.getName().equals(methodName)) continue;
            if (m.getParameterTypes().length != nonVoid) continue;
            if (ret != null) {
                if (!complain) return null;
                throw new Error("two methods with " + nonVoid + " parameters: " + target.getName() + "." + methodName);
            }
            ret = m;
        }
        if (ret != null) return ret;
        Class t = target;
        while(t != null) {
            Class c = Reflection.forNameOrNull(t.getName() + "$" + methodName);
            if (c != null) {
                for (Constructor m : c.getConstructors()) {
                    if (m.getParameterTypes().length != nonVoid) continue;
                    if (ret != null) {
                        if (!complain) return null;
                        throw new Error("two constructors with " + nonVoid + " parameters: " + c.getName() + ".<init>()");
                    }
                    ret = m;
                }
                if (ret != null) return ret;
            }
            t = t.getSuperclass();
        }
        if (ret != null) return ret;
        if (!complain) return null;
        throw new Error("while computing return type of " +methodName+ " could not find method with name " + methodName +
                        " and " + nonVoid + " arguments");
    }
}
