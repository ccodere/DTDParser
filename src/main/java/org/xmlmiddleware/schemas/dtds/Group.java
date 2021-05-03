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

import java.util.Vector;

/**
 * A content particle that is either a choice group or a sequence group.
 *
 * @author Ronald Bourret
 * @version 2.0
 */

public class Group extends Particle
{
   // ********************************************************************
   // Variables
   // ********************************************************************

   /**
    * A Vector containing the members of the group.
    *
    * <p>This contains Particles (either Groups or References).</p>
    */
   public Vector members = new Vector(); // Contains Particles

   // ********************************************************************
   // Constructors
   // ********************************************************************

   /** Construct a new Group. */
   public Group()
   {
   }
}
