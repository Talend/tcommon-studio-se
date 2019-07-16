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
package org.talend.utils.format;

/**
 * @author scorreia
 *
 * This utility class facilitates the creation of title boxes. Code adapted from J. Jean-Charles.
 *
 * <code>
 *  <br>CST_PAR: !!----------------------------!!
 *  <br>CST_PAR: !!                            !!
 *  <br>CST_PAR: !!          MAIN  TITLE       !!
 *  <br>CST_PAR: !!          subtitle          !!
 *  <br>CST_PAR: !!                            !!
 *  <br>CST_PAR: !!----------------------------!!
 *  </code>
 *
 */
public final class PresentableBox {

    /** constant at start and end. */
    private static final String ENCAP = "!!";

    /** carriage return (for lisibility only). */
    private static final String CR = "\n";

    /** for presentation only. */
    private static final String DASH = "-";

    /** for presentation only. */
    private static final String SPACE = " ";

    /** for presentation only. */
    private static final int MAX_BOX_SIZE = 256;

    /** for presentation only this optimize repetition. */
    private static final String MANY_DASHES = makeRepeat(DASH, MAX_BOX_SIZE);

    /** for presentation only this optimize repetition. */
    private static final String MANY_SPACES = makeRepeat(SPACE, MAX_BOX_SIZE);

    /**
     * @param in the char to be repeated in returned string
     * @param nbFold number of times we want 'in' to be repeated in returned value
     * @return 'in' concatenated nbTimes (for presentation only )
     */
    private static String makeRepeat(final String in, final int nbFold) {
        String ret = "";
        for (int i = 0; i < nbFold; i++) {
            ret += in;
        }
        return ret;
    }

    /** in the middle of box. */
    private final String title;

    /** in the middle of box. */
    private final String subTitle;

    /** in the middle of box. */
    private final int boxSize;

    /** top Of box. */
    private final String topLine;

    /** in the middle of box. */
    private final String titleLine;

    /** in the middle of box. */
    private final String subTitleLine;

    /** in the middle of box. */
    private final String bottomLine;

    /** in the middle of box. */
    private final String emptyLine;

    /** @return as its name says */
    public String getTitleLine() {
        return titleLine;
    }

    /** @return as its name says */
    public String getTopLine() {
        return topLine;
    }

    /** @return as its name says */
    public String getBottomLine() {
        return bottomLine;
    }

    /** @return as its name says */
    public String getEmptyLine() {
        return emptyLine;
    }

    /** @return as its name says */
    public String getSubTitleLine() {
        return subTitleLine;
    }

    /**
     * <br>
     * An example of return is like this <br>
     * <code>
     * 	<br>final String tokPar = "\nCST_PAR: " ; // on each line
     * 	<br>final PresentableBox box = new PresentableBox("PARAMS CONSTANT DECS" , "MINORTEST" , 30) ;
     *  <br>System.out.println(box.getFullBox(tokPar));
     *  <br>CST_PAR: !!----------------------------!!
     *	<br>CST_PAR: !!							   !!
     *	<br>CST_PAR: !!    PARAMS CONSTANT DECS    !!
     *	<br>CST_PAR: !!         MINORTEST          !!
     *	<br>CST_PAR: !!                            !!
     *	<br>CST_PAR: !!----------------------------!!
     *  </code>
     *
     * @param startTokAtEachLine added in front each line
     * @return a full box : in order to + empty + title + subtitle + empty + bottom with token at start of each lines
     */
    public String getFullBox(final String startTokAtEachLine) {
        String tmp = (startTokAtEachLine == null) ? "" : startTokAtEachLine;
        final String tokPar = (!tmp.startsWith(CR)) ? CR + tmp : tmp;
        String ret = "";
        ret += tokPar + getTopLine();
        ret += tokPar + getEmptyLine();
        ret += tokPar + getTitleLine();
        ret += tokPar + getSubTitleLine();
        ret += tokPar + getEmptyLine();
        ret += tokPar + getBottomLine();
        return ret;
    } // eom getFullBox

    /**
     * Method "getFullBox".
     *
     * @return the boxed presentation string without any token in front of each line.
     * @see this.{@link #getFullBox(String)}
     */
    public String getFullBox() {
        return getFullBox("");
    } // eom getFullBox

    /**
     * @param aTitle will be centered
     * @param aSubTitle will be centered
     * @param aBoxSize al together
     */
    public PresentableBox(final String aTitle, final String aSubTitle, final int aBoxSize) {
        this.title = (aTitle == null) ? "" : aTitle.trim();
        this.subTitle = (aSubTitle == null) ? "" : aSubTitle.trim();
        final int maxTextSize = Math.max(title.length(), subTitle.length());

        final String encapers = ENCAP;
        final int lgAddedEncap = 2 * encapers.length(); // TOTAL
        this.boxSize = Math.max(aBoxSize + aBoxSize % 2, maxTextSize + 2 * lgAddedEncap); // must be even
        final String transparent = SPACE + SPACE;
        this.topLine = surroundFill(DASH, encapers, DASH + DASH, boxSize);
        this.bottomLine = topLine;
        this.emptyLine = surroundFill(SPACE, encapers, transparent, boxSize);
        this.titleLine = surroundFill(SPACE, encapers, title, boxSize);
        this.subTitleLine = surroundFill(SPACE, encapers, subTitle, boxSize);
    }

    /**
     * @param arround at start and end
     * @param center is in middle
     * @return arround + center + arround
     */
    private static String surroundFill(final String filler, final String startEnd, final String center,
            final int totalSize) {
        final int totFillers = totalSize - center.length() - startEnd.length();
        final String filler1 = getFiller(filler).substring(0, totFillers / 2);
        final String filler2 = getFiller(filler).substring(0, totFillers - (totFillers / 2));
        return startEnd + filler1 + center + filler2 + startEnd;
    }

    private static String getFiller(final String filler) {
        if (filler == null) {
            return MANY_SPACES;
        }
        if (filler.length() == 0) {
            return MANY_SPACES;
        }
        if (filler.charAt(0) == '-') {
            return MANY_DASHES;
        }
        return MANY_SPACES;
    }

} // EOC PresentableBox

