// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.meta;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.chr.*;
import edu.berkeley.sbp.misc.*;
import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.io.*;

/**
 *  The inner classes of this class represent nodes in the Abstract
 *  Syntax Tree of a grammar.
 */
public class GrammarAST {

    public static interface ImportResolver {
        public InputStream getImportStream(String importname);
    }

    /**
     *  Returns a Union representing the metagrammar (<tt>meta.g</tt>); the Tree produced by
     *  parsing with this Union should be provided to <tt>buildFromAST</tt>
     */
    public static Union getMetaGrammar() {
        return buildFromAST(MetaGrammar.meta, "s", null);
    }

    /**
     *  Create a grammar from a parse tree and binding resolver
     * 
     *  @param t   a tree produced by parsing a grammar using the metagrammar
     *  @param s   the name of the "start symbol"
     *  @param gbr a GrammarBindingResolver that resolves grammatical reductions into tree-node-heads
     */
    public static Union buildFromAST(Tree grammarAST, String startingNonterminal, ImportResolver resolver) {
        return new GrammarAST(resolver, "").buildGrammar(grammarAST, startingNonterminal);
    }

    /** This does not work yet */
    public static void emitCode(PrintWriter pw, Tree grammarAST, String startingNonterminal, ImportResolver resolver) {
        GrammarAST ga = new GrammarAST(resolver, "");
        Object o = ga.walk(grammarAST);
        GrammarAST.GrammarNode gn = (GrammarAST.GrammarNode)o;
        EmitContext ecx = ga.new EmitContext(gn);
        gn.emitCode(ecx, pw, "com.foo", "ClassName");
    }

    private static Object illegalTag = ""; // this is the tag that should never appear in the non-dropped output FIXME

    // Instance //////////////////////////////////////////////////////////////////////////////

    private final String prefix;
    private final ImportResolver resolver;

    public GrammarAST(ImportResolver resolver, String prefix) {
        this.prefix = prefix;
        this.resolver = resolver;
    }

    // Methods //////////////////////////////////////////////////////////////////////////////

    private Union buildGrammar(Tree t, String rootNonTerminal) {
        Object o = walk(t);
        if (o instanceof Union) return (Union)o;
        return ((GrammarAST.GrammarNode)o).build(rootNonTerminal);
    }

    private Object[] walkChildren(Tree t) {
        Object[] ret = new Object[t.size()];
        for(int i=0; i<ret.length; i++)
            ret[i] = walk(t.child(i));
        return Reflection.lub(ret);
    }
    private static String stringifyChildren(Tree t) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<t.size(); i++) {
            sb.append(t.child(i).head());
            sb.append(stringifyChildren(t.child(i)));
        }
        return sb.toString();
    }
    private static String unescape(Tree t) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<t.size(); i++)
            sb.append(t.child(i).head()+stringifyChildren(t.child(i)));
        return sb.toString();
    }

    private ElementNode walkElement(Tree t) { return (ElementNode)walk(t); }
    private String      walkString(Tree t) { return (String)walk(t); }
    private Seq         walkSeq(Tree t) { return (Seq)walk(t); }
    private Object walk(Tree t) {
        String head = (String)t.head();
        while(head.indexOf('.') > 0)
            head = head.substring(head.indexOf('.')+1);
        if (head==null) throw new RuntimeException("head is null: " + t);
        if (head.equals("|")) return walkChildren(t);
        if (head.equals("RHS")) return walkChildren(t);
        if (head.equals("Grammar")) return new GrammarNode(walkChildren(t));
        if (head.equals("(")) return new UnionNode((Seq[][])walkChildren(t.child(0)));
        if (head.equals("Word")) return stringifyChildren(t);
        if (head.equals("Elements")) return new Seq((ElementNode[])Reflection.rebuild(walkChildren(t), ElementNode[].class));
        if (head.equals("NonTerminalReference")) return new ReferenceNode(stringifyChildren(t.child(0)));
        if (head.equals(")"))   return new ReferenceNode(stringifyChildren(t.child(0)), true);
        if (head.equals(":"))   return new LabelNode(stringifyChildren(t.child(0)), walkElement(t.child(1)));
        if (head.equals("::"))  return walkSeq(t.child(1)).tag(walkString(t.child(0)));
        if (head.equals("...")) return new DropNode(new RepeatNode(new TildeNode(new AtomNode()), null, true,  true,  false));

        if (head.equals("++"))  return new RepeatNode(walkElement(t.child(0)), null,                      false, true,  true);
        if (head.equals("+"))   return new RepeatNode(walkElement(t.child(0)), null,                      false, true,  false);
        if (head.equals("++/")) return new RepeatNode(walkElement(t.child(0)), walkElement(t.child(1)),   false, true,  true);
        if (head.equals("+/"))  return new RepeatNode(walkElement(t.child(0)), walkElement(t.child(1)),   false, true,  false);
        if (head.equals("**"))  return new RepeatNode(walkElement(t.child(0)), null,                      true,  true,  true);
        if (head.equals("*"))   return new RepeatNode(walkElement(t.child(0)), null,                      true,  true,  false);
        if (head.equals("**/")) return new RepeatNode(walkElement(t.child(0)), walkElement(t.child(1)),   true,  true,  true);
        if (head.equals("*/"))  return new RepeatNode(walkElement(t.child(0)), walkElement(t.child(1)),   true,  true,  false);
        if (head.equals("?"))   return new RepeatNode(walkElement(t.child(0)), null,                      true,  false, false);

        if (head.equals("!"))   return new DropNode(walkElement(t.child(0)));
        if (head.equals("^"))   return new LiteralNode(walkString(t.child(0)), true);
        if (head.equals("`"))   return new BacktickNode(walkElement(t.child(0)));
        if (head.equals("Quoted")) return stringifyChildren(t);
        if (head.equals("Literal")) return new LiteralNode(walkString(t.child(0)));
        if (head.equals("->")) return walkSeq(t.child(0)).follow(walkElement(t.child(1)));
        if (head.equals("DropNT")) return new NonTerminalNode(walkString(t.child(0)), (Seq[][])walkChildren(t.child(1)), false, null, true, false);
        if (head.equals("=")) return new NonTerminalNode(walkString(t.child(0)), (Seq[][])walk(t.child(2)),
                                                         true, t.size()==2 ? null : walkString(t.child(1)), false, false);
        if (head.equals("&"))   return walkSeq(t.child(0)).and(walkSeq(t.child(1)));
        if (head.equals("&~"))  return walkSeq(t.child(0)).andnot(walkSeq(t.child(1)));
        if (head.equals("/"))   return (walkSeq(t.child(0))).separate(walkElement(t.child(1)));
        if (head.equals("()"))  return new LiteralNode("");
        if (head.equals("["))   return new AtomNode((char[][])Reflection.rebuild(walkChildren(t), char[][].class));
        if (head.equals("\\{")) return new DropNode(new AtomNode(new char[] { CharAtom.left, CharAtom.left }));
        if (head.equals("\\}")) return new DropNode(new AtomNode(new char[] { CharAtom.right, CharAtom.right }));
        if (head.equals(">>"))  return new DropNode(new AtomNode(new char[] { CharAtom.left, CharAtom.left }));
        if (head.equals("<<"))  return new DropNode(new AtomNode(new char[] { CharAtom.right, CharAtom.right }));
        if (head.equals("~"))   return new TildeNode(walkElement(t.child(0)));
        if (head.equals("~~"))  return new Seq(new RepeatNode(new TildeNode(new AtomNode()), null, true,  true,  false)).andnot(walkSeq(t.child(0)));
        if (head.equals("Range")) {
            if (t.size()==2 && ">".equals(t.child(0).head())) return new char[] { CharAtom.left, CharAtom.left };
            if (t.size()==2 && "<".equals(t.child(0).head())) return new char[] { CharAtom.right, CharAtom.right };
            if (t.size()==1) return new char[] { unescape(t).charAt(0), unescape(t).charAt(0) };
            return new char[] { unescape(t).charAt(0), unescape(t).charAt(1) };
        }
        if (head.equals("\"\"")) return "";
        if (head.equals("\n"))   return "\n";
        if (head.equals("\t"))   return "\t";
        if (head.equals("\r"))   return "\r";
        if (head.equals("SubGrammar")) return GrammarAST.buildFromAST(t.child(0), "s", resolver);
        if (head.equals("NonTerminal"))
            return new NonTerminalNode(walkString(t.child(0)),
                                       (Seq[][])walkChildren(t.child(1)), false, null, false, false);
        if (head.equals("Colons")) {
            String tag = walkString(t.child(0));
            Seq[][] seqs = (Seq[][])walk(t.child(1));
            for(Seq[] seq : seqs)
                for(int i=0; i<seq.length; i++)
                    seq[i] = seq[i].tag(tag);
            return new NonTerminalNode(tag, seqs, false, null, false, true);
        }
        if (head.equals("#import")) {
            if (resolver != null) {
                String fileName = (String)stringifyChildren(t.child(0));
                try {
                    String newPrefix = t.size()<2 ? "" : (walkString(t.child(1))+".");
                    InputStream fis = resolver.getImportStream(fileName);
                    if (fis==null)
                        throw new RuntimeException("unable to find #include file \""+fileName+"\"");
                    Tree tr = new CharParser(getMetaGrammar()).parse(fis).expand1();
                    return (GrammarNode)new GrammarAST(resolver, newPrefix).walk(tr);
                } catch (Exception e) {
                    throw new RuntimeException("while parsing " + fileName, e);
                }
            } else {
                throw new RuntimeException("no resolver given");
            }
        }
        throw new RuntimeException("unknown head: \"" + head + "\" => " + (head.equals("...")));
    }

    
    // Nodes //////////////////////////////////////////////////////////////////////////////

    /** Root node of a grammar's AST; a set of named nonterminals */
    private class GrammarNode extends HashMap<String,NonTerminalNode> {
        public GrammarNode(NonTerminalNode[] nonterminals) {
            for(NonTerminalNode nt : nonterminals) {
                if (nt==null) continue;
                if (this.get(nt.name)!=null)
                    throw new RuntimeException("duplicate definition of nonterminal \""+nt.name+"\"");
                this.put(nt.name, nt);
            }
        }
        public  GrammarNode(Object[] nt) { add(nt); }
        private void add(Object o) {
            if (o==null) return;
            else if (o instanceof Object[]) for(Object o2 : (Object[])o) add(o2);
            else if (o instanceof NonTerminalNode) {
                NonTerminalNode nt = (NonTerminalNode)o;
                if (this.get(nt.name)!=null)
                    throw new RuntimeException("duplicate definition of nonterminal \""+nt.name+"\"");
                this.put(nt.name, nt);
            }
            else if (o instanceof GrammarNode)
                for(NonTerminalNode n : ((GrammarNode)o).values())
                    add(n);
        }
        public String toString() {
            String ret = "[ ";
            for(NonTerminalNode nt : values()) ret += nt + ", ";
            return ret + " ]";
        }
        public Union build(String rootNonterminal) {
            BuildContext cx = new BuildContext(this);
            Union u = null;
            for(GrammarAST.NonTerminalNode nt : values())
                if (nt.name.equals(rootNonterminal))
                    return (Union)cx.get(nt.name);
            return null;
        }
        public void emitCode(EmitContext cx, PrintWriter pw, String packageName, String className) {
            pw.println("package " + packageName + ";");
            pw.println("public class " + className + " {");
            // FIXME: root walking method
            //pw.println("  public static XXX walk() root");
            for(NonTerminalNode nt : values()) {
                if (!(nt.name.charAt(0) >= 'A' && nt.name.charAt(0) <= 'Z')) continue;
                StringBuffer fieldDeclarations = new StringBuffer();
                StringBuffer walkCode = new StringBuffer();
                nt.getUnionNode().emitCode(cx, fieldDeclarations, walkCode);
                if (nt.tagged) {
                    pw.println("  public static class " + nt.name + "{");
                    pw.println(fieldDeclarations);
                    pw.println("  }");
                    pw.println("  public static " + nt.name + " walk"+nt.name+"(Tree t) {");
                    pw.println("    int i = 0;");
                    pw.println(walkCode);
                    pw.println("  }");
                } else {
                    // FIXME; list who extends it
                    pw.println("  public static interface " + nt.name + "{ }");
                    // FIXME: what on earth is this going to be?
                    pw.println("  public static " + nt.name + " walk"+nt.name+"(Tree t) {");
                    pw.println("    throw new Error(\"FIXME\");");
                    pw.println("  }");
                }
            }
            pw.println("}");
        }
    }

    /** a NonTerminal is always a union at the top level */
    private class NonTerminalNode {
        public final boolean alwaysDrop;
        public final String  name;
        public final ElementNode elementNode;
        public final UnionNode unionNode;
        public final boolean tagged;
        public NonTerminalNode(String name, Seq[][] sequences, boolean rep, String sep, boolean alwaysDrop, boolean tagged) {
            this.name = prefix + name;
            this.alwaysDrop = alwaysDrop;
            this.tagged = tagged;
            this.unionNode = new UnionNode(sequences, rep, sep==null?null:(prefix + sep));
            this.elementNode = alwaysDrop ? new DropNode(unionNode) : unionNode;
        }
        public boolean isDropped(Context cx) { return alwaysDrop; }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) { return cx.get(name); }
        public ElementNode getElementNode() { return elementNode; }
        public UnionNode   getUnionNode() { return unionNode; }
    }

    /** a sequence */
    private class Seq {
        /** elements of the sequence */
        ElementNode[] elements;
        /** follow-set, if explicit */
        ElementNode follow;
        /** tag to add when building the AST */
        String tag = null;
        /** positive conjuncts */
        HashSet<Seq> and = new HashSet<Seq>();
        /** negative conjuncts */
        HashSet<Seq> not = new HashSet<Seq>();
        public boolean alwaysDrop = false;

        public boolean isTagless() {
            if (alwaysDrop) return true;
            for(int i=0; i<elements.length; i++)
                if ((elements[i] instanceof LiteralNode) && ((LiteralNode)elements[i]).caret)
                    return false;
            if (tag==null) return true;
            return false;
        }

        public boolean isDropped(Context cx) {
            if (alwaysDrop) return true;
            if (tag!=null) return false;
            for(int i=0; i<elements.length; i++)
                if (!elements[i].isDropped(cx) || ((elements[i] instanceof LiteralNode) && ((LiteralNode)elements[i]).caret))
                    return false;
            return true;
        }
        public Seq(ElementNode e) { this(new ElementNode[] { e }); }
        public Seq(ElementNode[] elements) { this(elements, true); }
        public Seq(ElementNode[] el, boolean check) {
            this.elements = new ElementNode[el.length];
            System.arraycopy(el, 0, elements, 0, el.length);
            for(int i=0; i<elements.length; i++) {
                if (elements[i]==null)
                    throw new RuntimeException();
            }
            // FIXME: this whole mechanism is sketchy
            if (check)
                for(int i=0; i<elements.length; i++) {
                    if ((elements[i] instanceof ReferenceNode) && ((ReferenceNode)elements[i]).parenthesized) {
                        ReferenceNode rn = (ReferenceNode)elements[i];
                        ElementNode replace = null;
                        for(int j=0; j<elements.length; j++) {
                            if (!(elements[j] instanceof ReferenceNode)) continue;
                            ReferenceNode rn2 = (ReferenceNode)elements[j];
                            if (rn2.nonTerminal.equals(rn.nonTerminal) && !rn2.parenthesized) {
                                if (replace == null) {
                                    replace = new UnionNode(new Seq(rn2).andnot(new Seq(elements, false)));
                                }
                                elements[j] = replace;
                            }
                        }
                    }
                }
        }
        public Atom toAtom(BuildContext cx) {
            if (elements.length != 1)
                throw new Error("you attempted to use ->, **, ++, or a similar character-class"+
                                " operator on a [potentially] multicharacter production");
            return elements[0].toAtom(cx);
        }
        public Seq tag(String tag) { this.tag = tag; return this; }
        public Seq follow(ElementNode follow) { this.follow = follow; return this; }
        public Seq and(Seq s) { and.add(s); s.alwaysDrop = true; return this; }
        public Seq andnot(Seq s) { not.add(s); s.alwaysDrop = true; return this; }
        public Seq separate(ElementNode sep) {
            ElementNode[] elements = new ElementNode[this.elements.length * 2 - 1];
            for(int i=0; i<this.elements.length; i++) {
                elements[i*2] = this.elements[i];
                if (i<this.elements.length-1)
                    elements[i*2+1] = new DropNode(sep);
            }
            this.elements = elements;
            return this;
        }
        public Sequence build(BuildContext cx, Union u, NonTerminalNode cnt, boolean dropall) {
            Sequence ret = build0(cx, cnt, dropall);
            for(Seq s : and) ret = ret.and(s.build(cx, null, cnt, true));
            for(Seq s : not) ret = ret.andnot(s.build(cx, null, cnt, true));
            if (u!=null) u.add(ret);
            return ret;
        }
        public Sequence build0(BuildContext cx, NonTerminalNode cnt, boolean dropall) {
            boolean[] drops = new boolean[elements.length];
            Element[] els = new Element[elements.length];
            dropall |= isDropped(cx);
            for(int i=0; i<elements.length; i++) {
                if (dropall) drops[i] = true;
                else         drops[i] = elements[i].isDropped(cx);
                if (elements[i] instanceof LiteralNode && ((LiteralNode)elements[i]).caret) {
                    if (tag != null) throw new RuntimeException("cannot have multiple tags in a sequence: " + this);
                    tag = ((LiteralNode)elements[i]).getLiteralTag();
                }
            }
            Sequence ret = null;
            int idx = -1;
            boolean multiNonDrop = false;
            for(int i=0; i<drops.length; i++)
                if (!drops[i])
                    if (idx==-1) idx = i;
                    else multiNonDrop = true;
            for(int i=0; i<elements.length; i++) {
                if (!multiNonDrop && i==idx && tag!=null && elements[i] instanceof RepeatNode) {
                    els[i] = ((RepeatNode)elements[i]).build(cx, cnt, dropall, tag);
                    tag = null;
                } else
                    els[i] = elements[i].build(cx, cnt, dropall);
            }
            if (tag==null && multiNonDrop)
                throw new RuntimeException("multiple non-dropped elements in sequence: " + Sequence.create("", els));
            boolean[] lifts = new boolean[elements.length];
            for(int i=0; i<elements.length; i++)
                lifts[i] = elements[i].isLifted();
            if (!multiNonDrop) {
                if (idx == -1) 
                    ret = tag==null
                        ? Sequence.create(illegalTag, els)
                        : Sequence.create(tag, els, drops, lifts);
                else if (tag==null) ret = Sequence.create(els, idx);
                else ret = Sequence.create(tag, els, drops, lifts);
            }
            if (multiNonDrop)
                ret = Sequence.create(tag, els, drops, lifts);
            if (this.follow != null)
                ret = ret.followedBy(this.follow.toAtom(cx));
            return ret;
        }
    }

    /** a node in the AST which is resolved into an Element */
    private abstract class ElementNode {
        /** the field name to be used when synthesizing AST classes; null if none suggested */
        public String getFieldName() { return null; }
        public boolean isLifted() { return false; }
        public boolean isDropped(Context cx) { return false; }
        //public abstract boolean isTagless();
        public boolean isTagless() { return false; }
        public void _emitCode(EmitContext cx,
                              StringBuffer fieldDeclarations,
                              StringBuffer walkCode) {
            throw new RuntimeException("not implemented " + this.getClass().getName());
        }
        public final void emitCode(EmitContext cx,
                                   StringBuffer fieldDeclarations,
                                   StringBuffer walkCode) {
            if (isDropped(cx)) return;
            if (isTagless()) {
                // parse just the literal text, create an int/float/char/string
                // FIXME: how do we know which one?
                walkCode.append("      stringify");
            } else {
            }
            _emitCode(cx, fieldDeclarations, walkCode);
            walkCode.append("      i++;");
        }
        public Atom toAtom(BuildContext cx) { throw new Error("can't convert a " + this.getClass().getName() + " to an atom: " + this); }
        public abstract Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall);
    }

    /** a union, produced by a ( .. | .. | .. ) construct */
    private class UnionNode extends ElementNode {

        /** each component of a union is a sequence */
        public Seq[][] sequences;

        /** if the union is a NonTerminal specified as Foo*=..., this is true */
        public boolean rep;

        /** if the union is a NonTerminal specified as Foo* /ws=..., then this is "ws" */
        public String  sep = null;

        public UnionNode(Seq seq) { this(new Seq[][] { new Seq[] { seq } }); }
        public UnionNode(Seq[][] sequences) { this(sequences, false, null); }
        public UnionNode(Seq[][] sequences, boolean rep, String sep) {
            this.sequences = sequences;
            this.rep = rep;
            this.sep = sep;
        }

        public boolean isTagless() {
            for (Seq[] ss : sequences)
                for (Seq s : ss)
                    if (!s.isTagless()) return false;
            return true;
        }

        public String[] getPossibleEmitClasses() {
            HashSet<String> cl = new HashSet<String> ();
            for(Seq[] ss : sequences)
                for(Seq s : ss) {
                    /*
                    String cls = s.getEmitClass();
                    if (cls != null) cl.add(cls);
                    */
                }
            return (String[])cl.toArray(new String[0]);
        }

        public void _emitCode(EmitContext cx,
                              StringBuffer fieldDeclarations,
                              StringBuffer walkCode) {
            throw new RuntimeException("not implemented " + this.getClass().getName());
        }

        public String getFieldName() { return null; }
        public boolean isLifted() { return false; }
        public boolean isDropped(Context cx) {
            for(Seq[] seqs : sequences)
                for(Seq seq : seqs)
                    if (!seq.isDropped(cx))
                        return false;
            return true;
        }
        public Atom toAtom(BuildContext cx) {
            Atom ret = null;
            for(Seq[] ss : sequences)
                for(Seq s : ss)
                    ret = ret==null ? s.toAtom(cx) : (Atom)ret.union(s.toAtom(cx));
            return ret;
        }

        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) {
            return buildIntoPreallocatedUnion(cx, cnt, dropall, new Union(null, false)); }
        public Element buildIntoPreallocatedUnion(BuildContext cx, NonTerminalNode cnt, boolean dropall, Union u) {
            Union urep = null;
            if (rep) {
                urep = new Union(null, false);
                urep.add(Sequence.create(cnt.name, new Element[0]));
                urep.add(sep==null
                         ? Sequence.create(new Element[] { u }, 0)
                         : Sequence.create(new Element[] { cx.get(sep), u }, 1));
            }
            HashSet<Sequence> bad2 = new HashSet<Sequence>();
            for(int i=0; i<sequences.length; i++) {
                Seq[] group = sequences[i];
                Union u2 = new Union(null, false);
                if (sequences.length==1) u2 = u;
                for(int j=0; j<group.length; j++)
                    if (!rep)
                        group[j].build(cx, u2, cnt, dropall);
                    else {
                        Union u3 = new Union(null, false);
                        group[j].build(cx, u3, cnt, dropall);
                        Sequence s = Sequence.create(cnt.name,
                                                     new Element[] { u3, urep },
                                                     new boolean[] { false, false },
                                                     new boolean[] { false, true});
                        u2.add(s);
                    }
                if (sequences.length==1) break;
                Sequence seq = Sequence.create(u2);
                for(Sequence s : bad2) seq = seq.andnot(s);
                u.add(seq);
                bad2.add(Sequence.create(u2));
            }
            return u;
        }
    }

    /** reference to a NonTerminal by name */
    private class ReferenceNode extends ElementNode {
        public String nonTerminal;
        public boolean parenthesized;
        public ReferenceNode() { }
        public ReferenceNode(String nonTerminal) { this(nonTerminal, false); }
        public ReferenceNode(String nonTerminal, boolean parenthesized) {
            this.nonTerminal = nonTerminal.indexOf('.')==-1 ? (prefix + nonTerminal) : nonTerminal;
            this.parenthesized = parenthesized;
        }
        public NonTerminalNode resolve(Context cx) {
            NonTerminalNode ret = cx.grammar.get(nonTerminal);
            if (ret==null) throw new RuntimeException("undefined nonterminal: " + nonTerminal);
            return ret;
        }
        public Atom toAtom(BuildContext cx) {
            ElementNode ret = cx.grammar.get(nonTerminal).getElementNode();
            if (ret == null) throw new RuntimeException("unknown nonterminal \""+nonTerminal+"\"");
            return ret.toAtom(cx);
        }
        public boolean isDropped(Context cx) { return resolve(cx).isDropped(cx); }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) {
            Element ret = cx.get(nonTerminal);
            if (ret == null) throw new RuntimeException("unknown nonterminal \""+nonTerminal+"\"");
            return ret;
        }
        public String getFieldName() { return StringUtil.uncapitalize(nonTerminal); }
    }

    /** a literal string */
    private class LiteralNode extends ElementNode {
        private String string;
        private final String thePrefix = prefix;
        private boolean caret;
        public LiteralNode(String string) { this(string, false); }
        public LiteralNode(String string, boolean caret) {
            this.string = string;
            this.caret = caret;
        }
        public String getLiteralTag() { return caret ? thePrefix+string : null; }
        public String toString() { return "\""+string+"\""; }
        public boolean isDropped(Context cx) { return true; }
        public Atom toAtom(BuildContext cx) {
            if (string.length()!=1) return super.toAtom(cx);
            Range.Set set = new Range.Set();
            set.add(string.charAt(0), string.charAt(0));
            return CharAtom.set(set);
        }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) { return CharAtom.string(string); }
    }

    /** an atom (usually a character class) */
    private class AtomNode extends ElementNode {
        char[][] ranges;
        public AtomNode() { this(new char[0][]); }
        public AtomNode(char[][] ranges) { this.ranges = ranges; }
        public AtomNode(char[] range) { this.ranges = new char[][] { range }; }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) { return toAtom(cx); }
        public Atom toAtom(BuildContext cx) {
            Range.Set set = new Range.Set();
            for(char[] r : ranges) set.add(r[0], r[1]);
            return CharAtom.set(set);
        }
    }

    /** a repetition */
    private class RepeatNode extends ElementNode {
        public ElementNode e, sep;
        public final boolean zero, many, max;
        public RepeatNode(ElementNode e, ElementNode sep, boolean zero, boolean many, boolean max) {
            this.e = e; this.sep = sep; this.zero = zero; this.many = many; this.max = max;
        }
        public Atom toAtom(BuildContext cx) { return sep==null ? e.toAtom(cx) : super.toAtom(cx); }
        public boolean isDropped(Context cx) { return e.isDropped(cx); }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) {
            Element ret = build(cx, cnt, dropall, illegalTag);
            String must = "must be tagged unless they appear within a dropped expression or their contents are dropped: ";
            if (!dropall && !isDropped(cx) && !e.isDropped(cx))
                if (!many)      throw new RuntimeException("options (?) " + must + ret);
                else if (zero)  throw new RuntimeException("zero-or-more repetitions (*) " + must + ret);
                else            throw new RuntimeException("one-or-more repetitions (+) " + must + ret);
            return ret;
        }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall, Object repeatTag) {
            return (!max)
                ? Repeat.repeat(e.build(cx, null, dropall), zero, many, sep==null ? null : sep.build(cx, null, dropall), repeatTag)
                : sep==null
                ? Repeat.repeatMaximal(e.toAtom(cx), zero, many, repeatTag)
                : Repeat.repeatMaximal(e.build(cx, null, dropall), zero, many, sep.toAtom(cx), repeatTag);
        }
    }

    /** helper class for syntactic constructs that wrap another construct */
    private abstract class ElementNodeWrapper extends ElementNode {
        protected ElementNode _e;
        public ElementNodeWrapper(ElementNode e) { this._e = e; }
        public boolean isDropped(Context cx) { return _e.isDropped(cx); }
        public Atom toAtom(BuildContext cx) { return _e.toAtom(cx); }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) { return _e.build(cx, cnt, dropall); }
        public String getFieldName() { return _e.getFieldName(); }
        public void _emitCode(EmitContext cx, StringBuffer fieldDeclarations, StringBuffer walkCode) {
            _e._emitCode(cx, fieldDeclarations, walkCode);
        }
    }

    /** a backtick node indicating that, when building the AST, the node's children should be inserted here */
    private class BacktickNode extends ElementNodeWrapper {
        public BacktickNode(ElementNode e) { super(e); }
        public boolean isLifted() { return true; }
        public String getFieldName() { throw new Error("FIXME: backtick isn't a single field"); }
        public void _emitCode(EmitContext cx, StringBuffer fieldDeclarations, StringBuffer walkCode) {
            _e._emitCode(cx, fieldDeclarations, walkCode);
        }
    }

    /** negation */
    private class TildeNode extends ElementNodeWrapper {
        public TildeNode(ElementNode e) { super(e); }
        public Atom toAtom(BuildContext cx) { return (Atom)((Topology<Character>)_e.toAtom(cx).complement()); }
        public Element build(BuildContext cx, NonTerminalNode cnt, boolean dropall) { return toAtom(cx); }
    }

    private class DropNode extends ElementNodeWrapper {
        public DropNode(ElementNode e) { super(e); }
        public boolean isDropped(Context cx) { return true; }
    }

    /** provides a label on the fields of a Seq */
    private class LabelNode extends ElementNodeWrapper {
        public final String label;
        public LabelNode(String label, ElementNode e) { super(e); this.label = label; }
        public String getFieldName() { return label; }
    }

    //////////////////////////////////////////////////////////////////////////////

    public class Context {
        public HashMap<String,Union> map = new HashMap<String,Union>();
        public GrammarNode grammar;
        public Context() {  }
        public Context(GrammarNode g) { this.grammar = g; }
    }


    public class EmitContext extends Context {
        public EmitContext(GrammarNode g) { super(g); }
    }

    public class BuildContext extends Context {
        public BuildContext(Tree t) { }
        public BuildContext(GrammarNode g) { super(g); }
        public Union build() {
            Union ret = null;
            for(NonTerminalNode nt : grammar.values()) {
                Union u = get(nt.name);
                if ("s".equals(nt.name))
                    ret = u;
            }
            return ret;
        }
        public Union peek(String name) { return map.get(name); }
        public void  put(String name, Union u) { map.put(name, u); }
        public Union get(String name) {
            Union ret = map.get(name);
            if (ret != null) return ret;
            NonTerminalNode nt = grammar.get(name);
            if (nt==null) {
                throw new Error("warning could not find " + name);
            } else {
                ret = new Union(name, false);
                map.put(name, ret);
                nt.getUnionNode().buildIntoPreallocatedUnion(this, nt, nt.isDropped(this), ret);
            }
            return ret;
        }

    }

}
