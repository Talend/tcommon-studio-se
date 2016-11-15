// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

public class NoHeaderObjectInputStream extends ObjectInputStream {

    public NoHeaderObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    protected NoHeaderObjectInputStream() throws IOException, SecurityException {
        super();
    }

    @Override
    protected void readStreamHeader() throws IOException, StreamCorruptedException {
        // don't need to check the header
    }
}
