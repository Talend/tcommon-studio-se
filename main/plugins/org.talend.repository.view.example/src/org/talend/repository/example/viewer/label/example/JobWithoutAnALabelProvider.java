package org.talend.repository.example.viewer.label.example;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.talend.repository.example.viewer.content.example.JobWithoutAnAContentProvider;
import org.talend.repository.viewer.label.AbstractRepoViewLabelProvider;

public class JobWithoutAnALabelProvider extends AbstractRepoViewLabelProvider {

    @Override
    public String getText(Object element) {
        if (element == JobWithoutAnAContentProvider.ROOT) {
            return element.toString();
        }
        return super.getText(element);
    }

    @Override
    public Image getImage(Object element) {
        if (element == JobWithoutAnAContentProvider.ROOT) {
            return null;
        }
        return super.getImage(element);
    }

    @Override
    public Font getFont(Object element) {
        if (element == JobWithoutAnAContentProvider.ROOT) {
            return null;
        }
        return super.getFont(element);
    }

    @Override
    public Color getBackground(Object element) {
        if (element == JobWithoutAnAContentProvider.ROOT) {
            return null;
        }
        return super.getBackground(element);
    }

    @Override
    public Color getForeground(Object element) {
        if (element == JobWithoutAnAContentProvider.ROOT) {
            return null;
        }
        return super.getForeground(element);
    }
}
