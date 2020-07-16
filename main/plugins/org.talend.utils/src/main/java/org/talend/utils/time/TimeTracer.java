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
package org.talend.utils.time;

import org.slf4j.Logger;

/**
 * @author scorreia
 *
 *
 * Simple timer to trace time taken when calling a piece of code. Nesting the calls is possible : indentation of traces.
 * if done automatically NOTE that it is the user responsability to produce matching statement.
 *
 * EXAMPLE OF USAGE :
 *
 * TimeTracer timeTracer = new TimeTracer( "BASE" , logger) ; // at object level //
 * <p>
 * also new TimeTracer( "BASE" , null ) ; // log on console only
 *
 *
 * int nbTest = 10 ; timeTracer.start(" ALL") ; // nested level 0 for ( int i = 0 ; i < nbTest ; i++ ) {
 * timeTracer.start(" INS") ; // nested level 1 mytest() ; timeTracer.end( " ONE" ) ; // nested level 1 }
 * timeTracer.end( " FIN ALL" , nbTest ) ; // nested level 0
 *
 * Adapted from a code from Jerome JEAN-CHARLES.
 */
public class TimeTracer {

    private static final int MAX = 20;

    private static final String EMPTY = "";

    private static final String SPACE = " ";

    private Logger log;

    private String name;

    private int idx = 0;

    private long[] starts = new long[MAX];

    private static String[] textIndentations = makeTextIndentations(); // offsets used for indenting messages

    private static String[] makeTextIndentations() {
        String[] indentations = new String[MAX];
        String m = EMPTY;
        for (int i = 0; i < MAX; i++) {
            indentations[i] = m;
            m += "\t";
        }
        return indentations;
    }

    /**
     * CST.
     *
     * @param aName : could be null. If not, it will be printed in front of each trace of this TimeTracer.
     * @param aLog : if null then trace shall be done on System.out
     */
    public TimeTracer(String aName, Logger aLog) {
        this.log = aLog;
        this.name = (aName != null) ? aName : EMPTY;
    }

    /** if the user is not confident in his/her nesting he might use this to reinitialise. */
    public void reset() {
        idx = 0;
    }

    /** see #method end(text). */
    public void start() {
        start(EMPTY);
    }

    /**
     * If text not empty (null or length 0 ) : - will print the text and the time of call ( with indentation
     * corresponding to nesting else - will only remember calling time to show it when end() is called.
     *
     * @param text marker from caller
     */
    public void start(String text) {
        String msg = textIndentations[idx]; // INCREMENT before incrementing SVP see DECREMENT
        starts[idx] = System.currentTimeMillis();
        if (idx < MAX - 1) {
            idx++;
        }
        if ((text == null) || (text.length() == 0)) {
            return;
        }

        msg += "ZZ***B:" + name + SPACE + text; // B as begin
        show(msg);
    } // eof start

    /**
     * @param nbTest the number of test caller performed since the start() call
     * @return the end time spend since last call to start of this timer
     */
    public long end(int nbTest) {
        return end(EMPTY, nbTest);
    }

    /** @return the end time spend since last call to start of this timer */
    public long end() {
        return end(EMPTY, 1);
    }

    /**
     * @param text text to show the number of test will be assumed to be 1
     * @return the end time spend since last call to start of this timer
     */
    public long end(String text) {
        return end(text, 1);
    }

    /**
     * if text not empty (null or no length) will print the <code>text</code> will print the name of the class will
     * show the time elapsed with eventually average if nbTest significant (>=2) prints are done according to the logger
     * if any otherwise using System.println.
     *
     * @param text marker from caller
     * @param nbTest number of test ( or call) from caller used for averaging
     * @return the spent time.
     */
    public long end(String text, int nbTest) {

        if (idx > 0) {
            idx--;
        }
        String msg = textIndentations[idx]; // DECREMENT after decrementing SVP see INCREMENT
        long beginTime = starts[idx];
        long spent = System.currentTimeMillis() - beginTime;

        msg += "ZZ***E:" + name + SPACE; // E as end
        if ((text != null) && (text.length() != 0)) {
            msg = msg + text;
        }

        if (nbTest >= 2) {
            msg = msg + " nbTest=" + nbTest + " spent=" + spent + " ms" + " avg=" + spent / nbTest + " begin was"
                    + beginTime;
        } else {
            msg = msg + " spent=" + spent + " ms";
        }
        show(msg);
        return spent;
    } // eof endTime

    /** Internal to print with System or with logger: depending on construction. */
    private void show(String msg) {
        if (log != null) {
            log.info(msg);
        } else {
            System.out.println(msg);
        }
    }

} // EOC TimeTracer
