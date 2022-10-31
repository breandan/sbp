// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.io.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

// FIXME: decent error reporting
/** Random reflection-related utilities */
public final class Reflection {
    
    public static Object rebuild(Object o, Class c) {
        //System.out.println("rebuild " + o + " (a " + (o==null?null:o.getClass().getName()) + ") " + c);
        if (o==null || c.isAssignableFrom(o.getClass())) return o;
        if ((c == Character.class || c == Character.TYPE) && o instanceof String && ((String)o).length()==1) return new Character(((String)o).charAt(0));
        if (o instanceof Character && c == String.class) return o+"";
        if (o instanceof Object[]) {
            Object[] a = (Object[])o;
            if (c.isArray()) {
                Object[] ret = (Object[])Array.newInstance(c.getComponentType(), a.length);
                for(int i=0; i<ret.length; i++) {
                    Object o2 = rebuild(a[i], c.getComponentType());
                    //if (o2 != null) System.out.println("storing " + o2.getClass().getName() + " to " + c.getComponentType());
                    ret[i] = o2;
                }
                return ret;
            }
            if (c == String.class) {
                boolean ok = true;
                for(int i=0; i<a.length; i++)
                    if (a[i]==null || (!(a[i] instanceof Character) && !(a[i] instanceof String)))
                        ok = false;
                if (ok) {
                    StringBuffer s = new StringBuffer();
                    for(int i=0; i<a.length; i++)
                        s.append(a[i] instanceof Character
                                 ? (((Character)a[i]).charValue())+""
                                 : (String)a[i]
                                 );
                    return s.toString();
                }
            }
        } else if (c.isArray()) {
            Object[] ret = (Object[])Array.newInstance(c.getComponentType(), 1);
            ret[0] = o;
            return ret;
        } else if (c==int.class && o instanceof Number) { return o;
        } else {
            throw new Error("unable to cast " + o + " from " + o.getClass().getName() + " to " + c.getName());
        }
        return o;
    }

    public static Object[] newArray(Class c, int i) {
        return (Object[])Array.newInstance(c, i);
    }

    public static String show(Object o) {
        if (o==null) return "null";
        if (o instanceof Show) return show((Show)o);
        if (! (o instanceof Object[])) return o.toString() + " : " + o.getClass().getName();
        Object[] arr = (Object[])o;
        StringBuffer ret = new StringBuffer();
        ret.append(o.getClass().getComponentType().getName());
        ret.append("["+arr.length+"]:");
        for(int i=0; i<arr.length; i++)
            ret.append(StringUtil.indent("\n"+show(arr[i]), 4));
        return ret.toString();
    }

    public static String show(Show o) {
        StringBuffer ret = new StringBuffer();
        for(Field f : o.getClass().getFields()) {
            ret.append("\n" + f.getName() + " = ");
            try {
                ret.append(show(f.get(o)));
            } catch (Exception e) {
                ret.append("**"+e+"**");
                throw new RuntimeException(e);
            }
        }
        return o.getClass().getName() + " {" + StringUtil.indent(ret.toString(), 4) + "\n}";
    }

    public static Object lub(Object argo) {
        if (argo instanceof Object[]) return lub((Object[])argo);
        return argo;
    }
    public static Object[] lub(Object[] argo) {
        if (argo==null) return null;
        Class c = null;
        for(int i=0; i<argo.length; i++) {
            if (argo[i]==null) continue;
            argo[i] = lub(argo[i]);
            c = Reflection.lub(c, argo[i].getClass());
        }
        if (c==null) c = Object.class;
        Object[] ret = Reflection.newArray(c, argo.length);
        System.arraycopy(argo, 0, ret, 0, argo.length);
        for(int i=0; i<ret.length; i++)
            ret[i] = lub(ret[i]);
        return ret;
    }

    public static Class lub(Class a, Class b) {
        if (a==null) return b;
        if (b==null) return a;
        if (a==b) return a;
        if (a.isAssignableFrom(b)) return a;
        if (b.isAssignableFrom(a)) return b;
        if (a.isArray() && b.isArray())
            return arrayClass(lub(a.getComponentType(), b.getComponentType()));
        return lub(b, a.getSuperclass());
    }

    public static Class arrayClass(Class c) {
        return Reflection.newArray(c, 0).getClass();
    }

    /** a version of <tt>Class.forName</tt> that returns <tt>null</tt> instead of throwing an exception */
    public static Class forNameOrNull(String s) {
        try {
            return Class.forName(s); 
        } catch (ClassNotFoundException _) {
            return null;
        }
    }

    public static Object fuzzyInvoke(Object o, Member m) { return fuzzyInvoke(o, m, new Object[0]); }
    /** invoke method/constructor m on object o with arguments args, and perform reasonable coercions as needed */
    public static Object fuzzyInvoke(Object o, Member m, Object[] args) {
        try {
            Class[] argTypes = m instanceof Method ? ((Method)m).getParameterTypes() : ((Constructor)m).getParameterTypes();
            boolean ok = true;
            Object[] args2 = new Object[args.length];
            System.arraycopy(args, 0, args2, 0, args.length);
            args = args2;
            for(int i=0; i<args.length; i++) {
                Class goal = argTypes[i];
                Class actual = args[i] == null ? null : args[i].getClass();
                if (args[i] == null || goal.isAssignableFrom(actual)) continue;
                try {
                    args[i] = rebuild(args[i], goal);
                } catch (Error e) {
                    throw new Error(e.getMessage() + "\n   while trying to fuzzyInvoke("+m.getName()+")");
                }
            }
            // for debugging
            /*
            System.out.println("\ninvoking " + o + "." + m);
            for(int i=0; i<args.length; i++) {
                if (args[i] instanceof Object[]) {
                    System.out.print("  arg => " + zoo(args[i]));
                } else {
                    System.out.println("  arg => " + args[i] + (args[i]==null?"":(" (a " + args[i].getClass().getName() + ")")));
                }
            }
            */
            // FIXME: deal with the case where it is an inner class ctor
            return m instanceof Method ? ((Method)m).invoke(o, args) : ((Constructor)m).newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String zoo(Object o) {
        if (o==null) return "null";
        if (o instanceof Object[]) {
            String ret = "[ ";
            for(int j=0; j<((Object[])o).length; j++)
                ret += zoo(((Object[])o)[j]) + " ";
            return ret+"]";
        }
        return o+"";
    }

    public static boolean isConcrete(Class c) {
        if ((c.getModifiers() & Modifier.ABSTRACT) != 0) return false;
        if ((c.getModifiers() & Modifier.INTERFACE) != 0) return false;
        return true;
    }
    public static boolean isStatic(Field f) {
        if ((f.getModifiers() & Modifier.STATIC) != 0) return true;
        return false;
    }

    public static Field getField(Class c, String s) {
        try {
            for(Field f : c.getDeclaredFields())
                if (f.getName().equals(s))
                    return f;
        } catch (Exception e) { }
        if (c.getSuperclass()==null || c.getSuperclass()==c) return null;
        return getField(c.getSuperclass(), s);
    }
    public static Field getField(Class c, int i) {
        try {
            for(Field f : c.getDeclaredFields()) {
                if (isStatic(f)) continue;
                return f;
            }
            if (c.getSuperclass()==null || c.getSuperclass()==c) return null;
            return getField(c.getSuperclass(), i);
        } catch (Exception e) { }
        return null;
    }

    public static interface Show {
    }

    public static Object impose(Object o, Object[] fields) {
        if (o instanceof Class) return impose((Class)o, fields);
        if (o instanceof Method) return impose((Method)o, fields);
        if (o instanceof Constructor) return impose((Constructor)o, fields);
        return null;
    }
    public static Object impose(Class _class, Object[] fields) {
        Object ret = null;
        try { ret = _class.newInstance(); }
        catch (Exception e) { rethrow(e, "while trying to instantiate a " + _class.getName()); }
        Field[] f = _class.getFields();
        int j = 0;
        for(int i=0; i<f.length; i++) {
            Object tgt = Reflection.lub(fields[i]);
            if (f[i].getType() == String.class) tgt = stringify(tgt);
            // FUGLY
            tgt = coerce(tgt, f[i].getType());
            try {
                f[i].set(ret, tgt);
            } catch (Exception e) {
                rethrow(e, "while setting \n    " +
                        f[i] +
                        "\n     to " + show(tgt));
            }
        }
        return ret;
    }

    public static Object rethrow(Exception e, String message) {
        e.printStackTrace();
        StackTraceElement[] st = e.getStackTrace();
        RuntimeException re = new RuntimeException(e.getMessage() + "\n  " + message);
        re.setStackTrace(st);
        throw re;
    }

    public static Object impose(Method _method, Object[] fields) {
        Object[] args = mkArgs(_method.getParameterTypes(), fields);
        try {
            return _method.invoke(null, args);
        } catch (Exception e) {
            return rethrow(e, "while trying to invoke \n    " +
                           _method +
                           "\n    with arguments " + show(args));
        }
    }
    public static Object impose(Constructor _ctor, Object[] fields) {
        Object[] args = mkArgs(_ctor.getParameterTypes(), fields);
        try {
            return _ctor.newInstance(args);
        } catch (Exception e) {
            return rethrow(e, "while trying to invoke \n    " +
                           _ctor +
                           "\n    with arguments " + show(args));
        }
    }

    private static Object[] mkArgs(Class[] argTypes, Object[] fields) {
        int j = 0;
        Object[] args = new Object[argTypes.length];
        for(int i=0; i<args.length; i++) {
            Object tgt = Reflection.lub(fields[i]);
            if (argTypes[i] == String.class) tgt = Reflection.stringify(tgt);
            // FUGLY
            tgt = Reflection.coerce(tgt, argTypes[i]);
            args[i] = tgt;
        }
        return args;
    }

    public static String stringify(Object o) {
        if (o==null) return "";
        if (!(o instanceof Object[])) return o.toString();
        Object[] arr = (Object[])o;
        StringBuffer ret = new StringBuffer();
        for(int i=0; i<arr.length; i++)
            ret.append(arr[i]);
        return ret.toString();
    }

    public static Object coerce(Object o, Class c) {
        try {
            return coerce0(o, c);
        } catch (Exception e) {
            return (Object[])rethrow(e, "while trying to coerce " + show(o) + " to a " + c.getName());
        }
    }
    public static Object coerce0(Object o, Class c) {
        if (o==null) return null;
        if (c.isInstance(o)) return o;
        if (c == char.class) {
            return o.toString().charAt(0);
        }
        if (c==int.class || c==Integer.class) {
            String s = (String)coerce(o, String.class);
            return new Integer(Integer.parseInt(s));
        }

        if (o.getClass().isArray() &&
            o.getClass().getComponentType().isArray() &&
            o.getClass().getComponentType().getComponentType() == String.class &&
            c.isArray() &&
            c.getComponentType() == String.class) {
            String[] ret = new String[((Object[])o).length];
            for(int i=0; i<ret.length; i++) {
                StringBuffer sb = new StringBuffer();
                for(Object ob : (Object[])(((Object[])o)[i]))
                    sb.append(ob);
                ret[i] = sb.toString();
            }
            return ret;
        }

        if (c.isArray() && (c.getComponentType().isInstance(o))) {
            Object[] ret = (Object[])Array.newInstance(c.getComponentType(), 1);
            ret[0] = o;
            return ret;
        }

        if (o.getClass().isArray() && c.isArray()) {
            boolean ok = true;
            for(int i=0; i<((Object[])o).length; i++) {
                Object ob = (((Object[])o)[i]);
                if (ob != null) {
                    ok = false;
                }
            }
            if (ok) {
                return Array.newInstance(c.getComponentType(), ((Object[])o).length);
            }
        }
        return o;
    }

}
