/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * [See end of file]
 */

package com.hp.hpl.jena.iri.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.iri.IRI;
import com.hp.hpl.jena.iri.IRIComponents;
import com.hp.hpl.jena.iri.IRIFactory;
import com.hp.hpl.jena.iri.Violation;
import com.hp.hpl.jena.iri.ViolationCodes;
//import com.ibm.icu.text.IDNA;
//import com.ibm.icu.text.StringPrepParseException;

public class Parser implements IRIComponents, ViolationCodes {

    static Lexer lexers[][] = new Lexer[8][];
    static {
        for (int i = 0; i < lexers.length; i++)
            lexers[i] = new Lexer[] { 
                    new LexerScheme((Reader) null),
                    new LexerUserinfo((Reader) null),
                    new LexerHost((Reader) null), 
                    new LexerPort((Reader) null),
                    new LexerPath((Reader) null),
                    new LexerQuery((Reader) null),
                    new LexerFragment((Reader) null), 
                    new LexerXHost((Reader) null), 
                    };
    }

    static int nextLexer = 0;

    static boolean DEBUG = false;

    static final int fields[] = { SCHEME, USER, HOST, PORT, PATH, QUERY,
            FRAGMENT, AUTHORITY, PATHQUERY };

    static final int invFields[] = new int[32];
    static {
        for (int i = 0; i < invFields.length; i++)
            invFields[i] = -1;
        for (int i = 0; i < fields.length; i++)
            invFields[fields[i]] = i;
    }

    static final Pattern p = Pattern.compile("(([^:/?#]*):)?" + // scheme
            "(//((([^/?#@]*)@)?" + // user
            "(\\[[^/?#]*\\]|([^/?#:]*))?" + // host
            "(:([^/?#]*))?))?" + // port
            "([^#?]*)?" + // path
            "(\\?([^#]*))?" + // query
            "(#(.*))?", // frag
            Pattern.DOTALL);

    final Matcher m;

    final String uri;
    
    int found;

    final long errors[] = new long[fields.length];

    // public long allErrors = 0l;
    IRIImpl iri;
    
    
    public Parser(String uri, IRIImpl iri) {
        this.uri = uri;
        this.iri = iri;
        m = p.matcher(uri);
        if (!m.matches()) {
            throw new RuntimeException("not meant to happen");
        }
        Lexer[] lex = nextLexer();

        IRIFactoryImpl factory = iri.getFactory();
        if (!has(SCHEME)) {
//            recordError(SCHEME, RELATIVE_URI);
            iri.scheme = factory.noScheme();
        } else {
            iri.scheme = factory.getScheme(get(SCHEME).toLowerCase(),this);
        }
        for (int i = 0; i < lex.length-1; i++) {
            int range = fields[i];
            if (has(range)) {
                if ((range!=PATH && range != HOST)|| start(range)!=end(range) ) {
                    found |= 1<<range;   
                }
                lex[i].analyse(this, range);
                iri.scheme.analyse(this, range);
                if (range==PORT) {
                    try {
                      int port = Integer.parseInt(get(PORT));
                      if (iri.scheme.port()== port )
                          recordError(PORT,DEFAULT_PORT_SHOULD_BE_OMITTED);
                      if (port<1024 && port>=0)
                          recordError(PORT,PORT_SHOULD_NOT_BE_WELL_KNOWN);
                    }
                    catch (Exception e) {
                        // ignore
                    }
                }
            } 
        }
        if (has(AUTHORITY))
            found |= 1<<AUTHORITY;
        iri.scheme.analyse(this,PATHQUERY);
        if ((errors[invFields[HOST]] & ((1l << DOUBLE_DASH_IN_REG_NAME)
                | (1l << ACE_PREFIX) | (1l << NON_URI_CHARACTER))) != 0) {

            String h = get(HOST);
            try {
//                try {
//
//                    IDNA.convertIDNToUnicode(IDNA.convertIDNToASCII(h,
//                            IDNA.USE_STD3_RULES), IDNA.USE_STD3_RULES
//                            | IDNA.ALLOW_UNASSIGNED);
//
//                } catch (StringPrepParseException e) {
//                    try {
//                        IDNA.convertIDNToUnicode(IDNA.convertIDNToASCII(h,
//                                IDNA.USE_STD3_RULES | IDNA.ALLOW_UNASSIGNED),
//                                IDNA.USE_STD3_RULES | IDNA.ALLOW_UNASSIGNED);
//                        recordError(HOST, BAD_IDN_UNASSIGNED_CHARS, e);
//                    } catch (StringPrepParseException e1) {
//                        recordError(HOST, BAD_IDN, e);
//                    }
//
//                }
            	System.out.println("Parse. IRIpackage");
            } catch (IndexOutOfBoundsException e) {
                recordError(HOST, BAD_IDN, e);
            }
        }
        
        hasComponents(~found & iri.scheme.getRequired(),
                REQUIRED_COMPONENT_MISSING);
        hasComponents(found & iri.scheme.getProhibited(),
          PROHIBITED_COMPONENT_PRESENT);

    }

    static private Lexer[] nextLexer() {
        Lexer lex[] = lexers[nextLexer];
        nextLexer = (nextLexer + 1) % lexers.length;
        return lex;
    }
    
    static LexerHost hostLexer() {
        return (LexerHost)nextLexer()[2];
    }
    
    private void hasComponents(int errorComponents,int eCode) {
        if (errorComponents==0)
            return;
        int i=0;
        while (errorComponents!=0) {
            if ((errorComponents&1)==1)
                recordError(i,eCode);
            i++;
            errorComponents >>= 1;
        }
    }

    public boolean has(int f) {
        return m.start(f) != -1;
    }

    public int start(int f) {
        return m.start(f==PATHQUERY?PATH:f);
    }

    public int end(int f) {
        if (f!=PATHQUERY)return m.end(f);
        return has(QUERY)?end(QUERY):end(PATH);
    }

    public String get(int f) {
        if (f!=PATHQUERY)return m.group(f);
        return uri.substring(start(PATH),end(PATHQUERY));
    }

    static private void show(IRI iri) {

        System.out.println("Scheme: " + iri.getScheme());

        System.out.println("Authority: " + iri.getRawAuthority());

        System.out.println("User: " + iri.getRawUserinfo());

        System.out.println("Host: " + iri.getRawHost());
        System.out.println("Port: " + iri.getPort());

        System.out.println("Path: " + iri.getRawPath());

        System.out.println("Query: " + iri.getRawQuery());

        System.out.println("Fragment: " + iri.getRawFragment());
        Iterator<Violation> it = ((AbsIRIImpl) iri).allViolations();
        while (it.hasNext()) {
            System.out.println(it.next().getLongMessage());
        }
        it = ((AbsIRIImpl) iri).violations(true);
        while (it.hasNext()) {
            System.out.println("+"+it.next().getLongMessage());
        }
        it = ((AbsIRIImpl) iri).violations(false);
        while (it.hasNext()) {
            System.out.println("++"+it.next().getLongMessage());
        }
    }

    static public void main(String args[]) throws IOException {
        LineNumberReader in = new LineNumberReader(new InputStreamReader(
                System.in));
        IRIImpl last = null;
        DEBUG = true;

        IRIFactory factory = IRIFactory.iriImplementation();
        while (true) {
            String s = in.readLine().trim();
            if (s.equals("quit"))
                return;
            IRIImpl iri = (IRIImpl) factory.create(s);
            show(iri);

            if (last != null) {
                IRI r = last.create(iri);
                System.out.println("Resolved: " + r.toString());
                show(r);
            }
            last = iri;
        }
    }

    public void recordError(int range, int e) {
        errors[invFields[range]] |= (1l << e);
        iri.allErrors |= (1l << e);
    }
    public void recordError(int range, int e, Exception ex) {
        errors[invFields[range]] |= (1l << e);
        iri.allErrors |= (1l << e);
        iri.idnaException = ex;
    }
    
    long errors(int r) {
        return errors[invFields[r]];
    }

    public void matchedRule(int range, int rule) {
        if (DEBUG)
            System.err.println("Rule " + rule + " in range " + range);

    }

    public void matchedRule(int range, int rule, String string) {
        if (DEBUG)
            System.err.println("Rule " + rule + " in range " + range
                    + " yytext: '" + string + "'");

    }

}

/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP All rights
 * reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. The name of the author may not
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

