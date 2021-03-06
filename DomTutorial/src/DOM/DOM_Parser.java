package DOM;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//aid:https://www.youtube.com/watch?v=HfGWVy-eMRc

public class DOM_Parser {
	public static void main( String args[] )
	  {
		DocumentBuilderFactory myFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = myFactory.newDocumentBuilder();
			
			Document doc = builder.parse("test4.8.xml");
			
			NodeList wordList = doc.getElementsByTagName("entry");
			
			for(int i = 0; i < wordList.getLength();i++)
			{
				Node w = wordList.item(i);
				
				if(w.getNodeType() == Node.ELEMENT_NODE)
				{
					Element word = (Element) w;
					
					String id = word.getAttribute("def");
					NodeList defList = word.getChildNodes();
					
					for(int j = 0; j < defList.getLength();j++)
					{
						Node def = defList.item(j);
						if(def.getNodeType() == Node.ELEMENT_NODE)
						{
							Element fulldef = (Element) def;
							System.out.println("Entry" + id +":" + fulldef.getTagName()+ 
									"-" + fulldef.getTextContent());
						}
						
					}
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

}
