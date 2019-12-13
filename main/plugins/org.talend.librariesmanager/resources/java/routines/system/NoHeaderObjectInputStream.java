// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.util.Arrays;

/**
 * DOC bchen class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 *
 */
public class NoHeaderObjectInputStream extends ObjectInputStream {

    /**
     * Expected types for deserialized object (optional - for security purpose)
     */
    private Class<?>[] expectedTypes;

    /**
     * If {@link #expectedTypes} is set, flags if the class has already been checked
     */
    private boolean valid = false;

    public NoHeaderObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    public NoHeaderObjectInputStream(InputStream in, Class<?>... expectedTypes) throws IOException {
        this(in);
        if (expectedTypes != null) {
            this.expectedTypes = new Class<?>[expectedTypes.length];
            System.arraycopy(expectedTypes, 0, this.expectedTypes, 0, expectedTypes.length) ;
        }
    }

    /**
     * DOC bchen NoHeaderObjectInputStream constructor comment.
     *
     * @throws IOException
     * @throws SecurityException
     */
    protected NoHeaderObjectInputStream() throws IOException, SecurityException {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void readStreamHeader() throws IOException, StreamCorruptedException {
        // don't need to check the header
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        if (expectedTypes != null && !valid) {
            if (Arrays.stream(expectedTypes)
                    .anyMatch(c -> c.getName().equals(desc.getName()))) {
                valid = true;
            } else {
                throw new InvalidClassException("Unauthorized deserialization attempt : " + desc.getName());
            }
        }
        return super.resolveClass(desc);
    }
}
