package parsed;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParseHandler extends DefaultHandler  {

	/*
	 *  Declarations to be used in overrided methods
	 */
	
	//word object to return and modify
	private Word parsedWord;
	private boolean ewTag;
	private boolean defTag;
	
	SAXParseHandler()
	{
		super();
		parsedWord = new Word();
		ewTag = false;
		defTag = false;
	}
	/*
	 *  Read From File
	 *  
	 *  @param: filename
	 *  @return: word object
	 */
	public Word readDataFromXML(String filename)
	{
		return parsedWord;
	}
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		System.out.println("Starting Document Processsing ...\n");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equalsIgnoreCase("ew"))
		{
			StringBuilder tmpBuilder = new StringBuilder();  
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		System.out.println("Starting Document Processsing ...\n");
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

}
