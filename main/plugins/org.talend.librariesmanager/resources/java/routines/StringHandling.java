// ============================================================================
//
// %GENERATED_LICENSE%
//
// ============================================================================
package routines;

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
	public String SUBSTR(String string, int start, Integer length) {

		if (string == null) {
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
	public String LTRIM(String value, String trim_set) {
		if (value == null) {
			return null;
		}
		int len = value.length();
		int st = 0;
		char[] val = value.toCharArray();
		if (trim_set == null) {

			while ((st < len) && (val[st] <= ' ')) {
				st++;
			}
			return st > 0 ? value.substring(st) : value;
		} else {
			while (value.indexOf(trim_set, st) == st) {
				st += trim_set.length();
			}
			return st > 0 ? value.substring(st) : value;
		}

	}

	public String LTRIM(String value) {
		return LTRIM(value, null);
	}
	
	/**
	 * 
	 * @param value : Any string value. Passes the strings you want to modify.
	 * @param trim_set : Any string value. Passes the characters you want to remove from the beginning of the first string
	 * @return
	 * {example} RTRIM("aatestaa","a") #aatest
	 */
	public String RTRIM(String value, String trim_set) {
		if (value == null) {
			return null;
		}
		int len = value.length();
		char[] val = value.toCharArray();
		if (trim_set == null) {

			while ((0 < len) && (val[len - 1] <= ' ')) {
				len--;
			}
			return len < value.length() ? value.substring(0, len) : value;
		} else {
			int temp = 0;
			while (value.lastIndexOf(trim_set) == len - trim_set.length()) {
				len -= trim_set.length();
				value = value.substring(0, len);
			}
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
	public String LPAD(String first_string, int length, String second_string) {

		if (first_string == null || length < 1) {
			return null;
		}

		int OriginLength = first_string.length();
		if (OriginLength >= length) {
			return first_string;
		}
		for (int i = OriginLength; i < length; i++) {
			if (second_string == null) {
				first_string = " " + first_string;
			} else {
				first_string = second_string + first_string;
				if(first_string.length()>length){
					first_string = first_string.substring(first_string.length()-length);
				}
			}
		}

		return first_string;
	}

	public String LPAD(String first_string, int length) {
		return LPAD(first_string, length, null);
	}
	
	/**
	 * 
	 * @param first_string : The strings you want to change.
	 * @param length : Must be a positive integer literal. Specifies the length you want each string to be.
	 * @param second_string : Any string value. Passes the string you want to append to the right-side of the first_string values.
	 * @return
	 * {example} RPAD("test",6,"a") #testaa
	 */
	public String RPAD(String first_string, int length, String second_string) {

		if (first_string == null || length < 1) {
			return null;
		}

		int OriginLength = first_string.length();
		if (OriginLength >= length) {
			return first_string;
		}
		for (int i = OriginLength; i < length; i++) {
			if (second_string == null) {
				first_string = first_string + " ";
			} else {
				first_string = first_string + second_string;
				if(first_string.length()>length){
					first_string = first_string.substring(0, length);
				}
			}
		}

		return first_string;
	}

	public String RPAD(String first_string, int length) {
		return RPAD(first_string, length, null);
	}

}
