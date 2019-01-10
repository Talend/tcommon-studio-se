package org.talend.core.model.metadata.designerproperties;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.daikon.security.CryptoHelper;

/**
 * created by ycbai on 2016年3月11日
 * <p>
 * The class is only use to test password parameter. I will be moved to RepositoryToComponentPropertyTest after.
 * 
 *
 */
public class RepositoryToComponentPropertyForPasswordTest {

    @Test
    public void testGetPassword() {
        DatabaseConnection dbConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        dbConnection.setContextMode(false);
        String encryptPassword = CryptoHelper.getDefault().encrypt("thepassword"); //$NON-NLS-1$
        dbConnection.setPassword(encryptPassword);
        Object resultPassword = RepositoryToComponentProperty.getValue(dbConnection, "PASSWORD", null); //$NON-NLS-1$
        assertEquals("\"thepassword\"", resultPassword); //$NON-NLS-1$
    }

}