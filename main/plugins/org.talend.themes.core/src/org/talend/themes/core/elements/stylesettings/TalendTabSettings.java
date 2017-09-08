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
package org.talend.themes.core.elements.stylesettings;

import java.util.HashMap;
import java.util.Map;

import net.jeeeyul.eclipse.themes.rendering.JTabSettings;
import net.jeeeyul.eclipse.themes.rendering.JeeeyulsTabRenderer;
import net.jeeeyul.swtend.ui.HSB;

import org.eclipse.swt.graphics.Image;

/**
 * created by cmeng on Feb 9, 2015 Detailled comment
 *
 */
public class TalendTabSettings extends JTabSettings {

    protected HSB minimizeButtonForgroundColor = null;

    protected HSB minimizeButtonBackgroundColor = null;

    protected Image minimizeButtonNoneStatueImage = null;

    protected Image minimizeButtonHotStatueImage = null;

    protected Image minimizeButtonSelectedStatueImage = null;

    protected HSB maximizeButtonForgroundColor = null;

    protected HSB maximizeButtonBackgroundColor = null;

    protected Image maximizeButtonNoneStatueImage = null;

    protected Image maximizeButtonHotStatueImage = null;

    protected Image maximizeButtonSelectedStatueImage = null;

    protected static Map<String, Image> cTabItemImageMap = new HashMap<String, Image>();

    /**
     * DOC cmeng TalendTabSettings constructor comment.
     * 
     * @param renderer
     */
    public TalendTabSettings(JeeeyulsTabRenderer renderer) {
        super(renderer);
    }

    public HSB getMinimizeButtonForgroundColor() {
        return this.minimizeButtonForgroundColor;
    }

    public HSB getMinimizeButtonBackgroundColor() {
        return this.minimizeButtonBackgroundColor;
    }

    public Image getMinimizeButtonNoneStatueImage() {
        return this.minimizeButtonNoneStatueImage;
    }

    public Image getMinimizeButtonHotStatueImage() {
        return this.minimizeButtonHotStatueImage;
    }

    public Image getMinimizeButtonSelectedStatueImage() {
        return this.minimizeButtonSelectedStatueImage;
    }

    public HSB getMaximizeButtonForgroundColor() {
        return this.maximizeButtonForgroundColor;
    }

    public HSB getMaximizeButtonBackgroundColor() {
        return this.maximizeButtonBackgroundColor;
    }

    public Image getMaximizeButtonNoneStatueImage() {
        return this.maximizeButtonNoneStatueImage;
    }

    public Image getMaximizeButtonHotStatueImage() {
        return this.maximizeButtonHotStatueImage;
    }

    public Image getMaximizeButtonSelectedStatueImage() {
        return this.maximizeButtonSelectedStatueImage;
    }

    public void setMinimizeButtonForgroundColor(HSB minimizeButtonForgroundColor) {
        if (areSame(this.minimizeButtonForgroundColor, minimizeButtonForgroundColor)) {
            return;
        }
        HSB old = this.minimizeButtonForgroundColor;
        this.minimizeButtonForgroundColor = minimizeButtonForgroundColor;
        // pcs.firePropertyChange("xxx", old,
        // this.minimizeButtonForgroundColor);
    }

    public void setMinimizeButtonBackgroundColor(HSB minimizeButtonBackgroundColor) {
        if (areSame(this.minimizeButtonBackgroundColor, minimizeButtonBackgroundColor)) {
            return;
        }
        HSB old = this.minimizeButtonBackgroundColor;
        this.minimizeButtonBackgroundColor = minimizeButtonBackgroundColor;
        // pcs.firePropertyChange("xxx", old,
        // this.minimizeButtonBackgroundColor);
    }

    public void setMinimizeButtonNoneStatueImage(Image minimizeButtonNoneStatueImage) {
        if (areSame(this.minimizeButtonNoneStatueImage, minimizeButtonNoneStatueImage)) {
            return;
        }
        Image old = this.minimizeButtonNoneStatueImage;
        this.minimizeButtonNoneStatueImage = minimizeButtonNoneStatueImage;
        // pcs.firePropertyChange("xxx", old,
        // this.minimizeButtonNoneStatueImage);
    }

    public void setMinimizeButtonHotStatueImage(Image minimizeButtonHotStatueImage) {
        if (areSame(this.minimizeButtonHotStatueImage, minimizeButtonHotStatueImage)) {
            return;
        }
        Image old = this.minimizeButtonHotStatueImage;
        this.minimizeButtonHotStatueImage = minimizeButtonHotStatueImage;
        // pcs.firePropertyChange("xxx", old,
        // this.minimizeButtonHotStatueImage);
    }

    public void setMinimizeButtonSelectedStatueImage(Image minimizeButtonSelectedStatueImage) {
        if (areSame(this.minimizeButtonSelectedStatueImage, minimizeButtonSelectedStatueImage)) {
            return;
        }
        Image old = this.minimizeButtonSelectedStatueImage;
        this.minimizeButtonSelectedStatueImage = minimizeButtonSelectedStatueImage;
        // pcs.firePropertyChange("xxx", old,
        // this.minimizeButtonSelectedStatueImage);
    }

    public void setMaximizeButtonForgroundColor(HSB maximizeButtonForgroundColor) {
        if (areSame(this.maximizeButtonForgroundColor, maximizeButtonForgroundColor)) {
            return;
        }
        HSB old = this.maximizeButtonForgroundColor;
        this.maximizeButtonForgroundColor = maximizeButtonForgroundColor;
        // pcs.firePropertyChange("xxx", old,
        // this.maximizeButtonForgroundColor);
    }

    public void setMaximizeButtonBackgroundColor(HSB maximizeButtonBackgroundColor) {
        if (areSame(this.maximizeButtonBackgroundColor, maximizeButtonBackgroundColor)) {
            return;
        }
        HSB old = this.maximizeButtonBackgroundColor;
        this.maximizeButtonBackgroundColor = maximizeButtonBackgroundColor;
        // pcs.firePropertyChange("xxx", old,
        // this.maximizeButtonBackgroundColor);
    }

    public void setMaximizeButtonNoneStatueImage(Image maximizeButtonNoneStatueImage) {
        if (areSame(this.maximizeButtonNoneStatueImage, maximizeButtonNoneStatueImage)) {
            return;
        }
        Image old = this.maximizeButtonNoneStatueImage;
        this.maximizeButtonNoneStatueImage = maximizeButtonNoneStatueImage;
        // pcs.firePropertyChange("xxx", old,
        // this.maximizeButtonNoneStatueImage);
    }

    public void setMaximizeButtonHotStatueImage(Image maximizeButtonHotStatueImage) {
        if (areSame(this.maximizeButtonHotStatueImage, maximizeButtonHotStatueImage)) {
            return;
        }
        Image old = this.maximizeButtonHotStatueImage;
        this.maximizeButtonHotStatueImage = maximizeButtonHotStatueImage;
        // pcs.firePropertyChange("xxx", old,
        // this.maximizeButtonHotStatueImage);
    }

    public void setMaximizeButtonSelectedStatueImage(Image maximizeButtonSelectedStatueImage) {
        if (areSame(this.maximizeButtonSelectedStatueImage, maximizeButtonSelectedStatueImage)) {
            return;
        }
        Image old = this.maximizeButtonSelectedStatueImage;
        this.maximizeButtonSelectedStatueImage = maximizeButtonSelectedStatueImage;
        // pcs.firePropertyChange("xxx", old,
        // this.maximizeButtonSelectedStatueImage);
    }

    public static void addCTabItemImage(String id, Image image) {
        if (id == null || image == null) {
            return;
        }
        cTabItemImageMap.put(id, image);
    }

    public static Image getCTabItemImage(String id) {
        if (id == null) {
            return null;
        }
        return cTabItemImageMap.get(id);
    }

    public static Map<String, Image> getCTabItemImageMap() {
        return cTabItemImageMap;
    }

    protected boolean areSame(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else if ((a == null && b != null) || (a != null && b == null)) {
            return false;
        } else {
            return a.equals(b);
        }
    }
}
