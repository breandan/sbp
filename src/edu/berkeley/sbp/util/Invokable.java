// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.ref.*;

public interface Invokable<A,B,C> {
    public void invoke(A a, B b, C c);
}
