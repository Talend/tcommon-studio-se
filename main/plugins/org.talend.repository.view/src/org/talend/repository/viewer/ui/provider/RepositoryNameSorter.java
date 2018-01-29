// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.viewer.ui.provider;

import java.util.Comparator;

import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * Name sorter for the repository view.<br/>
 * 
 * $Id$
 * 
 * @deprecated by TUP-507
 */
@Deprecated
public class RepositoryNameSorter extends ViewerSorter {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
     */
    @Override
    public int category(Object element) {
        if (!(element instanceof RepositoryNode)) {// this sorter return super value if this is not a repository
                                                   // element.
            return super.category(element);
        }
        RepositoryNode node = (RepositoryNode) element;

        if (node.isBin()) {
            return 150;
        }

        if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER) {
            ERepositoryObjectType contentType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (contentType == null) {
                return 199;
            }
            if (contentType == ERepositoryObjectType.BUSINESS_PROCESS) {
                return 1;
            } else if (contentType == ERepositoryObjectType.PROCESS) {
                return 5;
            } else if (contentType == ERepositoryObjectType.JOBLET) {
                return 6;
            } else if (contentType == ERepositoryObjectType.CONTEXT) {
                return 10;
            } else if (contentType == ERepositoryObjectType.CODE) {
                return 15;
            } else if (contentType == ERepositoryObjectType.ROUTINES) {
                return 15;
            } else if (contentType == ERepositoryObjectType.METADATA) {
                return 20;
            } else if (contentType == ERepositoryObjectType.METADATA_CONNECTIONS) {
                return 25;
            } else if (contentType == ERepositoryObjectType.METADATA_SAPCONNECTIONS) {
                return 28;
            } else if (contentType == ERepositoryObjectType.SQLPATTERNS) {
                return 19;
            } else if (contentType == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
                return 30;
            } else if (contentType == ERepositoryObjectType.METADATA_FILE_POSITIONAL) {
                return 35;
            } else if (contentType == ERepositoryObjectType.METADATA_FILE_REGEXP) {
                return 40;
            } else if (contentType == ERepositoryObjectType.METADATA_FILE_XML) {
                return 45;
            } else if (contentType == ERepositoryObjectType.METADATA_FILE_EXCEL) {
                return 46;
            } else if (contentType == ERepositoryObjectType.METADATA_FILE_LDIF) {
                return 50;
            } else if (contentType == ERepositoryObjectType.METADATA_LDAP_SCHEMA) {
                return 55;
            } else if (contentType == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA) {
                return 56;
            } else if (contentType == ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
                return 58;
            } else if (contentType == ERepositoryObjectType.METADATA_MDMCONNECTION) {
                return 59;
            } else if (contentType == ERepositoryObjectType.METADATA_RULES_MANAGEMENT) {
                return 60;
            } else if (contentType == ERepositoryObjectType.METADATA_HEADER_FOOTER) {
                return 61;
            } else if (contentType == ERepositoryObjectType.METADATA_FILE_EBCDIC) {
                return 62;
            } else if (contentType == ERepositoryObjectType.METADATA_WSDL_SCHEMA) {
                return 63;
            } else if (contentType == ERepositoryObjectType.METADATA_VALIDATION_RULES) {
                return 64;
            } else if (contentType == ERepositoryObjectType.DOCUMENTATION) {
                return 65;
            } else if (!contentType.isStaticNode()) {
                return contentType.ordinal();
            } else {
                return 199;
            }
        } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            return 120;
        } else if (node.getType() == ENodeType.REFERENCED_PROJECT) {
            return 200;
        } else {
            if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                ERepositoryObjectType contentType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (contentType != null) {
                    if (contentType == ERepositoryObjectType.METADATA_CON_CDC) {
                        return 120;
                    } else if (contentType == ERepositoryObjectType.METADATA_CON_TABLE) {
                        return 125;
                    } else {
                        return 130;
                    }
                }
            }
            return 130;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerSorter#getComparator()
     */
    @Override
    protected Comparator<String> getComparator() {

        return new Comparator<String>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */

            @Override
            public int compare(String o1, String o2) {
                String a = o1.replace("> ", "");
                String b = o2.replace("> ", "");
                // Replace all "_" to " " due to avoid this situation: job name
                // "a_b_c" before "a_b" in the job list.
                return a.replaceAll("_", " ").compareToIgnoreCase( //$NON-NLS-1$ //$NON-NLS-2$
                        b.replaceAll("_", " ")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        };
    }

}
