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
package org.talend.commons.ui.swt.colorstyledtext;

import java.util.ArrayList;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Mode;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Modes;
import org.talend.commons.ui.swt.colorstyledtext.rules.CToken;
import org.talend.commons.ui.swt.colorstyledtext.scanner.ColoringScanner;
import org.talend.commons.utils.threading.ExecutionLimiter;

/**
 * This component is an adaptation of a Color Editor for a StyledText.
 * 
 * The original editor can be found on http://www.gstaff.org/colorEditor/ <br/>
 * 
 * <b>How to use it, example :</b> <br/>
 * ColorStyledText text = new ColorStyledText(parent, SWT.H_SCROLL | SWT.V_SCROLL,
 * CorePlugin.getDefault().getPreferenceStore(), ECodeLanguage.PERL.getName());</i> <br/>
 * <br/>
 * 
 * $Id: ColorStyledText.java 7183 2007-11-23 11:03:36Z amaumont $
 * 
 */
public class ColorStyledText extends StyledText {

    private final static int MAXIMUM_CHARACTERS_BEFORE_USE_TIMER = 1000;

    private final ColorManager colorManager;

    private final ColoringScanner scanner;

    private final String languageMode;

    private final MenuItem pasteItem;

    private boolean coloring = true;

    private UndoRedoManager undoRedoManager;

    public ColorStyledText(Composite parent, int style, IPreferenceStore store, String languageMode) {
        super(parent, style);
        this.languageMode = languageMode;
        this.colorManager = new ColorManager(store);

        /*
         * set the Shortcuts of the undo/redo
         */
        this.setKeyBinding('Z' | SWT.CTRL, ActionCode.UNDO);
        this.setKeyBinding('Y' | SWT.CTRL, ActionCode.REDO);
        UndoRedoManager undoManager = new UndoRedoManager(50);
        undoManager.connect(this);
        this.setUndoManager(undoManager);

        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        Menu popupMenu = new Menu(this);

        MenuItem redoItem = new MenuItem(popupMenu, SWT.PUSH);
        redoItem.setText(Messages.getString("ColorStyledText.RedoItem.Text")); //$NON-NLS-1$
        redoItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                redo();
            }
        });

        MenuItem undoItem = new MenuItem(popupMenu, SWT.PUSH);
        undoItem.setText(Messages.getString("ColorStyledText.UndoItem.Text")); //$NON-NLS-1$
        undoItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                undo();
            }
        });

        Image image = ImageProvider.getImage(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        MenuItem copyItem = new MenuItem(popupMenu, SWT.PUSH);
        copyItem.setText(Messages.getString("ColorStyledText.CopyItem.Text")); //$NON-NLS-1$
        copyItem.setImage(image);
        copyItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                copy();
            }
        });

        image = ImageProvider.getImage(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        pasteItem = new MenuItem(popupMenu, SWT.PUSH);
        pasteItem.setText(Messages.getString("ColorStyledText.PasteItem.Text")); //$NON-NLS-1$
        pasteItem.setData(this);
        pasteItem.setImage(image);
        pasteItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                paste();
            }
        });
        pasteItem.setEnabled(getEditable());

        this.setMenu(popupMenu);
        MenuItem selectAllItem = new MenuItem(popupMenu, SWT.PUSH);
        selectAllItem.setText(Messages.getString("ColorStyledText.SelectAllItem.Text")); //$NON-NLS-1$
        selectAllItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                selectAll();
            }
        });
        this.setMenu(popupMenu);

        Listener selectAllListener = new Listener() {

            public void handleEvent(Event event) {
                if (event.character == '\u0001') { // CTRL + A
                    selectAll();
                }
            }
        };

        addListener(SWT.KeyDown, selectAllListener);

        Mode mode = Modes.getMode(languageMode + XmlUtil.FILE_XML_SUFFIX);
        scanner = new ColoringScanner(mode, colorManager);

        addExtendedModifyListener(modifyListener);
    }

    /**
     * DOC qli Comment method "invokeAction".
     * 
     * @param action
     * 
     * */
    public void invokeAction(int action) {
        super.invokeAction(action);

        switch (action) {
        case ActionCode.UNDO:
            undo(); // ctrl+Z
            break;
        case ActionCode.REDO:
            redo(); // ctrl+Y
            break;
        }
    }

    /**
     * Getter for undoRedoManager.
     * 
     * @return the undoRedoManager
     */
    public UndoRedoManager getUndoManager() {
        return this.undoRedoManager;
    }

    /**
     * Sets the undoRedoManager.
     * 
     * @param undoRedoManager the undoRedoManager to set
     */
    public void setUndoManager(UndoRedoManager undoRedoManager) {
        this.undoRedoManager = undoRedoManager;
    }

    public static class ActionCode {

        public static final int UNDO = Integer.MAX_VALUE;

        public static final int REDO = UNDO - 1;

    }

    private void undo() {
        if (undoRedoManager != null) {
            undoRedoManager.undo();
        }
    }

    private void redo() {
        if (undoRedoManager != null) {
            undoRedoManager.redo();
        }
    }

    protected void colorize(final ColoringScanner scanner) {
        final ArrayList<StyleRange> styles = new ArrayList<StyleRange>();
        if (this.coloring) {
            IToken token;
            if (this.isDisposed()) {
                return;
            }

            scanner.parse(this.getText().replaceAll("\"", " ").replaceAll("'", " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

            do {
                token = scanner.nextToken();
                if (!token.isEOF()) {
                    if (token instanceof CToken) {
                        CToken ctoken = (CToken) token;
                        StyleRange styleRange;
                        styleRange = new StyleRange();
                        styleRange.start = scanner.getTokenOffset();
                        styleRange.length = scanner.getTokenLength();
                        if (ctoken.getType() == null) {
                            styleRange.fontStyle = colorManager.getStyleFor(ctoken.getColor());
                            styleRange.foreground = colorManager.getColor(ctoken.getColor());
                        } else {
                            styleRange.fontStyle = colorManager.getStyleForType(ctoken.getColor());
                            styleRange.foreground = colorManager.getColorForType(ctoken.getColor());
                        }
                        styles.add(styleRange);
                    }
                }
            } while (!token.isEOF());
            setStyles(styles);
        } else {
            StyleRange styleRange = new StyleRange();
            styles.add(styleRange);
            styleRange.start = 0;
            styleRange.length = this.getText().getBytes().length;
            styleRange.foreground = null;
            setStyles(styles);
        }
    }

    public void setStyles(final ArrayList<StyleRange> styles) {
        if (ColorStyledText.this.isDisposed()) {
            return;
        }
        int countChars = getCharCount();

        for (int i = 0; i < styles.size(); i++) {
            StyleRange styleRange = styles.get(i);
            // System.out.println("styleRange.start=" + styleRange.start);
            // System.out.println("styleRange.length=" + styleRange.length);
            if (!(0 <= styleRange.start && styleRange.start + styleRange.length <= countChars)) {
                continue;
            }
            setStyleRange(styleRange);
        }
    }

    ExtendedModifyListener modifyListener = new ExtendedModifyListener() {

        public void modifyText(ExtendedModifyEvent event) {
            if (ColorStyledText.this.getCharCount() > MAXIMUM_CHARACTERS_BEFORE_USE_TIMER) {
                colorizeLimiter.resetTimer();
                colorizeLimiter.startIfExecutable(true, null);
            } else {
                getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        colorize(scanner);
                    }

                });
            }
        }
    };

    public ColorManager getColorManager() {
        return this.colorManager;
    }

    public String getLanguageMode() {
        return this.languageMode;
    }

    public ColoringScanner getScanner() {
        return this.scanner;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.custom.StyledText#setEditable(boolean)
     */
    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        if (pasteItem != null) {
            pasteItem.setEnabled(editable);
        }
    }

    /**
     * Getter for coloring.
     * 
     * @return the coloring
     */
    public boolean isColoring() {
        return this.coloring;
    }

    /**
     * Sets the coloring.
     * 
     * @param coloring the coloring to set
     */
    public void setColoring(boolean coloring) {
        boolean wasDifferent = this.coloring != coloring;
        this.coloring = coloring;
        if (!coloring) {
            removeExtendedModifyListener(modifyListener);
        } else if (wasDifferent) {
            colorizeLimiter.resetTimer();
            colorizeLimiter.startIfExecutable(true, null);
            addExtendedModifyListener(modifyListener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        colorManager.dispose();
    }

    private final ExecutionLimiter colorizeLimiter = new ExecutionLimiter(1000, true) {

        @Override
        public void execute(final boolean isFinalExecution, Object data) {
            if (!isDisposed()) {
                getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        colorize(scanner);
                    }

                });
            }
        }
    };

}
