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

package test.common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * DOC sgandon tric to display class information of classes that failed to load during process of finding all the test
 * classes for all the plugins <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ErrorMessageTestCase {

    // have to use static field and method caus class is instantiatied by the junit framework later
    static List<String> errorMessagesList = new ArrayList<String>();

    @Test
    public void FAILING_TO_LOAD_TEST_CASES() {
        // create a string with all the classes details that failed to load
        StringBuffer mess = new StringBuffer();
        mess.append(
                "Some test cases or plugins could not be loaded (probably because their package does not start with org.talend):")
                .append('\n');
        for (String detail : errorMessagesList) {
            mess.append(detail).append("\n");
        }
        fail(mess.toString());
    }

    /**
     * record the error information to display them when the test is executed
     * 
     * @param message error that will be displayed during the test case execution
     */
    public static void addNewMessage(String message) {
        errorMessagesList.add(message);
    }
}