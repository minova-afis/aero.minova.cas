package aero.minova.cas.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtils {
	/**
	 * Create document from XML bytes
	 */
	public static Document getDocument(byte[] xmlData) throws ParserConfigurationException, SAXException, IOException {
    	if(xmlData == null || xmlData.length == 0)
    		return null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false); // No DTD validation! As it will produce connection timeouts on customer systems without internet connection!
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		DocumentBuilder builder = factory.newDocumentBuilder();
    	InputSource is = new InputSource(new ByteArrayInputStream(xmlData));
    	is.setEncoding("UTF-8"); // set your encoding here
    	Document toRet = builder.parse(is);
    	//optional, but recommended
    	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    	toRet.getDocumentElement().normalize();
    	return toRet;
	}
	
	/** Recursively searches for elements with given tag-name
     */
    public static List<Element> findElement(Element el, String tagName) {
    	return findElement(el, tagName, null);
    }
    
	/** Recursively searches for elements with given tag-name
     */
    private static List<Element> findElement(Element el, String tagName, List<Element> into) {
    	if(into == null)
    		into = new LinkedList<>();
        if (el == null || tagName == null)
            return null;

        if(el.getTagName().equalsIgnoreCase(tagName))
        	into.add(el);
        for(Element ch : toArray(el.getChildNodes()))
            findElement(ch, tagName, into);
        return into;	
    }

	/** Find all elements that have given attribute (values)
     * @param el Starting element
     * @param key Desired attribute. It must be present
     * @param value If not null, also the value of the attribute must match
     */
    public static List<Element> findElementWithAttribute(Element el, String key, Function<String, Boolean> valueAcceptor) {
    	return findElementWithAttribute(el, key, valueAcceptor, null);
    }
    
	/** Find all elements that have given attribute (values)
     * @param el Starting element
     * @param key Desired attribute. It must be present
     * @param value If not null, also the value of the attribute must match
     */
    public static List<Element> findElementWithAttribute(Element el, String key, Function<String, Boolean> valueAcceptor, List<Element> into) {
    	if(into == null)
    		into = new LinkedList<>();
        if (el == null || key == null)
            return null;
        
    	if(el.hasAttribute(key) && (valueAcceptor == null || valueAcceptor.apply(el.getAttribute(key))))
    		into.add(el);
        
    	for(Element ch : toArray(el.getChildNodes()))
    		findElementWithAttribute(ch, key, valueAcceptor, into);
        
        return into;	
    }
    
	/**
     * Converts a nodelist into element
     */
    public static List<Element> toArray(NodeList nl) {
        List<Element> toRet = new LinkedList<>();
        if (nl == null)
            return toRet;
        Node n;
        for (int i = 0; i < nl.getLength(); i++) {
            n = nl.item(i);
            if (n instanceof Element)
                toRet.add((Element) n);
        }
        return toRet;
    }
    
	
	/** Return the document as bytes
	 * @throws TransformerException 
	 */
	public static byte[] toBytes(Document doc) throws TransformerException {
		if(doc == null)
			return new byte[0];
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // BUG, jre 5
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        
	    DOMSource source = new DOMSource(doc);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    StreamResult result = new StreamResult(baos);
	    transformer.transform(source, result);
	    return baos.toByteArray();
	}
	
	private XMLUtils() {
	}
}