package routines.system;

/**
 * store some global constant
 * 
 * @author Administrator
 *
 */
public class Constant {

    /**
     * the default pattern for date parse and format
     */
    public static final String dateDefaultPattern = "dd-MM-yyyy";

    /**
     * the default user agent string for AWS and Azure components
     */
    public static String getUserAgent(String studioVersion) {
        return  "APN/1.0 Talend/" + studioVersion + " Studio/" + studioVersion;
    }
    
    /**
     * the default user agent string for GCS components
     */
    public static String getUserAgentGCS(String studioVersion) {
        return "Studio/" + studioVersion + " (GPN:Talend) DataIntegration/" + studioVersion + " Jets3t/0.9.1";
    }
}
