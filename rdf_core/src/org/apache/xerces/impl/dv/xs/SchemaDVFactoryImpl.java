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

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
//import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.util.SymbolHash;

/**
 * the factory to create/return built-in schema DVs and create user-defined DVs
 * @xerces.internal  
 * @author  Neeraj Bajaj, Sun Microsystems, inc.
 * @author  Sandy Gao, IBM
 * @version  $Id: SchemaDVFactoryImpl.java,v 1.20 2005/06/13 04:55:30 mrglavas Exp $
 */
public class SchemaDVFactoryImpl extends SchemaDVFactory {

    static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
	 * @uml.property  name="fBuiltInTypes"
	 * @uml.associationEnd  
	 */
    static SymbolHash fBuiltInTypes = new SymbolHash();
    static {
        createBuiltInTypes();
    }

    //protected XSDeclarationPool fDeclPool = null;

    /**
     * Get a built-in simple type of the given name
     * REVISIT: its still not decided within the Schema WG how to define the
     *          ur-types and if all simple types should be derived from a
     *          complex type, so as of now we ignore the fact that anySimpleType
     *          is derived from anyType, and pass 'null' as the base of
     *          anySimpleType. It needs to be changed as per the decision taken.
     *
     * @param name  the name of the datatype
     * @return      the datatype validator of the given name
     */
    public XSSimpleType getBuiltInType(String name) {
        return (XSSimpleType)fBuiltInTypes.get(name);
    }

    /**
     * get all built-in simple types, which are stored in a hashtable keyed by
     * the name
     *
     * @return      a hashtable which contains all built-in simple types
     */
    public SymbolHash getBuiltInTypes() {
        return (SymbolHash)fBuiltInTypes.makeClone();
    }

    /**
     * Create a new simple type which is derived by restriction from another
     * simple type.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param base              base type of the new type
     * @param annotations       set of annotations
     * @return                  the newly created simple type
//     */
//    public XSSimpleType createTypeRestriction(String name, String targetNamespace,
//                                              short finalSet, XSSimpleType base, XSObjectList annotations) {
//        
//        /*if (fDeclPool != null) {
//           XSSimpleTypeDecl st= fDeclPool.getSimpleTypeDecl();
//           return st.setRestrictionValues((XSSimpleTypeDecl)base, name, targetNamespace, finalSet, annotations);
//        }*/
//        return new XSSimpleTypeDecl((XSSimpleTypeDecl)base, name, targetNamespace, finalSet, false, annotations);
//    }

    /**
     * Create a new simple type which is derived by list from another simple
     * type.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param itemType          item type of the list type
     * @param annotations       set of annotations
     * @return                  the newly created simple type
     */
//    public XSSimpleType createTypeList(String name, String targetNamespace,
//                                       short finalSet, XSSimpleType itemType,
//                                       XSObjectList annotations) {
//       /* if (fDeclPool != null) {
//           XSSimpleTypeDecl st= fDeclPool.getSimpleTypeDecl();
//           return st.setListValues(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType, annotations);
//        }*/
//        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, (XSSimpleTypeDecl)itemType, false, annotations);
//    }

    /**
     * Create a new simple type which is derived by union from a list of other
     * simple types.
     *
     * @param name              name of the new type, could be null
     * @param targetNamespace   target namespace of the new type, could be null
     * @param finalSet          value of "final"
     * @param memberTypes       member types of the union type
     * @param annotations       set of annotations
     * @return                  the newly created simple type
     */
//    public XSSimpleType createTypeUnion(String name, String targetNamespace,
//                                        short finalSet, XSSimpleType[] memberTypes,
//                                        XSObjectList annotations) {
//        int typeNum = memberTypes.length;
//        XSSimpleTypeDecl[] mtypes = new XSSimpleTypeDecl[typeNum];
//        System.arraycopy(memberTypes, 0, mtypes, 0, typeNum);
//
//       /* if (fDeclPool != null) {
//           XSSimpleTypeDecl st= fDeclPool.getSimpleTypeDecl();
//           return st.setUnionValues(name, targetNamespace, finalSet, mtypes, annotations);
//        }*/
//        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, mtypes, annotations);
//    }

    // create all built-in types
    static void createBuiltInTypes() {
        // all schema simple type names
        final String ANYSIMPLETYPE     = "anySimpleType";
//        final String ANYATOMICTYPE	   = "anyAtomicType";
        final String ANYURI            = "anyURI";
        final String BASE64BINARY      = "base64Binary";
        final String BOOLEAN           = "boolean";
        final String BYTE              = "byte";
        final String DATE              = "date";
        final String DATETIME          = "dateTime";
        final String DAY               = "gDay";
        final String DECIMAL           = "decimal";
        final String DOUBLE            = "double";
        final String DURATION          = "duration";
        final String ENTITY            = "ENTITY";
//        final String ENTITIES          = "ENTITIES";
        final String FLOAT             = "float";
        final String HEXBINARY         = "hexBinary";
        final String ID                = "ID";
        final String IDREF             = "IDREF";
//        final String IDREFS            = "IDREFS";
        final String INT               = "int";
        final String INTEGER           = "integer";
        final String LONG              = "long";
        final String NAME              = "Name";
        final String NEGATIVEINTEGER   = "negativeInteger";
        final String MONTH             = "gMonth";
        final String MONTHDAY          = "gMonthDay";
        final String NCNAME            = "NCName";
        final String NMTOKEN           = "NMTOKEN";
//        final String NMTOKENS          = "NMTOKENS";
        final String LANGUAGE          = "language";
        final String NONNEGATIVEINTEGER= "nonNegativeInteger";
        final String NONPOSITIVEINTEGER= "nonPositiveInteger";
        final String NORMALIZEDSTRING  = "normalizedString";
        final String NOTATION          = "NOTATION";
        final String POSITIVEINTEGER   = "positiveInteger";
        final String QNAME             = "QName";
        final String SHORT             = "short";
        final String STRING            = "string";
        final String TIME              = "time";
        final String TOKEN             = "token";
        final String UNSIGNEDBYTE      = "unsignedByte";
        final String UNSIGNEDINT       = "unsignedInt";
        final String UNSIGNEDLONG      = "unsignedLong";
        final String UNSIGNEDSHORT     = "unsignedShort";
        final String YEAR              = "gYear";
        final String YEARMONTH         = "gYearMonth";
        final String YEARMONTHDURATION = "yearMonthDuration";
        final String DAYTIMEDURATION   = "dayTimeDuration";
//        final String PRECISIONDECIMAL  = "precisionDecimal";

        final XSFacets facets = new XSFacets();

        //REVISIT: passing "anyType" here.
        XSSimpleTypeDecl anySimpleType = XSSimpleTypeDecl.fAnySimpleType;
//        XSSimpleTypeDecl anyAtomicType = XSSimpleTypeDecl.fAnyAtomicType;
//        XSSimpleTypeDecl baseAtomicType = null;
        
//        if (Constants.SCHEMA_1_1_SUPPORT) {
//            baseAtomicType = anyAtomicType;
//            fBuiltInTypes.put(ANYATOMICTYPE, anyAtomicType);
//        }
//        else {
//            baseAtomicType = anySimpleType;
//        }
        
        fBuiltInTypes.put(ANYSIMPLETYPE, anySimpleType);
        XSSimpleTypeDecl stringDV = new XSSimpleTypeDecl(anySimpleType, STRING, XSSimpleTypeDecl.DV_STRING);
        fBuiltInTypes.put(STRING, stringDV);
        fBuiltInTypes.put(BOOLEAN, new XSSimpleTypeDecl(anySimpleType, BOOLEAN, XSSimpleTypeDecl.DV_BOOLEAN));
        XSSimpleTypeDecl decimalDV = new XSSimpleTypeDecl(anySimpleType, DECIMAL, XSSimpleTypeDecl.DV_DECIMAL);
        fBuiltInTypes.put(DECIMAL, decimalDV);

        fBuiltInTypes.put(ANYURI, new XSSimpleTypeDecl(anySimpleType, ANYURI, XSSimpleTypeDecl.DV_ANYURI));
        fBuiltInTypes.put(BASE64BINARY, new XSSimpleTypeDecl(anySimpleType, BASE64BINARY, XSSimpleTypeDecl.DV_BASE64BINARY));
        
        XSSimpleTypeDecl durationDV = new XSSimpleTypeDecl(anySimpleType, DURATION, XSSimpleTypeDecl.DV_DURATION);
        fBuiltInTypes.put(DURATION, durationDV);
        
        if (Constants.SCHEMA_1_1_SUPPORT) {
            fBuiltInTypes.put(YEARMONTHDURATION, new XSSimpleTypeDecl(durationDV, YEARMONTHDURATION, XSSimpleTypeDecl.DV_YEARMONTHDURATION));
            fBuiltInTypes.put(DAYTIMEDURATION, new XSSimpleTypeDecl(durationDV, DAYTIMEDURATION, XSSimpleTypeDecl.DV_DAYTIMEDURATION));
            
        }
        
        fBuiltInTypes.put(DATETIME, new XSSimpleTypeDecl(anySimpleType, DATETIME, XSSimpleTypeDecl.DV_DATETIME));
        fBuiltInTypes.put(TIME, new XSSimpleTypeDecl(anySimpleType, TIME, XSSimpleTypeDecl.DV_TIME));
        fBuiltInTypes.put(DATE, new XSSimpleTypeDecl(anySimpleType, DATE, XSSimpleTypeDecl.DV_DATE));
        fBuiltInTypes.put(YEARMONTH, new XSSimpleTypeDecl(anySimpleType, YEARMONTH, XSSimpleTypeDecl.DV_GYEARMONTH));
        fBuiltInTypes.put(YEAR, new XSSimpleTypeDecl(anySimpleType, YEAR, XSSimpleTypeDecl.DV_GYEAR));
        fBuiltInTypes.put(MONTHDAY, new XSSimpleTypeDecl(anySimpleType, MONTHDAY, XSSimpleTypeDecl.DV_GMONTHDAY));
        fBuiltInTypes.put(DAY, new XSSimpleTypeDecl(anySimpleType, DAY, XSSimpleTypeDecl.DV_GDAY));
        fBuiltInTypes.put(MONTH, new XSSimpleTypeDecl(anySimpleType, MONTH, XSSimpleTypeDecl.DV_GMONTH));

        XSSimpleTypeDecl integerDV = new XSSimpleTypeDecl(decimalDV, INTEGER, XSSimpleTypeDecl.DV_INTEGER);
        fBuiltInTypes.put(INTEGER, integerDV);

        facets.maxInclusive = "0";
        XSSimpleTypeDecl nonPositiveDV = new XSSimpleTypeDecl(integerDV, NONPOSITIVEINTEGER);
        nonPositiveDV.applyFacets1(facets , XSSimpleType.FACET_MAXINCLUSIVE, (short)0);
        fBuiltInTypes.put(NONPOSITIVEINTEGER, nonPositiveDV);

        facets.maxInclusive = "-1";
        XSSimpleTypeDecl negativeDV = new XSSimpleTypeDecl(integerDV, NEGATIVEINTEGER);
        negativeDV.applyFacets1(facets , XSSimpleType.FACET_MAXINCLUSIVE, (short)0);
        fBuiltInTypes.put(NEGATIVEINTEGER, negativeDV);

        facets.maxInclusive = "9223372036854775807";
        facets.minInclusive = "-9223372036854775808";
        XSSimpleTypeDecl longDV = new XSSimpleTypeDecl(integerDV, LONG);
        longDV.applyFacets1(facets , (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(LONG, longDV);


        facets.maxInclusive = "2147483647";
        facets.minInclusive =  "-2147483648";
        XSSimpleTypeDecl intDV = new XSSimpleTypeDecl(longDV, INT);
        intDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(INT, intDV);

        facets.maxInclusive = "32767";
        facets.minInclusive = "-32768";
        XSSimpleTypeDecl shortDV = new XSSimpleTypeDecl(intDV, SHORT);
        shortDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(SHORT, shortDV);

        facets.maxInclusive = "127";
        facets.minInclusive = "-128";
        XSSimpleTypeDecl byteDV = new XSSimpleTypeDecl(shortDV, BYTE );
        byteDV.applyFacets1(facets, (short)(XSSimpleType.FACET_MAXINCLUSIVE | XSSimpleType.FACET_MININCLUSIVE), (short)0 );
        fBuiltInTypes.put(BYTE, byteDV);

        facets.minInclusive =  "0" ;
        XSSimpleTypeDecl nonNegativeDV = new XSSimpleTypeDecl(integerDV, NONNEGATIVEINTEGER );
        nonNegativeDV.applyFacets1(facets, XSSimpleType.FACET_MININCLUSIVE, (short)0 );
        fBuiltInTypes.put(NONNEGATIVEINTEGER, nonNegativeDV);

        facets.maxInclusive = "18446744073709551615" ;
        XSSimpleTypeDecl unsignedLongDV = new XSSimpleTypeDecl(nonNegativeDV, UNSIGNEDLONG );
        unsignedLongDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDLONG, unsignedLongDV);

        facets.maxInclusive = "4294967295" ;
        XSSimpleTypeDecl unsignedIntDV = new XSSimpleTypeDecl(unsignedLongDV, UNSIGNEDINT );
        unsignedIntDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDINT, unsignedIntDV);

        facets.maxInclusive = "65535" ;
        XSSimpleTypeDecl unsignedShortDV = new XSSimpleTypeDecl(unsignedIntDV, UNSIGNEDSHORT );
        unsignedShortDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDSHORT, unsignedShortDV);

        facets.maxInclusive = "255" ;
        XSSimpleTypeDecl unsignedByteDV = new XSSimpleTypeDecl(unsignedShortDV, UNSIGNEDBYTE);
        unsignedByteDV.applyFacets1(facets, XSSimpleType.FACET_MAXINCLUSIVE, (short)0 );
        fBuiltInTypes.put(UNSIGNEDBYTE, unsignedByteDV);

        facets.minInclusive = "1" ;
        XSSimpleTypeDecl positiveIntegerDV = new XSSimpleTypeDecl(nonNegativeDV, POSITIVEINTEGER );
        positiveIntegerDV.applyFacets1(facets, XSSimpleType.FACET_MININCLUSIVE, (short)0 );
        fBuiltInTypes.put(POSITIVEINTEGER, positiveIntegerDV);


        fBuiltInTypes.put(FLOAT, new XSSimpleTypeDecl(anySimpleType, FLOAT, XSSimpleTypeDecl.DV_FLOAT));
        fBuiltInTypes.put(DOUBLE, new XSSimpleTypeDecl(anySimpleType, DOUBLE, XSSimpleTypeDecl.DV_DOUBLE));
        fBuiltInTypes.put(HEXBINARY, new XSSimpleTypeDecl(anySimpleType, HEXBINARY, XSSimpleTypeDecl.DV_HEXBINARY));
        fBuiltInTypes.put(NOTATION, new XSSimpleTypeDecl(anySimpleType, NOTATION, XSSimpleTypeDecl.DV_NOTATION));


        facets.whiteSpace =  XSSimpleType.WS_REPLACE;
        XSSimpleTypeDecl normalizedDV = new XSSimpleTypeDecl(stringDV, NORMALIZEDSTRING );
        normalizedDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0 );
        fBuiltInTypes.put(NORMALIZEDSTRING, normalizedDV);

        facets.whiteSpace = XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl tokenDV = new XSSimpleTypeDecl(normalizedDV, TOKEN );
        tokenDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0 );
        fBuiltInTypes.put(TOKEN, tokenDV);

        facets.whiteSpace = XSSimpleType.WS_COLLAPSE;
        facets.pattern  = "([a-zA-Z]{1,8})(-[a-zA-Z0-9]{1,8})*";
        XSSimpleTypeDecl languageDV = new XSSimpleTypeDecl(tokenDV, LANGUAGE);
        languageDV.applyFacets1(facets, (short)(XSSimpleType.FACET_WHITESPACE | XSSimpleType.FACET_PATTERN) ,(short)0);
        fBuiltInTypes.put(LANGUAGE, languageDV);


        facets.whiteSpace =  XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl nameDV = new XSSimpleTypeDecl(tokenDV, NAME );
        nameDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0);
        fBuiltInTypes.put(NAME, nameDV);

        facets.whiteSpace = XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl ncnameDV = new XSSimpleTypeDecl(nameDV, NCNAME) ;
        ncnameDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0);
        fBuiltInTypes.put(NCNAME, ncnameDV);

        fBuiltInTypes.put(QNAME, new XSSimpleTypeDecl(anySimpleType, QNAME, XSSimpleTypeDecl.DV_QNAME));
//
        fBuiltInTypes.put(ID, new XSSimpleTypeDecl(ncnameDV,  ID, XSSimpleTypeDecl.DV_ID));
        XSSimpleTypeDecl idrefDV = new XSSimpleTypeDecl(ncnameDV,  IDREF , XSSimpleTypeDecl.DV_IDREF);
        fBuiltInTypes.put(IDREF, idrefDV);

//        facets.minLength = 1;
//        XSSimpleTypeDecl tempDV = new XSSimpleTypeDecl(null, URI_SCHEMAFORSCHEMA, (short)0, idrefDV, true, null);
//        XSSimpleTypeDecl idrefsDV = new XSSimpleTypeDecl(tempDV, IDREFS, URI_SCHEMAFORSCHEMA, (short)0, false, null);
//        idrefsDV.applyFacets1(facets, XSSimpleType.FACET_MINLENGTH, (short)0);
//        fBuiltInTypes.put(IDREFS, idrefsDV);

        XSSimpleTypeDecl entityDV = new XSSimpleTypeDecl(ncnameDV, ENTITY , XSSimpleTypeDecl.DV_ENTITY);
        fBuiltInTypes.put(ENTITY, entityDV);

//        facets.minLength = 1;
//        tempDV = new XSSimpleTypeDecl(null, URI_SCHEMAFORSCHEMA, (short)0, entityDV, true, null);
//        XSSimpleTypeDecl entitiesDV = new XSSimpleTypeDecl(tempDV, ENTITIES, URI_SCHEMAFORSCHEMA, (short)0, false, null);
//        entitiesDV.applyFacets1(facets, XSSimpleType.FACET_MINLENGTH, (short)0);
//        fBuiltInTypes.put(ENTITIES, entitiesDV);


        facets.whiteSpace  = XSSimpleType.WS_COLLAPSE;
        XSSimpleTypeDecl nmtokenDV = new XSSimpleTypeDecl(tokenDV, NMTOKEN);
        nmtokenDV.applyFacets1(facets, XSSimpleType.FACET_WHITESPACE, (short)0);
        fBuiltInTypes.put(NMTOKEN, nmtokenDV);

//        facets.minLength = 1;
//        tempDV = new XSSimpleTypeDecl(null, URI_SCHEMAFORSCHEMA, (short)0, nmtokenDV, true, null);
//        XSSimpleTypeDecl nmtokensDV = new XSSimpleTypeDecl(tempDV, NMTOKENS, URI_SCHEMAFORSCHEMA, (short)0, false, null);
//        nmtokensDV.applyFacets1(facets, XSSimpleType.FACET_MINLENGTH, (short)0);
//        fBuiltInTypes.put(NMTOKENS, nmtokensDV);
    }//createBuiltInTypes()

    /*public void setDeclPool (XSDeclarationPool declPool){
        fDeclPool = declPool;
    }*/

}//SchemaDVFactoryImpl
