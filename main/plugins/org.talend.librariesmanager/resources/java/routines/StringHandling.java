// ============================================================================
//
// %GENERATED_LICENSE%
//
// ============================================================================
package routines;

import java.math.BigDecimal;

public class StringHandling {

    /**
     * Determines whether the expression is sorted alphabetically or not.
     * 
     * {talendTypes} boolean | Boolean
     * 
     * {Category} StringHandling
     * 
     * {param} string("abcdefg") input: String need to be known whether is sorted alphabetically or not.
     * 
     * {example} ALPHA("abcdefg") # true
     */
    public static boolean ALPHA(String input) {
        if (input != null) {
            char[] val = input.toCharArray();

            for (int i = 0; i < val.length - 1; i++) {
                if (val[i] > val[i + 1]) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Determines whether the expression is an alphabetic or nonalphabetic.
     * 
     * {talendTypes} boolean | Boolean
     * 
     * {Category} StringHandling
     * 
     * {param} string("abc") input: String need to be known whether is an alphabetic or not.
     * 
     * {example} ALPHA("abc") # true
     */
    public static boolean IS_ALPHA(String input) {
        if (input != null) {
            char[] val = input.toCharArray();

            for (int i = 0; i < val.length; i++) {
                if (!Character.isLetter(val[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Substitutes all substrings that match the given regular expression in the given old string with the given replacement and returns a new string.
     * @param oldStr: the old string.
     * @param regex: the regular expression to match.
     * @param replacement: the string to be substituted for every match.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("hello world!") oldStr: The whole string.
     * 
     * {param} string("world") regex: Regx.
     * 
     * {param} string("guy") replacement: Replacement.
     * 
     * {example} CHANGE("hello world!","world","guy") # hello world
     */
    public static String CHANGE(String oldStr, String regex, String replacement) {
        if (oldStr == null || regex == null || replacement == null)
        	return oldStr;
        else 
        	return oldStr.replaceAll(regex, replacement);
    }

    /**
     * Evaluates the number of times a substring is repeated in a string.
     * 
     * {talendTypes} int | Integer
     * 
     * {Category} StringHandling
     * 
     * {param} string("hello world!") string: The whole string.
     * 
     * {param} string("world") subString: subString.
     * 
     * {example} COUNT("hello world!","world") # 1
     */
    public static int COUNT(String string, String subString) {
    	if (string == null || subString == null){
    		return 0;
    	} else{
	        int counter = 0;
	        int i = -1;
	        while ((i = string.indexOf(subString, i + 1)) != -1) {
	            counter++;
	        }
	        return counter;
    	}
    }

    /**
     * Converts all uppercase letters in an expression to lowercase.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("Hello") string: String
     * 
     * {example} DOWNCASE("Hello") # hello
     */
    public static String DOWNCASE(String string) {
         return string == null ? null : string.toLowerCase();
    }

    /**
     * Converts all lowercase letters in an expression to uppercase.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("Hello") string: String
     * 
     * {example} UPCASE("Hello") # HELLO
     */
    public static String UPCASE(String string) {
        return string == null ? null : string.toUpperCase();
    }

    /**
     * Encloses an expression in double quotation marks.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("Hello") string: String
     * 
     * {example} DQUOTE("hello") # "hello"
     */
    public static String DQUOTE(String string) {
        return string == null ? null : ("\"" + string + "\""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Substitutes all substrings that match the given regular expression in the given old string with the given replacement and returns a new string.
     * @param oldStr: the old string.
     * @param regex: the regular expression to match.
     * @param replacement: the string to be substituted for every match.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("hello world!") oldStr: The whole string.
     * 
     * {param} string("world") regex: Regx.
     * 
     * {param} string("guy") replacement: Replacement.
     * 
     * {example} EREPLACE("hello world!","world","guy") # hello world
     */
    public static String EREPLACE(String oldStr, String regex, String replacement) {
        return CHANGE(oldStr, regex, replacement);
    }

    /**
     * Returns the starting column position of a specified occurrence of a particular substring within a string
     * expression.
     * 
     * {talendTypes} int | Integer
     * 
     * {Category} StringHandling
     * 
     * {param} string("hello world!") string: string.
     * 
     * {param} string("hello") element: element
     * 
     * {example} INDEX("hello world!","hello") # 0
     */
    public static int INDEX(String string, String element) {
    	if (string == null || element == null)
    		return -1;
    	else
    		return string.indexOf(element);
    }

    /**
     * Specifies a substring consisting of the first n characters of a string.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("hello world!") string: String.
     * 
     * {param} int(5) index : index
     * 
     * {example} LEFT("hello world!",5) # hello
     */
    public static String LEFT(String string, int index) {
        return string == null ? null : string.substring(0, Math.min(string.length(), index));
    }

    /**
     * Specifies a substring consisting of the last n characters of a string.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("hello world!") string: String
     * 
     * {param} int(6) index : Index
     * 
     * {example} RIGHT("hello world!",6) # world!
     */
    public static String RIGHT(String string, int index) {
        return string == null ? null : string.substring(string.length() - Math.min(string.length(), index));
    }

    /**
     * Calculates the length of a string.
     * 
     * {talendTypes} int | Integer
     * 
     * {Category} StringHandling
     * 
     * {param} string("hello world!") string:
     * 
     * {example} LEN("hello world!") # 12
     */
    public static int LEN(String string) {
        return string == null ? -1 : string.length();
    }

    /**
     * Generates a string consisting of a specified number of blank spaces.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} int(2) i: amount of blank space need to generate.
     * 
     * {example} SPACE(2) # " "
     */
    public static String SPACE(int i) {
        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < i; j++) {
            buffer.append(" "); //$NON-NLS-1$
        }
        return buffer.toString();
    }

    /**
     * Encloses an expression in single quotation marks.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("hellow world!") string:
     * 
     * {example} SQUOTE("hellow world!") # 'hello world!'
     */
    public static String SQUOTE(String string) {
        return string == null ? null : ("'" + string + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Generates a particular character string a specified number of times.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string('a') string: character
     * 
     * {param} int(6) int: times
     * 
     * {example} SPACE("hellow world!",2) # hello world!
     */
    public static String STR(char letter, int i) {
        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < i; j++) {
            buffer.append(letter);
        }
        return buffer.toString();
    }

    /**
     * Deletes extra blank spaces and tabs from a character string.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string(" hellow world! ") string: string.
     * 
     * {example} TRIM(" hellow world! ") # hello world!
     */
    public static String TRIM(String string) {
        return string == null ? null : string.trim();
    }

    /**
     * Deletes all blank spaces and tabs after the last nonblank character in an expression.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string("hellow world! ") string: string.
     * 
     * {example} BTRIM("hellow world! ") # helloworld!
     */
    public static String BTRIM(String string) {
    	if (string == null){
    		return null;
    	} else {
    		char[] val = string.toCharArray();
	        int len = val.length;
	        while (len > 0 && val[len - 1] <= ' ') {
	            len--;
	        }
	        return string.substring(0, len);
    	}
    }

    /**
     * Deletes all blank spaces and tabs up to the first nonblank character in an expression.
     * 
     * {talendTypes} String
     * 
     * {Category} StringHandling
     * 
     * {param} string(" hellow world!") string: string.
     * 
     * {example} FTRIM(" hellow world!") # hello world!
     */
    public static String FTRIM(String string) {
    	if (string == null){
    		return null;
    	} else {
	        char[] val = string.toCharArray();
	        int st = 0;
	        int len = val.length;
	        while ((st < len) && (val[st] <= ' ')) {
	            st++;
	        }
	        return string.substring(st);
    	}
    }
    

	/**
	 * 
	 * @param string : Passes the strings you want to search.
	 * @param start : Must be an integer. The position in the string where you want to start counting.
	 * @param length : Must be an integer greater than 0. The number of characters you want SUBSTR to return
	 * @return
	 * {example} SUBSTR("This is a test.",1,5) #his i
	 */
	public static String SUBSTR(String string, int start, Integer length) {

		if (isVacant(string)) {
			return null;
		}
		if (start > string.length()) {
			return "";
		}
		if (start > 0) {
			start--;
		} else if (start < 0) {
			start = string.length() + start;
			if (start < 0) {
				start = 0;
			}
		}
		String result;
		if (length == null || (length + start) > string.length()) {
			result = string.substring(start);
		} else if (length <= 0) {
			return "";
		} else {
			result = string.substring(start, length + start);
		}
		return result;

	}
	
	/**
	 * 
	 * @param value : Any string value. Passes the strings you want to modify.
	 * @param trim_set : Any string value. Passes the characters you want to remove from the end of the string.
	 * @return
	 * {example} LTRIM("aatestaa","a") #testaa
	 */
	public static String LTRIM(String value, String trim_set) {
        if (isVacant(value)) {
            return null;
        }
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();
        if (isVacant(trim_set)) {

            while ((st < len) && (val[st] <= ' ')) {
                st++;
            }
            return st > 0 ? value.substring(st) : value;
        } else {
            char[] chars = trim_set.toCharArray();
            do {
                for (char c : chars) {
                    for (; value.indexOf(c, st) == st; st++);
                }
                if(st==len){
                    return "";
                }
            }while(trim_set.contains(String.valueOf(value.charAt(st))));

            return st > 0 ? value.substring(st) : value;
        }

    }

	public static String LTRIM(String value) {
		return LTRIM(value, null);
	}
	
	/**
	 * 
	 * @param value : Any string value. Passes the strings you want to modify.
	 * @param trim_set : Any string value. Passes the characters you want to remove from the beginning of the first string
	 * @return
	 * {example} RTRIM("aatestaa","a") #aatest
	 */
	public static String RTRIM(String value, String trim_set) {
        if (isVacant(value)) {
            return null;
        }
        int len = value.length();
        char[] val = value.toCharArray();
        if (isVacant(trim_set)) {

            while ((0 < len) && (val[len - 1] <= ' ')) {
                len--;
            }
            return len < value.length() ? value.substring(0, len) : value;
        } else {
            
            char[] chars = trim_set.toCharArray();
            
            do {
                for (int i = chars.length-1;i>=0;i--) {
                    for (; value.lastIndexOf(chars[i]) == len-1; len--){
                        if(len==0){
                            return "";
                        }
                        value = value.substring(0, len-1);
                        }
                }
            }while(trim_set.contains(value.substring(value.length()-1)));

            return value;

        }

    }

	public String RTRIM(String value) {
		return RTRIM(value, null);
	}
	
	/**
	 * 
	 * @param first_string : The strings you want to change.
	 * @param length : Must be a positive integer literal. Specifies the length you want each string to be.
	 * @param second_string : Can be any string value. The characters you want to append to the left-side of the first_string values.
	 * @return
	 * {example} LPAD("test",6,"a") #aatest
	 */
    public static String LPAD(String first_string, int length, String second_string) {

    	 if (isVacant(first_string) || length < 1) {
             return null;
         }

         int OriginLength = first_string.length();
         if (OriginLength >= length) {
             return first_string;
         }
         
         if (isVacant(second_string)) {
             StringBuilder result = new StringBuilder(first_string);
             for (int i = OriginLength; i < length; i++) {
                 result.append(" ");
             }
             return result.toString();
         } else {
             StringBuilder result = new StringBuilder(second_string);
             for (int len = result.length(); len < length - OriginLength; result.append(second_string))
                 len = result.length();
             
             first_string = result.substring(0, length - OriginLength)+first_string;
             return first_string;
         }
    }
	
	/**
	 * 
	 * @param first_string : The strings you want to change.
	 * @param length : Must be a positive integer literal. Specifies the length you want each string to be.
	 * @param second_string : Any string value. Passes the string you want to append to the right-side of the first_string values.
	 * @return
	 * {example} RPAD("test",6,"a") #testaa
	 */
	public static String RPAD(String first_string, int length, String second_string) {

		if (isVacant(first_string) || length < 1) {
            return null;
        }

        int OriginLength = first_string.length();
        if (OriginLength >= length) {
            return first_string;
        }
        StringBuilder result = new StringBuilder(first_string);
        for (int i = OriginLength; i < length; i++) {
            if (isVacant(second_string)) {
                result.append(" ");
            } else {
                result.append(second_string);
                if(result.length()>length){
                    return result.toString().substring(0, length);
                }
            }
        }

        return result.toString();
	}

	public String RPAD(String first_string, int length) {
		return RPAD(first_string, length, null);
	}
	
	
	
    /**
     * 
     * @param string Can be a String
     * @param search_value Can be a String 
     * @param start 
     * 		The default is 1, meaning that INSTR starts the search at the first character in the string.
     * @param occurrence
     * 		If the search value appears more than once in the string, you can specify which occurrence you want to search for.
     * @param comparison_type
     * 		default 0: INSTR performs a linguistic string comparison. 1: INSTR performs a binary string comparison.
     * @return
     * {example} new StringHandling<String>.INSTR("This is a test","t",1,2,0) #14
     */
	
    public static Integer INSTR(String string, String search_value, Integer start, Integer occurrence) {
    	// linguistic string comparison.

        int defaultStart = 1;
        int defaultOccurrence = 1;
        Integer result = 0;
        
        if (isVacant(string) || isVacant(search_value)|| Math.abs(defaultStart) >= string.length()) {
            return null;
        }

        if (start != null && start != 0) {
            defaultStart = start;
        }
        if (occurrence != null) {
            if (occurrence <= 0) {
                throw new IllegalArgumentException(
                        "The occurrence argument can only accept a positive integer greater than 0.");
            }
            defaultOccurrence = occurrence;
        }

        if (defaultStart < 0) {
            string = string.substring(0, string.length() + defaultStart + 1);
            int temp = string.lastIndexOf(search_value);
            while (temp != -1 && defaultOccurrence != 1) {
                string = string.substring(0, temp);
                defaultOccurrence--;
                temp = string.lastIndexOf(search_value);
            }
            return ++temp;

        } else {
            string = string.substring(defaultStart - 1);

            if (defaultOccurrence != 1) {
                int temp;
                do {
                    temp = string.indexOf(search_value) + 1;
                    string = string.substring(temp);
                    result += temp;
                    defaultOccurrence--;
                } while (defaultOccurrence != 0);
                if (temp == 0) {
                    result = 0;
                }

            } else {
                result = string.indexOf(search_value) + 1;
            }

            return result;
        }

    }
    
    /**
     * 
     * @param string Can be a byte[]
     * @param search_value Can be byte[]
     * @param start 
     * 		The default is 1, meaning that INSTR starts the search at the first character in the string.
     * @param occurrence
     * 		If the search value appears more than once in the string, you can specify which occurrence you want to search for.
     * @param comparison_type
     * 		default 0: INSTR performs a linguistic string comparison. 1: INSTR performs a binary string comparison.
     * @return
     * {example} new StringHandling<String>.INSTR("This is a test".getBytes(),"t".getBytes(),1,2,0) #14
     */

    public static Integer INSTR(byte[] string, byte[] search_value, Integer start, Integer occurrence) {

        int defaultStart = 1;
        int defaultOccurrence = 1;

        // binary string comparison
        if (string == null || search_value == null || Math.abs(defaultStart) >= string.length) {
            return null;
        }
        if (start != null && start != 0) {
            defaultStart = start;
        }
        int max = string.length - 1;

        if (occurrence != null) {
            if (occurrence <= 0) {
                throw new IllegalArgumentException(
                        "The occurrence argument can only accept a positive integer greater than 0.");
            }
            defaultOccurrence = occurrence;
        }

        if (defaultStart > 0) {
            return byteINSTR(string, search_value, defaultStart, defaultOccurrence);
        } else {
            int total = string.length;
            byte[] revers = new byte[total];
            byte[] revers_search = new byte[search_value.length];
            for (int i = 0; i < total; i++)
                revers[max - i] = string[i];
            for (int i = 0; i < search_value.length; i++)
                revers_search[search_value.length - 1 - i] = search_value[i];
            defaultStart = -defaultStart;
            int result = byteINSTR(revers, revers_search, defaultStart, defaultOccurrence);
            if (result == 0) {
                return 0;
            }

            return total - result + 2 - search_value.length;
        }
    }

    private static int byteINSTR(byte[] string, byte[] search_value, int defaultStart, int defaultOccurrence) {
        int max = string.length - 1;
        for (int i = defaultStart - 1; i <= max; i++) {
            /* Look for first character. */
            if (string[i] != search_value[0]) {
                while (++i <= max && string[i] != search_value[0])
                    ;
            }
            if (i > max) {
                return 0;
            }
            if (search_value.length > 1) {

                /* Found first character, now look at the rest of v2 */
                if (i <= max) {
                    int j = i;
                    int end = i + search_value.length - 1;
                    for (int k = 1; j + 1 <= max && k < search_value.length
                            && string[j + 1] == search_value[k]; j++, k++)
                        ;

                    if (j == end) {
                        /* Found whole string. */
                        if (defaultOccurrence == 1) {
                            return i - defaultStart + 2;
                        } else {
                            defaultOccurrence--;
                            continue;
                        }
                    }
                }
            } else {
                if (defaultOccurrence == 1) {
                    return i - defaultStart + 2;
                } else {
                    defaultOccurrence--;
                    continue;
                }
            }
        }
        return 0;
    }
    
	protected static boolean isVacant(String value) {
		return value == null || "".equals(value);
	}
	
    /**
     * 
     * @param numeric_value
     *            Numeric datatype. The numeric value you want to convert to a
     *            string
     * @return String. NULL if a value passed to the function is NULL.
     * 
     *         Converts double values to strings of up to 16 digits and provides
     *         accuracy up to 15 digits. If you pass a number with more than 15
     *         digits, TO_CHAR rounds the number to the sixteenth digit.
     *         Returns decimal notation for numbers in the ranges (-1e16,-1e-16]
     *         and [1e-16, 1e16). TO_CHAR returns scientific notation for
     *         numbers outside these ranges.
     */

    public static String TO_CHAR(Object numeric_value) {
        if(numeric_value==null){
            return null;
        }
        
        BigDecimal bigDecimal = new BigDecimal(numeric_value.toString());
        if(bigDecimal.abs().compareTo(new BigDecimal("1e16"))<0&&bigDecimal.abs().compareTo(new BigDecimal("1e-16"))>0){
            return bigDecimal.toPlainString();
        }
        
        return numeric_value.toString();
    }

}
