// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.chr;
import java.io.*;
import java.util.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.util.*;

public class CharParser extends Parser<Character,String> {

    public Forest<String> parse(InputStream is) throws IOException, ParseFailed { return super.parse(new CharInput(is)); }
    public Forest<String> parse(Reader r)       throws IOException, ParseFailed { return super.parse(new CharInput(r)); }
    public Forest<String> parse(String s)       throws IOException, ParseFailed { return parse(new StringReader(s)); }

    public CharParser(Union u) { super(u); }

    public Topology<Character> emptyTopology() { return new CharTopology(); }
    public Forest<String> shiftToken(Character ct, Input.Region region) {
        return Forest.create(region, ct.toString(), null); }

}
