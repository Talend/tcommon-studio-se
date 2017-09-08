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
package org.talend.commons.ui.swt.geftree.figure;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.geftree.ITreeAction;
import org.talend.commons.ui.swt.geftree.TreeAnimation;
import org.talend.commons.ui.swt.geftree.TreeSelectManager;
import org.talend.commons.ui.swt.geftree.layout.TreeAnimatingLayer;
import org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout;
import org.talend.commons.ui.swt.geftree.layout.TreeHangingLayout;
import org.talend.commons.ui.swt.geftree.layout.TreeLayout;
import org.talend.commons.ui.swt.geftree.layout.TreeNormalLayout;

/**
 * cli class global comment. Detailled comment
 */
@SuppressWarnings("unchecked")
public class TreeBranch extends Figure {

    public static final int STYLE_HANGING = 1;

    public static final int STYLE_NORMAL = 2;

    private int aligment = PositionConstants.CENTER;

    private TreeAnimatingLayer contents;

    private boolean expanded = true;

    private IFigure element;

    private ExpandCollapseFigure expandCollapseFigure;

    private ImageFigure titleImage;

    private int style;

    private LineBorder border;

    private final TreeSelectManager selectManager = TreeSelectManager.getManager();

    private final Color backgroundColor = ColorConstants.white;

    private boolean animate = true;;

    private boolean enableExpand;

    public TreeBranch(IFigure title) {
        this(title, STYLE_HANGING);
    }

    public TreeBranch(IFigure title, int style) {
        this(title, style, true);
    }

    public TreeBranch(IFigure title, boolean enableExpand) {
        this(title, STYLE_HANGING, enableExpand);
    }

    public TreeBranch(IFigure title, int style, boolean enableExpand) {
        setStyle(style);
        this.enableExpand = enableExpand;
        this.element = title;

        createExpandCollapse();
        createTitleImage();

        add(getContents());
        add(title);

        this.border = new LineBorder();
        title.setBorder(border);

        initTree();
    }

    private void createExpandCollapse() {
        if (isEnableExpand()) {
            this.expandCollapseFigure = new ExpandCollapseFigure(this);
            add(this.expandCollapseFigure);
        }
    }

    private void createTitleImage() {
        if (getElement() instanceof ITreeAction) {
            Image elementImage = ((ITreeAction) getElement()).getElementImage();
            if (elementImage != null && !elementImage.isDisposed()) {
                this.titleImage = new ImageFigure(elementImage);
                add(this.titleImage);
            }
        }
    }

    public boolean isEnableExpand() {
        return this.enableExpand;
    }

    public void setEnableExpand(boolean enableExpand) {
        this.enableExpand = enableExpand;
    }

    private void initTree() {
        setBorderVisible(false);
        updateExpandCollapseState(true);
        if (enableSelect()) {
            addSelectListener(element);
        }

        if (enableDoubleClick()) {
            element.addMouseListener(new MouseListener.Stub() {

                @Override
                public void mouseDoubleClicked(MouseEvent me) {
                    doExpandCollapse();
                }
            });
        }
        addHoverListener(getElement());

    }

    private void addSelectListener(final IFigure parentFig) {
        if (parentFig instanceof ITreeAction && ((ITreeAction) parentFig).enableSelect()) {
            parentFig.addMouseListener(new MouseListener.Stub() {

                public void mousePressed(MouseEvent me) {
                    selectManager.setSelection(TreeBranch.this);
                }
            });
            // children
            for (Object o : parentFig.getChildren()) {
                if (o instanceof IFigure) {
                    addSelectListener((IFigure) o);
                }
            }
        }
    }

    private void addHoverListener(final IFigure colFigure) {
        if (colFigure instanceof ITreeAction && ((ITreeAction) colFigure).enableHover()) {
            colFigure.addMouseMotionListener(new MouseMotionListener.Stub() {

                @Override
                public void mouseHover(MouseEvent me) {
                    //
                }

                @Override
                public void mouseEntered(MouseEvent me) {
                    setHoverColor(colFigure, true);
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    setHoverColor(colFigure, false);
                }
            });
        }
        // children
        for (Object o : colFigure.getChildren()) {
            if (o instanceof IFigure) {
                addHoverListener((IFigure) o);
            }
        }
    }

    public void setHoverColor(final IFigure fig, boolean enter) {
        if (fig instanceof ITreeAction) {
            ((ITreeAction) fig).setHoverColor(fig, enter);
        }
    }

    public TreeAnimatingLayer getContents() {
        if (this.contents == null) {
            this.contents = new TreeAnimatingLayer();
        }
        return this.contents;
    }

    public boolean isAnimate() {
        return this.animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    protected TreeSelectManager getSelectManager() {
        return this.selectManager;
    }

    /**
     * recursively set all nodes and sub-treebranch nodes to the same location. This gives the appearance of all nodes
     * coming from the same place.
     * 
     * @param bounds where to set
     */
    public void animationReset(Rectangle bounds) {
        List subtrees = getContents().getChildren();
        getContents().setBounds(bounds);

        // Make the center of this node match the center of the given bounds
        Rectangle r = element.getBounds();
        int dx = bounds.x + bounds.width / 2 - r.x - r.width / 2;
        int dy = bounds.y + bounds.height / 2 - r.y - r.height / 2;
        getElement().translate(dx, dy);
        revalidate(); // Otherwise, this branch will not layout

        // Pass the location to all children
        for (int i = 0; i < subtrees.size(); i++) {
            TreeBranch subtree = (TreeBranch) subtrees.get(i);
            subtree.setBounds(bounds);
            subtree.animationReset(bounds);
        }
    }

    public void collapse() {
        if (!expanded)
            return;

        IFigure root = this;
        Viewport port = null;
        Point viewportStart = null;
        while (root.getParent() != null) {
            if (root instanceof Viewport)
                port = ((Viewport) root);
            root = root.getParent();
        }
        viewportStart = port.getViewLocation();
        Point elementStart = getElement().getBounds().getLocation();
        setExpanded(false);
        root.validate();

        setExpanded(true);
        animationReset(getElementBounds());
        TreeAnimation.mark(getElement());
        TreeAnimation.captureLayout(getRoot());
        TreeAnimation.swap();
        TreeAnimation.trackLocation = elementStart;

        root.validate();
        port.setViewLocation(viewportStart);
        while (TreeAnimation.step())
            getUpdateManager().performUpdate();
        TreeAnimation.end();
        setExpanded(false);
    }

    /**
     * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
     */
    public boolean containsPoint(int x, int y) {
        return getElement().containsPoint(x, y) || getContents().containsPoint(x, y);
    }

    public void expand() {
        if (expanded)
            return;
        setExpanded(true);
        animationReset(getElementBounds());

        TreeAnimation.mark(getElement());
        TreeAnimation.captureLayout(getRoot());

        while (TreeAnimation.step())
            getUpdateManager().performUpdate();
        TreeAnimation.end();
    }

    public int getAlignment() {
        return aligment;
    }

    protected TreeBranchLayout getBranchLayout() {
        return (TreeBranchLayout) getLayoutManager();
    }

    public IFigure getContentsPane() {
        return getContents();
    }

    public ImageFigure getTitleImage() {
        return this.titleImage;
    }

    public int[] getContourLeft() {
        return getBranchLayout().getContourLeft();
    }

    public int[] getContourRight() {
        return getBranchLayout().getContourRight();
    }

    public int getDepth() {
        return getBranchLayout().getDepth();
    }

    /**
     * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
     */
    public Dimension getMinimumSize(int wHint, int hHint) {
        if (!TreeAnimation.PLAYBACK)
            validate();
        return super.getMinimumSize(wHint, hHint);
    }

    public IFigure getElement() {
        return element;
    }

    public IFigure getStartFigure() {
        ImageFigure titleImage = getTitleImage();
        if (titleImage != null) { // if have image, use this
            return titleImage;
        }
        return getElement();
    }

    public Rectangle getElementBounds() {
        return getElement().getBounds();
    }

    public ExpandCollapseFigure getExpandCollapseFigure() {
        return this.expandCollapseFigure;
    }

    public int[] getPreferredRowHeights() {
        return getBranchLayout().getPreferredRowHeights();
    }

    /**
     * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
     */
    public Dimension getPreferredSize(int wHint, int hHint) {
        if (!TreeAnimation.PLAYBACK)
            validate();
        return super.getPreferredSize(wHint, hHint);
    }

    public TreeRoot getRoot() {
        if (getParent() != null && getParent().getParent() != null) {
            return ((TreeBranch) getParent().getParent()).getRoot();
        }
        return null;
    }

    public int getStyle() {
        return style;
    }

    /**
     * @return
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        if (isExpanded())
            getBranchLayout().paintLines(graphics);
        // if (getDepth() == 2)
        // graphics.drawRectangle(getBounds().getResized(-1, -1));
    }

    public void setAlignment(int value) {
        aligment = value;
        revalidate();
    }

    /**
     * @param b
     */
    public void setExpanded(boolean b) {
        if (expanded == b)
            return;
        expanded = b;
        getContents().setVisible(b);
        revalidate();
        updateExpandCollapseState(b);
    }

    public void setNode(IFigure node) {
        remove(this.element);
        add(this.element, 0);
    }

    public void setRowHeights(int heights[], int offset) {
        getBranchLayout().setRowHeights(heights, offset);
    }

    public void setStyle(int style) {
        if (this.style == style)
            return;
        this.style = style;
        switch (style) {
        case STYLE_HANGING:
            setLayoutManager(new TreeHangingLayout(this));
            break;

        default:
            setLayoutManager(new TreeNormalLayout(this));
            getContents().setLayoutManager(new TreeLayout());
            break;
        }
    }

    public void setLineStyle(int lineStyle) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof TreeBranchLayout) {
            ((TreeBranchLayout) layoutManager).setBranchLineStyle(lineStyle);
        }
    }

    public void setLineColor(RGB color) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof TreeBranchLayout) {
            ((TreeBranchLayout) layoutManager).setBranchLineColor(color);
        }
    }

    public void doExpandCollapse() {
        if (!isEnableExpand()) {
            return;
        }
        if (selectManager.getSelection() == null) {
            return;
        }
        TreeBranch parent = (TreeBranch) selectManager.getSelection();
        if (parent.getContentsPane().getChildren().isEmpty()) {
            return;
        }
        if (isAnimate()) {
            if (parent.isExpanded()) {
                parent.collapse();
            } else {
                parent.expand();
            }
        } else {
            parent.setExpanded(!parent.isExpanded());
        }

    }

    public void doExpandCollapse(TreeBranch branch) {
        if (!isEnableExpand()) {
            return;
        }
        if (branch == null) {
            return;
        }
        if (branch.getContentsPane().getChildren().isEmpty()) {
            return;
        }
        if (isAnimate()) {
            if (branch.isExpanded()) {
                branch.collapse();
            } else {
                branch.expand();
            }
        } else {
            branch.setExpanded(!branch.isExpanded());
        }

    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return toString(0);
    }

    public String toString(int level) {
        String result = "";
        for (int i = 0; i < level; i++)
            result += "  ";
        result += getChildren().get(1) + "\n";
        for (int i = 0; i < getContents().getChildren().size(); i++)
            result += ((TreeBranch) getContents().getChildren().get(i)).toString(level + 1);
        return result;
    }

    /**
     * @see org.eclipse.draw2d.Figure#validate()
     */
    public void validate() {
        if (isValid())
            return;
        if (style == STYLE_HANGING) {
            ToolbarLayout layout = new ToolbarLayout(!getRoot().isHorizontal()) {

                public void layout(IFigure parent) {
                    TreeAnimation.recordInitialState(parent);
                    if (TreeAnimation.playbackState(parent))
                        return;

                    super.layout(parent);
                }

            };
            layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
            layout.setStretchMinorAxis(false);
            getContents().setLayoutManager(layout);
        }
        repaint();
        super.validate();
    }

    protected void setBorderVisible(boolean visible) {
        if (visible) {
            border.setColor(ColorConstants.blue);
        } else {
            border.setColor(backgroundColor);
        }
    }

    protected void updateExpandCollapseState(boolean expand) {
        if (isEnableExpand()) {
            if (expand) {
                this.expandCollapseFigure.setImage(ImageProvider.getImage(EImage.TREE_EXPAND));
            } else {
                this.expandCollapseFigure.setImage(ImageProvider.getImage(EImage.TREE_COLLAPSE));
            }
        }
    }

    public void setSelected(boolean selected) {
        setBorderVisible(selected);
        if (getElement() instanceof ITreeAction) {
            ((ITreeAction) getElement()).setSelected(selected);
        }
    }

    public boolean enableSelect() {
        if (getElement() instanceof ITreeAction) {
            return ((ITreeAction) getElement()).enableSelect();
        }
        return false;
    }

    public boolean enableDoubleClick() {
        if (getElement() instanceof ITreeAction) {
            return ((ITreeAction) getElement()).enableDoubleClick();
        }
        return false;
    }

    public boolean enableHover() {
        if (getElement() instanceof ITreeAction) {
            return ((ITreeAction) getElement()).enableHover();
        }
        return false;
    }

}
