// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.misc;
import edu.berkeley.sbp.util.*;
import edu.berkeley.sbp.*;
import edu.berkeley.sbp.chr.*;
import java.util.*;
import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
import java.net.*;
import java.util.*;

public class ParserServlet /*extends HttpServlet*/ {
    /*
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        doPost(req, resp);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
        try {
            System.out.println("a");
           
            Tree<String> gram =
                new CharParser(MetaGrammar.make()).parse(new StringReader(req.getParameter("grammar").trim())).expand1();
            System.out.println("b");
            Union meta = MetaGrammar.make();
            Forest<String> out = new CharParser(meta).parse(new StringReader(req.getParameter("input").trim()));
            System.out.println(out);
            
            final GraphViz gv = new GraphViz();
            out.toGraphViz(gv);

            resp.setContentType("image/svg+xml");
            final Process proc = Runtime.getRuntime().exec(new String[] { "dot", "-Tsvg" });
            new Thread() {
                public void run() {
                    try {
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()));
                        gv.dump(pw);
                        pw.flush();
                        pw.close();
                    } catch (Exception e) {
                        e.printStackTrace(); 
                   }
                }
            }.start();

            byte[] buf = new byte[1024];
            InputStream is = proc.getInputStream();
            OutputStream os = resp.getOutputStream();

            while(true) {
                int numread = is.read(buf, 0, buf.length);
                if (numread==-1) break;
                os.write(buf, 0, numread);
            }
            os.flush();
            os.close();

        } catch (ParseFailed e) {
            e.printStackTrace();
            resp.setContentType("text/plain");
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(resp.getOutputStream()));
            pw.println(e);
            pw.flush();
            pw.close();
        }
        
        } catch (Exception e) {
            throw new ServletException(e);
        }
       
  }
    */
}
