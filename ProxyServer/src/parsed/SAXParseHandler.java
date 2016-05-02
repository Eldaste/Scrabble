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
	StringBuilder tmpBuilder;
	
	public SAXParseHandler()
	{
		super();
		parsedWord = new Word();
		ewTag = false;
		defTag = false;
		tmpBuilder = new StringBuilder();
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
			tmpBuilder = new StringBuilder();
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		tmpBuilder.append(ch);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		System.out.println("Starting Document Processsing ...\n");
		if(ewTag)
		{
			parsedWord.setWord(tmpBuilder.toString());
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

}
