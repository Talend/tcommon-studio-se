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

import java.util.Date;


/**
 * Holds the count information, and notifies
 * interested figures of changes in animation.
 * Created by a root, which loops through the
 * animation process.
 * 
 * <p>
 * Code taken from Eclipse reference bugzilla #98820
 *
 * @canBeSeenBy %level0
 */
public class AnimationModel {

private long startTime = new Date().getTime();
private long duration = 0;

private boolean ascending;

/**
 * Default constructor taking in number of
 * milliseconds the animation should take.
 */
public AnimationModel(long duration, boolean ascending){
    this.duration = duration;
    this.ascending = ascending;
}

/**
 * Called to notify the start of the animation process.
 * Notifies all listeners to get ready for animation start.
 */
public void animationStarted(){
    startTime = new Date().getTime();
}

/**
 * Returns (0.0<=value<=1.0), of current position
 */
public float getProgress(){
    long presentTime = new Date().getTime();
    float elapsed = (presentTime-startTime);
    float progress = Math.min(1.0f, elapsed/duration);
    if (!ascending)
        return 1.0f - progress;
    return progress;
}

public boolean isFinished(){
    return (new Date().getTime() - startTime) > duration;
}

}
