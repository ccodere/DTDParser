package org.xmlmiddleware;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlmiddleware.schemas.dtds.Attribute;
import org.xmlmiddleware.schemas.dtds.DTD;
import org.xmlmiddleware.schemas.dtds.DTDParser;
import org.xmlmiddleware.schemas.dtds.ElementType;
import org.xmlmiddleware.xmlutils.XMLName;

public class DTDParserTest extends TestCase
{

  /** Class that resolves external entities stored
   *  as resources. 
   *
   */
  public class localEntityResolver implements EntityResolver
  {
    public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException
    {
      String finalPath = "res/"+systemId;
      InputStream is = getClass().getClassLoader().getResourceAsStream(finalPath);
      return new InputSource(is);
    }
    
  }

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
  
  
  public void testComment01()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/testcmt01.dtd");
    
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
    
   ElementType id = (ElementType) dtd.elementTypes.get(XMLName.create(null,"id"));
   assertNotNull(id);
   assertEquals(ElementType.CONTENT_PCDATA,id.contentType);
   
  }
  
  public void testComment02()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/testcmt02.dtd");
    
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
    
   ElementType id = (ElementType) dtd.elementTypes.get(XMLName.create(null,"id"));
   assertNotNull(id);
   assertEquals(ElementType.CONTENT_PCDATA,id.contentType);
   
  }
  
  
  public void testEADDTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/ead.dtd");
    
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
  }
  
  public void testSVGDTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/svg.dtd");
    
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
  }
  
  
  public void testTest01DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/test1.dtd");
    
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
  }
  
  
  public void testTest02DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/test2.dtd");
    
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
  }
  
  
  public void testTest02aDTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/test2a.dtd");
    
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
  }
  
  
  public void testTest03DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/test3.dtd");
    
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
  }

  public void testTest04DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/test4.dtd");
    
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
  }
  
  public void testTest05DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/test5.dtd");
    
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
  }
  
  public void testTestPE1DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/testPE1.dtd");
    
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
  }
  
  public void testTestPE2DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/testPE2.dtd");
    
    DTDParser dtdParser = new DTDParser();
    
    InputSource input = new InputSource(is);
    try
    {
      dtd = dtdParser.parseExternalSubset(input, null, new localEntityResolver());
    } catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
  }
  
  public void testTestPE3DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/testPE3.dtd");
    
    DTDParser dtdParser = new DTDParser();
    
    InputSource input = new InputSource(is);
    try
    {
      Hashtable namespacesURI = new Hashtable();
      namespacesURI.put("epub", "http://www.idpf.org/2007/ops");
      dtd = dtdParser.parseExternalSubset(input, namespacesURI, new localEntityResolver());
      XMLName elementName = XMLName.create(null, "h1");
      ElementType el = dtd.elementTypes.get(elementName);
      XMLName epubTypeAttr = XMLName.create("http://www.idpf.org/2007/ops", "type", "epub"); 
      Attribute attr = el.attributes.get(epubTypeAttr);
      assertNotNull(attr);
    } catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
  }
  

  public void testXMLDBMS2DTD()
  {
    DTD dtd = null;
    String nameofCurrMethod = new Exception().getStackTrace()[0].getMethodName();
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("res/xmldbms2.dtd");
    
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
  }
  

}
