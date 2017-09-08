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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.colorstyledtext.scanner.ColoringScanner;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ModeReader.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ModeReader {

    protected ISyntaxListener listener;

    public ModeReader(ISyntaxListener listener) {
        super();
        this.listener = listener;
    }

    public void read(String filename) {
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(CommonsPlugin.getFileInputStream(filename));
        } catch (DocumentException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
            return;
        } catch (IOException ioe) {
            // ioe.printStackTrace();
            ExceptionHandler.process(ioe);
        }
        Element root = doc.getRootElement();
        // List properties = root.elements("PROPS");
        // todo parse the properties
        for (Iterator iter = root.elementIterator("RULES"); iter.hasNext();) { //$NON-NLS-1$
            Element rulesElement = (Element) iter.next();
            createRule(rulesElement);
            /*
             * Because over of rules is important I must read the elements in order instead of using
             * elementIterators("SPAN", etc)
             */
            List allTypes = rulesElement.elements();
            for (Iterator allTypesI = allTypes.iterator(); allTypesI.hasNext();) {
                Element element = (Element) allTypesI.next();
                createType(element);
            }
        }
    }

    private void createType(Element element) {
        if (element.getName().equals("SPAN")) { //$NON-NLS-1$
            createSpan(element);
        } else if (element.getName().equals("EOL_SPAN")) { //$NON-NLS-1$
            createEOL(element);
        } else if (element.getName().equals("SEQ")) { //$NON-NLS-1$
            createTextSequence(element);
        } else if (element.getName().equals("KEYWORDS")) { //$NON-NLS-1$
            createKeywords(element);
        } else if (element.getName().equals("MARK_PREVIOUS")) { //$NON-NLS-1$
            createMark(element, true);
        } else if (element.getName().equals("MARK_FOLLOWING")) { //$NON-NLS-1$
            createMark(element, false);
        } else if (element.getName().equals("WHITESPACE")) { //$NON-NLS-1$
            // ignore for now
        } else {
            System.out.println(Messages.getString("ModeReader.IgnoreElement") + element.getName()); //$NON-NLS-1$
        }
    }

    private void createKeywords(Element keywordsE) {
        KeywordMap keywords = new KeywordMap(bool(keywordsE, "IGNORE_CASE", true)); //$NON-NLS-1$
        keywords.put(ColoringScanner.KEYWORD1, toStringArray(keywordsE.elements("KEYWORD1"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.KEYWORD2, toStringArray(keywordsE.elements("KEYWORD2"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.KEYWORD3, toStringArray(keywordsE.elements("KEYWORD3"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.COMMENT1, toStringArray(keywordsE.elements("COMMENT1"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.COMMENT2, toStringArray(keywordsE.elements("COMMENT2"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.LITERAL1, toStringArray(keywordsE.elements("LITERAL1"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.LITERAL2, toStringArray(keywordsE.elements("LITERAL2"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.LABEL, toStringArray(keywordsE.elements("LABEL"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.FUNCTION, toStringArray(keywordsE.elements("FUNCTION"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.MARKUP, toStringArray(keywordsE.elements("MARKUP"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.OPERATOR, toStringArray(keywordsE.elements("OPERATOR"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.DIGIT, toStringArray(keywordsE.elements("DIGIT"))); //$NON-NLS-1$
        keywords.put(ColoringScanner.INVALID, toStringArray(keywordsE.elements("INVALID"))); //$NON-NLS-1$

        // for special database
        for (Iterator iter = keywordsE.elementIterator("DATABASE"); iter.hasNext();) { //$NON-NLS-1$
            Element dbElement = (Element) iter.next();
            String dbProduct = dbElement.attributeValue("product"); //$NON-NLS-1$
            if (dbProduct != null && !"".equals(dbProduct)) {
                keywords.put(dbProduct, toStringArray(dbElement.elements("KEYWORD4"))); //$NON-NLS-1$
            }

        }
        listener.newKeywords(keywords);
    }

    private String[] toStringArray(List list) {
        if (list.isEmpty()) {
            return new String[0];
        }
        String[] strings = new String[list.size()];
        int i = 0;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            strings[i] = ((Element) iter.next()).getText();
            i++;
        }
        return strings;
    }

    private void createTextSequence(Element seqElement) {
        boolean atLineStart = bool(seqElement, "AT_LINE_START", false); //$NON-NLS-1$
        boolean atWhitespaceEnd = bool(seqElement, "AT_WHITESPACE_END", false); //$NON-NLS-1$
        boolean atWordStart = bool(seqElement, "AT_WORD_START", false); //$NON-NLS-1$
        String type = seqElement.attributeValue("TYPE"); //$NON-NLS-1$
        String delegate = seqElement.attributeValue("DELEGATE"); //$NON-NLS-1$
        listener.newTextSequence(type, seqElement.getText(), atLineStart, atWhitespaceEnd, atWordStart, delegate);
    }

    private void createEOL(Element eolElement) {
        listener.newEOLSpan(eolElement.attributeValue("TYPE"), eolElement.getText()); //$NON-NLS-1$
    }

    private void createSpan(Element spanElement) {
        String type = spanElement.attributeValue("TYPE"); //$NON-NLS-1$
        boolean atLineStart = bool(spanElement, "AT_LINE_START", false); //$NON-NLS-1$
        boolean excludeMatch = bool(spanElement, "EXCLUDE_MATCH", false); //$NON-NLS-1$
        boolean noLineBreak = bool(spanElement, "NO_LINE_BREAK", false); //$NON-NLS-1$
        boolean noWordBreak = bool(spanElement, "NO_WORD_BREAK", false); //$NON-NLS-1$
        String delegate = spanElement.attributeValue("DELEGATE"); //$NON-NLS-1$
        String begin = spanElement.element("BEGIN").getText(); //$NON-NLS-1$
        String end = spanElement.element("END").getText(); //$NON-NLS-1$
        listener.newSpan(type, begin, end, atLineStart, excludeMatch, noLineBreak, noWordBreak, delegate);
    }

    private void createMark(Element markElement, boolean isPrevious) {
        boolean atLineStart = bool(markElement, "AT_LINE_START", false); //$NON-NLS-1$
        boolean atWhitespaceEnd = bool(markElement, "AT_WHITESPACE_END", false); //$NON-NLS-1$
        boolean excludeMatch = bool(markElement, "EXCLUDE_MATCH", false); //$NON-NLS-1$
        boolean atWordStart = bool(markElement, "AT_WORD_START", false); //$NON-NLS-1$
        String type = markElement.attributeValue("TYPE"); //$NON-NLS-1$
        String delegate = markElement.attributeValue("DELEGATE"); //$NON-NLS-1$
        listener.newMark(type, markElement.getText(), atLineStart, atWhitespaceEnd, atWordStart, delegate, isPrevious,
                excludeMatch);
    }

    protected void createRule(Element rulesElement) {
        String name = rulesElement.attributeValue("SET", Rule.DEFAULT_NAME); //$NON-NLS-1$
        boolean highlightDigits = bool(rulesElement, "HIGHLIGHT_DIGITS", false); //$NON-NLS-1$
        boolean ignoreCase = bool(rulesElement, "IGNORE_CASE", true); //$NON-NLS-1$
        String digitRE = rulesElement.attributeValue("DIGIT_RE"); //$NON-NLS-1$
        char escape = rulesElement.attributeValue("ESCAPE", "" + (char) 0).charAt(0); //$NON-NLS-1$ //$NON-NLS-2$
        String defaultTokenType = rulesElement.attributeValue("DEFAULT", ColoringScanner.NULL); //$NON-NLS-1$
        listener.newRules(name, highlightDigits, ignoreCase, digitRE, escape, defaultTokenType);
    }

    protected boolean bool(Element element, String attributeName, boolean defaultValue) {
        return Boolean.valueOf(element.attributeValue(attributeName, String.valueOf(defaultValue))).booleanValue();
    }
}
