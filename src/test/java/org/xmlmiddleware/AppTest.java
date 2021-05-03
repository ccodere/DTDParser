package org.xmlmiddleware;

import java.io.InputStream;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.xml.sax.InputSource;
import org.xmlmiddleware.schemas.dtds.DTD;
import org.xmlmiddleware.schemas.dtds.DTDParser;
import org.xmlmiddleware.schemas.dtds.ElementType;
import org.xmlmiddleware.xmlutils.XMLName;

public class AppTest extends TestCase
{


  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testSimpleElementTypes()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    System.out.print(nameofCurrMethod+": ");
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/test.dtd");
    
    DTDParser dtdParser = new DTDParser();
    
    InputSource input = new InputSource(is);
    try
    {
      dtd = dtdParser.parseExternalSubset(input, null, null);
    } catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
    
   ElementType elementType = (ElementType) dtd.elementTypes.get(XMLName.create(null,"bookList"));
   assertNotNull(elementType);
   Hashtable childrenElements = elementType.children;
   ElementType bookElement = (ElementType) childrenElements.get(XMLName.create(null,"book"));
   assertNotNull(bookElement);
   
   
   ElementType author = (ElementType) bookElement.children.get(XMLName.create(null,"author"));
   assertNotNull(author);
   assertEquals(ElementType.CONTENT_PCDATA,author.contentType);
   
   ElementType title = (ElementType) bookElement.children.get(XMLName.create(null,"title"));
   assertNotNull(title);
   assertEquals(ElementType.CONTENT_PCDATA,title.contentType);
   
   ElementType id = (ElementType) bookElement.children.get(XMLName.create(null,"id"));
   assertNotNull(id);
   assertEquals(ElementType.CONTENT_PCDATA,id.contentType);
   
  }

}
