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
package org.talend.core.model.properties.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.PropertiesFactory;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ByteArrayResource extends ResourceImpl {

    public ByteArrayResource(URI uri) {
        super(uri);
    }

    protected void doLoad(InputStream inputStream, Map options) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] content = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(content);
        bufferedInputStream.close();
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContent(content);
        getContents().add(byteArray);
    }

    protected void doSave(OutputStream outputStream, Map options) throws IOException {
        if (getContents().size() > 0) {
            ByteArray byteArray = (ByteArray) getContents().get(0);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(byteArray.getInnerContent());
            bufferedOutputStream.flush();
        }
    }

    protected void doUnload() {
        super.doUnload();
    }
}
