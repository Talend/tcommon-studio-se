// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.download;

import java.io.File;
import java.net.URL;

/**
 * created by wchen on Apr 24, 2015 Detailled comment
 *
 */
public interface IDownloadHelper {

    public void setCancel(boolean cancel);

    public void download(URL componentUrl, File destination) throws Exception;

}
