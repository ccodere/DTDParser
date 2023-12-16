// This software is in the public domain.
//
// The software is provided "as is", without warranty of any kind,
// express or implied, including but not limited to the warranties
// of merchantability, fitness for a particular purpose, and
// noninfringement. In no event shall the author(s) be liable for any
// claim, damages, or other liability, whether in an action of
// contract, tort, or otherwise, arising from, out of, or in connection
// with the software or the use or other dealings in the software.

import org.xmlmiddleware.schemas.dtds.*;
import org.xmlmiddleware.xmlutils.XMLName;

import java.io.*;
import java.util.*;

import org.xml.sax.*;

/**
 * Generate classes from a DTD.
 *
 * <p>
 * The code writes a class definition for each complex element type -- that is,
 * each element type with attributes and/or element content. In the class, it
 * creates one property for each attribute and one property for each child
 * element. The data types of the properties are String (for attributes and
 * simple children) and class references (for complex children). Each class is
 * written to a file named &lt;element-type>.java.
 * </p>
 *
 * <p>
 * ClassGenerator is run from the command line using the following syntax:
 * </p>
 *
 * <pre>
 *    java ClassGenerator &lt;dtd-file>
 * </pre>
 *
 * @author Ronald Bourret
 */

public class ClassGenerator
{
  //**************************************************************************
  // Variables
  //**************************************************************************

  Writer out;

  //**************************************************************************
  // Constants
  //**************************************************************************

  private static final String NEWLINE = System.getProperty("line.separator");
  private static final String INDENT = "   ";

  //**************************************************************************
  // Constructors
  //**************************************************************************

  /** Construct a new ClassGenerator. */
  public ClassGenerator()
  {
  }

  //**************************************************************************
  // Public methods
  //**************************************************************************

  /**
   * Create a set of classes from a DTD.
   *
   * <p>
   * See the introduction for details.
   * </p>
   */
  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      System.out.println("Syntax: java ClassGenerator <dtd-file>");
      return;
    }

    ClassGenerator generator = new ClassGenerator();
    try
    {
      InputSource src = new InputSource(new FileReader(args[0]));
      generator.generateClasses(src);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  //**************************************************************************
  // Private methods -- process element type definitions
  //**************************************************************************

  private void generateClasses(InputSource src) throws Exception
  {
    DTDParser parser = new DTDParser();
    DTD dtd;

    Hashtable uris = new Hashtable();
    uris.put("xlink", "http://www.w3.org/1999/xlink");
    uris.put("", "http://www.w3.org/2000/svg");
    dtd = parser.parseExternalSubset(src, uris, null);
    processElementTypes(dtd);
  }

  private void processElementTypes(DTD dtd) throws Exception
  {
    Enumeration e;

    e = dtd.elementTypes.elements();
    while (e.hasMoreElements())
    {
      processElementType((ElementType) e.nextElement());
    }
  }

  private void processElementType(ElementType elementType) throws Exception
  {
    // Check if the element is treated as a class. If not, return and don't
    // process it now. Instead, we will process it when we encounter it in
    // each of its parents.

    if (!isClass(elementType))
      return;

    // Open a new FileWriter for the class file.

    out = new FileWriter(elementType.name.getLocalName() + ".java");

    // Create a class for the element type.

    out.write("public class ");
    out.write(elementType.name.getLocalName());
    out.write(NEWLINE);
    out.write("{");
    out.write(NEWLINE);

    // Process the attributes, adding one property for each.

    processAttributes(elementType.attributes);

    // Process the content, adding properties for each child element.

    switch (elementType.contentType)
    {
      case ElementType.CONTENT_ANY:
      case ElementType.CONTENT_MIXED:
        System.out
            .println("Can't process element types with mixed or ANY content. No code generated for the child elements of: "
                + elementType.name.getUniversalName());
        break;
      //            throw new Exception("Can't process element types with mixed or ANY content: " + elementType.name.getUniversalName());

      case ElementType.CONTENT_ELEMENT:
        processElementContent(elementType.content, elementType.children);
        break;

      case ElementType.CONTENT_PCDATA:
        processPCDATAContent(elementType);
        break;

      case ElementType.CONTENT_EMPTY:
        // No content to process.
        break;
    }

    // Close the class.

    out.write("}");
    out.write(NEWLINE);
    out.write(NEWLINE);

    // Close the file

    out.close();
  }

  private boolean isClass(ElementType elementType)
  {
    // If an element type has any attributes or child elements, it is
    // treated as a class. Otherwise, it is treated as a property.

    // BUG! This code actually misses a special case. If an element type
    // has no children, no attributes, and no parents, it needs to be
    // treated as a class. However, the corresponding XML document is:
    //
    //    <?xml version="1.0"?>
    //    <!DOCTYPE [<!ELEMENT foo EMPTY>]>
    //    <foo/>
    //
    // which really isn't worth worrying about...

    return (!elementType.children.isEmpty() || !elementType.attributes.isEmpty());
  }

  //**************************************************************************
  // Private methods - process attribute definitions
  //**************************************************************************

  private void processAttributes(Hashtable attributes) throws Exception
  {
    Enumeration e;
    Attribute attribute;

    // Add a property for each attribute of the element (if any).

    e = attributes.elements();
    while (e.hasMoreElements())
    {
      attribute = (Attribute) e.nextElement();
      addAttrProperty(attribute);
    }
  }

  //**************************************************************************
  // Private methods - process content
  //**************************************************************************

  private void processPCDATAContent(ElementType elementType) throws Exception
  {
    // This is the special case where the element type has attributes
    // but no child element types. In this case, we create a property
    // in the class for the PCDATA. (Hence, the argument is false, meaning
    // that the PCDATA is single-valued.)

    addPCDATAProperty(elementType);
  }

  private void processElementContent(Group content, Hashtable children) throws Exception
  {
    Enumeration e;
    ElementType child;
    Hashtable repeatInfo = new Hashtable();
    boolean repeatable;

    // Determine which element types-as-properties are repeatable. We
    // need this information to decide whether to map them to arrays or
    // single-valued properties.

    setRepeatInfo(repeatInfo, content, content.isRepeatable);

    // Process the children and either add class or scalar properties for them.

    e = children.elements();
    while (e.hasMoreElements())
    {
      child = (ElementType) e.nextElement();
      repeatable = ((Boolean) repeatInfo.get(child.name)).booleanValue();
      if (isClass(child))
      {
        addClassProperty(child, repeatable);
      }
      else
      {
        addElementTypeProperty(child, repeatable);
      }
    }
  }

  private void setRepeatInfo(Hashtable repeatInfo, Group content, boolean parentRepeatable)
  {
    Particle particle;
    boolean repeatable;
    ElementType child;

    for (int i = 0; i < content.members.size(); i++)
    {
      // Get the content particle and determine if it is repeatable.
      // A content particle is repeatable if it is repeatable or its
      // parent is repeatable.

      particle = (Particle) content.members.elementAt(i);
      repeatable = parentRepeatable || particle.isRepeatable;

      // Process the content particle.
      //
      // If the content particle is a reference to an element type,
      // save information about whether the property is repeatable.
      // If the content particle is a group, process it recursively.

      if (particle.type == Particle.TYPE_ELEMENTTYPEREF)
      {
        child = ((Reference) particle).elementType;
        repeatInfo.put(child.name, new Boolean(repeatable));
      }
      else
      // particle.type == Particle.TYPE_CHOICE || Particle.TYPE_SEQUENCE
      {
        setRepeatInfo(repeatInfo, (Group) particle, repeatable);
      }
    }
  }

  //**************************************************************************
  // Private methods -- properties
  //**************************************************************************

  private void addScalarProperty(XMLName name, boolean multiValued) throws Exception
  {
    // Add a property of the form:
    //
    // String m_ElementTypeName;
    //
    // or (for multiply-occurring children or multi-valued attributes):
    //
    // String[] m_ElementTypeNames;

    out.write(INDENT);
    out.write("String");
    if (multiValued)
      out.write("[]");
    out.write(" m_");
    out.write(name.getLocalName());
    if (multiValued)
      out.write("s");
    out.write(";");
    out.write(NEWLINE);
  }

  private void addAttrProperty(Attribute attribute) throws Exception
  {
    boolean multiValued;

    multiValued = ((attribute.type == Attribute.TYPE_IDREFS)
        || (attribute.type == Attribute.TYPE_ENTITIES) || (attribute.type == Attribute.TYPE_NMTOKENS));

    addScalarProperty(attribute.name, multiValued);
  }

  private void addPCDATAProperty(ElementType elementType) throws Exception
  {
    XMLName name;

    name = XMLName.create(null, elementType.name.getLocalName() + "PCDATA", null);
    addScalarProperty(name, false);
  }

  private void addElementTypeProperty(ElementType elementType, boolean multiValued)
      throws Exception
  {
    addScalarProperty(elementType.name, multiValued);
  }

  private void addClassProperty(ElementType elementType, boolean multiValued)
      throws Exception
  {
    // Add a property of the form:
    //
    // ElementTypeName m_ElementTypeName;
    //
    // or (for multiply-occurring children):
    //
    // ElementTypeName[] m_ElementTypeNames;

    out.write(INDENT);
    out.write(elementType.name.getLocalName());
    if (multiValued)
      out.write("[]");
    out.write(" m_");
    out.write(elementType.name.getLocalName());
    if (multiValued)
      out.write("s");
    out.write(";");
    out.write(NEWLINE);
  }
}
