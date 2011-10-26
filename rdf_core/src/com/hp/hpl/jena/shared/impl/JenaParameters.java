/******************************************************************
 * File:        JenaParameters.java
 * Created by:  Dave Reynolds
 * Created on:  23-Aug-2003
 * 
 * (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * [See end of file]
 * $Id: JenaParameters.java,v 1.13 2008/12/28 19:32:40 andy_seaborne Exp $
 *****************************************************************/
package com.hp.hpl.jena.shared.impl;

/**
 * This class holds global, static, configuration parameters that
 * affect the behaviour of the Jena API. These should not be changed
 * unless you are sure you know what you are doing and even then 
 * should ideally only be changed before any Models are created or processed.
 * <p>
 * These parameters should not be regarded as a stable part of the API. If
 * we find them being used significantly that probably means they should be
 * moved to being model-specific rather than global.
 * </p>
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.13 $ on $Date: 2008/12/28 19:32:40 $
 */
public class JenaParameters {
    
//  =======================================================================
//  Parameters affected handling of typed literals
        
    public static boolean enableEagerLiteralValidation = false;

    public static boolean enablePlainLiteralSameAsString = true;
    
    public static boolean enableSilentAcceptanceOfUnknownDatatypes = true;

    public static boolean enableWhitespaceCheckingOfTypedLiterals = false;
    
    public static boolean enableFilteringOfHiddenInfNodes = true;    
   
    public static boolean enableOWLRuleOverOWLRuleWarnings = true;
   
    public static boolean disableBNodeUIDGeneration = false;
}



/*
    (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/