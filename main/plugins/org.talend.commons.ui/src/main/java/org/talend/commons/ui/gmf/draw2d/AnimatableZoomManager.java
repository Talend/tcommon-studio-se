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
package org.talend.commons.ui.gmf.draw2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;


/**
 * @author sshaw
 *
 * Overriden to fully support animated zoom.
 */
public class AnimatableZoomManager
    extends ZoomManager {

    private int zoomAnimationStyle = ANIMATE_NEVER;
    private List animationListeners = new ArrayList();
    
    public static double currentZoom = 1.0;

    /**
     * @return Returns the zoomAnimationStyle.
     */
    public int getZoomAnimationStyle() {
        return zoomAnimationStyle;
    }
    
    /**
     * @param pane
     * @param viewport
     */
    public AnimatableZoomManager(ScalableFigure pane, Viewport viewport) {
        super(pane, viewport);
        currentZoom = 1.0;
    }

    /**
     * Sets which zoom methods get animated.
     * 
     * @param style the style bits determining the zoom methods to be animated.
     */
    public void setZoomAnimationStyle(int style) {
        zoomAnimationStyle = style;
    }
    
    /**
     * Adds the given ZoomListener to this ZoomManager's list of listeners.
     * @param listener the ZoomListener to be added
     */
    public void addZoomListener(ZoomListener listener) {
        super.addZoomListener(listener);
        if (listener instanceof AnimatedZoomListener) {
            animationListeners.add(listener);
        }
    }
    
    /**
     * Notifies listeners that the animated zoom has started.
     */
    protected void fireAnimatedZoomStarted() {
        Iterator iter = animationListeners.iterator();
        while (iter.hasNext())
            ((AnimatedZoomListener)iter.next()).animatedZoomStarted();
    }
    
    /**
     * Notifies listeners that the animated zoom has ended.
     */
    protected void fireAnimatedZoomEnded() {
        Iterator iter = animationListeners.iterator();
        while (iter.hasNext())
            ((AnimatedZoomListener)iter.next()).animatedZoomEnded();
    }
    
    /**
     * Allows implementators to zoom to a certain level centered around a given point.
     * 
     * @param zoom <code>double</code> value where 1.0 represents 100%.
     * @param center <code>Point</code> around which the zoom will be centered in absolute coordinates
     */
    public void zoomTo(double zoom, Point center) {
        Point centerRel = center.getCopy();
        getScalableFigure().translateToRelative(centerRel);
        primSetZoom(zoom, centerRel);
    }
    
    /**
     * Allows implementors to zoom into or out to a rectangular area.
     * 
     * @param rect <code>Rectangle</code> that the edit part will zoom into our out to in 
     * absolute coordinates.
     */
    public void zoomTo(Rectangle rect) {
        Dimension available = getViewport().getClientArea().getSize();
        Dimension desired = rect.getSize();

        double scaleX = available.width * getZoom() / desired.width;
        double scaleY = available.height * getZoom() / desired.height;
        
        double zoom = Math.min(getMaxZoom(), Math.max(getMinZoom(), Math.min(scaleX, scaleY)));
        zoomTo(zoom, rect.getCenter());
    }
    
    private static final int DURATION_INCREMENT = 400;

    /**
     * Sets the zoom level to the given value.  Min-max range check is not done.
     * 
     * @param zoom the new zoom level
     */
    protected void primSetZoom(double zoom) {
        Point center = getViewport().getClientArea().getCenter();
        primSetZoom(zoom, center);
    }
    
    /**
     * Calculate the animation duration based on the number of zoom increments being traversed.
     * 
     * @param zoom
     * @return <code>AnimationModel</code> that is appropriate for the zoom difference between requested and the
     * current zoom level.
     */
    private AnimationModel calculateAnimationModel(double zoom) {
        double dmod = Math.pow(zoom / getZoom(),(double)1/8);
        int steps = (int)Math.round(dmod > 1 ? dmod : 1/dmod);
        
        int duration = Math.max(DURATION_INCREMENT, steps * DURATION_INCREMENT);
        AnimationModel animationModel = new AnimationModel(duration, true);
        animationModel.animationStarted();
        return animationModel;
    }
    
    /**
     * Sets the zoom level to the given value.  Min-max range check is not done.
     * 
     * @param zoom the new zoom level
     */
    private void primSetZoom(double zoom, Point finalCenterAbs) {
        primAnimateSetZoom(zoom, finalCenterAbs, 
                getZoomAnimationStyle() == ANIMATE_ZOOM_IN_OUT ? calculateAnimationModel(zoom) : null);
    }

    /**
     * Performs the zoom with animation.
     * 
     * @param zoom
     * @param finalCenter
     * @param animationModel
     */
    private void primAnimateSetZoom(double zoom, Point finalCenter, AnimationModel animationModel) {
        
        double initialZoom = getZoom();
        double finalZoom = zoom;
        currentZoom = finalZoom;
        
        Point finalCenterRel = finalCenter.getCopy();
        Point originalViewLocation = getViewport().getViewLocation();
        Dimension centerDiff = originalViewLocation.getDifference(getViewport().getClientArea().getCenter());
        Point finalViewLocation = finalCenterRel.scale(finalZoom / initialZoom).getTranslated(centerDiff);
        LineSeg scrollVector = new LineSeg(originalViewLocation, finalViewLocation);
        
        float progress = 1.0f;
        if (animationModel != null) {
            animationModel.animationStarted();
            progress = animationModel.getProgress();
        }
        boolean finished = false;
        
        fireAnimatedZoomStarted();
        while(!finished) {
            
            if (animationModel == null || animationModel.isFinished())
                finished = true;
            
            double currentZoom = initialZoom + ((finalZoom - initialZoom) * progress);
            
            super.primSetZoom(currentZoom);
            
            Point currentViewLocation = scrollVector.locatePoint(progress, 0, LineSeg.Sign.POSITIVE);
            setViewLocation(currentViewLocation);   
            
            getViewport().getUpdateManager().performUpdate();
            
            if (animationModel != null)
                progress = animationModel.getProgress();
        }
        fireAnimatedZoomEnded();
    }
}
