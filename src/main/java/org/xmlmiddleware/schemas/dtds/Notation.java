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
 * Class representing a notation.
 *
 * @author Ronald Bourret
 * @version 2.0
 */

public class Notation
{
   // ********************************************************************
   // Variables
   // ********************************************************************

   /** The name of the notation. */
   public String name = null;

   /**
    * The public ID of the notation.
    *
    * <p>This variable or the systemID variable must be non-null.</p>
    */
   public String publicID = null;

   /**
    * The system ID of the notation.
    *
    * <p>This variable or the publicID variable must be non-null.</p>
    */
   public String systemID = null;

   // ********************************************************************
   // Constructors
   // ********************************************************************

   /** Construct a new Notation. */
   public Notation()
   {
   }
}
