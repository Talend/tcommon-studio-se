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
package org.talend.commons.ui.gmf.util;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class ColorPalettePopup {

    /** variable to store previous color */
    private int previousColor;

    private Button customColorButton;

    private HashMap buttonMap = new HashMap();

    /**
     * A descirptor for an inventory color
     */
    private static class InventoryColorDescriptor extends ImageDescriptor {

        /** the default preference color */
        private static final RGB OUTLINE_COLOR = new RGB(192, 192, 192);

        public RGB rgb;

        public InventoryColorDescriptor(RGB colorValue) {
            this.rgb = colorValue;

        }

        /**
         * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
         */
        public ImageData getImageData() {
            ImageData data = new ImageData(ICON_SIZE.x, ICON_SIZE.y, 1, new PaletteData(new RGB[] { rgb, OUTLINE_COLOR }));

            for (int i = 0; i < ICON_SIZE.y; i++)
                data.setPixel(0, i, 1);
            for (int i = 0; i < ICON_SIZE.y; i++)
                data.setPixel(ICON_SIZE.x - 1, i, 1);
            for (int i = 0; i < ICON_SIZE.x; i++)
                data.setPixel(i, 0, 1);
            for (int i = 0; i < ICON_SIZE.x; i++)
                data.setPixel(i, ICON_SIZE.y - 1, 1);
            return data;
        }

        /**
         * Creates and returns a new SWT image for this image descriptor. The returned image must be explicitly disposed
         * using the image's dispose call. The image will not be automatically garbage collected. In the even of an
         * error, a default image is returned if <code>returnMissingImageOnError</code> is true, otherwise
         * <code>null</code> is returned.
         * <p>
         * Note: Even if <code>returnMissingImageOnError</code> is true, it is still possible for this method to return
         * <code>null</code> in extreme cases, for example if SWT runs out of image handles.
         * </p>
         * 
         * @return a new image or <code>null</code> if the image could not be created
         * 
         */
        public Image createImage() {

            Device device = Display.getCurrent();
            ImageData data = getImageData();
            if (data == null)
                data = DEFAULT_IMAGE_DATA;

            /*
             * Try to create the supplied image. If there is an SWT Exception try and create the default image if that
             * was requested. Return null if this fails.
             */

            try {
                if (data.transparentPixel >= 0) {
                    ImageData maskData = data.getTransparencyMask();
                    return new Image(device, data, maskData);
                }
                return new Image(device, data);
            } catch (SWTException exception) {

                try {
                    return new Image(device, DEFAULT_IMAGE_DATA);
                } catch (SWTException nextException) {
                    return null;
                }

            }
        }
    }

    /** default color icon width */
    public static final Point ICON_SIZE = new Point(IDialogConstants.BUTTON_BAR_HEIGHT, IDialogConstants.BUTTON_BAR_HEIGHT);

    /** inventory colors */
    private static final InventoryColorDescriptor WHITE = new InventoryColorDescriptor(new RGB(255, 255, 255));

    private static final InventoryColorDescriptor BLACK = new InventoryColorDescriptor(new RGB(0, 0, 0));

    private static final InventoryColorDescriptor LIGHT_GRAY = new InventoryColorDescriptor(new RGB(192, 192, 192));

    private static final InventoryColorDescriptor GRAY = new InventoryColorDescriptor(new RGB(128, 128, 128));

    private static final InventoryColorDescriptor RED = new InventoryColorDescriptor(new RGB(227, 164, 156));

    private static final InventoryColorDescriptor GREEN = new InventoryColorDescriptor(new RGB(166, 193, 152));

    private static final InventoryColorDescriptor BLUE = new InventoryColorDescriptor(new RGB(152, 168, 191));

    private static final InventoryColorDescriptor YELLOW = new InventoryColorDescriptor(new RGB(225, 225, 135));

    private static final InventoryColorDescriptor PURPLE = new InventoryColorDescriptor(new RGB(184, 151, 192));

    private static final InventoryColorDescriptor TEAL = new InventoryColorDescriptor(new RGB(155, 199, 204));

    private static final InventoryColorDescriptor PINK = new InventoryColorDescriptor(new RGB(228, 179, 229));

    private static final InventoryColorDescriptor ORANGE = new InventoryColorDescriptor(new RGB(237, 201, 122));

    /** the inventory color list key: anRGB, value: anImage */
    private static final HashMap imageColorMap = new HashMap();

    private static final String DEFAULT_COLOR_STRING ="Default";

    private static final String CUSTOM_COLOR_STRING = "Custom";

    static {

        // inventory colors
        imageColorMap.put(WHITE.rgb, WHITE.createImage());
        imageColorMap.put(BLACK.rgb, BLACK.createImage());
        imageColorMap.put(LIGHT_GRAY.rgb, LIGHT_GRAY.createImage());
        imageColorMap.put(GRAY.rgb, GRAY.createImage());
        imageColorMap.put(RED.rgb, RED.createImage());
        imageColorMap.put(GREEN.rgb, GREEN.createImage());
        imageColorMap.put(BLUE.rgb, BLUE.createImage());
        imageColorMap.put(YELLOW.rgb, YELLOW.createImage());
        imageColorMap.put(PURPLE.rgb, PURPLE.createImage());
        imageColorMap.put(TEAL.rgb, TEAL.createImage());
        imageColorMap.put(PINK.rgb, PINK.createImage());
        imageColorMap.put(ORANGE.rgb, ORANGE.createImage());
    }

    private Shell shell;

    private RGB selectedColor = null;

    /**
     * The default color to be used if the user presses the default button.
     */
    private boolean useDefaultColor = false;

    /**
     * Creates a color palette popup above the specified shell.
     * 
     * @param parent a Shell control which will be the parent of the new instance (cannot be null)
     * @deprecated Use the other constructor. This one does not retrieve the default value from the correct preference
     * store.
     */
    public ColorPalettePopup(Shell parent, String preferenceId, int rowHeight) {
        this(parent, rowHeight);
    }

    /**
     * Creates a PopupList above the specified shell.
     * 
     * @param parent a widget which will be the parent of the new instance (cannot be null)
     * @param style the style of widget to construct
     */
    public ColorPalettePopup(Shell parent, int rowHeight) {
        shell = new Shell(parent, checkStyle(SWT.NONE));
        shell.setLayout(new FillLayout());
        GridLayout layout = new GridLayout(4, true);
        shell.setLayout(layout);

        for (Iterator e = imageColorMap.keySet().iterator(); e.hasNext();) {
            Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
            data.heightHint = rowHeight;
            data.widthHint = rowHeight;
            button.setLayoutData(data);

            final RGB rgb = (RGB) e.next();
            final Image image = (Image) imageColorMap.get(rgb);
            button.setImage(image);
            button.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e1) {
                    selectedColor = rgb;
                    shell.dispose();
                }
            });
            buttonMap.put(rgb, button);
        }
        Button defaultButton = new Button(shell, SWT.PUSH | SWT.FLAT);
        defaultButton.setText(DEFAULT_COLOR_STRING);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 4;
        defaultButton.setLayoutData(data);

        defaultButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                useDefaultColor = true;
                shell.dispose();
            }
        });

        Button moreColors = new Button(shell, SWT.PUSH | SWT.FLAT);
        moreColors.setText(CUSTOM_COLOR_STRING);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 4;
        moreColors.setLayoutData(data);

        moreColors.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {

                ColorDialog dialog = new ColorDialog(Display.getCurrent().getActiveShell());
                dialog.setRGB(integerToRGB(getPreviousColor()));
                centerDialog(dialog.getParent(), Display.getCurrent().getActiveShell());
                dialog.open();
                selectedColor = dialog.getRGB();
                shell.dispose();

            }
        });
        // close dialog if user selects outside of the shell
        shell.addListener(SWT.Deactivate, new Listener() {

            public void handleEvent(Event e) {
                shell.setVisible(false);
            }
        });
        customColorButton = moreColors;

    }
    
    public void centerDialog(Shell dialogShell, Shell parentShell) {
        try {
            Point dialogSize = dialogShell.getSize();
            //don't use Display and then get shell from there, since
            //we are trying to find the shell of the whole Eclipse window
            Point windowLocation = parentShell.getLocation();
            Point windowSize = parentShell.getSize();
            //do not take the absolute value.
            dialogShell.setLocation(
                ((windowSize.x - dialogSize.x) / 2) + windowLocation.x,
                ((windowSize.y - dialogSize.y) / 2) + windowLocation.y);
        } catch (Exception e) {
            //any exception when centering a dialog may be ignored
        }
    }

    /**
     * @param style
     * @return
     */
    private static int checkStyle(int style) {
        int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
        return style & mask;
    }

    /**
     * Launches the Popup List, waits for an item to be selected and then closes PopupList.
     * 
     * @param rect the initial size and location of the PopupList; the dialog will be positioned so that it does not run
     * off the screen and the largest number of items are visible
     * 
     * @return the text of the selected item or null if no item is selected
     */
    public RGB open(Point location) {

        Point listSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        shell.setBounds(location.x, location.y, listSize.x, listSize.y);

        shell.open();
        shell.setFocus();       
        Display display = shell.getDisplay();
        Button prevButton = (Button)buttonMap.get(integerToRGB(getPreviousColor()));
        if (prevButton != null){
            shell.setDefaultButton(prevButton);
        }   
        else{
            shell.setDefaultButton(customColorButton);
        }
        while (!shell.isDisposed() && shell.isVisible()) {
            if (!display.readAndDispatch())
                display.sleep();
        }       
        return getSelectedColor();
    }

    public RGB integerToRGB(Integer color) {
        int n = color.intValue();
        return new RGB((n & 0x000000FF), (n & 0x0000FF00) >> 8, (n & 0x00FF0000) >> 16);
    }

    /**
     * Gets the color the user selected. Could be null as the user may have cancelled the gesture or they may have
     * selected the default color button. See {@link #useDefaultColor()}.
     * 
     * @return the selected color or null
     */
    public RGB getSelectedColor() {
        return selectedColor;
    }

    /**
     * Returns true if the user selected to use the default color
     * 
     * @return true if the default color is to be used; false otherwise
     */
    public boolean useDefaultColor() {
        return useDefaultColor;
    }

    public int getPreviousColor() {
        return previousColor;
    }

    public void setPreviousColor(int previousColor) {
        this.previousColor = previousColor;
    }
}
