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
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * DOC bchen class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 *
 */
public class NoHeaderObjectOutputStream extends ObjectOutputStream {

    public NoHeaderObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    /**
     * DOC bchen TalendObjectOutputStream constructor comment.
     *
     * @throws IOException
     * @throws SecurityException
     */
    protected NoHeaderObjectOutputStream() throws IOException, SecurityException {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // do nothing
    }

}
