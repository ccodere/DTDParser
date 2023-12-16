// This software is in the public static domain.
//
// The software is provided "as is", without warranty of any kind,
// express or implied, including but not limited to the warranties
// of merchantability, fitness for a particular purpose, and
// noninfringement. In no event shall the author(s) be liable for any
// claim, damages, or other liability, whether in an action of
// contract, tort, or otherwise, arising from, out of, or in connection
// with the software or the use or other dealings in the software.
//
// Parts of this software were originally developed in the Database
// and Distributed Systems Group at the Technical University of
// Darmstadt, Germany:
//
//    http://www.informatik.tu-darmstadt.de/DVS1/

// Version 2.1
// Changes from version 1.x:
// * Change package name
// Changes from version 2.0:
// * Added isNamespaceDeclaration flag
// * Use Java generics

package org.xmlmiddleware.schemas.dtds;

import org.xmlmiddleware.xmlutils.XMLName;
import java.util.*;

/**
 * Class representing an attribute.
 *
 * @author Ronald Bourret
 * @version 2.0
 */

public class Attribute
{
   // ********************************************************************
   // Constants
   // ********************************************************************

   /** Attribute type unknown. */
   public static final int TYPE_UNKNOWN = 0;

   /** Attribute type CDATA. */
   public static final int TYPE_CDATA = 1;

   /** Attribute type ID. */
   public static final int TYPE_ID = 2;

   /** Attribute type IDREF. */
   public static final int TYPE_IDREF = 3;

   /** Attribute type IDREFS. */
   public static final int TYPE_IDREFS = 4;

   /** Attribute type ENTITY. */
   public static final int TYPE_ENTITY = 5;

   /** Attribute type ENTITIES. */
   public static final int TYPE_ENTITIES = 6;

   /** Attribute type NMTOKEN. */
   public static final int TYPE_NMTOKEN = 7;

   /** Attribute type NMTOKENS. */
   public static final int TYPE_NMTOKENS = 8;

   /** Enumerated attribute type. */
   public static final int TYPE_ENUMERATED = 9;

   /** Notation attribute type. */
   public static final int TYPE_NOTATION = 10;
   
   // Internal mapping to string   
   private static final String TYPE_MAPPING[] =
   {
     "''",           // 0: TYPE_UNKNOWN
     "CDATA",       // 1: TYPE_CDATA
     "ID",          // 2: TYPE_ID
     "IDREF",       // 3: TYPE_IDREF
     "IDREFS",      // 4: TYPE_IDREFS
     "ENTITY",      // 5: TYPE_ENTITY
     "ENTITIES",    // 6: TYPE_ENTITIES
     "NMTOKEN",     // 7: TYPE_NMTOKEN
     "NMTOKENS",    // 8: TYPE_NMTOKENS
     "Enumerated",  // 9: TYPE_ENUMERATED
     "NOTATION"    // 10: TYPE_NOTATION
   };
   

   /** Default type unknown. */
   public static final int REQUIRED_UNKNOWN = 0;

   /** Attribute is required, no default. Corresponds to #REQUIRED. */
   public static final int REQUIRED_REQUIRED = 1;

   /** Attribute is optional, no default. Corresponds to #IMPLIED. */
   public static final int REQUIRED_OPTIONAL = 2;

   /** Attribute has a fixed default. Corresponds to #FIXED "&lt;default&gt;". */
   public static final int REQUIRED_FIXED = 3;

   /** Attribute is optional and has a default. Corresponds to "&lt;default&gt;". */
   public static final int REQUIRED_DEFAULT = 4;
   
   // Internal mapping to string   
   private static final String REQUIRED_MAPPING[] =
   {
     "''",          // 0: REQUIRED_UNKNOWN
     "REQUIRED",    // 1: REQUIRED_REQUIRED
     "OPTIONAL",    // 2: REQUIRED_OPTIONAL
     "DEFAULT",     // 3: REQUIRED_FIXED
     "DEFAULT",     // 4: REQUIRED_FIXED
   };

   // ********************************************************************
   // Variables
   // ********************************************************************

   /** The XMLName of the attribute. */
   public XMLName name = null;

   /** The attribute type. */
   public int type = TYPE_UNKNOWN;

   /** Whether the attribute is required and has a default. 
    *  Possible values allowed here are {@link #REQUIRED_DEFAULT}, 
    *  {@link #REQUIRED_FIXED},{@link #REQUIRED_OPTIONAL} or 
    *  {@link #REQUIRED_REQUIRED}  */
   public int required = REQUIRED_UNKNOWN;

   /** The attribute's default value. May be null. */
   public String defaultValue = null;

   /**
    * The legal values for attributes with a type of TYPE_ENUMERATED or
    * TYPE_NOTATION. Otherwise null.
    */
   public Vector<String> enums = null;

   /** Whether the attribute represents an XML namespace "declaration". */
   public boolean isNamespaceDeclaration = false;

   // ********************************************************************
   // Constructors
   // ********************************************************************

   /** Construct a new Attribute. */
   public Attribute()
   {
   }

   /**
    * Construct a new Attribute from its namespace URI, local name, and prefix.
    *
    * @param uri Namespace URI of the attribute. May be null.
    * @param localName Local name of the attribute.
    * @param prefix Namespace prefix of the attribute. May be null.
    */
   public Attribute(String uri, String localName, String prefix)
   {
      name = XMLName.create(uri, localName, prefix);
   }

   /**
    * Construct a new Attribute from an XMLName.
    *
    * @param name XMLName of the attribute.
    */
   public Attribute(XMLName name)
   {
      this.name = name;
   }

  public String toString()
  {
    return "[type=" + TYPE_MAPPING[type] + ", required=" + REQUIRED_MAPPING[required] + "]";
  }

   
   
}
