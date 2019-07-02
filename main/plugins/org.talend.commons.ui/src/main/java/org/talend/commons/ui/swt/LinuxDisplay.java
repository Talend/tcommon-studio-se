package org.talend.commons.ui.swt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */
public class LinuxDisplay extends Display {

    private static Field fieldThread;

    private static Method methodCheckDisplay;

    private static Method methodCreateDisplay;

    private static Method methodRegister;

    public LinuxDisplay() {
        super();
    }

    public LinuxDisplay(DeviceData data) {
        super(data);
    }

    @Override
    protected void create(DeviceData data) {
        try {
            Thread t = Thread.currentThread();
            setThread(t);
            invokeCheckDisplay(t, true);
            invokeCreateDisplay(data);
            invokeRegister(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setThread(Thread thread) throws Exception {
        if (fieldThread == null) {
            fieldThread = Display.class.getDeclaredField("thread");
            fieldThread.setAccessible(true);
        }
        fieldThread.set(this, thread);
    }

    private void invokeCreateDisplay(DeviceData data) throws Exception {
        if (methodCreateDisplay == null) {
            methodCreateDisplay = Display.class.getDeclaredMethod("createDisplay", DeviceData.class);
            methodCreateDisplay.setAccessible(true);
        }
        methodCreateDisplay.invoke(this, data);
    }

    private void invokeCheckDisplay(Thread thread, boolean multiple) throws Exception {
        if (methodCheckDisplay == null) {
            methodCheckDisplay = Display.class.getDeclaredMethod("checkDisplay", Thread.class, boolean.class);
            methodCheckDisplay.setAccessible(true);
        }
        methodCheckDisplay.invoke(this, thread, multiple);
    }

    private void invokeRegister(Display display) throws Exception {
        if (methodRegister == null) {
            methodRegister = Display.class.getDeclaredMethod("register", Display.class);
            methodRegister.setAccessible(true);
        }
        methodRegister.invoke(this, display);
    }
}
