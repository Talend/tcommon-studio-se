package org.talend.core.repository.model.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.repository.documentation.ERepositoryActionName;

public abstract class AbstractJobCreateListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if (!event.getPropertyName().equals(ERepositoryActionName.CREATE.getName())
                && !event.getPropertyName().equals(ERepositoryActionName.IMPORT.getName())) {
            return;
        }

        if (!(event.getNewValue() instanceof ProcessItem) && !(event.getNewValue() instanceof JobletProcessItem)) {
            return;
        }

        final Item item = (Item) event.getNewValue();
        execute(item);
    }

    public abstract void execute(Item item);
}
