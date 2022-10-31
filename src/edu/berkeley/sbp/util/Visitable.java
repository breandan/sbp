// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import java.util.*;

public interface Visitable<V> {

    public <B,C> void visit(Invokable<V,B,C> ivbc, B b, C c);

}
