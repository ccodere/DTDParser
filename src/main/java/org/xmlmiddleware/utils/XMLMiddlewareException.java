// This software is in the public domain.
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
// Changes from version 1.x: New in version 2.0

package org.xmlmiddleware.utils;

/**
 * <p>This class can encapsulate another Exception. The code
 * is largely copied from SAXException, by David Megginson.</p>
 *
 * @author Ronald Bourret, 2001
 * @version 2.0
 */

public class XMLMiddlewareException extends Exception 
{

   // ********************************************************************
   // Variables
   // ********************************************************************
   
    private Exception exception;
   
    // ********************************************************************
    // Constructors
    // ********************************************************************
   
   /**
     * Create a new XMLMiddlewareException.
     *
     * @param message The error or warning message.
     */
   public XMLMiddlewareException(String message) 
   {
      super(message);
      this.exception = null;
   }
   
   /**
     * Create a new XMLMiddlewareException wrapping an existing exception.
     *
     * <p>The existing exception will be embedded in the new
     * one, and its message will become the default message for
     * the XMLMiddlewareException.</p>
     *
     * @param e The exception to be wrapped in a XMLMiddlewareException.
     */
   public XMLMiddlewareException(Exception e)
   {
      super();
      this.exception = e;
   }
   
   /**
     * Create a new XMLMiddlewareException from an existing exception.
     *
     * <p>The existing exception will be embedded in the new
     * one, but the new exception will have its own message.</p>
     *
     * @param message The detail message.
     * @param e The exception to be wrapped in a XMLMiddlewareException.
     */
   public XMLMiddlewareException(String message, Exception e)
   {
      super(message);
      this.exception = e;
   }
   
   // ********************************************************************
   // Public methods
   // ********************************************************************
   
   /**
     * Return a detail message for this exception.
     *
     * <p>If there is an embedded exception, and if the XMLMiddlewareException
     * has no detail message of its own, this method will return
     * the detail message from the embedded exception.</p>
     *
     * @return The error or warning message.
     */
   public String getMessage()
   {
      String message = super.getMessage();
   
      if(message == null && exception != null) 
         return exception.getMessage();
      else 
         return message;
   }
   
   /**
     * Return the embedded exception, if any.
     *
     * @return The embedded exception, or null if there is none.
     */
   public Exception getException()
   {
      if(exception == null)
         return new Exception(getMessage());
      else
         return exception;
   }

   /**
     * Override toString to pick up any embedded exception.
     *
     * @return A string representation of this exception.
     */
   public String toString ()
   {
      if (exception != null) 
         return exception.toString();
      else
         return super.toString();
   }
}

