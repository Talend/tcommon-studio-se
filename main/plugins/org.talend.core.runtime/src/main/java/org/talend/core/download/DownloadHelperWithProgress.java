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
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * created by sgandon on 19 sept. 2013 Detailled comment
 * 
 */
public class DownloadHelperWithProgress {

    /**
     * created by sgandon on 19 sept. 2013 Detailled comment
     * 
     */
    protected final class DownloadListenerImplementation implements DownloadListener {

        protected final IProgressMonitor progressMonitor;

        /**
         * DOC sgandon DownloadHelperWithProgress.DownloadListenerImplementation constructor comment.
         */
        public DownloadListenerImplementation(IProgressMonitor progressMonitor) {
            this.progressMonitor = progressMonitor;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.download.DownloadListener#downloadStart(int)
         */
        @Override
        public void downloadStart(int totalSize) {
            progressMonitor.beginTask(null, totalSize);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.download.DownloadListener#downloadProgress(org.talend.core.download.DownloadHelper, int)
         */
        @Override
        public void downloadProgress(IDownloadHelper downloader, int bytesDownloaded) {
            progressMonitor.worked(bytesDownloaded);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.download.DownloadListener#downloadComplete()
         */
        @Override
        public void downloadComplete() {
            progressMonitor.done();

        }

        public boolean isCanceled() {
            return progressMonitor.isCanceled();
        }
    }

    /**
     * download the content of the URL and save it into the destination file.
     * 
     * @param componentUrl, url to download the data
     * @param destination, file destination
     * @param progressMonitor, to monitor the progress, must never be null
     * @throws IOException, if an IO error occurs.
     */
    public void download(URL componentUrl, File destination, @SuppressWarnings("hiding") IProgressMonitor progressMonitor)
            throws Exception {
        DownloadListenerImplementation downloadProgress = new DownloadListenerImplementation(progressMonitor);
        IDownloadHelper downloadHelper = createDownloadHelperDelegate(downloadProgress);
        downloadHelper.download(componentUrl, destination);
    }

    /**
     * create a delegate helper that maintain the cancel state according to the monitor
     * 
     * @param downloadProgress
     * @return
     */
    protected IDownloadHelper createDownloadHelperDelegate(final DownloadListenerImplementation downloadProgress) {
        DownloadHelper downloadHelper = new DownloadHelper() {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.core.download.DownloadHelper#isCancel()
             */
            @Override
            public boolean isCancel() {
                return downloadProgress.isCanceled();
            }
        };
        downloadHelper.addDownloadListener(downloadProgress);
        return downloadHelper;
    }

}
