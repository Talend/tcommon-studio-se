package routines.system;

import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * helper for xml source
 * @author Administrator
 *
 */
public class XMLHelper {
	
	private static XMLHelper instance;
	
	private XMLReader reader;

	private XMLHelper() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
	    try {
			reader = factory.newSAXParser().getXMLReader();
			reader.setErrorHandler(null);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static XMLHelper getInstance() {
		if(instance == null) {
			instance = new XMLHelper();
		}
		return instance;
	}
	
	
	
	/**
	 * validate xml source
	 * return true if xml is well formed
	 * @param source
	 * @return
	 */
	public boolean isValid(String xml) {
		try {
			InputSource source = new InputSource(new StringReader(xml));
			reader.parse(source);
        	return true;
        } catch(Exception e) {
        	return false;
        }
	}
	
}
