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
package org.talend.commons.utils.time;

/**
 * Manage a stack of time events. These events could be start, step or pause.
 */
class TimeStack {

    long elapsedTime;

    long previousTimeResume;

    long previousElapsedTimeSinceLastStep;

    long previousStepTime;

    boolean isPaused = false;

    // private long previousElpasedTimeSinceLastStep;

    private boolean testMode = false;

    private int testModeIndex;

    private long currentElapsedTimeSinceLastStep;

    public TimeStack() {
        previousStepTime = previousTimeResume = getCurrentTime();

    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public long getTotalElapsedTime() {
        if (isPaused) {
            return elapsedTime;
        } else {
            return elapsedTime + getCurrentTime() - previousTimeResume;
        }
    }

    public long getLastStepElapsedTime() {
        // if (isPaused) {
        // } else {
        // return elpasedTimeSinceLastStep;
        // }
        // return previousElpasedTimeSinceLastStep;
        return previousElapsedTimeSinceLastStep;
    }

    public void pause() {
        if (isPaused) {
            new Exception("Pause can't be done").printStackTrace();
        } else {
            long currentTime = getCurrentTime();
            elapsedTime += currentTime - previousTimeResume;
            currentElapsedTimeSinceLastStep += currentTime - previousStepTime;
            // previousTime = System.currentTimeMillis();
            isPaused = true;
        }
    }

    private long getCurrentTime() {

        int[] times = { 0, 20, 50, 120, 230, 370, 390 };

        if (testMode) {
            int time = times[testModeIndex++];
            return time;
        } else {
            return System.currentTimeMillis();
        }
    }

    public void resume() {
        long currentTime = getCurrentTime();
        // if (!isPaused) {
        // new Exception("Resume can't be done").printStackTrace();
        // } else {
        previousStepTime = previousTimeResume = currentTime;
        isPaused = false;
        // }
    }

    public void addStep() {
        long currentTime = getCurrentTime();
        long tempElapsedTime = currentTime - previousStepTime;
        if (isPaused) {
            // previousElpasedTimeSinceLastStep = elpasedTimeSinceLastStep;
            // elpasedTimeSinceLastStep = 0;
            previousElapsedTimeSinceLastStep = currentElapsedTimeSinceLastStep;
        } else {
            // previousElpasedTimeSinceLastStep = elpasedTimeSinceLastStep + tempElapsedTime;
            previousElapsedTimeSinceLastStep = currentElapsedTimeSinceLastStep + tempElapsedTime;
        }
        currentElapsedTimeSinceLastStep = 0;
        previousStepTime = currentTime;
    }
}
