// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/** <font color=blue>a tree (or node in a tree); see jargon.txt for details</font> */
public class Tree<NodeType>
    extends PrintableTree<Tree<NodeType>>
    implements Iterable<Tree<NodeType>> {

    private static final Tree[] emptyTree = new Tree[0];

    private final Input.Region     location;
    private final NodeType         ihead;
    private final Tree<NodeType>[] children;

    /** the number of children the tree has */
    public int               size() { return children.length; }

    /** the element at the head of the tree */
    public NodeType                 head()        { return ihead; }
    public NodeType              getHead()        { return ihead; }

    /** the tree's children */
    public Iterable<Tree<NodeType>> children()    { return this; }

    /** the tree's children */
    public Iterator<Tree<NodeType>> iterator()    { return new ArrayIterator(children); }

    /** get the <tt>i</t>th child */
    public Tree<NodeType> child(int i)            { return children[i]; }

    /** get the input region that this tree was parsed from */
    public Input.Region    getRegion() { return location; }

    public Tree(Input.Region loc, NodeType head) {
        location = loc;
        ihead = head;
        children = emptyTree;
    }
    public Tree(Input.Region loc, NodeType head, List<Tree<NodeType>> kids){
        location = loc;
        ihead = head;
        if (kids.size() == 0)
            children = emptyTree;
        else {
            children = new Tree[kids.size()];
            kids.toArray(children);
        }
    }
    public Tree(Input.Region loc, NodeType head, Tree<NodeType>[] kids) {
        location = loc;
        ihead = head;
        children = (kids == null ? emptyTree : kids.clone());
    }

    // FIXME: fairly inefficient because we keep copying arrays
    /** package-private constructor, allows setting the "lift" bit */
    Tree(Input.Region loc, NodeType head, Tree<NodeType>[] children, boolean[] lifts) {
        this.location = loc;
        this.ihead = head;

        int count = 0;
        for(int i=0; i<children.length; i++)
            count += lifts[i] ? children[i].size() : 1;

        this.children = new Tree[count];
        int j = 0;
        for(int i=0; i<children.length; i++) {
            if (!lifts[i])
                this.children[j++] = children[i];
            else
                for(int k=0; k<children[i].size(); k++)
                    this.children[j++] = children[i].child(k);
        }
    }


    // PrintableTree /////////////////////////////////////////////////////////////////////////////

    protected String headToString() { return head()==null?null:head().toString(); }
    protected String headToJava()   {
      // FIXME
        if (head()==null) return null;
        if (head() instanceof ToJava) {
            StringBuffer sb = new StringBuffer();
            try {
                ((ToJava)head()).toJava(sb);
            } catch (IOException e) { throw new RuntimeException(e); }
            return sb.toString();
        }
        return (head()==null?"null":("\""+StringUtil.toJavaString(head().toString())+"\""));
    }
    protected String left()   { return "{"; }
    protected String right()  { return "}"; }
    protected boolean ignoreSingleton() { return false; }


}
