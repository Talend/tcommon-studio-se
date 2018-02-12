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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.i18n.Messages;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: Mode.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class Mode {

    /**
     * DOC nrousseau Mode class global comment. Detailled comment <br/>
     * 
     * $Id: Mode.java 7038 2007-11-15 14:05:48Z plegall $
     * 
     */
    class ModeSyntaxListener implements ISyntaxListener {

        public void newEOLSpan(String type, String text) {
            currentRule.add(Type.newEOLSpan(type, text));
        }

        public void newKeywords(KeywordMap keywords) {
            currentRule.setKeywords(keywords);
        }

        public void newRules(String theName, boolean highlightDigits, boolean ignoreCase, String digitRE, char escape,
                String defaultTokenType) {

            currentRule = Rule.newRule(Mode.this, theName, highlightDigits, ignoreCase, digitRE, escape, defaultTokenType);
            Mode.this.add(currentRule);
        }

        public void newSpan(String type, String begin, String end, boolean atLineStart, boolean excludeMatch,
                boolean noLineBreak, boolean noWordBreak, String delegate) {
            currentRule.add(Type.newSpan(type, begin, end, atLineStart, excludeMatch, noLineBreak, noWordBreak, delegate));
        }

        public void newMark(String type, String text, boolean atLineStart, boolean atWhitespaceEnd, boolean atWordStart,
                String delegate, boolean isPrevious, boolean excludeMatch) {
            currentRule.add(Type.newMark(type, text, atLineStart, atWhitespaceEnd, atWordStart, delegate, isPrevious,
                    excludeMatch));
        }

        public void newTextSequence(String type, String text, boolean atLineStart, boolean atWhitespaceEnd, boolean atWordStart,
                String delegate) {
            currentRule.add(Type.newTextSequence(type, text, atLineStart, atWhitespaceEnd, atWordStart, delegate));
        }

    }

    protected Map<String, Rule> rules;

    protected String name;

    protected String displayName;

    protected String filename;

    protected String filenameGlob;

    protected String firstLineGlob;

    protected String[] contentTypes;

    protected RE re;

    protected Map delegates;

    protected boolean isLoaded;

    protected Rule currentRule;

    public Mode(String name) {
        super();
        this.name = name.toLowerCase();
        rules = new HashMap<String, Rule>();
        delegates = new HashMap();
        displayName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public Collection getRules() {
        return rules.values();
    }

    public void add(Rule rule) {
        rules.put(rule.getName(), rule);
    }

    public Rule getDefaultRuleSet() {
        return (Rule) rules.get(Rule.DEFAULT_NAME);
    }

    /**
     * Answer the named rule. This may be a rule local to this mode or may refer to a rule in another mode.
     * 
     * BUGFIX I also found that some of the modes specify modes that do not exist. (asp.xml) in cases where that happens
     * answer the default rule.
     * 
     * @param name
     * @return Rule
     */
    public Rule getRule(String theName) {
        int colonIndex = theName.indexOf("::"); //$NON-NLS-1$
        Rule localRule;
        if (colonIndex == -1) {
            localRule = (Rule) rules.get(theName);
            if (localRule == null) {
                localRule = getDefaultRuleSet();
            }
            return localRule;
        }
        return Modes.resolveDelegate(this, theName);
    }

    public String toString() {
        return Messages.getString("Mode.Mode.ToString", name); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static Mode newMode(String name, String filename, String fileGlob, String firstLineGlob) {
        Mode mode = new Mode(name);
        mode.filename = filename;
        mode.filenameGlob = fileGlob;
        mode.firstLineGlob = firstLineGlob;
        return mode;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Answer true if the receiver can represent the filename.
     */
    public boolean matches(String aFilename) {
        if (re == null) {
            createRE();
        }
        return re.match(aFilename);
    }

    public Map getDelegates() {
        return delegates;
    }

    private void createRE() {
        try {
            if (filenameGlob == null) {
                re = new RE(filename);
            }
            if (filenameGlob == null) {
                re = new RE(filename + "$"); //$NON-NLS-1$
                return;
            }
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < filenameGlob.length(); i++) {
                char c = filenameGlob.charAt(i);
                switch (c) {
                case '*':
                    buf.append(".*"); //$NON-NLS-1$
                    break;
                case '{':
                    buf.append('(');
                    break;
                case ',':
                    buf.append('|');
                    break;
                case '}':
                    buf.append(')');
                    break;
                case '[':
                    buf.append('[');
                    break;
                case ']':
                    buf.append(']');
                    break;
                case '.':
                case '\\':
                case '+':
                case '?':
                case '$':
                case '^':
                case '|':
                case '(':
                case ')':
                    buf.append('\\');
                default:
                    buf.append(c);
                    break;
                }
            }
            buf.append('$');
            re = new RE(buf.toString());
        } catch (RESyntaxException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
    }

    public String getFilename() {
        return filename;
    }

    public void load() {
        isLoaded = true;
        ModeReader reader = new ModeReader(new ModeSyntaxListener());
        reader.read("modes/" + filename); //$NON-NLS-1$
    }

    public boolean notLoaded() {
        return !isLoaded;
    }

    public void appendExtensionsOnto(Set<String> extensions) {
        if (filenameGlob == null) {
            return;
        }
        StringBuffer buf = new StringBuffer();
        Stack<Character> charClass = new Stack<Character>();
        boolean inCharClass = false;
        boolean charClassBefore = false;
        for (int i = 0; i < filenameGlob.length(); i++) {
            char c = filenameGlob.charAt(i);
            switch (c) {
            case '*':
            case '.':
            case '{':
            case '}':
                break;
            case '[':
                inCharClass = true;
                charClassBefore = buf.length() == 0;
                break;
            case ']':
                inCharClass = false;
                break;
            case ',':
                extensions.add(buf.toString());
                buf.setLength(0);
                break;
            default:
                if (inCharClass) {
                    charClass.push(new Character(c));
                    break;
                }
                buf.append(c);
                break;
            }
        }
        if (charClass.size() > 0) {
            String suffixOrPrefix = buf.toString();
            buf.setLength(0);
            while (!charClass.isEmpty()) {
                Character ch = (Character) charClass.pop();
                extensions.add(charClassBefore ? ch + suffixOrPrefix : suffixOrPrefix + ch);
            }
            return;
        }
        if (buf.length() > 0) {
            extensions.add(buf.toString());
        }
    }

}
