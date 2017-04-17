package routines;

import java.util.HashMap;
import java.util.Map;

public class TalendStringUtil<T,Y> {
	
	
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
	
			return search.containsKey(value)?search.get(value):defaultValue;
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
    @SuppressWarnings("unchecked")
    public Y DECODE(T value, Y defaultValue, Object... searchAndResult) {
        if (searchAndResult.length % 2 != 0) {
            throw new IllegalArgumentException("Parameter searchAndResult should be in pair.");
        }
        Map<T, Y> search = new HashMap<T, Y>();
        for (int i = 0; i < searchAndResult.length; i += 2) {
            search.put((T)searchAndResult[i], (Y)searchAndResult[i + 1]);
        }
        return DECODE(value, defaultValue, search);
    }

}
