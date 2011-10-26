/*
 * Copyright 2001-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XMLChar;

/**
 * Represent the schema type "QName" and "NOTATION"
 *
 * @xerces.internal 
 *
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @author Sandy Gao, IBM
 *
 * @version $Id: QNameDV.java,v 1.9 2005/05/06 15:31:14 ankitp Exp $
 */
public class QNameDV extends TypeValidator {

    private static final String EMPTY_STRING = "".intern();

    public short getAllowedFacets() {
        return ( XSSimpleTypeDecl.FACET_MINLENGTH | XSSimpleTypeDecl.FACET_PATTERN | XSSimpleTypeDecl.FACET_WHITESPACE);
    }

    public Object getActualValue(String content, ValidationContext context)
        throws InvalidDatatypeValueException {

        // "prefix:localpart" or "localpart"
        // get prefix and local part out of content
        String prefix, localpart;
        int colonptr = content.indexOf(":");
        if (colonptr > 0) {
            prefix = context.getSymbol(content.substring(0,colonptr));
            localpart = content.substring(colonptr+1);
        } else {
            prefix = EMPTY_STRING;
            localpart = content;
        }

        // both prefix (if any) a nd localpart must be valid NCName
        if (prefix.length() > 0 && !XMLChar.isValidNCName(prefix))
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "QName"});

        if(!XMLChar.isValidNCName(localpart))
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "QName"});

        // resove prefix to a uri, report an error if failed
        String uri = context.getURI(prefix);
        if (prefix.length() > 0 && uri == null)
            throw new InvalidDatatypeValueException("UndeclaredPrefix", new Object[]{content, prefix});

        return content;

    }
}
    // REVISIT: qname and notation shouldn't support length facets.
    //          now we just return the length of the rawname
//    public int getDataLength(Object value) {
//        return ((XQName)value).rawname.length();
//    }

    