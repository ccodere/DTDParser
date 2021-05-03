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

// Version 2.0
// Changes from version 1.x:
// * Change package name
// * Now uses XMLWriter
// * Split off serialization methods.
// * Moved post-production methods to DTDParser.

package org.xmlmiddleware.schemas.dtds;

import org.xmlmiddleware.xmlutils.XMLName;
import java.io.*;
import java.util.*;

/**
 * Class representing a DTD.
 *
 * <p>DTD and the classes it points to are designed to be read-only. While you
 * can use them to create your own model of a DTD, you do so at your own risk.
 * This is because DTD and the classes it points to use public class variables
 * to hold information. The lack of mutator (set) methods means it is easy to
 * construct an invalid DTD.</p>
 *
 * @author Ronald Bourret
 * @version 2.0
 */

public class DTD
{
   // ********************************************************************
   // Public variables
   // ********************************************************************

   /**
    * A Hashtable of ElementTypes defined in the DTD.
    *
    * <p>Keyed by the element type's XMLName.</p>
    */
   public Hashtable elementTypes = new Hashtable();

   /**
    * A Hashtable of Notations defined in the DTD.
    *
    * <p>Keyed by the notation's name.</p>
    */
   public Hashtable notations = new Hashtable();

   /**
    * A Hashtable of ParameterEntities defined in the DTD.
    *
    * <p>Keyed by the entity's name.</p>
    *
    * <p><b>WARNING!</b> The ParameterEntity objects in this Hashtable are
    * used during parsing. After parsing, this Hashtable and the ParameterEntity
    * objects it contains are not guaranteed to contain useful information. In
    * particular, ParameterEntity objects cannot be used to reconstruct a DTD and
    * are not reference from where they were used, such as in content models.
    * This is because DTD and its related classes are designed to be used by
    * applications that want to explore the "logical" structure of a DTD --
    * that is, its element types, attributes, and notations -- rather than its
    * physical structure.</p>
    */
   public Hashtable parameterEntities = new Hashtable();

   /**
    * A Hashtable of ParsedGeneralEntities defined in the DTD.
    *
    * <p>Keyed by the entity's name.</p>
    */
   public Hashtable parsedGeneralEntities = new Hashtable();

   /**
    * A Hashtable of UnparsedEntities defined in the DTD.
    *
    * <p>Keyed by the entity's name.</p>
    */
   public Hashtable unparsedEntities = new Hashtable();

   // ********************************************************************
   // Constructors
   // ********************************************************************

   /** Construct a new DTD. */
   public DTD()
   {
   }

   // ********************************************************************
   // Public Methods -- creating element types
   // ********************************************************************

   /**
    * Create an ElementType by XMLName.
    *
    * <p>If the ElementType already exists, it is returned. Otherwise, a new
    * ElementType is created.</p>
    *
    * @param name The XMLName of the element type.
    * @return The ElementType.
    */
   public ElementType createElementType(XMLName name)
   {
      // Get an existing ElementType or add a new one if it doesn't exist.
      //
      // This method exists because we frequently need to refer to an 
      // ElementType object before it is formally created. For example, if
      // element type A is defined before element type B, and the content model
      // of element type A contains element type B, we need to add the
      // ElementType object for B (as a reference in the content model of A)
      // before it is formally defined and created.

      ElementType elementType;

      elementType = (ElementType)elementTypes.get(name);
      if (elementType == null)
      {
         elementType = new ElementType(name);
         elementTypes.put(name, elementType);
      }

      return elementType;
   }

   /**
    * Create an ElementType by URI, local name, and prefix.
    *
    * <p>If the ElementType already exists, it is returned. Otherwise, a new
    * ElementType is created.</p>
    *
    * @param uri The namespace URI. May be null.
    * @param localName The local name of the element type.
    * @param prefix The namespace prefix. May be null.
    * @return The ElementType.
    */
   public ElementType createElementType(String uri, String localName, String prefix)
   {
      return createElementType(XMLName.create(uri, localName, prefix));
   }
}
