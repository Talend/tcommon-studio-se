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
package org.talend.core.model.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.talend.commons.exception.SystemException;
import org.talend.commons.runtime.xml.XmlNodeRetriever;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.metadata.types.DBTypeUtil;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.runtime.i18n.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class MappingFileLoader {

    private static Logger log = Logger.getLogger(MappingFileLoader.class);

    private Dbms dbms;

    /**
     * DOC amaumont Comment method "load".
     * 
     * @param file
     * @throws SystemException
     */
    public void load(File file) throws SystemException {
        XmlNodeRetriever defaultNodeRetriever = new XmlNodeRetriever();
        ECodeLanguage codeLanguage = MetadataTalendType.getCodeLanguage();

        Set<String> hAllTalendTypes = new HashSet<String>();
        if (codeLanguage == ECodeLanguage.JAVA) {

            JavaType[] javaTypes = JavaTypesManager.getJavaTypes();
            for (JavaType javaType : javaTypes) {
                hAllTalendTypes.add(javaType.getId());
            }

        } else if (codeLanguage == ECodeLanguage.PERL) {

            String[] perlTypes = MetadataTalendType.getPerlTypes();
            for (String perlType : perlTypes) {
                hAllTalendTypes.add(perlType);
            }

        } else {
            throw new IllegalStateException("Case language not found"); //$NON-NLS-1$
        }
        StringBuffer stringBuff = new StringBuffer();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder analyser = documentBuilderFactory.newDocumentBuilder();
            Document document = analyser.parse(file);
            stringBuff.append("language=" + codeLanguage + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
            stringBuff.append("file=" + file + "\n"); //$NON-NLS-1$ //$NON-NLS-2$

            NodeList dbmsNodes = document.getElementsByTagName("dbms"); //$NON-NLS-1$

            for (int iDbms = 0; iDbms < dbmsNodes.getLength(); iDbms++) {
                Node dbmsNode = dbmsNodes.item(iDbms);

                Set<String> hTalendTypesProcessed = new HashSet<String>(hAllTalendTypes);

                NamedNodeMap dbmsAttributes = dbmsNode.getAttributes();
                String dbmsProductValue = dbmsAttributes.getNamedItem("product").getNodeValue(); //$NON-NLS-1$
                String dbmsIdValue = dbmsAttributes.getNamedItem("id").getNodeValue(); //$NON-NLS-1$
                String dbmsLabelValue = dbmsAttributes.getNamedItem("label").getNodeValue(); //$NON-NLS-1$
                Node defaultDbmsItem = dbmsAttributes.getNamedItem("default"); //$NON-NLS-1$
                boolean defaultDbms = false;
                if (defaultDbmsItem != null && "true".equals(defaultDbmsItem.getNodeValue())) { //$NON-NLS-1$
                    defaultDbms = true;
                }

                dbms = new Dbms(dbmsIdValue);
                dbms.setLabel(dbmsLabelValue);
                dbms.setProduct(dbmsProductValue);
                dbms.setDefaultDbms(defaultDbms);
                boolean dbmsOverwriteExisting = !MetadataTalendType.getDbmsSet().add(dbms);
                if (dbmsOverwriteExisting) {
                    log.warn(Messages.getString("MappingFileLoader.DbmsIdAlreadyExists", new Object[] { dbmsIdValue })); //$NON-NLS-1$
                }

                // list all dbms children nodes
                List<Node> childrenOfDbmsNode = getChildElementNodes(dbmsNode);

                // TODO create a dtd

                // search "dbTypes" node
                Node dbTypesNode = childrenOfDbmsNode.get(0);

                Set<String> hAllDbTypes = new HashSet<String>();

                // search and load "dbTypes/dbType" nodes
                // search and load ignoreLength and ignorePrecision nodes
                // search and load preBeforelen nodes
                ArrayList<String> dbTypes = new ArrayList<String>();
                ArrayList<DbDefaultLengthAndPrecision> dbDefault = new ArrayList<DbDefaultLengthAndPrecision>();
                ArrayList<DbIgnoreLengthAndPrecision> dbIgnore = new ArrayList<DbIgnoreLengthAndPrecision>();
                ArrayList<DbPreBeforeLength> dbPbeforeLList = new ArrayList<DbPreBeforeLength>();
                dbms.setDbmsTypes(dbTypes);
                dbms.setDefaultLengthPrecision(dbDefault);
                dbms.setIgnoreLengthPrecision(dbIgnore);
                dbms.setPrebeforelength(dbPbeforeLList);
                List<Node> typeNodes = getChildElementNodes(dbTypesNode);

                for (Node typeNode : typeNodes) {
                    NamedNodeMap typeNodeAtttributes = typeNode.getAttributes();
                    String typeValue = typeNodeAtttributes.getNamedItem("type").getNodeValue(); //$NON-NLS-1$
                    stringBuff.append("typeValue=" + typeValue + "\n"); //$NON-NLS-1$ //$NON-NLS-2$

                    Node defaultTypeItem = typeNodeAtttributes.getNamedItem("default"); //$NON-NLS-1$
                    if (hAllDbTypes.contains(typeValue)) {
                        String message = Messages
                                .getString(
                                        "MappingFileLoader.DbTypeAlreadyExists", new Object[] { dbmsIdValue, typeValue, file.getName(), defaultNodeRetriever.getAbsoluteXPathFromNode(typeNode) });//$NON-NLS-1$
                        log.warn(message);
                        continue;
                    }
                    dbTypes.add(typeValue);
                    hAllDbTypes.add(typeValue);
                    DbDefaultLengthAndPrecision dbDefaultLP = new DbDefaultLengthAndPrecision();
                    DbIgnoreLengthAndPrecision dbIgnoreLP = new DbIgnoreLengthAndPrecision();
                    DbPreBeforeLength dbPBeforeL = new DbPreBeforeLength();
                    // default length and precision
                    Node defaultLengthItem = typeNodeAtttributes.getNamedItem("defaultLength"); //$NON-NLS-1$
                    Node defaultPrecision = typeNodeAtttributes.getNamedItem("defaultPrecision"); //$NON-NLS-1$

                    if (defaultLengthItem != null)
                        dbDefaultLP.setDefaultLength(Integer.parseInt(defaultLengthItem.getNodeValue()));
                    if (defaultPrecision != null)
                        dbDefaultLP.setDefaultPrecision(Integer.parseInt(defaultPrecision.getNodeValue()));

                    dbDefaultLP.setDbTypeName(typeValue);
                    dbDefault.add(dbDefaultLP);

                    // ignore Length and Precision
                    Node ignoreLength = typeNodeAtttributes.getNamedItem("ignoreLen"); //$NON-NLS-1$
                    Node ignorePrecision = typeNodeAtttributes.getNamedItem("ignorePre"); //$NON-NLS-1$

                    if (ignoreLength != null)
                        dbIgnoreLP.setIgnoreLength(ignoreLength.getNodeValue());
                    if (ignorePrecision != null)
                        dbIgnoreLP.setIgnorePrecision(ignorePrecision.getNodeValue());
                    dbIgnoreLP.setDbType(typeValue);
                    dbIgnore.add(dbIgnoreLP);

                    // Precision before Length
                    Node preBeforelen = typeNodeAtttributes.getNamedItem("preBeforelen"); //$NON-NLS-1$
                    if (preBeforelen != null)
                        dbPBeforeL.setPreBeforeLen(preBeforelen.getNodeValue());
                    dbPBeforeL.setDbType(typeValue);
                    dbPbeforeLList.add(dbPBeforeL);

                    if (defaultTypeItem != null && "true".equals(defaultTypeItem.getNodeValue())) { //$NON-NLS-1$
                        dbms.setDefaultDbType(typeValue);
                    }
                }

                Set<String> hDbTypesProcessed = new HashSet<String>(hAllDbTypes);

                // search and load "language/type" nodes
                List<Node> languageNodes = childrenOfDbmsNode.subList(1, childrenOfDbmsNode.size());
                for (int i = 0; i < languageNodes.size(); i++) {
                    Node languageNode = languageNodes.get(i);

                    String languageValue = languageNode.getAttributes().getNamedItem("name").getNodeValue(); //$NON-NLS-1$

                    if (codeLanguage.getName().equalsIgnoreCase(languageValue)) {

                        List<Node> mappingDirections = getChildElementNodes(languageNode);
                        Node talendTypesToDbTypesNode = mappingDirections.get(0);

                        Set<MappingType> mappingTypes = new HashSet<MappingType>();
                        dbms.setTalendToDbTypes(mappingTypes);

                        List<Node> talendTypeSourcesList = getChildElementNodes(talendTypesToDbTypesNode);
                        int talendTypeSourcesListListSize = talendTypeSourcesList.size();
                        for (int iTalendTypeSource = 0; iTalendTypeSource < talendTypeSourcesListListSize; iTalendTypeSource++) {
                            Node talendTypeSource = talendTypeSourcesList.get(iTalendTypeSource);

                            NamedNodeMap talendTypeAttributes = talendTypeSource.getAttributes();
                            Node talendTypeItem = talendTypeAttributes.getNamedItem("type"); //$NON-NLS-1$
                            if (talendTypeItem == null) {
                                continue;
                            }
                            String talendType = talendTypeItem.getNodeValue();

                            stringBuff.append("talendTypeItem=" + talendType + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                            if (!hAllTalendTypes.contains(talendType)) { // test
                                // if
                                // the
                                // type
                                // exists
                                String message = Messages.getString(
                                        "MappingFileLoader.InvalidTalendType", //$NON-NLS-1$
                                        new Object[] { talendType, codeLanguage.getName(), dbmsIdValue,
                                                defaultNodeRetriever.getAbsoluteXPathFromNode(talendTypeSource) });
                                // System.out.println(message);
                                log.warn(message);
                                continue;
                            }

                            hTalendTypesProcessed.remove(talendType);

                            List<Node> languageTypesNodes = getChildElementNodes(talendTypeSource);

                            for (int j = 0; j < languageTypesNodes.size(); j++) {

                                Node languageTypeNode = languageTypesNodes.get(j);

                                NamedNodeMap dbTypeAttributes = languageTypeNode.getAttributes();

                                Node dbTypeItem = dbTypeAttributes.getNamedItem("type"); //$NON-NLS-1$
                                if (dbTypeItem == null) {
                                    continue;
                                }
                                String dbType = dbTypeItem.getNodeValue();

                                if (!hAllDbTypes.contains(dbType)) {
                                    String message = Messages
                                            .getString(
                                                    "MappingFileLoader.UndeclaredDbType", new Object[] { dbType, dbmsIdValue, defaultNodeRetriever.getAbsoluteXPathFromNode(languageTypeNode) }); //$NON-NLS-1$
                                    // System.out.println(message);
                                    log.warn(message);
                                    continue;
                                }

                                Node defaultSelectedItem = dbTypeAttributes.getNamedItem("default"); //$NON-NLS-1$

                                MappingType objectMappingType = new MappingType();
                                objectMappingType.setTalendType(talendType);
                                objectMappingType.setDbType(dbType);
                                objectMappingType
                                        .setDefaultSelected(defaultSelectedItem != null
                                                && defaultSelectedItem.getNodeValue().equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE); //$NON-NLS-1$
                                mappingTypes.add(objectMappingType);

                            }
                        }

                        Node dbToTalendTypes = mappingDirections.get(1);

                        mappingTypes = new HashSet<MappingType>();
                        dbms.setDbToTalendTypes(mappingTypes);

                        List<Node> dbTypeSourcesList = getChildElementNodes(dbToTalendTypes);
                        int dbTypeSourcesListListSize = dbTypeSourcesList.size();
                        for (int iDbTypeSource = 0; iDbTypeSource < dbTypeSourcesListListSize; iDbTypeSource++) {
                            Node dbTypeSource = dbTypeSourcesList.get(iDbTypeSource);

                            NamedNodeMap dbTypeAttributes = dbTypeSource.getAttributes();
                            Node dbTypeItem = dbTypeAttributes.getNamedItem("type"); //$NON-NLS-1$
                            if (dbTypeItem == null) {
                                continue;
                            }
                            String dbType = dbTypeItem.getNodeValue();
                            stringBuff.append("dbType=" + dbType + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                            if (!hAllDbTypes.contains(dbType)) {
                                String message = Messages
                                        .getString(
                                                "MappingFileLoader.UndeclaredDbType", new Object[] { dbType, dbmsIdValue, defaultNodeRetriever.getAbsoluteXPathFromNode(dbTypeSource) }); //$NON-NLS-1$
                                // System.out.println(message);
                                log.warn(message);
                                continue;
                            }

                            hDbTypesProcessed.remove(dbType);
                            // System.out.println("Removed :" + dbType);

                            List<Node> languageTypesNodes = getChildElementNodes(dbTypeSource);

                            for (int j = 0; j < languageTypesNodes.size(); j++) {

                                Node languageTypeNode = languageTypesNodes.get(j);

                                NamedNodeMap talendTypeAttributes = languageTypeNode.getAttributes();

                                Node talendTypeItem = talendTypeAttributes.getNamedItem("type"); //$NON-NLS-1$
                                if (talendTypeItem == null) {
                                    continue;
                                }
                                String talendType = talendTypeItem.getNodeValue();

                                if (!hAllTalendTypes.contains(talendType)) {
                                    String message = Messages.getString(
                                            "MappingFileLoader.InvalidTalendType", //$NON-NLS-1$
                                            new Object[] { talendType, codeLanguage.getName(), dbmsIdValue,
                                                    defaultNodeRetriever.getAbsoluteXPathFromNode(languageTypeNode) });
                                    // System.out.println(message);
                                    log.warn(message);
                                    continue;
                                }

                                Node defaultSelectedItem = talendTypeAttributes.getNamedItem("default"); //$NON-NLS-1$

                                boolean defaultSelected = defaultSelectedItem != null
                                        && defaultSelectedItem.getNodeValue().equalsIgnoreCase("true") ? true : false; //$NON-NLS-1$

                                MappingType mappingType = new MappingType();
                                mappingType.setDbType(dbType);
                                mappingType.setTalendType(talendType);
                                mappingType.setDefaultSelected(defaultSelected);
                                mappingTypes.add(mappingType);
                            }
                        }

                    }
                }
                if (hTalendTypesProcessed.size() > 0) {
                    Map<String, Map<String, List<DBTypeUtil>>> javaTypeMappingFromExtension = JavaTypesManager
                            .getJavaTypeMappingFromExtension();
                    Iterator<String> iterator = hTalendTypesProcessed.iterator();
                    while (iterator.hasNext()) {
                        String talendType = iterator.next();
                        Map<String, List<DBTypeUtil>> dbAndDBType = javaTypeMappingFromExtension.get(talendType);
                        if (dbAndDBType != null) {
                            // remove talend type if from extension
                            iterator.remove();
                            List<DBTypeUtil> dBTypeUtils = dbAndDBType.get(dbms.getId());
                            Set<MappingType> talendToDbTypes = dbms.getTalendToDbTypes();
                            Set<MappingType> dbToTalendTypes = dbms.getDbToTalendTypes();
                            if (dBTypeUtils != null && !dBTypeUtils.isEmpty()) {
                                for (DBTypeUtil dbType : dBTypeUtils) {
                                    MappingType mappingType = new MappingType();
                                    mappingType.setDbType(dbType.getDbTypeName());
                                    mappingType.setTalendType(talendType);
                                    mappingType.setDefaultSelected(dbType.isDefault());
                                    talendToDbTypes.add(mappingType);

                                    mappingType = new MappingType();
                                    mappingType.setDbType(dbType.getDbTypeName());
                                    mappingType.setTalendType(talendType);
                                    // user added talend type won't be the default mapping type to dbtype
                                    mappingType.setDefaultSelected(false);
                                    dbToTalendTypes.add(mappingType);
                                }
                            }
                        }
                    }

                }

                if (hDbTypesProcessed.size() > 0) {
                    String dbTypesStr = ""; //$NON-NLS-1$
                    for (String dbType : hDbTypesProcessed) {
                        dbTypesStr += "\n" + dbType; //$NON-NLS-1$
                    }
                    String message = Messages
                            .getString(
                                    "MappingFileLoader.UnbindedDbTypes", new Object[] { dbmsIdValue, codeLanguage.getName(), dbTypesStr }); //$NON-NLS-1$
                    // System.out.println(message);
                    log.warn(message);
                }

                if (hTalendTypesProcessed.size() > 0) {
                    String talendTypesStr = ""; //$NON-NLS-1$
                    for (String talendType : hTalendTypesProcessed) {
                        talendTypesStr += "\n" + talendType; //$NON-NLS-1$
                    }
                    String message = Messages
                            .getString(
                                    "MappingFileLoader.UnbindedTalendTypes", new Object[] { dbmsIdValue, codeLanguage.getName(), talendTypesStr }); //$NON-NLS-1$
                    // System.out.println(message);
                    log.warn(message);
                }

            }
        } catch (Exception e) {
            throw new SystemException("Error when load db mapping, lasted loaded data are:\n" + stringBuff.toString()); //$NON-NLS-1$
        }

    }

    /**
     * Get children of type ELEMENT_NODE from parent <code>parentNode</code>.
     * 
     * @param parentNode
     * @return
     */
    private List<Node> getChildElementNodes(Node parentNode) {
        Node childNode = parentNode.getFirstChild();
        ArrayList<Node> list = new ArrayList<Node>();
        while (childNode != null) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                list.add(childNode);
            }
            childNode = childNode.getNextSibling();
        }
        return list;
    }

}
