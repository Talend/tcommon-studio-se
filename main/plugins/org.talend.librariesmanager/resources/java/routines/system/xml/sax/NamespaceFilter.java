package routines.system.xml.sax;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Responsible for removing of namespaces from XML content.
 *
 * <p>Example of usage:
 * <pre>
 *     XMLReader reader = ...;
 *
 *     ContentHandler contentHandler = ...;
 *
 *     NamespaceFilter nsFilter = new NamespaceFilter(contentHandler, new NamespaceFilter.Matcher() {
 *          public boolean matches(String uri) {
 *              return "urn:my.namespace".equals(uri);
 *          }
 *     });
 *
 *     reader.setContentHandler(nsFilter);
 *
 *     reader.parse(...);
 * </pre>
 */
public class NamespaceFilter extends XMLFilterImpl {

    private Deque<Context> nsStack = new ArrayDeque<Context>();

    private Matcher matcher;

    public NamespaceFilter() {
    }

    public NamespaceFilter(ContentHandler contentHandler, Matcher matcher) {
        setContentHandler(contentHandler);
        setMatcher(matcher);
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (matchNamespace(uri)) {
            nsStack.peek().setMapping(prefix, uri);
        } else {
            super.startPrefixMapping(prefix, uri);
        }
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        if (nsStack.isEmpty()) {
            return;
        }
        String uri = nsStack.peek().getUri(prefix);
        if (uri != null && matchNamespace(uri)) {
            return;
        } else {
            super.endPrefixMapping(prefix);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        nsStack.push(new Context());
        if (matchNamespace(uri)) {
            super.startElement("", localName, localName, atts);
        } else {
            super.startElement(uri, localName, qName, atts);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (matchNamespace(uri)) {
            super.endElement("", localName, localName);
        } else {
            super.endElement(uri, localName, qName);
        }
        nsStack.pop();
    }

    private boolean matchNamespace(String uri) {
        if (uri.length() != 0 && matcher != null && matcher.matches(uri)) {
            return true;
        }
        return false;
    }

    /**
     * Holds namespace mappings for an element.
     */
    static class Context {

        /** Table of [prefix, namespace URI] mappings. */
        private Map<String, String> prefixToUriMap;

        public void setMapping(String prefix, String uri) {
            if (prefixToUriMap == null) {
                prefixToUriMap = new HashMap<String, String>();
            }
            prefixToUriMap.put(uri, prefix);
        }

        public String getUri(String prefix) {
            return prefixToUriMap != null ? prefixToUriMap.get(prefix) : null;
        }
    }

    /**
     * Performs matching of namespace URI
     */
    public interface Matcher {

        /**
         * Check whether given namespace URI matches criteria.
         *
         * @param uri namespace URI to be checked
         * @return {@code true} if namespace URI matches criteria, {@code false} otherwise
         */
        boolean matches(String uri);
    }

}
