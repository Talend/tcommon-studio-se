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
package org.talend.commons.utils.encoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.ibm.icu.text.CharsetMatch;

/**
 * Utility class to guess the encoding of a given byte array. The guess is unfortunately not 100% sure. Especially for
 * 8-bit charsets. It's not possible to know which 8-bit charset is used. Except through statistical analysis. We will
 * then infer that the charset encountered is the same as the default standard charset.
 */
public class CharsetToolkit {

    private byte[] buffer;

    private Charset defaultCharset;

    private boolean enforce8Bit = false;

    /**
     * Constructor utility class.
     * 
     * @param buffer the byte buffer of which we want to know the encoding.
     */
    public CharsetToolkit(byte[] buffer) {
        this.buffer = buffer;
        this.defaultCharset = getDefaultSystemCharset();
    }

    /**
     * Constructor utility class.
     * 
     * @param buffer the byte buffer of which we want to know the encoding.
     * @param defaultCharset the default Charset to use in case an 8-bit charset is recognized.
     */
    public CharsetToolkit(byte[] buffer, Charset defaultCharset) {
        this.buffer = buffer;
        setDefaultCharset(defaultCharset);
    }

    /**
     * Defines the default <code>Charset</code> used in case the buffer represents an 8-bit <code>Charset</code>.
     * 
     * @param defaultCharset the default <code>Charset</code> to be returned by <code>guessEncoding()</code> if an 8-bit
     * <code>Charset</code> is encountered.
     */
    public void setDefaultCharset(Charset defaultCharset) {
        if (defaultCharset != null) {
            this.defaultCharset = defaultCharset;
        } else {
            this.defaultCharset = getDefaultSystemCharset();
        }
    }

    /**
     * If US-ASCII is recognized, enforce to return the default encoding, rather than US-ASCII. It might be a file
     * without any special character in the range 128-255, but that may be or become a file encoded with the default
     * <code>charset</code> rather than US-ASCII.
     * 
     * @param enforce a boolean specifying the use or not of US-ASCII.
     */
    public void setEnforce8Bit(boolean enforce) {
        this.enforce8Bit = enforce;
    }

    /**
     * Gets the enforce8Bit flag, in case we do not want to ever get a US-ASCII encoding.
     * 
     * @return a boolean representing the flag of use of US-ASCII.
     */
    public boolean getEnforce8Bit() {
        return this.enforce8Bit;
    }

    /**
     * Retrieves the default Charset.
     * 
     * @return defaultCharset
     */
    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    /**
     * Guess the encoding of the provided buffer.
     * 
     * @return the Charset recognized.
     */
    public Charset guessEncoding() {
        // if the file has a Byte Order Marker, we can assume the file is in UTF-xx
        // otherwise, the file would not be human readable
        if (hasUTF8Buffer(buffer)) {
            return Charset.forName("UTF-8"); //$NON-NLS-1$
        }
        if (hasUTF16LEBuffer(buffer)) {
            return Charset.forName("UTF-16LE"); //$NON-NLS-1$
        }
        if (hasUTF16BEBuffer(buffer)) {
            return Charset.forName("UTF-16BE"); //$NON-NLS-1$
        }

        // if a byte has its most significant bit set, the file is in UTF-8 or in the default encoding
        // otherwise, the file is in US-ASCII
        boolean highOrderBit = false;

        // if the file is in UTF-8, high order bytes must have a certain value, in order to be valid
        // if it's not the case, we can assume the encoding is the default encoding of the system
        boolean validU8Char = true;

        int length = buffer.length;
        int i = 0;
        while (i < length - 6) {
            byte b0 = buffer[i];
            byte b1 = buffer[i + 1];
            byte b2 = buffer[i + 2];
            byte b3 = buffer[i + 3];
            byte b4 = buffer[i + 4];
            byte b5 = buffer[i + 5];
            if (b0 < 0) {
                // a high order bit was encountered, thus the encoding is not US-ASCII
                // it may be either an 8-bit encoding or UTF-8
                highOrderBit = true;
                // a two-bytes sequence was encoutered
                if (isTwoBytesSequence(b0)) {
                    // there must be one continuation byte of the form 10xxxxxx,
                    // otherwise the following characteris is not a valid UTF-8 construct
                    if (!isContinuationChar(b1)) {
                        validU8Char = false;
                    } else {
                        i++;
                    }
                } else if (isThreeBytesSequence(b0)) {
                    // there must be two continuation bytes of the form 10xxxxxx,
                    // otherwise the following characteris is not a valid UTF-8 construct
                    if (!(isContinuationChar(b1) && isContinuationChar(b2))) {
                        validU8Char = false;
                    } else {
                        i += 2;
                    }
                } else if (isFourBytesSequence(b0)) {
                    // there must be three continuation bytes of the form 10xxxxxx,
                    // otherwise the following characteris is not a valid UTF-8 construct
                    if (!(isContinuationChar(b1) && isContinuationChar(b2) && isContinuationChar(b3))) {
                        validU8Char = false;
                    } else {
                        i += 3;
                    }
                } else if (isFiveBytesSequence(b0)) {
                    // there must be four continuation bytes of the form 10xxxxxx,
                    // otherwise the following characteris is not a valid UTF-8 construct
                    if (!(isContinuationChar(b1) && isContinuationChar(b2) && isContinuationChar(b3) && isContinuationChar(b4))) {
                        validU8Char = false;
                    } else {
                        i += 4;
                    }
                } else if (isSixBytesSequence(b0)) {
                    // there must be five continuation bytes of the form 10xxxxxx,
                    // otherwise the following characteris is not a valid UTF-8 construct
                    if (!(isContinuationChar(b1) && isContinuationChar(b2) && isContinuationChar(b3) && isContinuationChar(b4) && isContinuationChar(b5))) {
                        validU8Char = false;
                    } else {
                        i += 5;
                    }
                } else {
                    validU8Char = false;
                }
            }
            if (!validU8Char) {
                break;
            }
            i++;
        }
        // if no byte with an high order bit set, the encoding is US-ASCII
        // (it might have been UTF-7, but this encoding is usually internally used only by mail systems)
        if (!highOrderBit) {
            // returns the default charset rather than US-ASCII if the enforce8Bit flag is set.
            if (this.enforce8Bit) {
                return this.defaultCharset;
            } else {
                return Charset.forName("US-ASCII"); //$NON-NLS-1$
            }
        }
        // if no invalid UTF-8 were encountered, we can assume the encoding is UTF-8,
        // otherwise the file would not be human readable
        if (validU8Char) {
            return Charset.forName("UTF-8"); //$NON-NLS-1$
        }
        // finally, if it's not UTF-8 nor US-ASCII, let's assume the encoding is the default encoding
        return this.defaultCharset;
    }

    /**
     * Guess the encoding of the provided buffer.
     * 
     */
    public static Charset guessEncoding(File f, int bufferLength) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] buffer = new byte[bufferLength];
        fis.read(buffer);
        fis.close();
        CharsetToolkit toolkit = new CharsetToolkit(buffer);
        toolkit.setDefaultCharset(getDefaultSystemCharset());
        return toolkit.guessEncoding();
    }

    /**
     * Guess the encoding of the provided buffer.
     * 
     */
    public static Charset guessEncoding(File f, int bufferLength, Charset defaultCharset) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] buffer = new byte[bufferLength];
        fis.read(buffer);
        fis.close();
        CharsetToolkit toolkit = new CharsetToolkit(buffer);
        toolkit.setDefaultCharset(defaultCharset);
        return toolkit.guessEncoding();
    }

    /**
     * If the byte has the form 10xxxxx, then it's a continuation byte of a multiple byte character.
     * 
     * @param b a byte.
     * @return true if it's a continuation char.
     */
    private static boolean isContinuationChar(byte b) {
        return -128 <= b && b <= -65;
    }

    /**
     * If the byte has the form 110xxxx, then it's the first byte of a two-bytes sequence character.
     * 
     * @param b a byte.
     * @return true if it's the first byte of a two-bytes sequence.
     */
    private static boolean isTwoBytesSequence(byte b) {
        return -64 <= b && b <= -33;
    }

    /**
     * If the byte has the form 1110xxx, then it's the first byte of a three-bytes sequence character.
     * 
     * @param b a byte.
     * @return true if it's the first byte of a three-bytes sequence.
     */
    private static boolean isThreeBytesSequence(byte b) {
        return -32 <= b && b <= -17;
    }

    /**
     * If the byte has the form 11110xx, then it's the first byte of a four-bytes sequence character.
     * 
     * @param b a byte.
     * @return true if it's the first byte of a four-bytes sequence.
     */
    private static boolean isFourBytesSequence(byte b) {
        return -16 <= b && b <= -9;
    }

    /**
     * If the byte has the form 11110xx, then it's the first byte of a five-bytes sequence character.
     * 
     * @param b a byte.
     * @return true if it's the first byte of a five-bytes sequence.
     */
    private static boolean isFiveBytesSequence(byte b) {
        return -8 <= b && b <= -5;
    }

    /**
     * If the byte has the form 1110xxx, then it's the first byte of a six-bytes sequence character.
     * 
     * @param b a byte.
     * @return true if it's the first byte of a six-bytes sequence.
     */
    private static boolean isSixBytesSequence(byte b) {
        return -4 <= b && b <= -3;
    }

    /**
     * Retrieve the default charset of the system.
     * 
     * @return the default <code>Charset</code>.
     */
    public static Charset getDefaultSystemCharset() {
        return Charset.forName(System.getProperty("file.encoding")); //$NON-NLS-1$
    }

    /**
     * DOC ycbai Comment method "getInternalSystemCharset".
     * 
     * Retrieve the system charset from "sun.jnu.encoding" property.
     * 
     * @return
     */
    public static Charset getInternalSystemCharset() {
        return Charset.forName(System.getProperty("sun.jnu.encoding")); //$NON-NLS-1$
    }

    /**
     * Has a Byte Order Marker for UTF-8 (Used by Microsoft's Notepad and other editors).
     * 
     * @param buffer a buffer.
     * @return true if the buffer has a BOM for UTF8.
     */
    private static boolean hasUTF8Buffer(byte[] buffer) {
        return (buffer[0] == -17 && buffer[1] == -69 && buffer[2] == -65);
    }

    /**
     * Has a Byte Order Marker for UTF-16 Low Endian (ucs-2le, ucs-4le, and ucs-16le).
     * 
     * @param buffer a buffer.
     * @return true if the buffer has a BOM for UTF-16 Low Endian.
     */
    private static boolean hasUTF16LEBuffer(byte[] buffer) {
        return (buffer[0] == -1 && buffer[1] == -2);
    }

    /**
     * Has a Byte Order Marker for UTF-16 Big Endian (utf-16 and ucs-2).
     * 
     * @param buffer a buffer.
     * @return true if the buffer has a BOM for UTF-16 Big Endian.
     */
    private static boolean hasUTF16BEBuffer(byte[] buffer) {
        return (buffer[0] == -2 && buffer[1] == -1);
    }
    
    public static String getCharset(File file){
        String charset = "UTF-8";
        byte[] fileContent = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file.getPath());
            fileContent = new byte[(int) file.length()];
            fin.read(fileContent);

            byte[] data =  fileContent;
            charset = getCharset(data);
        } catch (IOException e) {
            charset = "UTF-8";
        }
        return charset;
    }
    
    public static String getCharset(byte[] data){
        String charset = "UTF-8";
        com.ibm.icu.text.CharsetDetector detector = new com.ibm.icu.text.CharsetDetector();
        detector.setText(data);
        CharsetMatch cm = detector.detect();

        if (cm != null) {
//              int confidence = cm.getConfidence();
           charset = cm.getName();
        }
        return charset;
    }
}
