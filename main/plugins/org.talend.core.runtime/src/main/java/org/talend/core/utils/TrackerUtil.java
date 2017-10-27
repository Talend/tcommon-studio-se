package org.talend.core.utils;

import org.talend.commons.utils.Version;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;

//============================================================================
//
//Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
//This source code is available under agreement available at
//%InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
//You should have received a copy of the agreement
//along with this program; if not, write to Talend SA
//9 rue Pages 92150 Suresnes, France
//
//============================================================================
public class TrackerUtil {

	public static String getAWSTracker() {
		String strVersion = VersionUtils.getDisplayVersion();
		Version version = new Version(strVersion);
		IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault()
				.getService(IBrandingService.class);
		String productName = brandingService.getProductName();
		StringBuffer sb = new StringBuffer();
		sb.append("APN/1.0 Talend/").append(getStrVersion(version)).append(" Studio/").append(getStrVersion(version)) //$NON-NLS-1$ //$NON-NLS-2$
				.append(" (").append(productName).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	private static String getStrVersion(Version version) {
		StringBuffer sb = new StringBuffer();
		sb.append(version.getMajor()).append(".").append(version.getMinor()); //$NON-NLS-1$
		return sb.toString();
	}

	public static String getGoogleTracker() {
		return "GPN:Talend"; //$NON-NLS-1$
	}
}
