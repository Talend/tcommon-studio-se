package org.talend.core.repository.logintask;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.login.AbstractLoginTask;
import org.talend.repository.model.IRepositoryService;

public class ReadOnlyProjectConfirm extends AbstractLoginTask implements IRunnableWithProgress {

    private static Logger log = Logger.getLogger(ReadOnlyProjectConfirm.class);

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        log.info("ReadOnly confirm"); //$NON-NLS-1$
        SubMonitor subMonitor = SubMonitor.convert(monitor, 1);
        subMonitor.setTaskName("ReadOnly confirm.");
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        service.openReadOnlyDialog(DisplayUtils.getDefaultShell(false));

        subMonitor.done();
    }

}
