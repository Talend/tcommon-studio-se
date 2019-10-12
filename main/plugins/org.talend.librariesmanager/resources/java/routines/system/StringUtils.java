// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static final String EMPTY = "";

    public static String newStringFromSplit(CharsetDecoder decoder, CharsetDecoder utf8Decoder, String encoding,
            byte[] fieldBytes, int length) {
        ByteBuffer fieldBuf = ByteBuffer.wrap(fieldBytes, 0, length);
        CharBuffer fieldCharBuf = CharBuffer.allocate(length);
        utf8Decoder.reset();
        CoderResult res = utf8Decoder.decode(fieldBuf, fieldCharBuf, true);
        if (res.isError() && decoder != null) {
            decoder.reset();
            res = decoder.decode(fieldBuf, fieldCharBuf, true);
            if (!res.isError()) {
                decoder.flush(fieldCharBuf);
                return new String(fieldCharBuf.array());
            }
        } else {
            utf8Decoder.flush(fieldCharBuf);
            return new String(fieldCharBuf.array());
        }
        return "";
    }

    public static String[] splitNotRegexWithEncoding(byte[] bline, String encoding, String separatorChars)
            throws UnsupportedEncodingException {
        if (bline == null) {
            return null;
        }

        ByteBuffer line = ByteBuffer.wrap(bline);

        byte[] sep = null;
        CharsetDecoder decoder = null;
        if (encoding != null) {
            sep = separatorChars.getBytes(encoding);

            decoder = Charset.forName(encoding).newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPORT);
            decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        } else {
            sep = separatorChars.getBytes();
        }

        if (sep.length == 0) {
            String[] result = new String[1];
            result[0] = new String(bline, encoding);
            return result;
        }

        CharsetDecoder utf8Decoder = Charset.forName("UTF-8").newDecoder(); //$NON-NLS-1$
        utf8Decoder.onMalformedInput(CodingErrorAction.REPORT);
        utf8Decoder.onUnmappableCharacter(CodingErrorAction.REPORT);

        ArrayList<String> substrings = new ArrayList<String>();

        int lineLength = line.limit();
        int sepCursor = 0;
        int fieldCursor = 0;
        byte[] fieldBytes = new byte[lineLength];
        while (line.position() < line.limit()) {
            if (sepCursor < sep.length) {
                byte currentByte = line.get();
                if (currentByte == sep[sepCursor]) {
                    sepCursor++;
                } else {
                    sepCursor = 0;
                    fieldBytes[fieldCursor++] = currentByte;
                }
            } else {
                // we found a new field
                if (fieldCursor > 0) {
                    substrings.add(newStringFromSplit(decoder, utf8Decoder, encoding, fieldBytes, fieldCursor));
                    fieldCursor = 0;
                } else {
                    // empty field
                    substrings.add(""); //$NON-NLS-1$
                }
                sepCursor = 0;
            }
        }
        if (fieldCursor > 0) {
            substrings.add(newStringFromSplit(decoder, utf8Decoder, encoding, fieldBytes, fieldCursor));
        }
        if (sepCursor == sep.length) {
            substrings.add(""); //$NON-NLS-1$
        }

        int resultSize = substrings.size();
        if (resultSize == 0) {
            // no delimiter found so we have only one column
            String[] result = new String[1];
            result[0] = new String(bline, encoding);
            return result;
        }
        String[] result = new String[resultSize];
        substrings.toArray(result);
        return result;
    }

	/**
	 * replace the method : String.split(String regex)
     *
	 * @param str
	 * @param separatorChars
	 * @return
	 */
    public static String[] splitNotRegex(String str, String separatorChars) {
		if (str == null) {
            return null;
        }

        int len = str.length();

        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }

        int separatorLength = separatorChars.length();

        ArrayList<String> substrings = new ArrayList<String>();
        int beg = 0;
        int end = 0;
        while (end < len) {
            end = str.indexOf(separatorChars, beg);

            if (end > -1) {
                if (end > beg) {
                    substrings.add(str.substring(beg, end));
                    beg = end + separatorLength;
                } else {
                    substrings.add(EMPTY);
                    beg = end + separatorLength;
                }
            } else {
                substrings.add(str.substring(beg));
                end = len;
            }
        }


        int resultSize = substrings.size();
        String[] result = substrings.toArray(new String[resultSize]);

        while (resultSize > 0 && substrings.get(resultSize - 1).equals("")) {
            resultSize--;
            // Setting data to null for empty string in last columns to keep original behavior
            result[resultSize] = null;
        }
        return result;
	}

	/**
     * split SQL columns like that :
     * from :
     * id, name, CONCAT(name,UPPER(address)), CONCAT(age,name)
     * to
     * [id] [name] [CONCAT(name,UPPER(address))] [CONCAT(age,name)]
     */

    public static String[] splitSQLColumns(String sql) {
    	List<String> result = new ArrayList<String>();
    	int blockCount = 0;
    	int start = 0;
    	for(int i=0;i<sql.length();i++) {
    		char c = sql.charAt(i);
    		if(c == '(') {
    			blockCount++;
    		} else if(c == ')') {
    			blockCount--;
    		}

    		if((c == ',' && (blockCount<1))) {
    			result.add(sql.substring(start, i));
    			start = i + 1;
    		}

    		if(i == (sql.length()-1)) {
    			result.add(sql.substring(start));
    		}
    	}
    	return result.toArray(new String[0]);
    }

    public static String[] split(String str, String separator) {
        return str.split(separator);
    }

    public static String deleteChar(String s, char delChar) {
        int len = s.length();
        char[] val = s.toCharArray();
        char buf[] = new char[len];
        int m = 0;
        for (int k = 0; k < len; k++) {
            char c = val[k];
            if (c != delChar) {
                buf[m] = c;
                m++;
            }
        }

        return new String(buf, 0, m);
    }

    public static String list(String[] stringArray) {
        return list(stringArray, null, null, null, null);
    }

    public static String list(String[] stringArray, String separator) {
        return list(stringArray, separator, null, null, null);
    }

    public static String list(String[] stringArray, String separator, String startEnclosure, String endEnclosure) {
        return list(stringArray, separator, startEnclosure, endEnclosure, null);
    }

    public static String list(String[] stringArray, String separator, String escaper) {
        return list(stringArray, separator, null, null, escaper);
    }

    public static String list(String[] stringArray, String separator, String startEnclosure, String endEnclosure, String escaper) {
        if (separator == null) {
            separator = ""; //$NON-NLS-1$
        } else {
            separator = separator.trim();
        }
        if (startEnclosure == null) {
            startEnclosure = ""; //$NON-NLS-1$
        } else {
            startEnclosure = startEnclosure.trim();
        }
        if (endEnclosure == null) {
            endEnclosure = ""; //$NON-NLS-1$
        } else {
            endEnclosure = endEnclosure.trim();
        }
        if (escaper == null) {
            escaper = ""; //$NON-NLS-1$
        } else {
            escaper = escaper.trim();
        }
        StringBuilder result = new StringBuilder();

        result.append(startEnclosure);

        boolean flag = false;
        for (String item : stringArray) {
            item = item.trim();
            if (flag) {
                result.append(separator);
            } else {
                flag = true;
            }
            result.append(escaper);
            result.append(item);
            result.append(escaper);
        }
        result.append(endEnclosure);

        return result.toString();
    }

    /**
     * to discuss the case: src == null || regex == null || replacement == null
     *
     */
    public static String replaceAll(String src, String regex, String replacement) {

        // case 1:
        if (regex == null) {
            if (src == null) {
                return replacement; // regex == null && src == null
            } else {
                return src; // regex == null && src != null
            }
        } else {
            // case 2:
            if (src == null) {
                return null; // regex != null && src == null
            } else {
                // case 3:
                if (replacement == null) {
                    if (src.matches(regex)) {
                        // regex != null && src != null && replacement != null, and match the whole src
                        return replacement;
                    } else {
                        return src; // can't match the whole src
                    }

                } else {
                    // regex != null && src != null && replacement != null
                    return src.replaceAll(regex, replacement);

                }
            }
        }
    }

    /**
     * ignore regex
     *
     */
    public static String replaceAllStrictly(String src, String search, String replacement, boolean entirelyMatch,
            boolean caseSensitive) {
        // case 1:
        if (search == null) {
            if (src == null) {
                return replacement; // regex == null && src == null
            } else {
                return src; // regex == null && src != null
            }
        } else {
            // case 2:
            if (src == null) {
                return null; // regex != null && src == null
            } else {
                // case 3:
                if (replacement == null || entirelyMatch) {
                    if ((caseSensitive && src.equals(search)) || (!caseSensitive && src.equalsIgnoreCase(search))) {
                        // regex != null && src != null && replacement != null, and match the whole src
                        return replacement;
                    } else {
                        return src; // can't match the whole src
                    }

                } else {
                    int flag = caseSensitive ? Pattern.LITERAL : Pattern.LITERAL | Pattern.CASE_INSENSITIVE;
                    return Pattern.compile(search, flag).matcher(src).replaceAll(Matcher.quoteReplacement(replacement));
                }
            }
        }
    }

    /**
     * make \n to \\n. It will process these chars: \n, \r, \t, \f, \\, \", \', \b
     *
     */
    public static String escapeChar(String s) {
        if (s == null) {
            return null;
        }

        int length = s.length();
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++) {

            char c = s.charAt(i);

            switch (c) {
            case '\n':
                sb.append("\\").append('n'); //$NON-NLS-1$
                break;
            case '\r':
                sb.append("\\").append('r'); //$NON-NLS-1$
                break;
            case '\t':
                sb.append("\\").append('t'); //$NON-NLS-1$
                break;
            case '\f':
                sb.append("\\").append('f'); //$NON-NLS-1$
                break;
            case '\b':
                sb.append("\\").append('b'); //$NON-NLS-1$
                break;
            case '\"':
                sb.append("\\").append('\"'); //$NON-NLS-1$
                break;
            case '\'':
                sb.append("\\").append('\''); //$NON-NLS-1$
                break;
            default:
                sb.append(c);
            }
        }

        return sb.toString();
    }
    
    /**
     * check if string contains search string case-insensitivity
     * the code is copied from apache commons-lang3 StringUtils and do some adjust to avoid the useless code
     */
    public static boolean containsIgnoreCase(final String str, final String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int len = searchStr.length();
        final int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * return null value not "null" String when obj is null that is the only difference with String.valueOf(Object obj)
     *
     * @param obj
     * @return
     */
	public static String valueOf(Object obj) {
		return (obj == null) ? null : obj.toString();
	}

	public static String valueOf(char data[]) {
		return String.valueOf(data);
	}

	public static String valueOf(boolean b) {
		return String.valueOf(b);
	}

	public static String valueOf(char c) {
		return String.valueOf(c);
	}

	public static String valueOf(int i) {
		return String.valueOf(i);
	}

	public static String valueOf(long l) {
		return String.valueOf(l);
	}

	public static String valueOf(float f) {
		return String.valueOf(f);
	}

	public static String valueOf(double d) {
		return String.valueOf(d);
	}

}
