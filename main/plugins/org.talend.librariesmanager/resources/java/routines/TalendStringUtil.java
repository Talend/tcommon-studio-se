package routines;

import java.util.HashMap;
import java.util.Map;

public class TalendStringUtil<T,Y> {
	
    /**
     * 
     * @param string Can be a String or byte[]
     * @param search_value Can be a String or byte[], should be 
     * @param start 
     * 		The default is 1, meaning that INSTR starts the search at the first character in the string.
     * @param occurrence
     * 		If the search value appears more than once in the string, you can specify which occurrence you want to search for.
     * @param comparison_type
     * 		default 0: INSTR performs a linguistic string comparison. 1: INSTR performs a binary string comparison.
     * @return
     * {example} new StringHandling<String>.INSTR("This is a test","t",1,2,0) #14
     */
	public Integer INSTR(T string, T search_value, Integer start, Integer occurrence, Integer comparison_type) {
		String linguistic_origin = null;
		byte[] binary_origin = null;
		String linguistic_search_value = null;
		byte[] binary_search_value = null;
		int defultStart = 1;
		int defultOccurrence = 1;
		int defultComparison_type = 0;

		if (search_value != null) {
			if (search_value instanceof String) {
				linguistic_search_value = (String) search_value;
				binary_search_value = ((String) search_value).getBytes();
			} else if (search_value instanceof byte[]) {
				linguistic_search_value = new String((byte[]) search_value);
				binary_search_value = (byte[]) search_value;
			} else {
				throw new RuntimeException("Please enter a vaild 'search_value' or 'binary search_value'.");
			}
		}

		if (start != null && start != 0) {
			defultStart = start;
		}
		if (occurrence != null) {
			if (occurrence <= 0) {
				throw new RuntimeException(
						"The occurrence argument can only accept a positive integer greater than 0.");
			}
			defultOccurrence = occurrence;
		}

		if (comparison_type != null) {
			if (comparison_type == 0) {
				linguistic_origin = (String) string;
			} else if (comparison_type == 1) {
				binary_origin = (byte[]) string;
			} else {
				throw new RuntimeException("The comparison_type argument can only be either 0 or 1.");
			}
			defultComparison_type = comparison_type;
		} else {
			linguistic_origin = (String) string;
		}

		Integer result = 0;

		if (defultComparison_type == 0) {// linguistic string comparison.
			if (linguistic_origin == null || linguistic_origin.equals("") || linguistic_search_value == null
					|| linguistic_search_value.equals("") || Math.abs(defultStart) >= linguistic_origin.length()) {
				return null;
			}
			if (defultStart < 0) {
				linguistic_origin = linguistic_origin.substring(0, linguistic_origin.length() + defultStart);
				result += defultStart;
			} else {
				linguistic_origin = linguistic_origin.substring(defultStart - 1);
			}

			if (defultOccurrence != 1) {
				int temp;
				do {
					temp = linguistic_origin.indexOf(linguistic_search_value) + 1;
					linguistic_origin = linguistic_origin.substring(temp);
					result += temp;
					defultOccurrence--;
				} while (defultOccurrence != 0);
				if (temp == 0) {
					result = 0;
				}

			} else {
				result = linguistic_origin.indexOf(linguistic_search_value) + 1;
			}
		} else {
			// binary string comparison
			if (binary_origin == null || binary_search_value == null || Math.abs(defultStart) >= binary_origin.length) {
				return null;
			}
			int max = binary_origin.length - 1;
			if (defultStart < 0) {
				max = max + defultStart;
				defultStart = 1;
			}

			for (int i = defultStart - 1; i <= max; i++) {
				/* Look for first character. */
				if (binary_origin[i] != binary_search_value[0]) {
					while (++i <= max && binary_origin[i] != binary_search_value[0])
						;
				}
				if (binary_search_value.length > 1) {

					/* Found first character, now look at the rest of v2 */
					if (i <= max) {
						int j = i + 1;
						int end = j + binary_search_value.length - 1;
						for (int k = 1; j < max && binary_origin[j] == binary_search_value[k]; j++, k++)
							;

						if (j == end) {
							/* Found whole string. */
							if (defultOccurrence == 1) {
								return i - binary_search_value.length + 2;
							} else {
								defultOccurrence--;
								continue;
							}
						}
					}
				} else {
					if (defultOccurrence == 1) {
						return i - binary_search_value.length + 2;
					} else {
						defultOccurrence--;
						continue;
					}
				}
			}
			return 0;

		}

		return result;
	}
	
	public Integer INSTR(T string, T search_value) {
		return INSTR(string, search_value, null, null, null);
	}
	
	/**
	 * Searches a port for a value you specify. If the function finds the value, it returns a result value, which you define.
	 *  You can build an unlimited number of searches within a DECODE function.
	 * 
	 * @param value : Passes the values you want to search
	 * @param defaultValue : The value you want to return if the search does not find a matching value.
	 * @param search : a Map contains the search-value & result-value.
	 * @return result-value if the search finds a matching value. Default-value if the search does not find a matching value.
	 * 
	 *  PS:You cannot create a DECODE function with both string and numeric return
	 * values. When you validate the expression above, you receive the following
	 * mismatching datatypes.
	 */

	public Y DECODE(T value, Y defaultValue, Map<T, Y> search) {
		if (search.containsKey(value)) {
			return search.get(value);
		} else {
			return defaultValue;
		}
	}

	// this is more fast
	public String DECODE(String value, String defaultValue, Map<String, String> search) {
		if (search.containsKey(value)) {
			return search.get(value);
		} else {
			return defaultValue;
		}
	}	
	
	/**
	 * Searches a port for a value you specify. If the function finds the value, it returns a result value, which you define. 
	 * You can build an unlimited number of searches within a DECODE function.
	 * 
	 * @param value : Passes the values you want to search
	 * @param defaultValue : The value you want to return if the search does not find a matching value.
	 * @param search : a Map contains the search-value & result-value.
	 * @param searchAndResult : pairs of search-value & result-value. You can enter one or more pairs of values.
	 * @return result-value if the search finds a matching value. Default-value if the search does not find a matching value.
	 */

	public Y DECODE(T value, Y defaultValue, T... searchAndResult) {
		if (searchAndResult.length % 2 != 0) {
			throw new IllegalArgumentException("Parameter searchAndResult should be in pair.");
		}
		Map<T, Y> search = new HashMap<T, Y>();
		for (int i = 0; i < searchAndResult.length; i += 2) {
			search.put(searchAndResult[i], (Y) searchAndResult[i + 1]);
		}
		return DECODE(value, defaultValue, search);
	}

	public String DECODE(String value, String defaultValue, String... searchAndResult) {
		if (searchAndResult.length % 2 != 0) {
			throw new IllegalArgumentException("Parameter searchAndResult should be in pair.");
		}
		Map<String, String> search = new HashMap<String, String>();
		for (int i = 0; i < searchAndResult.length; i += 2) {
			search.put(searchAndResult[i], searchAndResult[i + 1]);
		}
		return DECODE(value, defaultValue, search);
	}

}
