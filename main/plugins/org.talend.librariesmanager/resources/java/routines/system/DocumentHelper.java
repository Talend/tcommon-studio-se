package routines.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Namespace;

import routines.TalendString;

/**
 * dom4j Document helper
 * 
 * @author Administrator
 *
 */
public class DocumentHelper {

    /**
     * 
     * @param element : current element
     * @return the path of current element
     */
    public static String getPath(Element element, Map<String, String> defaultNamespaceUriTOPrefix) {
        Element parent = element.getParent();

        if (parent == null) {
            return "/" + getXPathNameStep(element, defaultNamespaceUriTOPrefix);
        }

        return getPath(parent, defaultNamespaceUriTOPrefix) + "/" + getXPathNameStep(element, defaultNamespaceUriTOPrefix);
    }

    private static String getXPathNameStep(Element element, Map<String, String> defaultNamespaceUriTOPrefix) {
        String uri = element.getNamespaceURI();

        if ((uri == null) || (uri.length() == 0)) {
            return element.getName();
        }

        String prefix = element.getNamespacePrefix();

        if ((prefix == null) || (prefix.length() == 0)) {
            String defaultNamespacePrefix = defaultNamespaceUriTOPrefix.get(uri);
            return defaultNamespacePrefix + ":" + element.getName();
        }

        return element.getQName().getQualifiedName();
    }

    /**
     * compare DOM NODE
     * 
     * @return true when path is the same && all the node namespaces are the same in the path && the declared namespace of the
     * current node is the same
     */
    public static boolean compareNodes(String path, Element element, Map<String, String> defaultNamespaceUriTOPrefix,
            Map<String, String> declaredNamespacesMapping, boolean appendDoc) {
        boolean samePath = path.equals(getPath(element, defaultNamespaceUriTOPrefix));

        if (!samePath) {
            return false;
        }

        if (appendDoc) {
            List<Namespace> declaredNamespaces = element.declaredNamespaces();
            int size = declaredNamespaces.size();
            if (size == 0) {
                return declaredNamespacesMapping == null || declaredNamespacesMapping.size() == 0;
            } else if (size == 1) {
                if (declaredNamespacesMapping != null && declaredNamespacesMapping.size() == 1) {
                    Namespace namespace = declaredNamespaces.get(0);
                    for (Map.Entry<String, String> entry : declaredNamespacesMapping.entrySet()) {
                        return namespace.getPrefix().equals(entry.getKey()) && namespace.getURI().equals(entry.getValue());
                    }
                } else {
                    return false;
                }
            } else {// a few case : two or more declared namespaces
                if (declaredNamespacesMapping != null && declaredNamespacesMapping.size() == size) {
                    Map<String, String> namespaces = new HashMap<String, String>();
                    for (Namespace namespace : declaredNamespaces) {
                        namespaces.put(namespace.getPrefix(), namespace.getURI());
                    }
                    return (namespaces.hashCode() == declaredNamespacesMapping.hashCode())
                            && namespaces.equals(declaredNamespacesMapping);
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * for the xml append and check whether the root element is the same
     * 
     * @param root
     * @param prefixToUri
     * @return
     */
    public static boolean isMatchAtRoot(Element root, Map<String, String> prefixToUri) {
        if (root == null) {
            return false;
        }
        Namespace namespace = root.getNamespace();
        String uri = namespace.getURI();
        if ("".equals(namespace.getPrefix()) && !"".equals(uri)) {
            for (Map.Entry<String, String> entry : prefixToUri.entrySet()) {
                if (uri.equals(entry.getValue()) && entry.getKey() != null && entry.getKey().startsWith("TPrefix")) {// TPrefix
                                                                                                                     // mean that
                                                                                                                     // default
                                                                                                                     // namespace
                                                                                                                     // in UI tree
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static void applyNamespace1(org.dom4j.Element currentElement, String prefix, String uri) {
        applyNamespace2(currentElement, currentElement.getName(), prefix, uri);
    }

    public static void applyNamespace2(org.dom4j.Element currentElement, String localName, String prefix, String uri) {
        currentElement.setQName(org.dom4j.DocumentHelper.createQName(localName,
                org.dom4j.DocumentHelper.createNamespace(prefix, TalendString.replaceSpecialCharForXML(uri))));
    }

    private static final String NAMESPACE_ATTRIBUTE_PREFIX = "xmlns";

    public static void applyNamespace2Attribute(org.dom4j.Element currentElement, String prefix, String localname, String value) {
        if (value == null) {
            return;
        }

        if (prefix == null) {
            if (NAMESPACE_ATTRIBUTE_PREFIX.equals(localname)) {
                currentElement.addNamespace("", value);
            } else {
                currentElement.addAttribute(localname, value);
            }

            return;
        }

        if (NAMESPACE_ATTRIBUTE_PREFIX.equals(prefix)) {
            currentElement.addNamespace(localname, value);
            return;
        }

        Namespace namespace = currentElement.getNamespaceForPrefix(prefix);
        if (namespace == null) {
            currentElement.addAttribute(localname, value);
        }

        currentElement.addAttribute(org.dom4j.DocumentHelper.createQName(localname, namespace), value);
    }
}
