// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.repository.model.EAuthenticationMethod;
import org.talend.core.repository.model.EEncryptionMethod;
import org.talend.core.repository.model.ldap.AdvancedSocketFactory;

import com.ca.commons.jndi.SchemaOps;
import com.ca.commons.naming.DN;
import com.ca.commons.naming.DXAttribute;
import com.ca.commons.naming.DXAttributes;
import com.ca.commons.naming.DXEntry;
import com.ca.commons.naming.DXNamingEnumeration;

/**
 * This class is used for getting connection and retrieving data for LDAP. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPConnectionUtils {

    private static java.util.Hashtable<String, String> env = null;

    private static final String LDAP_URL_PREFIX = "ldap://"; //$NON-NLS-1$

    private static final String LDAPS_URL_PREFIX = "ldaps://"; //$NON-NLS-1$

    private static final String CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory"; //$NON-NLS-1$

    private static InitialLdapContext ctx = null;

    /**
     * qli Comment method "getAttributes".
     * 
     * @param schema
     * 
     * @return array
     */

    public static Object[] getAttributes(SchemaOps schema, LDAPSchemaConnection talendLDAPConnection) {

        Set<String> attributeSet = new TreeSet<String>();
        List<DXEntry> allEntrys = new ArrayList<DXEntry>();
        if (schema == null) {
            return null;
        }
        Attribute oc = null;
        DXAttributes dx = null;
        DXEntry entry = null;
        DN dn = null;
        final String selectedDN = talendLDAPConnection.getSelectedDN();
        try {
            final EList baseDNs = talendLDAPConnection.getBaseDNs();
            if (selectedDN == null || "".equals(selectedDN)) { //$NON-NLS-1$
                dx = new DXAttributes(LDAPSchemaStep3Form.dirOps.read(dn, null));
                oc = dx.getAllObjectClasses();
                if (oc != null) {
                    entry = new DXEntry(dx, dn);
                    allEntrys.add(entry);
                }
            } else {
                NamingEnumeration searchSubTree = LDAPSchemaStep3Form.dirOps.searchSubTree(selectedDN, "(objectClass=*)", 100, 1); //$NON-NLS-1$
                while (searchSubTree.hasMore()) {
                    SearchResult next = (SearchResult) searchSubTree.next();
                    dn = new DN(next.getName());
                    dx = new DXAttributes(LDAPSchemaStep3Form.dirOps.read(dn, null));
                    oc = dx.getAllObjectClasses();
                    if (oc != null) {
                        entry = new DXEntry(dx, dn);
                        allEntrys.add(entry);
                    }

                }
            }

        } catch (NamingException e1) {
            try {
                if ("CN=SCHEMA".equals(selectedDN)) { //$NON-NLS-1$
                    dn = new DN("CN=SCHEMA"); //$NON-NLS-1$
                    dx = new DXAttributes(LDAPSchemaStep3Form.dirOps.read(dn, null));
                    oc = dx.getAllObjectClasses();
                    if (oc != null) {
                        entry = new DXEntry(dx, dn);
                        allEntrys.add(entry);
                    }
                } else {
                    ExceptionHandler.process(e1);
                }
            } catch (NamingException e) {
                ExceptionHandler.process(e1);
            }

        }
        for (DXEntry oneEntry : allEntrys) {
            Attribute allObjectClasses = oneEntry.getAllObjectClasses();
            if (allObjectClasses != null) {
                featchAttributes(allObjectClasses, attributeSet, schema);
            }
        }

        // qli modification to fix the bug 7545.
        for (DXEntry oneEntry : allEntrys) {
            DXNamingEnumeration possible = (DXNamingEnumeration) oneEntry.getAll();
            possible.sort();

            while (possible.hasMore()) {
                DXAttribute temp = (DXAttribute) possible.next();
                String ldapName = temp.getID();
                if (!attributeSet.contains(ldapName)) {
                    attributeSet.add(ldapName);
                }
            }
        }

        Object[] array = attributeSet.toArray();

        return array;
    }

    private static void featchAttributes(Attribute oc, Set<String> attributeSet, SchemaOps schema) {
        try {
            if (oc.contains(SchemaOps.SCHEMA_FAKE_OBJECT_CLASS_NAME)) {
                return;
            }
            NamingEnumeration ocs = oc.getAll();
            while (ocs.hasMore()) {
                final Object next = ocs.next();
                String objectClass = (String) next;
                attributeSet.add(objectClass);
                Attributes ocAttrs = schema.getAttributes("ClassDefinition/" + objectClass);//$NON-NLS-1$
                Attribute mustAtt = ocAttrs.get("MUST"); //$NON-NLS-1$     // get mandatory attribute IDs
                Attribute mayAtt = ocAttrs.get("MAY"); //$NON-NLS-1$       // get optional attribute IDs

                if (mustAtt != null) {
                    NamingEnumeration musts = mustAtt.getAll();
                    while (musts.hasMore()) {
                        String attributeName = (String) musts.next();
                        if (attributeName.indexOf(";binary") > 0) {
                            attributeName = attributeName.substring(0, attributeName.indexOf(";binary"));//$NON-NLS-1$
                        }
                        String ldapName = null;
                        if (schema.knownOID(attributeName)) {
                            ldapName = schema.translateOID(attributeName);
                            attributeSet.add(ldapName);
                        } else {
                            Attributes myldapEntry = schema.getAttributes("AttributeDefinition/" + attributeName);//$NON-NLS-1$
                            ldapName = myldapEntry.get("NAME").get().toString();//$NON-NLS-1$
                            attributeSet.add(ldapName);
                        }
                    }
                }

                if (mayAtt != null) {
                    NamingEnumeration mays = mayAtt.getAll();
                    while (mays.hasMore()) {
                        String attOID = (String) mays.next();
                        // XXX isNonString hack
                        if (attOID.indexOf(";binary") > 0) {
                            attOID = attOID.substring(0, attOID.indexOf(";binary"));//$NON-NLS-1$
                        }

                        String ldapName = null;
                        if (schema.knownOID(attOID)) {
                            ldapName = schema.translateOID(attOID);
                            attributeSet.add(ldapName);
                        } else {
                            Attributes myldapEntry = schema.getAttributes("AttributeDefinition/" + attOID);//$NON-NLS-1$
                            if (myldapEntry != null) {
                                Attribute attribute = myldapEntry.get("NAME");
                                ldapName = attribute.get().toString();
                                attributeSet.add(ldapName);
                            }

                        }
                    }
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Comment method "fetchBaseDNs".
     * 
     * @return
     */
    public static List<String> fetchBaseDNs() {
        if (ctx == null) {
            return null;
        }

        String searchBase = null;
        List<String> list = new ArrayList<String>();

        javax.naming.directory.Attributes namingContexts;
        try {
            namingContexts = ctx.getAttributes("", new String[] { "namingContexts" }); //$NON-NLS-1$ //$NON-NLS-2$

            javax.naming.directory.Attribute namingContextValue = namingContexts.get("namingContexts"); //$NON-NLS-1$

            if (namingContextValue == null) {
                namingContextValue = namingContexts.get("namingcontexts"); //$NON-NLS-1$
            }
            if (namingContexts.size() == 0 && namingContextValue == null) {
                // list.add("Impossible to get Base DN automatically, please input Base DN manually");
                return list;
            }
            NamingEnumeration<Object> namingEnumeration = (NamingEnumeration<Object>) namingContextValue.getAll();

            while (namingEnumeration.hasMore()) {
                list.add(namingEnumeration.next().toString());
            }

            return list;

        } catch (NamingException e) {
            MessageBoxExceptionHandler.process(e);
            return null;
        }
    }

    /**
     * Administrator Comment method "checkParam".
     * 
     * @param connection
     * @return
     */
    public static boolean checkParam(LDAPSchemaConnection connection, boolean isStep1Check) {
        String hostName = connection.getHost();
        String port = connection.getPort();
        String protocol = connection.getProtocol();

        if (isStep1Check) {
            protocol = EAuthenticationMethod.ANONYMOUS.getName();
        }

        String encryptionMethod = connection.getEncryptionMethodName();
        String userOrBindId = connection.getBindPrincipal();
        String password = connection.getValue(connection.getBindPassword(), false);

        String aliasesDereference = connection.getAliases();
        String referral = connection.getReferrals();

        boolean isAuthUsed = connection.isUseAuthen();

        env = new java.util.Hashtable<String, String>();
        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, CONTEXT_FACTORY);
        if (protocol.equals(EAuthenticationMethod.SIMPLE.getName())) {
            // env.put(javax.naming.Context.SECURITY_AUTHENTICATION, protocol.equals(EAuthenticationMethod.SIMPLE
            // .getName()) ? "none" : "simple");// "none","simple","strong"
            env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");//  //$NON-NLS-1$
        } else {
            env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "none"); //$NON-NLS-1$
        }

        if (isAuthUsed && userOrBindId != null && userOrBindId.length() > 0) {
            env.put(javax.naming.Context.SECURITY_PRINCIPAL, userOrBindId);
        }
        if (isAuthUsed && password != null && password.length() > 0) {
            env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);
        }

        if (aliasesDereference != null && aliasesDereference.length() > 0) {
            env.put("java.naming.ldap.derefAliases", aliasesDereference); //$NON-NLS-1$
        }

        if (referral != null && referral.length() > 0) {
            env.put(javax.naming.Context.REFERRAL, referral);
        }

        String hostUrl = ""; //$NON-NLS-1$
        if (encryptionMethod.equals(EEncryptionMethod.SSL_ENCRYPTION_METHOD.getName())
                || (encryptionMethod.equals(EEncryptionMethod.STARTTSL_EXTENSION_METHOD.getName()))) {
            hostUrl = LDAPS_URL_PREFIX + hostName + ":" + port; //$NON-NLS-1$
            env.put(javax.naming.Context.SECURITY_PROTOCOL, "ssl"); //$NON-NLS-1$
            env.put("java.naming.ldap.factory.socket", AdvancedSocketFactory.class.getName()); //$NON-NLS-1$ 
        } else if (encryptionMethod.equals(EEncryptionMethod.NO_ENCRYPTION_METHOD.getName())) {
            hostUrl = LDAP_URL_PREFIX + hostName + ":" + port; //$NON-NLS-1$
            env.remove(javax.naming.Context.SECURITY_PROTOCOL);
        }
        env.put(javax.naming.Context.PROVIDER_URL, hostUrl);

        try {
            ctx = new javax.naming.ldap.InitialLdapContext(env, null);
            if (encryptionMethod.equals(EEncryptionMethod.STARTTSL_EXTENSION_METHOD)) {
                javax.naming.ldap.StartTlsRequest tldsReq = new javax.naming.ldap.StartTlsRequest();
                javax.naming.ldap.StartTlsResponse tls = (javax.naming.ldap.StartTlsResponse) ctx.extendedOperation(tldsReq);
                javax.net.ssl.SSLSession session = tls.negotiate((javax.net.ssl.SSLSocketFactory) AdvancedSocketFactory
                        .getDefault());
            }

            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
            return false;
        }
    }

}
