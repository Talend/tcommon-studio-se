package org.talend.updates.runtime.ui;

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.talend.updates.runtime.model.ExtraFeature;

public class ShowWizardHandler extends AbstractHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell activeShell = HandlerUtil.getActiveShell(event);
        showUpdateWizard(activeShell, null);
        return null;
    }

    /**
     * This shows the talend update wizard, this should be the only method called to show the wizard.
     * 
     * @param shell, the shell used to display the wizard
     * @param uninstalledExtraFeatures the list of features that may be installled. May be null and if that is the case
     * then the list is computed again.
     */
    public void showUpdateWizard(final Shell shell, Set<ExtraFeature> uninstalledExtraFeatures) {
        Set<ExtraFeature> extraFeatures = uninstalledExtraFeatures;
        UpdateStudioWizard updateStudioWizard = new UpdateStudioWizard(extraFeatures);
        updateStudioWizard.show(shell);
    }

}
