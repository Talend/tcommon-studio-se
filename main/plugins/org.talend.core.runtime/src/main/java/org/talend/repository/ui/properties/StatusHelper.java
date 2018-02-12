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
package org.talend.repository.ui.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: StatusHelper.java 914 2006-12-08 08:28:53 +0000 (星期五, 08 十二月 2006) bqian $
 * 
 */
public class StatusHelper {

    private Map<String, Status> code2status;

    private Map<String, String> label2code;

    private IProxyRepositoryFactory repositoryFactory;

    public StatusHelper(IProxyRepositoryFactory repositoryFactory) {
        super();
        this.repositoryFactory = repositoryFactory;
    }

    public String getStatusLabel(String statusCode) {
        if (statusCode == null) {
            return ""; //$NON-NLS-1$
        }
        Status status = null;
        if (code2status != null) {
            status = code2status.get(statusCode);
        }
        return status == null ? statusCode : status.getLabel();
    }

    public String getStatusCode(String label) {
        String text;
        text = label2code.get(label);
        return (text == null ? label : text);
    }

    public List<Status> getStatusList(Property property) throws PersistenceException {
        List<Status> status = null;
        Item item =null;
        if(property!=null)
            item = property.getItem();
        if (item != null) {
            EClass propertyEClass = item.eClass();
            int i = propertyEClass.getClassifierID();
            switch (propertyEClass.getClassifierID()) {
            case PropertiesPackage.CSV_FILE_CONNECTION_ITEM:
            case PropertiesPackage.DATABASE_CONNECTION_ITEM:
            case PropertiesPackage.SAP_CONNECTION_ITEM:
            case PropertiesPackage.DELIMITED_FILE_CONNECTION_ITEM:
            case PropertiesPackage.POSITIONAL_FILE_CONNECTION_ITEM:
            case PropertiesPackage.CONTEXT_ITEM:
            case PropertiesPackage.PROCESS_ITEM:
            case PropertiesPackage.JOBLET_PROCESS_ITEM:
            case PropertiesPackage.ROUTINE_ITEM:
            case PropertiesPackage.PIGUDF_ITEM:
            case PropertiesPackage.REG_EX_FILE_CONNECTION_ITEM:
            case PropertiesPackage.GENERIC_SCHEMA_CONNECTION_ITEM:
            case PropertiesPackage.LDAP_SCHEMA_CONNECTION_ITEM:
            case PropertiesPackage.LDIF_FILE_CONNECTION_ITEM:
            case PropertiesPackage.XML_FILE_CONNECTION_ITEM:
            case PropertiesPackage.EXCEL_FILE_CONNECTION_ITEM:
            case PropertiesPackage.SALESFORCE_SCHEMA_CONNECTION_ITEM:
            case PropertiesPackage.WSDL_SCHEMA_CONNECTION_ITEM:
            case PropertiesPackage.EBCDIC_CONNECTION_ITEM:
            case PropertiesPackage.HL7_CONNECTION_ITEM:
            case PropertiesPackage.FTP_CONNECTION_ITEM:
            case PropertiesPackage.BRMS_CONNECTION_ITEM:
            case PropertiesPackage.MDM_CONNECTION_ITEM:
            case PropertiesPackage.JOB_SCRIPT_ITEM:
            case PropertiesPackage.SQL_PATTERN_ITEM:
            case PropertiesPackage.EDIFACT_CONNECTION_ITEM:
            case PropertiesPackage.VALIDATION_RULES_CONNECTION_ITEM:
                status = repositoryFactory.getTechnicalStatus();
                break;
            case PropertiesPackage.RULES_ITEM:
                status = repositoryFactory.getTechnicalStatus();
                break;
            case PropertiesPackage.LINK_RULES_ITEM:
                status = repositoryFactory.getTechnicalStatus();
                break;
            case PropertiesPackage.BUSINESS_PROCESS_ITEM:
            case PropertiesPackage.SVG_BUSINESS_PROCESS_ITEM:
            case PropertiesPackage.DOCUMENTATION_ITEM:
                status = repositoryFactory.getDocumentationStatus();
                break;
            }
            if (status == null) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
                    IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
                    if (service != null) {
                        boolean flag = service.isServiceItem(i);
                        if (flag) {
                            status = repositoryFactory.getTechnicalStatus();
                        }
                    }
                }
                for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                    List<Status> propertyStatus = handler.getPropertyStatus(item);
                    if (propertyStatus != null && propertyStatus.size() > 0) {
                        status = propertyStatus;
                        break;
                    }
                }
            }
        }
        if (status == null) {
            status = new ArrayList<Status>();
        }
        toMaps(status);
        return status;
    }

    /**
     * DOC tguiu Comment method "asMap".
     * 
     * @param status
     * @return
     */
    private void toMaps(List<Status> status) {
        code2status = new HashMap<String, Status>();
        label2code = new HashMap<String, String>();
        for (Status s : status) {
            code2status.put(s.getCode(), s);
            label2code.put(s.getLabel(), s.getCode());
        }
    }
}
