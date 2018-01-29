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
package org.talend.core.model.process;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public interface ITargetExecutionConfig extends IServerConfiguration {

    public boolean isRemote();

    public int getFileTransferPort();

    public void setFileTransferPort(int transferFilePort);

    public void setUseSSL(boolean useSSL);

    public boolean useSSL();
    
    public boolean isUseJMX();

	public void setUseJMX(boolean useJMX);

	public int getRemotePort();

	public void setRemotePort(int remotePort);
	
	public String getRunAsUser();
	
	public void setRunAsUser(String runAsUser);

	// If the remote id is not null means this execution configuration from TAC
	public Integer getRemoteId();

	public boolean isVirtualServer();
	
	public boolean isActiveServer();
	
	public String getEncryptedPassword();
}
