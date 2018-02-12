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
package org.talend.test.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.powermock.reflect.Whitebox;

/**
 * 
 * SingletonUtil.
 * 
 * The advantages of this class is that it creates a spy instead a mock, does the required checks to ensure the
 * singleton is well spied, and initialize again the (static) singleton when calling again the method on the same
 * singleton class.
 * 
 * Warning: it is required to use PowerMock to use this class by using the following annotatoions such as:
 * 
 * <br/>
 * <b> <code>@RunWith(PowerMockRunner.class)<br/> </code> <code>@PrepareForTest({ MySingletonClass.class })<br/></code>
 * public class MyClassTest { <br/>
 * ... <br/>
 * }<br/>
 * </code> </b>
 */
public final class SingletonUtil {

    /**
     * 
     */
    private static final String DEFAULT_METHOD_NAME_GET_INSTANCE = "getInstance";

    /**
     * 
     * Method "spySingleton". This method will use by default the method "getInstance()" as method to retrieve the
     * Singleton instance.
     * 
     * @param <C>
     * @param singletonClass
     * @return
     * @throws Exception
     */
    public static <C> C spySingleton(Class<C> singletonClass) throws Exception {
        return spySingleton(singletonClass, DEFAULT_METHOD_NAME_GET_INSTANCE);
    }

    /**
     * 
     * Method "spySingleton".
     * 
     * @param <C>
     * @param singletonClass
     * @param getInstanceMethodName
     * @return
     * @throws Exception
     */
    public static <C> C spySingleton(Class<C> singletonClass, String getInstanceMethodName) throws Exception {
        synchronized (singletonClass) {

            clearInstance(singletonClass, getInstanceMethodName);

            // invoke default constructor
            C myInstance = Whitebox.invokeConstructor(singletonClass);
            // spy singleton class
            C mySingletonSpy = spy(myInstance);
            // spy default constructor to return the mocked singleton instance
            whenNew(singletonClass).withNoArguments().thenReturn(mySingletonSpy);

            // invoke getInstance() method to get instance of singleton
            Object newInstance = Whitebox.invokeMethod(singletonClass, getInstanceMethodName);
            if (newInstance != mySingletonSpy) {
                // like getInstance() does not return the expected reference, we force the internal state
                Whitebox.setInternalState(mySingletonSpy, singletonClass, mySingletonSpy, singletonClass);
            }
            newInstance = Whitebox.invokeMethod(singletonClass, getInstanceMethodName);
            // ensure the singleton instance returned by getInstance() method has the expected reference
            assertThat(buildErrorMessage(singletonClass), (C) newInstance, is(mySingletonSpy));

            // ensure the singleton instance returned by getInstance() method has the expected reference
            C internalStateAfterReset = Whitebox.getInternalState(singletonClass, singletonClass);
            // ensure the internal singleton instance has the expected reference
            assertThat(internalStateAfterReset, is(mySingletonSpy));
            return mySingletonSpy;
        }
    }

    /**
     * 
     * Method "spySingleton". This method will use by default the method "getInstance()" as method to retrieve the
     * Singleton instance.
     * 
     * @param <C>
     * @param singletonClass
     * @return
     * @throws Exception
     */
    public static <C> C mockSingleton(Class<C> singletonClass) throws Exception {
        return mockSingleton(singletonClass, DEFAULT_METHOD_NAME_GET_INSTANCE);
    }

    /**
     * 
     * Method "spySingleton".
     * 
     * @param <C>
     * @param singletonClass
     * @param getInstanceMethodName
     * @return
     * @throws Exception
     */
    public static <C> C mockSingleton(Class<C> singletonClass, String getInstanceMethodName) throws Exception {
        synchronized (singletonClass) {

            clearInstance(singletonClass, getInstanceMethodName);

            // mock singleton class
            C mySingletonMock = mock(singletonClass);
            // mock default constructor to return the mocked singleton instance
            whenNew(singletonClass).withNoArguments().thenReturn(mySingletonMock);

            // invoke getInstance() method to get instance of singleton
            Object newInstance = Whitebox.invokeMethod(singletonClass, getInstanceMethodName);
            if (newInstance != mySingletonMock) {
                // like getInstance() does not return the expected reference, we force the internal state
                Whitebox.setInternalState(mySingletonMock, singletonClass, mySingletonMock, singletonClass);
            }
            newInstance = Whitebox.invokeMethod(singletonClass, getInstanceMethodName);
            // ensure the singleton instance returned by getInstance() method has the expected reference
            assertThat(buildErrorMessage(singletonClass), (C) newInstance, is(mySingletonMock));

            // ensure the singleton instance returned by getInstance() method has the expected reference
            C internalStateAfterReset = Whitebox.getInternalState(singletonClass, singletonClass);
            // ensure the internal singleton instance has the expected reference
            assertThat(internalStateAfterReset, is(mySingletonMock));
            return mySingletonMock;
        }
    }

    /**
     * 
     * Method "clearInstance". Set the internal singleton instance by setting the null value.
     * 
     * @param singletonClass
     * @param getInstanceMethodName
     * @throws Exception
     */
    private static <C> void clearInstance(Class<C> singletonClass, String getInstanceMethodName) throws Exception {
        // invoke getInstance() method to get instance of singleton
        Object mySingleton = Whitebox.invokeMethod(singletonClass, getInstanceMethodName);
        // clear singleton instance by setting singleton=null
        Whitebox.setInternalState(mySingleton, singletonClass, (C) null, singletonClass);

        // get internal instance value
        C internalStateBeforeReset = Whitebox.getInternalState(singletonClass, singletonClass);
        // ensure internal instance value is null
        assertThat(buildErrorMessage(singletonClass), internalStateBeforeReset, is(nullValue()));
    }

    /**
     * 
     * Method "clearInstance". Reset the internal singleton instance by setting a new instance value.
     * 
     * @param singletonClass
     * @throws Exception
     */
    public static <C> C resetInstance(Class<C> singletonClass) throws Exception {
        return resetInstance(singletonClass, DEFAULT_METHOD_NAME_GET_INSTANCE);
    }

    /**
     * 
     * Method "clearInstance". Reset the internal singleton instance by setting a new instance value.
     * 
     * @param singletonClass
     * @param getInstanceMethodName
     * @return
     * @throws Exception
     */
    public static <C> C resetInstance(Class<C> singletonClass, String getInstanceMethodName) throws Exception {
        // invoke getInstance() method to get instance of singleton
        Object newInstance = Whitebox.invokeConstructor(singletonClass);
        // clear singleton instance by setting singleton=null
        Whitebox.setInternalState(newInstance, singletonClass, newInstance, singletonClass);

        // get internal instance value
        C internalStateAfterReset = Whitebox.getInternalState(singletonClass, singletonClass);
        // ensure internal instance value is null
        assertThat(buildErrorMessage(singletonClass), internalStateAfterReset, is(newInstance));

        Object instanceFromGetInstance = Whitebox.invokeMethod(singletonClass, getInstanceMethodName);
        // ensure the singleton instance returned by getInstance() method has the expected reference
        assertThat(buildErrorMessage(singletonClass), instanceFromGetInstance, is(newInstance));
        return (C) newInstance;
    }

    private static <C> String buildErrorMessage(Class<C> singletonClass) {
        return "Not able to mock or spy the class '"
                + singletonClass.getName()
                + "' as a singleton. You may have forgotten to write the following instructions at beginning of your Test class: \n\n@RunWith(PowerMockRunner.class)\n"
                + "@PrepareForTest({ " + singletonClass.getSimpleName() + ".class })\n";
    }

}
