// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Node;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class Document implements java.io.Serializable{

    private org.dom4j.Document doc = null;

    public void setDocument(org.dom4j.Document doc) {
        this.doc = doc;
    }

    public org.dom4j.Document getDocument() {
        return this.doc;
    }

    public String toString() {
        if (this.doc == null)
            return null;

        return this.doc.asXML();
    }
    /**
     * Format XML String by a OutputFormat object
     * (Used in tAdvancedFileOutputXML )
     *
     * @param format
     * @return the formatted time string.
     *
     */
    public String formatXMLString(org.dom4j.io.OutputFormat format){

		if (this.doc == null){
			return null;
		}
		java.io.OutputStream outputStream = new java.io.ByteArrayOutputStream();
		org.dom4j.io.XMLWriter writer;
		try {
			writer = new org.dom4j.io.XMLWriter(outputStream, format);
			writer.write(this.doc);
			writer.close();
		} catch (java.io.IOException e) {
			System.err.println("Format the XML document error.");
			return this.doc.asXML();
		}

		return outputStream.toString();

    }

    /**
     * lookup document action
     * @param loopXPath
     * @param lookupInfo
     * @param xpathOfResults
     * @param nsMapping
     * @param xpathToTypeMap
     * @param xpathToPatternMap
     * @param matchingMode
     * @return
     */
    public List<Map<String, Object>> LookupDocument(String loopXPath,boolean isOptionalLoop, Map<String, Object> lookupInfo,
            Map<String, String> xpathOfResults, Map<String, String> nsMapping,
            Map<String, String> xpathToTypeMap,Map<String, String> xpathToPatternMap,String matchingMode) {
        if (doc == null || lookupInfo == null) {
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        org.dom4j.Document document = doc.getDocument();

        //init document to flat tool
        DocumentToFlat docToFlat = new DocumentToFlat(lookupInfo, xpathOfResults, xpathToTypeMap, xpathToPatternMap);
        docToFlat.setDoc(document);
        docToFlat.setOriginalLoop(loopXPath);
        docToFlat.setXmlNameSpaceMap(nsMapping);
        docToFlat.flatForLookup(isOptionalLoop);
        if(docToFlat.isLoopChanged()) {//never change the original lookup information object state
        	lookupInfo = docToFlat.getLookupInfo();
        	xpathOfResults = docToFlat.getXpathOfResults();
        	xpathToTypeMap = docToFlat.getXpathToTypeMap();
        	xpathToPatternMap = docToFlat.getXpathToPatternMap();
        }
        java.util.List<org.dom4j.tree.AbstractNode> nodes = docToFlat.getNodes();

        for (org.dom4j.tree.AbstractNode node : nodes) {
            boolean reject = false;
            // lookup action
            if(!"ALL_ROWS".equals(matchingMode)) {
	            for (String xpath : lookupInfo.keySet()) {
	                Object lookupValue = lookupInfo.get(xpath);
	                org.dom4j.XPath xpathObjectForLookup = node.createXPath(xpath);
	                xpathObjectForLookup.setNamespaceURIs(nsMapping);
	                Node nodeOfLookup = xpathObjectForLookup.selectSingleNode(node);
	                //parse action
	            	String text = (nodeOfLookup == null ? null : xpathObjectForLookup.valueOf(node));
	            	String pattern = xpathToPatternMap.get(xpath);
	            	String javaType = xpathToTypeMap.get(xpath);
	            	Object value = ParserUtils.parse(text, javaType, pattern);

	            	if(lookupValue == null && value == null) {
	            		//do nothing(null==null)
	            	} else {
	                	if(value == null || !value.equals(lookupValue)) {
	                		reject = true;
	                    	break;
	                	}
	            	}

	            }
            }
            // generate result action
            if (reject) {
                // do nothing
            } else {
                Map<String, Object> row = new HashMap<String, Object>();
                for (Object key : xpathOfResults.keySet()) {
                    String xpath = xpathOfResults.get(key);
                    org.dom4j.XPath xpathObjectForResult = node.createXPath(xpath);
                    xpathObjectForResult.setNamespaceURIs(nsMapping);
                    Node nodeOfResult = xpathObjectForResult.selectSingleNode(node);
                    row.put(key.toString(), nodeOfResult == null ? null : nodeOfResult.getText());
                }
                result.add(row);
            }

        }
        //set resultset
		int count = result.size();
		if(count>0) {
			if("UNIQUE_MATCH".equals(matchingMode)) {
				List<Map<String,Object>> singleResult = new ArrayList<Map<String,Object>>();
				singleResult.add(result.get(count-1));
				return singleResult;
			} else if("FIRST_MATCH".equals(matchingMode)) {
				List<Map<String,Object>> singleResult = new ArrayList<Map<String,Object>>();
				singleResult.add(result.get(0));
				return singleResult;
			}
		}
		return result;
    }

}
