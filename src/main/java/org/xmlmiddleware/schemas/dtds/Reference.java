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

package org.xmlmiddleware.schemas.dtds;

/**
 * Class representing a reference to an ElementType.
 *
 * <p>Reference is used in the members of a Group.</p>
 *
 * @author Ronald Bourret
 * @version 2.0
 */

public class Reference extends Particle
{
   // ********************************************************************
   // Variables
   // ********************************************************************

   /** The referred-to element type. */
   public ElementType elementType = null;

   // ********************************************************************
   // Constructors
   // ********************************************************************

   /** Construct a new Reference. */
   public Reference()
   {
      this.type = Particle.TYPE_ELEMENTTYPEREF;
   }

   /**
    * Construct a new Reference and set the element type.
    *
    * @param elementType The referenced element type.
    */
   public Reference(ElementType elementType)
   {
      this.type = Particle.TYPE_ELEMENTTYPEREF;
      this.elementType = elementType;
   }
}
