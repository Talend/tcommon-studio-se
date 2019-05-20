package org.talend.utils.string;

//============================================================================
//
//Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
//This source code is available under agreement available at
//%InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
//You should have received a copy of the agreement
//along with this program; if not, write to Talend SA
//9 rue Pages 92150 Suresnes, France
//
//============================================================================
public class DigestUtil {

	public static byte[] sha256(final byte[] data) {
		return org.apache.commons.codec.digest.DigestUtils.sha256(data);
	}

	public static String sha256Hex(final byte[] data) {
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(data);
	}
}
