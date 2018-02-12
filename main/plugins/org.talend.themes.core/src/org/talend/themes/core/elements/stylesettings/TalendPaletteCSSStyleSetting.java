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
package org.talend.themes.core.elements.stylesettings;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

/**
 * created by cmeng on Jan 30, 2015 Detailled comment
 *
 */
public class TalendPaletteCSSStyleSetting extends CommonCSSStyleSetting {

    protected Color mouseOverForgroundColor1;

    protected Color mouseOverForgroundColor2;

    protected Color mouseOverForgroundColor3;

    protected Color mouseOverBackgroundColor1;

    protected Color mouseOverBackgroundColor2;

    protected Color mouseOverBackgroundColor3;

    protected Color expandedBackgroundColor;

    protected Color collapsedForgroundColor;

    protected Color collapsedBackgroundColor;

    protected Color listBackgroundColor;

    protected int colorIncrement;

    protected int xOffset;

    protected Border titleMarginBorder;

    protected Color fgColor;

    protected Border scrollPaneBorder;

    protected Border scrollPaneListBorder;

    protected Color sliderPaletteForgroundColor;

    protected Color sliderPaletteBackgroundColor;

    protected boolean showFolderImage;

    protected int entryEditPartArrowWidth;

    protected Border entryEditPartListBorder;

    protected Border entryEditPartIconBorder;

    protected Color entryEditPartToolEntryForgroundDisabledColor;

    protected Color entryEditPartToolEntrySelectedBackgroundColor;

    protected Color entryEditPartToolEntryHoverBackgroundColor;

    protected Color entryEditPartToolEntryBorderFocusForgroundColor;

    protected Color entryEditPartToolEntryBorderFocusBackgroundColor;

    protected Color drawerFigureBaseColor;

    protected Color collapseTopBorderForgroundLineColor1;

    protected Color collapseTopBorderForgroundLineColor2;

    protected Color collapseExpandedLineForgroundColor;

    protected Color collapseNotExpandedLineForgroundColor;

    protected Color entryEditPartToolEntryTopLineColor;

    protected Color entryEditPartToolEntryBottomLineColor;

    protected Color entryEditPartToolEntryBackgroundColor;

    protected boolean entryEditPartBackgroundColorInheritFromParent;

    protected Image searchButtonImage;

    protected Color searchButtonBackgroundColor;

    public TalendPaletteCSSStyleSetting() {
        resetPaletteCSSStyle();
    }

    public void resetPaletteCSSStyle() {

        mouseOverForgroundColor1 = getColorByRGB(new RGB(0xAD, 0xAD, 0xAD));
        mouseOverForgroundColor2 = getColorByRGB(new RGB(0xAD, 0xAD, 0xAD));
        mouseOverForgroundColor3 = getColorByRGB(new RGB(0xAD, 0xAD, 0xAD));
        mouseOverBackgroundColor1 = getColorByRGB(new RGB(0xAD, 0xAD, 0xAD));
        mouseOverBackgroundColor2 = getColorByRGB(new RGB(0xAD, 0xAD, 0xAD));
        mouseOverBackgroundColor3 = getColorByRGB(new RGB(0xAD, 0xAD, 0xAD));
        expandedBackgroundColor = getColorByRGB(ColorConstants.listBackground.getRGB());
        collapsedForgroundColor = getColorByRGB(new RGB(0xFF, 0xFF, 0xFF));
        collapsedBackgroundColor = getColorByRGB(new RGB(0xFF, 0xFF, 0xFF));
        colorIncrement = 10;
        xOffset = 17;
        titleMarginBorder = new MarginBorder(4, 10, 2, 2);
        fgColor = getColorByRGB(ColorConstants.button.getRGB());
        scrollPaneBorder = new MarginBorder(0, 0, 0, 0);
        scrollPaneListBorder = new MarginBorder(0, 0, 0, 0);

        sliderPaletteForgroundColor = getColorByRGB(ColorConstants.listForeground.getRGB());
        sliderPaletteBackgroundColor = getColorByRGB(ColorConstants.listBackground.getRGB());
        showFolderImage = false;

        entryEditPartArrowWidth = 9;
        // int base = 5;
        // entryEditPartIconBorder = new MarginBorder(4, base, 3, entryEditPartArrowWidth + base);
        // entryEditPartListBorder = new MarginBorder(3, entryEditPartArrowWidth + base + 3, 4, 0);
        entryEditPartIconBorder = new MarginBorder(3, 3, 3, 3);
        entryEditPartListBorder = new MarginBorder(3, 0, 3, 0);
        entryEditPartToolEntryForgroundDisabledColor = getColorByRGB(ColorConstants.gray.getRGB());
        entryEditPartToolEntrySelectedBackgroundColor = getColorByRGB(new RGB(207, 227, 250));
        entryEditPartToolEntryHoverBackgroundColor = getColorByRGB(new RGB(252, 228, 179));
        entryEditPartToolEntryBorderFocusForgroundColor = getColorByRGB(ColorConstants.red.getRGB());
        entryEditPartToolEntryBorderFocusBackgroundColor = getColorByRGB(ColorConstants.green.getRGB());

        drawerFigureBaseColor = getColorByRGB(ColorConstants.button.getRGB());

        entryEditPartToolEntryTopLineColor = getColorByRGB(new RGB(0xEC, 0xF0, 0xF1));
        entryEditPartToolEntryBottomLineColor = getColorByRGB(new RGB(0xEC, 0xF0, 0xF1));
        entryEditPartToolEntryBackgroundColor = getColorByRGB(new RGB(0xFF, 0xFF, 0xFF));
        entryEditPartBackgroundColorInheritFromParent = true;

        collapseTopBorderForgroundLineColor1 = getColorByRGB(new RGB(0xEC, 0xF0, 0xF1));
        collapseTopBorderForgroundLineColor2 = getColorByRGB(new RGB(0xFF, 0xFF, 0xFF));
        collapseExpandedLineForgroundColor = getColorByRGB(new RGB(0xFF, 0xFF, 0xFF));
        collapseNotExpandedLineForgroundColor = getColorByRGB(new RGB(0xFF, 0xFF, 0xFF));

        searchButtonImage = null;
    }

    public static Color getSubColor(Color color, int increment) {
        Color newColor = null;

        if (color != null) {
            newColor = CommonCSSStyleSetting.getColorByRGB(new RGB(getNewValue(color.getRed(), increment), getNewValue(
                    color.getGreen(), increment), getNewValue(color.getBlue(), increment)));
        }

        return newColor;
    }

    private static int getNewValue(int oldValue, int increment) {
        int result = oldValue - increment;
        return (result > 0 ? result : 0);
    }

    /**
     * Getter for fgColor.
     * 
     * @return the fgColor
     */
    public Color getFgColor() {
        return this.fgColor;
    }

    /**
     * Sets the fgColor.
     * 
     * @param fgColor the fgColor to set
     */
    public void setFgColor(Color fgColor) {

        this.fgColor = fgColor;
    }

    /**
     * Getter for scrollPaneBorder.
     * 
     * @return the scrollPaneBorder
     */
    public Border getScrollPaneBorder() {
        return this.scrollPaneBorder;
    }

    /**
     * Sets the scrollPaneBorder.
     * 
     * @param scrollPaneBorder the scrollPaneBorder to set
     */
    public void setScrollPaneBorder(Border scrollPaneBorder) {
        copyBorderSetting(scrollPaneBorder, this.scrollPaneBorder);
    }

    public void setScrollPaneBorderInstance(Border scrollPaneBorder) {
        this.scrollPaneBorder = scrollPaneBorder;
    }

    /**
     * Getter for scrollPaneListBorder.
     * 
     * @return the scrollPaneListBorder
     */
    public Border getScrollPaneListBorder() {
        return this.scrollPaneListBorder;
    }

    /**
     * Sets the scrollPaneListBorder.
     * 
     * @param scrollPaneListBorder the scrollPaneListBorder to set
     */
    public void setScrollPaneListBorder(Border scrollPaneListBorder) {
        copyBorderSetting(scrollPaneListBorder, this.scrollPaneListBorder);
    }

    public void setScrollPaneListBorderInstance(Border scrollPaneListBorder) {
        this.scrollPaneListBorder = scrollPaneListBorder;
    }

    public Color getMouseOverForgroundColor1() {
        return this.mouseOverForgroundColor1;
    }

    public void setMouseOverForgroundColor1(Color mouseOverForgroundColor1) {

        this.mouseOverForgroundColor1 = mouseOverForgroundColor1;
    }

    public Color getMouseOverForgroundColor2() {
        return this.mouseOverForgroundColor2;
    }

    public void setMouseOverForgroundColor2(Color mouseOverForgroundColor2) {

        this.mouseOverForgroundColor2 = mouseOverForgroundColor2;
    }

    public Color getMouseOverForgroundColor3() {
        return this.mouseOverForgroundColor3;
    }

    public void setMouseOverForgroundColor3(Color mouseOverForgroundColor3) {

        this.mouseOverForgroundColor3 = mouseOverForgroundColor3;
    }

    public Color getMouseOverBackgroundColor1() {
        return this.mouseOverBackgroundColor1;
    }

    public void setMouseOverBackgroundColor1(Color mouseOverBackgroundColor1) {

        this.mouseOverBackgroundColor1 = mouseOverBackgroundColor1;
    }

    public Color getMouseOverBackgroundColor2() {
        return this.mouseOverBackgroundColor2;
    }

    public void setMouseOverBackgroundColor2(Color mouseOverBackgroundColor2) {

        this.mouseOverBackgroundColor2 = mouseOverBackgroundColor2;
    }

    public Color getMouseOverBackgroundColor3() {
        return this.mouseOverBackgroundColor3;
    }

    public void setMouseOverBackgroundColor3(Color mouseOverBackgroundColor3) {

        this.mouseOverBackgroundColor3 = mouseOverBackgroundColor3;
    }

    public Color getExpandedBackgroundColor() {
        return this.expandedBackgroundColor;
    }

    public void setExpandedBackgroundColor(Color expandedBackgroundColor) {

        this.expandedBackgroundColor = expandedBackgroundColor;
    }

    public Color getCollapsedForgroundColor() {
        return this.collapsedForgroundColor;
    }

    public void setCollapsedForgroundColor(Color collapsedForgroundColor) {

        this.collapsedForgroundColor = collapsedForgroundColor;
    }

    public Color getCollapsedBackgroundColor() {
        return this.collapsedBackgroundColor;
    }

    public void setCollapsedBackgroundColor(Color collapsedBackgroundColor) {

        this.collapsedBackgroundColor = collapsedBackgroundColor;
    }

    /**
     * Getter for listBackgroundColor.
     * 
     * @return the listBackgroundColor
     */
    public Color getListBackgroundColor() {
        return this.listBackgroundColor;
    }

    /**
     * Sets the listBackgroundColor.
     * 
     * @param listBackgroundColor the listBackgroundColor to set
     */
    public void setListBackgroundColor(Color listBackgroundColor) {

        this.listBackgroundColor = listBackgroundColor;
    }

    /**
     * Getter for colorIncrement.
     * 
     * @return the colorIncrement
     */
    public int getColorIncrement() {
        return this.colorIncrement;
    }

    /**
     * Sets the colorIncrement.
     * 
     * @param colorIncrement the colorIncrement to set
     */
    public void setColorIncrement(int colorIncrement) {
        this.colorIncrement = colorIncrement;
    }

    /**
     * Getter for titleMarginBorder.
     * 
     * @return the titleMarginBorder
     */
    public Border getTitleMarginBorder() {
        return this.titleMarginBorder;
    }

    /**
     * Sets the titleMarginBorder.
     * 
     * @param titleMarginBorder the titleMarginBorder to set
     */
    public void setTitleMarginBorder(Border titleMarginBorder) {
        copyBorderSetting(titleMarginBorder, this.titleMarginBorder);
    }

    public void setTitleMarginBorderInstance(Border titleMarginBorder) {
        this.titleMarginBorder = titleMarginBorder;
    }

    /**
     * Getter for xOffset.
     * 
     * @return the xOffset
     */
    public int getxOffset() {
        return this.xOffset;
    }

    /**
     * Sets the xOffset.
     * 
     * @param xOffset the xOffset to set
     */
    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Getter for stylingChangeListener.
     * 
     * @return the stylingChangeListener
     */
    // public List<ICSSStylingChangedListener> getStylingChangeListeners() {
    // return this.stylingChangeListeners;
    // }

    /**
     * Sets the stylingChangeListener.
     * 
     * @param stylingChangeListener the stylingChangeListener to set
     */
    // public void addStylingChangeListener(ICSSStylingChangedListener stylingChangeListener) {
    // if (!this.stylingChangeListeners.contains(stylingChangeListener)) {
    // this.stylingChangeListeners.add(stylingChangeListener);
    // }
    // }

    /**
     * Getter for sliderPaletteForgroundColor.
     * 
     * @return the sliderPaletteForgroundColor
     */
    public Color getSliderPaletteForgroundColor() {
        return this.sliderPaletteForgroundColor;
    }

    /**
     * Sets the sliderPaletteForgroundColor.
     * 
     * @param sliderPaletteForgroundColor the sliderPaletteForgroundColor to set
     */
    public void setSliderPaletteForgroundColor(Color sliderPaletteForgroundColor) {

        this.sliderPaletteForgroundColor = sliderPaletteForgroundColor;
    }

    /**
     * Getter for sliderPaletteBackgroundColor.
     * 
     * @return the sliderPaletteBackgroundColor
     */
    public Color getSliderPaletteBackgroundColor() {
        return this.sliderPaletteBackgroundColor;
    }

    /**
     * Sets the sliderPaletteBackgroundColor.
     * 
     * @param sliderPaletteBackgroundColor the sliderPaletteBackgroundColor to set
     */
    public void setSliderPaletteBackgroundColor(Color sliderPaletteBackgroundColor) {

        this.sliderPaletteBackgroundColor = sliderPaletteBackgroundColor;
    }

    /**
     * Getter for showFolderImage.
     * 
     * @return the showFolderImage
     */
    public boolean isShowFolderImage() {
        return this.showFolderImage;
    }

    /**
     * Sets the showFolderImage.
     * 
     * @param showFolderImage the showFolderImage to set
     */
    public void setShowFolderImage(boolean showFolderImage) {
        this.showFolderImage = showFolderImage;
    }

    /**
     * Getter for entryEditPartArrowWidth.
     * 
     * @return the entryEditPartArrowWidth
     */
    public int getEntryEditPartArrowWidth() {
        return this.entryEditPartArrowWidth;
    }

    /**
     * Sets the entryEditPartArrowWidth.
     * 
     * @param entryEditPartArrowWidth the entryEditPartArrowWidth to set
     */
    public void setEntryEditPartArrowWidth(int entryEditPartArrowWidth) {
        this.entryEditPartArrowWidth = entryEditPartArrowWidth;
    }

    /**
     * Getter for entryEditPartListBorder.
     * 
     * @return the entryEditPartListBorder
     */
    public Border getEntryEditPartListBorder() {
        return this.entryEditPartListBorder;
    }

    /**
     * Sets the entryEditPartListBorder.
     * 
     * @param entryEditPartListBorder the entryEditPartListBorder to set
     */
    public void setEntryEditPartListBorder(Border entryEditPartListBorder) {
        this.entryEditPartListBorder = entryEditPartListBorder;
    }

    /**
     * Getter for entryEditPartIconBorder.
     * 
     * @return the entryEditPartIconBorder
     */
    public Border getEntryEditPartIconBorder() {
        return this.entryEditPartIconBorder;
    }

    /**
     * Sets the entryEditPartIconBorder.
     * 
     * @param entryEditPartIconBorder the entryEditPartIconBorder to set
     */
    public void setEntryEditPartIconBorder(Border entryEditPartIconBorder) {
        this.entryEditPartIconBorder = entryEditPartIconBorder;
    }

    /**
     * Getter for entryEditPartToolEntryForgroundDisabledColor.
     * 
     * @return the entryEditPartToolEntryForgroundDisabledColor
     */
    public Color getEntryEditPartToolEntryForgroundDisabledColor() {
        return this.entryEditPartToolEntryForgroundDisabledColor;
    }

    /**
     * Sets the entryEditPartToolEntryForgroundDisabledColor.
     * 
     * @param entryEditPartToolEntryForgroundDisabledColor the entryEditPartToolEntryForgroundDisabledColor to set
     */
    public void setEntryEditPartToolEntryForgroundDisabledColor(Color entryEditPartToolEntryForgroundDisabledColor) {

        this.entryEditPartToolEntryForgroundDisabledColor = entryEditPartToolEntryForgroundDisabledColor;
    }

    /**
     * Getter for entryEditPartToolEntrySelectedBackgroundColor.
     * 
     * @return the entryEditPartToolEntrySelectedBackgroundColor
     */
    public Color getEntryEditPartToolEntrySelectedBackgroundColor() {
        return this.entryEditPartToolEntrySelectedBackgroundColor;
    }

    /**
     * Sets the entryEditPartToolEntrySelectedBackgroundColor.
     * 
     * @param entryEditPartToolEntrySelectedBackgroundColor the entryEditPartToolEntrySelectedBackgroundColor to set
     */
    public void setEntryEditPartToolEntrySelectedBackgroundColor(Color entryEditPartToolEntrySelectedBackgroundColor) {
        this.entryEditPartToolEntrySelectedBackgroundColor = entryEditPartToolEntrySelectedBackgroundColor;
    }

    /**
     * Getter for entryEditPartToolEntryHoverBackgroundColor.
     * 
     * @return the entryEditPartToolEntryHoverBackgroundColor
     */
    public Color getEntryEditPartToolEntryHoverBackgroundColor() {
        return this.entryEditPartToolEntryHoverBackgroundColor;
    }

    /**
     * Sets the entryEditPartToolEntryHoverBackgroundColor.
     * 
     * @param entryEditPartToolEntryHoverBackgroundColor the entryEditPartToolEntryHoverBackgroundColor to set
     */
    public void setEntryEditPartToolEntryHoverBackgroundColor(Color entryEditPartToolEntryHoverBackgroundColor) {

        this.entryEditPartToolEntryHoverBackgroundColor = entryEditPartToolEntryHoverBackgroundColor;
    }

    /**
     * Getter for entryEditPartToolEntryBorderFocusForgroundColor.
     * 
     * @return the entryEditPartToolEntryBorderFocusForgroundColor
     */
    public Color getEntryEditPartToolEntryBorderFocusForgroundColor() {
        return this.entryEditPartToolEntryBorderFocusForgroundColor;
    }

    /**
     * Sets the entryEditPartToolEntryBorderFocusForgroundColor.
     * 
     * @param entryEditPartToolEntryBorderFocusForgroundColor the entryEditPartToolEntryBorderFocusForgroundColor to set
     */
    public void setEntryEditPartToolEntryBorderFocusForgroundColor(Color entryEditPartToolEntryBorderFocusForgroundColor) {
        this.entryEditPartToolEntryBorderFocusForgroundColor = entryEditPartToolEntryBorderFocusForgroundColor;
    }

    /**
     * Getter for entryEditPartToolEntryBorderFocusBackgroundColor.
     * 
     * @return the entryEditPartToolEntryBorderFocusBackgroundColor
     */
    public Color getEntryEditPartToolEntryBorderFocusBackgroundColor() {
        return this.entryEditPartToolEntryBorderFocusBackgroundColor;
    }

    /**
     * Sets the entryEditPartToolEntryBorderFocusBackgroundColor.
     * 
     * @param entryEditPartToolEntryBorderFocusBackgroundColor the entryEditPartToolEntryBorderFocusBackgroundColor to
     * set
     */
    public void setEntryEditPartToolEntryBorderFocusBackgroundColor(Color entryEditPartToolEntryBorderFocusBackgroundColor) {
        this.entryEditPartToolEntryBorderFocusBackgroundColor = entryEditPartToolEntryBorderFocusBackgroundColor;
    }

    /**
     * Getter for drawerFigureBaseColor.
     * 
     * @return the drawerFigureBaseColor
     */
    public Color getDrawerFigureBaseColor() {
        return this.drawerFigureBaseColor;
    }

    /**
     * Sets the drawerFigureBaseColor.
     * 
     * @param drawerFigureBaseColor the drawerFigureBaseColor to set
     */
    public void setDrawerFigureBaseColor(Color drawerFigureBaseColor) {

        this.drawerFigureBaseColor = drawerFigureBaseColor;
    }

    /**
     * Getter for collapseTopBorderForgroundLineColor1.
     * 
     * @return the collapseTopBorderForgroundLineColor1
     */
    public Color getCollapseTopBorderForgroundLineColor1() {
        return this.collapseTopBorderForgroundLineColor1;
    }

    /**
     * Sets the collapseTopBorderForgroundLineColor1.
     * 
     * @param collapseTopBorderForgroundLineColor1 the collapseTopBorderForgroundLineColor1 to set
     */
    public void setCollapseTopBorderForgroundLineColor1(Color collapseTopBorderForgroundLineColor1) {

        this.collapseTopBorderForgroundLineColor1 = collapseTopBorderForgroundLineColor1;
    }

    /**
     * Getter for collapseTopBorderForgroundLineColor2.
     * 
     * @return the collapseTopBorderForgroundLineColor2
     */
    public Color getCollapseTopBorderForgroundLineColor2() {
        return this.collapseTopBorderForgroundLineColor2;
    }

    /**
     * Sets the collapseTopBorderForgroundLineColor2.
     * 
     * @param collapseTopBorderForgroundLineColor2 the collapseTopBorderForgroundLineColor2 to set
     */
    public void setCollapseTopBorderForgroundLineColor2(Color collapseTopBorderForgroundLineColor2) {

        this.collapseTopBorderForgroundLineColor2 = collapseTopBorderForgroundLineColor2;
    }

    /**
     * Getter for collapseExpandedLineForgroundColor.
     * 
     * @return the collapseExpandedLineForgroundColor
     */
    public Color getCollapseExpandedLineForgroundColor() {
        return this.collapseExpandedLineForgroundColor;
    }

    /**
     * Sets the collapseExpandedLineForgroundColor.
     * 
     * @param collapseExpandedLineForgroundColor the collapseExpandedLineForgroundColor to set
     */
    public void setCollapseExpandedLineForgroundColor(Color collapseExpandedLineForgroundColor) {

        this.collapseExpandedLineForgroundColor = collapseExpandedLineForgroundColor;
    }

    /**
     * Getter for collapseNotExpandedLineForgroundColor.
     * 
     * @return the collapseNotExpandedLineForgroundColor
     */
    public Color getCollapseNotExpandedLineForgroundColor() {
        return this.collapseNotExpandedLineForgroundColor;
    }

    /**
     * Sets the collapseNotExpandedLineForgroundColor.
     * 
     * @param collapseNotExpandedLineForgroundColor the collapseNotExpandedLineForgroundColor to set
     */
    public void setCollapseNotExpandedLineForgroundColor(Color collapseNotExpandedLineForgroundColor) {

        this.collapseNotExpandedLineForgroundColor = collapseNotExpandedLineForgroundColor;
    }

    /**
     * Getter for entryEditPartToolEntryTopLineColor.
     * 
     * @return the entryEditPartToolEntryTopLineColor
     */
    public Color getEntryEditPartToolEntryTopLineColor() {
        return this.entryEditPartToolEntryTopLineColor;
    }

    /**
     * Sets the entryEditPartToolEntryTopLineColor.
     * 
     * @param entryEditPartToolEntryTopLineColor the entryEditPartToolEntryTopLineColor to set
     */
    public void setEntryEditPartToolEntryTopLineColor(Color entryEditPartToolEntryTopLineColor) {
        this.entryEditPartToolEntryTopLineColor = entryEditPartToolEntryTopLineColor;
    }

    /**
     * Getter for entryEditPartToolEntryBottomLineColor.
     * 
     * @return the entryEditPartToolEntryBottomLineColor
     */
    public Color getEntryEditPartToolEntryBottomLineColor() {
        return this.entryEditPartToolEntryBottomLineColor;
    }

    /**
     * Sets the entryEditPartToolEntryBottomLineColor.
     * 
     * @param entryEditPartToolEntryBottomLineColor the entryEditPartToolEntryBottomLineColor to set
     */
    public void setEntryEditPartToolEntryBottomLineColor(Color entryEditPartToolEntryBottomLineColor) {
        this.entryEditPartToolEntryBottomLineColor = entryEditPartToolEntryBottomLineColor;
    }

    /**
     * Getter for entryEditPartToolEntryBackgroundColor.
     * 
     * @return the entryEditPartToolEntryBackgroundColor
     */
    public Color getEntryEditPartToolEntryBackgroundColor() {
        return this.entryEditPartToolEntryBackgroundColor;
    }

    /**
     * Sets the entryEditPartToolEntryBackgroundColor.
     * 
     * @param entryEditPartToolEntryBackgroundColor the entryEditPartToolEntryBackgroundColor to set
     */
    public void setEntryEditPartToolEntryBackgroundColor(Color entryEditPartToolEntryBackgroundColor) {
        this.entryEditPartToolEntryBackgroundColor = entryEditPartToolEntryBackgroundColor;
    }

    /**
     * Getter for entryEditPartBackgroundColorInheritFromParent.
     * 
     * @return the entryEditPartBackgroundColorInheritFromParent
     */
    public boolean isEntryEditPartBackgroundColorInheritFromParent() {
        return this.entryEditPartBackgroundColorInheritFromParent;
    }

    /**
     * Sets the entryEditPartBackgroundColorInheritFromParent.
     * 
     * @param entryEditPartBackgroundColorInheritFromParent the entryEditPartBackgroundColorInheritFromParent to set
     */
    public void setEntryEditPartBackgroundColorInheritFromParent(boolean entryEditPartBackgroundColorInheritFromParent) {
        this.entryEditPartBackgroundColorInheritFromParent = entryEditPartBackgroundColorInheritFromParent;
    }

    /**
     * Getter for searchButtonImage.
     * 
     * @return the searchButtonImage
     */
    public Image getSearchButtonImage() {
        return this.searchButtonImage;
    }

    /**
     * Sets the searchButtonImage.
     * 
     * @param searchButtonImage the searchButtonImage to set
     */
    public void setSearchButtonImage(Image searchButtonImage) {
        this.searchButtonImage = searchButtonImage;
    }

    /**
     * Getter for searchButtonBackgroundColor.
     * 
     * @return the searchButtonBackgroundColor
     */
    public Color getSearchButtonBackgroundColor() {
        return this.searchButtonBackgroundColor;
    }

    /**
     * Sets the searchButtonBackgroundColor.
     * 
     * @param searchButtonBackgroundColor the searchButtonBackgroundColor to set
     */
    public void setSearchButtonBackgroundColor(Color searchButtonBackgroundColor) {
        this.searchButtonBackgroundColor = searchButtonBackgroundColor;
    }

}
