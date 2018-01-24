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
package org.talend.core.nexus;

import org.apache.commons.lang3.StringUtils;

/**
 * created by wchen on 2015-5-12 Detailled comment
 *
 */
public class NexusServerBean {

    public enum NexusType {
        NEXUS_2("NEXUS"),
        NEXUS_3("NEXUS 3");

        String repType;

        NexusType(String repType) {
            this.repType = repType;
        }

        public static NexusType getByRepType(String typeFromTAC) {
            for (NexusType type : NexusType.values()) {
                if (type.repType.equals(typeFromTAC)) {
                    return type;
                }
            }
            return null;
        }
        
        public String getRepType() {
            return this.repType;
        }
    }

    private String server;

    private String userName;

    private String password;

    private String repositoryId;

    private boolean official;

    private String snapshotRepId;

    private String type = NexusType.NEXUS_2.name();

    public NexusServerBean() {
    }

    public NexusServerBean(Boolean official) {
        this.official = official;
    }

    /**
     * Getter for server.
     *
     * @return the server
     */
    public String getServer() {
        return this.server;
    }

    /**
     * Sets the server.
     *
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Getter for userName.
     *
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Sets the userName.
     *
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for password.
     *
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for repositoryId.
     *
     * @return the repositoryId
     */
    public String getRepositoryId() {
        return this.repositoryId;
    }

    /**
     * Sets the repositoryId.
     *
     * @param repositoryId the repositoryId to set
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * Getter for snapshotRepId.
     *
     * @return the snapshotRepId
     */
    public String getSnapshotRepId() {
        return this.snapshotRepId;
    }

    /**
     * Sets the snapshotRepId.
     *
     * @param snapshotRepId the snapshotRepId to set
     */
    public void setSnapshotRepId(String snapshotRepId) {
        this.snapshotRepId = snapshotRepId;
    }

    /**
     * Getter for official.
     *
     * @return the official
     */
    public boolean isOfficial() {
        return this.official;
    }

    /**
     * Sets the official.
     *
     * @param official the official to set
     */
    public void setOfficial(boolean official) {
        this.official = official;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getRepositoryURI() {
        return getRepositoryURI(true);
    }

    public String getRepositoryURI(boolean isRelease) {
        if (StringUtils.isEmpty(this.server)) {
            return null; // no server, no uri
        }
        IRepositoryArtifactHandler repositoryHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(this);
        if (repositoryHandler != null) {
            return repositoryHandler.getRepositoryURL(isRelease);
        } else {
            String repId = "";
            if (isRelease) {
                repId = repositoryId;
            } else {
                repId = snapshotRepId;
            }
            String repositoryBaseURI = this.server;
            if (repositoryBaseURI.endsWith(NexusConstants.SLASH)) {
                repositoryBaseURI = repositoryBaseURI.substring(0, repositoryBaseURI.length() - 1);
            }
            repositoryBaseURI += NexusConstants.CONTENT_REPOSITORIES;
            repositoryBaseURI += repId + NexusConstants.SLASH;
            return repositoryBaseURI;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((server == null) ? 0 : server.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((repositoryId == null) ? 0 : repositoryId.hashCode());
        result = prime * result + ((snapshotRepId == null) ? 0 : snapshotRepId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NexusServerBean)) {
            return false;
        }
        NexusServerBean other = (NexusServerBean) obj;
        if (server == null) {
            if (other.server != null) {
                return false;
            }
        } else if (!server.equals(other.server)) {
            return false;
        }
        if (userName == null) {
            if (other.userName != null) {
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (repositoryId == null) {
            if (other.repositoryId != null) {
                return false;
            }
        } else if (!repositoryId.equals(other.repositoryId)) {
            return false;
        }

        if (snapshotRepId == null) {
            if (other.snapshotRepId != null) {
                return false;
            }
        } else if (!snapshotRepId.equals(other.snapshotRepId)) {
            return false;
        }

        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }

        return true;

    }
}
