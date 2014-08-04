package routines.system;

/**
 * talend pig component helper
 * 
 * @author Administrator
 * 
 */
public class PigHelper {

    private java.util.List<String[]> pigLatins = new java.util.ArrayList<String[]>();

    public java.util.List<String[]> getPigLatins() {
        return this.pigLatins;
    }

    public void add(String type, String pigLatin) {
        String[] pl = new String[2];
        pl[0] = type;
        pl[1] = pigLatin;
        pigLatins.add(pl);
    }

    public void add(String type, String alias, String function) {
        String[] pl = new String[3];
        pl[0] = type;
        pl[1] = alias;
        pl[2] = function;
        pigLatins.add(pl);
    }

}
