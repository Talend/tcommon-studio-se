// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.utils.ControlUtils;
import org.talend.commons.ui.swt.colorstyledtext.ColorStyledText;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TypedTextCommandExecutor.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class TypedTextCommandExecutor {

    private static final int KEY_CODE_REDO = 121; // 'y'

    private static final int KEY_CODE_UNDO = 122; // 'z'

    private static final int KEY_CODE_PROPOSAL = 27; // ' '

    public static final String PARAMETER_NAME = "PARAMETER_NAME"; //$NON-NLS-1$

    private Key previousKey;

    private String previousText;

    protected final KeyListener keyListener = new KeyListener() {

        public void keyPressed(KeyEvent e) {
            // keyPressedExecute(e);
        }

        public void keyReleased(KeyEvent e) {
            keyReleasedExecute(e);
        }

    };

    protected final FocusListener focusListener = new FocusListener() {

        public void focusGained(FocusEvent e) {
            focusGainedExecute(e);
        }

        public void focusLost(FocusEvent e) {
            focusLostExecute(e);
        }

    };

    SelectionListener selectionListener = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {

        }

        public void widgetSelected(SelectionEvent e) {
            modifyExecute(e);
        }

    };

    private final MouseAdapter mouseListener = new MouseAdapter() {

        @Override
        public void mouseUp(MouseEvent e) {
            if (e.button == 3) {
                modifyExecute(e);
            }
        }

    };

    // private final ModifyListener modifyListener = new ModifyListener() {
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
    // */
    // public void modifyText(ModifyEvent e) {
    // if (isModifyFromMouse) {
    // modifyExecute(e);
    // }
    // }
    //
    // };

    protected void modifyExecute(TypedEvent e) {
        Object source = e.getSource();
        if (source instanceof MenuItem) {
            source = ((MenuItem) source).getData();
        }
        Control control = (Control) source;
        String currentText = ControlUtils.getText(control);
        previousText2 = previousText;
        activeControl = control.getData(PARAMETER_NAME);
        if (!currentText.equals(previousText)) {
            addNewCommand(control);
            previousText = currentText;
        }
    }

    private Pattern patternAlphaNum;

    private Perl5Matcher matcher;

    /**
     * DOC amaumont TypedTextCommandExecutor constructor comment.
     * 
     * @param stack
     */
    public TypedTextCommandExecutor() {
        super();
        init();
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void init() {

        Perl5Compiler compiler = new Perl5Compiler();
        matcher = new Perl5Matcher();
        patternAlphaNum = null;
        try {
            patternAlphaNum = compiler.compile("\\w"); //$NON-NLS-1$
        } catch (MalformedPatternException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

    }

    protected void keyPressedExecute(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    protected void keyReleasedExecute(KeyEvent e) {

        Control control = (Control) e.getSource();

        String currentText = ControlUtils.getText(control);
        previousText2 = previousText;
        activeControl = control.getData(PARAMETER_NAME);
        // System.out.println(e);

        boolean alphaNumMatched = matcher.matches(String.valueOf(e.character), patternAlphaNum);
        boolean ctrlKey = (e.stateMask & (SWT.CTRL)) != 0;
        boolean combinedKeys = (e.stateMask & (SWT.CTRL | SWT.ALT | SWT.SHIFT)) != 0;
        boolean undoOrRedo = ctrlKey && (e.keyCode == KEY_CODE_REDO || e.keyCode == KEY_CODE_UNDO);
        boolean proposal = ctrlKey && (e.keyCode == KEY_CODE_PROPOSAL);
        if (combinedKeys && !undoOrRedo && currentText.equals(previousText) || proposal) {
            // nothing
        } else if (!currentText.equals(previousText)) {
            // System.out.println("undoOrRedo===============" + undoOrRedo);
            // System.out.println("ctrlKey===============" + ctrlKey);
            if (undoOrRedo) {
                updateCommand(control);
            } else if ((this.previousKey != null && alphaNumMatched && this.previousKey.alphaNumMatched) /*
                                                                                                          * ||
                                                                                                          * e.character
                                                                                                          * == ' '
                                                                                                          */) {
                updateCommand(control);
            } else {
                addNewCommand(control);
            }
            previousText = currentText;
            this.previousKey = new Key(e, alphaNumMatched);
        }
    }

    /**
     * DOC amaumont Comment method "updateCommand".
     * 
     * @param commandStack2
     */
    public void updateCommand(Control control) {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * To store previous typed key informations. <br/>
     * 
     * $Id: TypedTextCommandExecutor.java 7038 2007-11-15 14:05:48Z plegall $
     * 
     */
    class Key {

        public char character;

        public int stateMask;

        public int keyCode;

        private final boolean alphaNumMatched;

        /**
         * DOC amaumont Key constructor comment.
         * 
         * @param e
         * @param alphaNumMatched
         */
        public Key(KeyEvent e, boolean alphaNumMatched) {
            this.character = e.character;
            this.stateMask = e.stateMask;
            this.keyCode = e.keyCode;
            this.alphaNumMatched = alphaNumMatched;
        }

    }

    /**
     * 
     * Implement your method by overriding.
     */
    public void addNewCommand(Control control) {

    }

    /**
     * DOC amaumont Comment method "register".
     * 
     * @param control
     */
    public void register(Control control) {
        control.addFocusListener(focusListener);
        control.addKeyListener(keyListener);
        if (control instanceof ColorStyledText) {
            ColorStyledText colorStyledText = ((ColorStyledText) control);
            Menu menu = colorStyledText.getMenu();
            MenuItem[] items = menu.getItems();
            for (MenuItem menuItem : items) {
                if (colorStyledText.equals(menuItem.getData())) {
                    menuItem.addSelectionListener(selectionListener);
                }
            }
        } else if (control instanceof Text) {
            // ((Text) control).addModifyListener(modifyListener);
            ((Text) control).addMouseListener(mouseListener);
        }

    }

    /**
     * DOC amaumont Comment method "unregister".
     * 
     * @param control
     */
    public void unregister(Control control) {
        control.removeKeyListener(keyListener);
        control.removeFocusListener(focusListener);
        if (control instanceof ColorStyledText) {
            ColorStyledText colorStyledText = ((ColorStyledText) control);
            Menu menu = colorStyledText.getMenu();
            MenuItem[] items = menu.getItems();
            for (MenuItem menuItem : items) {
                if (colorStyledText.equals(menuItem.getData())) {
                    menuItem.removeSelectionListener(selectionListener);
                }
            }
        } else if (control instanceof Text) {
            // ((Text) control).removeModifyListener(modifyListener);
            ((Text) control).removeMouseListener(mouseListener);
        }
    }

    private void focusGainedExecute(FocusEvent e) {
        previousKey = null;
        previousText = ControlUtils.getText((Control) e.getSource());

    }

    private void focusLostExecute(FocusEvent e) {
        String currentText = ControlUtils.getText((Control) e.getSource());
        if (!currentText.equals(previousText)) {
            updateCommand((Control) e.getSource());
        }
    }

    private static String previousText2 = ""; //$NON-NLS-1$

    public String getPreviousText2() {
        return previousText2;
    }

    private static Object activeControl;

    public Object getActiveControl() {
        return activeControl;
    }

}
