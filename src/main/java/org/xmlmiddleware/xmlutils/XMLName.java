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

// Version 2.1
// Changes from version 1.0: None
// Changes from version 1.01:
// * Added prefix and uri variables
// * Changed "prefixed" to "qualified" and "qualified" to "universal"
// * Significantly rewrote API and restricted legal parameter values
// * Moved to xmlutils package
// Changes from version 2.0
// * Added namespace for xmlns namespace
// Changes from version 2.01
// * Added support for namespace-unawareness
// * Use Java generics

package org.xmlmiddleware.xmlutils;

import org.xmlmiddleware.utils.*;

import java.util.*;

/**
 * Contains information about an XML name, optionally including namespaces.
 *
 * <p>This class contains information about an XML name: its local, qualified
 * (prefixed), and universal (expanded) forms, as well as the namespace prefix
 * and URI. It can also handled qualified names (QNames) in namespace-unaware
 * fashion. (The term "expanded" was introduced in the Namespaces in XML 
 * Recommendation, version 1.1, and post-dates this class. Because some methods
 * in this class use the term "universal", we use it throughout.) </p>
 *
 * <p>Local names are names that do not contain prefixes. Qualified names
 * contain prefixes, separated from the local name by a colon (:). Universal
 * names are two-part names that contain a URI and a local name; they are
 * represented in this class as a URI and local name separated by a caret (^).
 * (A caret is used because it is not legal in a prefix or local name. The
 * resulting name is unique and can be hashed, compared with String.equals(), etc.)</p>
 *
 * <p>If a name is not in a namespace, the local, qualified, and universal
 * names are the same and the prefix and URI are null. That is, the only
 * name that matters is the local name and the other two names (qualified
 * and universal) are set to the local name for convenience.</p>
 *
 * <p>This is also true when XMLName is used in namespace-unaware fashion.
 * In this case, colons are ignored. The local, qualified, and universal
 * names are the same and the prefix and URI are null. Again, the only name
 * that matters is the local name and the other two names are set to the
 * local name for convenience.</p>
 *
 * <p>(There is a subtle difference between a name not in a namespace and
 * ignoring namespaces. In the first case, the name cannot contain a colon, but
 * is still a valid name according to the XML namespaces recommendation. In the
 * second case, the name can contain any number of colons; if it does contain
 * any colons, it is not valid according to the XML namespaces recommendation.</p>

 * <p><b>NOTES:</b></p>
 * <ul>
 * <li>Unprefixed attribute names do not belong to any namespace.</li>
 * <li> The prefix for the default namespace is an empty string ("").)</li>
 * </ul>
 *
 * <p>Here are some examples of local, qualified, and universal names:</p>
 *
 * <pre>
 *&lt;foo:element1 attr1="bar" foo:attr2="baz" xmlns="http://foo"&gt;
 *
 *    foo:element1:
 *    --------------------------------------
 *    Local name:      "element1"
 *    Qualified name:  "foo:element1"
 *    Universal name:  "http://foo^element1"
 *    Prefix:          "foo"
 *    Namespace URI:   "http://foo"
 *
 *    attr1:
 *    --------------------------------------
 *    Local name:      "attr1"
 *    Qualified name:  "attr1"
 *    Universal name:  "attr1"
 *    Prefix:          null
 *    Namespace URI:   null
 *
 *    foo:attr2:
 *    --------------------------------------
 *    Local name:      "attr2"
 *    Qualified name:  "foo:attr2"
 *    Universal name:  "http://foo^attr2"
 *    Prefix:          "foo"
 *    Namespace URI:   "http://foo"
 *
 *&lt;element2&gt;
 *
 *    element2:
 *    --------------------------------------
 *    Local name:      "element2"
 *    Qualified name:  "element2"
 *    Universal name:  "element2"
 *    Prefix:          null
 *    Namespace URI:   null
 *
 *&lt;element3 xmlns="http://foo" &gt; (not in any namespace)
 *
 *    element3:
 *    --------------------------------------
 *    Local name:      "element3"
 *    Qualified name:  "element3"
 *    Universal name:  "http://foo^element3"
 *    Prefix:          ""
 *    Namespace URI:   "http://foo"
 *
 *&lt;foo:element4 xmlns="http://foo" &gt; (namespace-unaware)
 *
 *    element4:
 *    --------------------------------------
 *    Local name:      "foo:element4"
 *    Qualified name:  "foo:element4"
 *    Universal name:  "foo:element4"
 *    Prefix:          null
 *    Namespace URI:   null
 * </pre>
 *
 * <p>XMLNames are namespace-unaware when they are created in the absence of
 * namespace information -- either a URI or a set of mappings from prefixes
 * to URIs. (Whether unprefixed names are namespace-aware or unaware depends
 * on whether namespace information was present at creation.) To determine if
 * an XMLName is namespace-aware, call the isNamespaceAware() method.
 * Namespace-unaware names can be made namespace aware by passing namespace
 * information to the resolveNamespace() or setURI() method.</p>
 *
 * <p>XMLName objects that have a namespace URI are not required to have a
 * prefix. However, setPrefix(String) must be called on such objects before
 * getPrefix() or getQualifiedName() can be called.</p>
 *
 * <p>Note that the methods in this class perform only cursory checks on
 * whether input local names, prefixes, and URIs are legal.</p>
 *
 * @author Ronald Bourret, 1998-9, 2001, 2004, 2007
 * @version 2.1
 */

public class XMLName
{
   //***********************************************************************
   // Public constants
   //***********************************************************************

   /**
    * The character used to separate the URI from the local name.
    *
    * <p>This follows the convention in John Cowan's SAX namespace filter and
    * uses a caret (^), which is neither a valid URI character nor a valid XML
    * name character.</p>
    */
   public static String SEPARATOR = "^";

   //***********************************************************************
   // Private constants
   //***********************************************************************

   private static String COLON = ":",
                         XML = "xml",
                         XMLNS = "xmlns",
                         W3CNAMESPACE = "http://www.w3.org/XML/1998/namespace",
                         XMLNSNAMESPACE = "http://www.w3.org/2000/xmlns/";

   //***********************************************************************
   // Variables
   //***********************************************************************

   private String local = null;
   private String qualified = null;
   private String universal = null;
   private String prefix = null;
   private String uri = null;
   private boolean isNamespaceAware = false;

   //***********************************************************************
   // Constructors
   //***********************************************************************

   private XMLName()
   {
   }

   //***********************************************************************
   // Factory methods
   //***********************************************************************

   /**
    * Construct an XMLName from a local name, prefix, and namespace URI.
    *
    * <p>If the URI is non-null, the XMLName is namespace aware. In this
    * case, the prefix may be null, although getPrefix() and getQualifiedName()
    * may not be called until the prefix is set.</p>
    *
    * <p>If the URI is null, whether the XMLName is namespace aware depends
    * on whether the local name includes a colon. If it does, the XMLName is
    * namespace-unaware. If it does not, the XMLName is namespace-aware (but
    * not in any namespace). In this case, the prefix must be null.</p>
    *
    * @param uri The namespace URI. May be null.
    * @param localName The local name.
    * @param prefix The namespace prefix. May be null. Use an empty string for
    *    the default namespace.
    *
    * @return The XMLName.
    */
   public static XMLName create(String uri, String localName, String prefix)
   {
      XMLName xmlName;
      String  qualified, universal;
      boolean isNamespaceAware;

      if ((prefix != null) && (uri == null))
         throw new IllegalArgumentException("If prefix is non-null, URI must not be null.");

      // 10/07, Ronald Bourret
      // Rewrote the code to handle namespace-unaware case.

      if (uri != null)
      {
         // Get the qualified and universal names. The getXxxx methods check that the names
         // are legal. Note that the qualified name is not set if the URI is non-null and
         // the prefix is null.

         isNamespaceAware = true;
         qualified = (prefix == null) ? null : getQualifiedName(prefix, localName);
      universal = getUniversalName(uri, localName);
      }
      else
      {
         // If there is no namespace URI, set namespace-awareness based on whether the local
         // name contains a colon. If it does, we are namespace-unaware. If it does not, assume
         // the user intends this to be a namespace-aware name not in any namespace.

         isNamespaceAware = (localName.indexOf(':') == -1);
         qualified = localName;
         universal = localName;
      }

      // Create and return a new XMLName.

      xmlName = new XMLName();

      xmlName.local = localName;
      xmlName.prefix = prefix;
      xmlName.uri = uri;
      xmlName.qualified = qualified;
      xmlName.universal = universal;
      xmlName.isNamespaceAware = isNamespaceAware;

      return xmlName;
   }

   /**
    * Construct an XMLName from a local name and a namespace URI.
    *
    * <p>If the URI is non-null, the XMLName is namespace aware. In this
    * case, getPrefix() and getQualifiedName() may not be called until
    * the prefix is set.</p>
    *
    * <p>If the URI is null, whether the XMLName is namespace aware depends
    * on whether the local name includes a colon. If it does, the XMLName is
    * namespace-unaware. If it does not, the XMLName is namespace-aware (but
    * not in any namespace).</p>
    *
    * @param uri The namespace URI. May be null.
    * @param localName The local name.
    *
    * @return The XMLName.
    */
   public static XMLName create(String uri, String localName)
   {
      return create(uri, localName, null);
   }

   /**
    * Construct an XMLName from a qualified name and a hashtable mapping
    * prefixes to namespace URIS.
    *
    * <p>If the uris argument is non-null, the XMLName is namespace-aware.
    * If the qualified name does not have a prefix, then (a) if the hashtable
    * provides a URI for the default namespace, the prefix is set to the empty
    * string (""), or (b) if the hashtable does not provide a URI for the
    * default namespace, the prefix is set to null.</p>
    *
    * <p>If the uris argument is null, then whether the XMLName is namespace
    * aware depends on whether the local name includes a colon. If it does,
    * the XMLName is namespace-unaware. If it does not, the XMLName is
    * namespace-aware (but not in any namespace).</p>
    *
    * @param qualifiedName Qualified name. Not required to contain a prefix.
    * @param uris Hashtable containing prefixes as keys and namespace URIs as
    *   values. Use an empty string ("") for the prefix of the default namespace.
    * @return The XMLName.
    * @exception IllegalArgumentException Thrown if the qualified name contains
    *   more than one colon and uris is non-null, or the Hashtable does not
    *   contain the prefix as a key.
    * 
    */
   public static XMLName create(String qualifiedName, Hashtable<String, String> uris)
   {
      // This method takes a (possibly) prefixed name and a Hashtable
      // relating namespace prefixes to URIs and returns an XMLName.

      String local = qualifiedName, prefix = null, uri = null;
      int    colon;

      // 10/07, Ronald Bourret
      // If uris is null, allow the local name to contain a colon. The checks
      // for this -- and whether the resulting XMLName is namespace-aware or
      // unaware -- are done in create(uri, localName, prefix). Note that
      // if uris is null, uri and prefix are null in the call to
      // create(uri, localName, prefix).

      checkQualifiedName(qualifiedName, uris != null);

      if (uris != null)
      {
      colon = qualifiedName.indexOf(COLON);
      if (colon == -1)
      {
            // Check for a default namespace. If one is found, set the prefix to
            // an empty string. Otherwise, the name is not in any namespace.

            uri = uris.get("");
            if (uri != null)
            {
               prefix = "";
            }
         }
      else
      {
         // Get the local name, prefix, and namespace URI.
         
         prefix = qualifiedName.substring(0, colon);
         local = qualifiedName.substring(colon + 1);
         if (prefix.toLowerCase().equals(XML))
         {
            // By definition, xml prefixes have a namespace of
            // http://www.w3.org/XML/1998/namespace.
            uri = W3CNAMESPACE;
         }
         else if (prefix.toLowerCase().equals(XMLNS))
         {
               // 8/19/04, Ronald Bourret
               // Added this case to comply with the Namespaces in XML 1.1 recommendation.

            // By definition, xmlns prefixes have a namespace of:
            // http://www.w3.org/2000/xmlns/
            uri = XMLNSNAMESPACE;
         }
         else
         {
            // Get the URI corresponding to the prefix.

               uri = uris.get(prefix);
            if (uri == null)
               throw new IllegalArgumentException("No namespace URI corresponding to prefix: " + prefix);
         }
      }
      }

      // Return a new XMLName.

      return create(uri, local, prefix);
   }

   /**
    * Construct an XMLName from a universal name.
    *
    * <p>getPrefix() and getQualifiedName() may not be called until
    * the prefix is set.</p>
    *
    * @param universalName The universal name.
    *
    * @return The XMLName.
    */
   public static XMLName create(String universalName)
   {
      return create(getURIFromUniversal(universalName), getLocalFromUniversal(universalName));
   }

   //***********************************************************************
   // Static utility methods
   //***********************************************************************

   /**
    * Construct a qualified name.
    *
    * <p>Returns the local name if the prefix is null or empty.</p>
    *
    * @param prefix The namespace prefix. May be null or empty.
    * @param localName The local name. May not contain a colon.
    * @return The qualified name of URI
    */
   public static String getQualifiedName(String prefix, String localName)
   {
      checkLocalName(localName, true);
      checkPrefix(prefix);

      // Return the local name if there is no prefix or if the prefix is
      // the empty string.

      if ((prefix == null) || (prefix.length() == 0)) return localName;
      return prefix + COLON + localName;
   }

   /**
    * Construct a qualified name from a universal name and a hashtable
    * mapping URIs to prefixes.
    *
    * @param universalName The universal name. Not required to contain a URI.
    * @param prefixes Hashtable containing namespace URIs as keys and prefixes as
    *    values. If the universal name does not contain a caret, this may be null.
    *    Use an empty string ("") for the prefix of the default namespace.
    * @exception IllegalArgumentException Thrown if no prefix corresponding to the
    *    namespace URI was found.
    */
   public static String getQualifiedName(String universalName, Hashtable<String, String> prefixes)
   {
      String uri, prefix, localName;

      uri = getURIFromUniversal(universalName);
      localName = getLocalFromUniversal(universalName);
      if (uri == null) return localName;

      if (prefixes == null)
         throw new IllegalArgumentException("prefixes argument cannot be null when the universal name contains a URI.");

      prefix = prefixes.get(uri);
      if (prefix == null)
         throw new IllegalArgumentException("No prefix corresponding to the namespace URI: " + uri);
      if (prefix.length() == 0)
      {
         return localName;
      }
      else
      {
         return prefix + COLON + localName;
      }
   }

   /**
    * Construct a universal name. Returns the local name if the URI is
    * null.
    *
    * @param uri The namespace URI.
    * @param localName The local name. May not contain a colon.
    */
   public static String getUniversalName(String uri, String localName)
   {
      checkLocalName(localName, true);
      checkURI(uri);

      if (uri == null) return localName;
      return uri + SEPARATOR + localName;
   }

   /**
    * Construct a universal name from a qualified name and a hashtable mapping
    * prefixes to namespace URIS.
    *
    * @param qualifiedName Qualified name. Not required to contain a prefix.
    * @param uris Hashtable containing prefixes as keys and namespace URIs as
    *   values.
    * @exception IllegalArgumentException Thrown if no URI corresponding to the
    *    prefix was found.
    */
   public static String getUniversalName(String qualifiedName, Hashtable<String, String> uris)
   {
      String local = qualifiedName, prefix = null, uri = null;
      int    colon;

      // 11/07, Ronald Bourret
      // Added second argument to checkQualifiedName. This method is always
      // namespace aware -- it makes no sense to care about universal names
      // in a namespace-unaware context.

      checkQualifiedName(qualifiedName, true);

      // Search the qualified name for a colon and get the prefix
      // namespace URI, and local name.

      colon = qualifiedName.indexOf(COLON);
      if (colon == -1)
      {
         // If namespaces are used, check for a default namespace.

         if (uris != null)
         {
            uri = uris.get("");
         }
      }
      else
      {
         if (uris == null)
            throw new IllegalArgumentException("Argument uris must not be null when the qualified name contains a prefix.");

         // Get the local name, prefix, and namespace URI.
         
         prefix = qualifiedName.substring(0, colon);
         local = qualifiedName.substring(colon + 1);

         if (prefix.toLowerCase().equals(XML))
         {
            // By definition, xml prefixes have a namespace of
            // http://www.w3.org/XML/1998/namespace.
            uri = W3CNAMESPACE;
         }
         else if (prefix.toLowerCase().equals(XMLNS))
         {
            // 8/19/04, Ronald Bourret
            // Added this case to comply with the Namespaces in XML 1.1 recommendation.

            // By definition, xmlns prefixes have a namespace of:
            // http://www.w3.org/2000/xmlns/
            uri = XMLNSNAMESPACE;
         }
         else
         {
            // Get the URI corresponding to the prefix.

            uri = uris.get(prefix);
            if (uri == null)
               throw new IllegalArgumentException("No namespace URI corresponding to prefix: " + prefix);
         }
      }

      // Return the universal name

      return getUniversalName(local, uri);
   }

   /**
    * Get the prefix from a qualified name.
    *
    * @param qualifiedName Qualified name.
    * @return The prefix or null if there is no prefix.
    */
   public static String getPrefixFromQualified(String qualifiedName)
   {
      int    colon;

      // 11/07, Ronald Bourret
      // Added second argument to checkQualifiedName. This method is always
      // namespace aware -- it makes no sense to care about prefixes in a
      // namespace-unaware context.

      checkQualifiedName(qualifiedName, true);

      colon = qualifiedName.indexOf(COLON);
      if (colon == -1) return null;
      return qualifiedName.substring(0, colon);
   }

   /**
    * Get the local name from a qualified name.
    *
    * @param qualifiedName Qualified name.
    * @return The local name.
    */
   public static String getLocalFromQualified(String qualifiedName)
   {
      int    colon;

      // 11/07, Ronald Bourret
      // Added second argument to checkQualifiedName. This method is always
      // namespace aware -- it makes no sense to care about qualified names
      // in a namespace-unaware context.

      checkQualifiedName(qualifiedName, true);

      colon = qualifiedName.indexOf(COLON);
      if (colon == -1) return qualifiedName;
      return qualifiedName.substring(colon + 1);
   }

   /**
    * Get the URI from a universal name.
    *
    * @param universalName Universal name.
    * @return The URI or null if there is no URI.
    */
   public static String getURIFromUniversal(String universalName)
   {
      int separator;

      checkUniversalName(universalName);

      separator = universalName.indexOf(SEPARATOR);
      if (separator == -1) return null;
      return universalName.substring(0, separator);
   }

   /**
    * Get the local name from a universal name.
    *
    * @param universalName Universal name.
    * @return The local name.
    */
   public static String getLocalFromUniversal(String universalName)
   {
      int separator;

      checkUniversalName(universalName);

      separator = universalName.indexOf(SEPARATOR);
      if (separator == -1) return universalName;
      return universalName.substring(separator + 1);
   }

   //***********************************************************************
   // Accessor and mutator methods
   //***********************************************************************

   /**
    * Get the local name.
    *
    * @return The local name.
    */
   public final String getLocalName()
   {
      return local;
   }

   /**
    * Get the qualified name.
    *
    * @return The qualified name.
    * @exception IllegalStateException Thrown if the namespace URI is non-null
    *    and the prefix has not been set.
    */
   public final String getQualifiedName()
   {
      if (qualified == null)
         throw new IllegalStateException("Cannot return the qualified name when the prefix is not set.");
      return qualified;
   }

   /**
    * Get the universal name.
    *
    * @return The universal name.
    */
   public final String getUniversalName()
   {
      return universal;
   }

   /**
    * Get the namespace prefix.
    *
    * <p>The prefix is null if the name is not in a namespace or is namespace-unaware.
    * The prefix is the empty string ("") if the name is in the default namespace.</p>
    *
    * @return The prefix.
    * @exception IllegalStateException Thrown if the namespace URI is non-null
    *    and the prefix has not been set.
    */
   public final String getPrefix()
   {
      if (qualified == null)
         throw new IllegalStateException("The prefix has not been set.");
      return prefix;
   }

   /**
    * Get the namespace URI.
    *
    * <p>The namespace URI is null if the name is not in a namespace or is namespace-unaware.</p>
    *
    * @return The namespace URI.
    */
   public final String getURI()
   {
      return uri;
   }

   /**
    * Set the namespace prefix.
    *
    * @param prefix The namespace prefix.
    */
   public final void setPrefix(String prefix)
   {
      if (uri == null)
         throw new IllegalStateException("Cannot set the prefix when the URI is null.");

      if (prefix == null)
         throw new IllegalArgumentException("prefix argument must be non-null.");

      this.qualified = getQualifiedName(this.local, prefix);
      this.prefix = prefix;
   }

   /**
    * Check if the XMLName is namespace-aware.
    *
    * @return True if the XMLName is namespace-aware. Otherwise false.
    */
   public final boolean isNamespaceAware()
   {
      return isNamespaceAware;
   }

   /**
    * Set the namespace URI in an XMLName.
    *
    * <p>This method sets the URI in an XMLName, overriding the existing namespace URI
    * (if any). It also sets the local, qualified, and universal names and the prefix,
    * as well as setting namespace-awareness to true. If the current qualified name
    * does not have a prefix, it sets the prefix to the empty string ("").</p>
    *
    * @param uri The namespace URI. Must not be null.
    *
    * @exception XMLMiddlewareException Thrown if the local name contains a colon.
    */
   public void setURI(String uri)
      throws XMLMiddlewareException
   {
      // 11/07, Ronald Bourret
      // New method.

      if (uri == null)
         throw new IllegalArgumentException("uri argument cannot be null.");

      checkURI(uri);
      try
      {
         checkQualifiedName(qualified, true);
      }
      catch (IllegalArgumentException e)
      {
         throw new XMLMiddlewareException(e.getMessage());
      }

      local = getLocalFromQualified(qualified);
      universal = getUniversalName(uri, local);
      prefix = getPrefixFromQualified(qualified);
      if (prefix == null) prefix = "";
      this.uri = uri;
      isNamespaceAware = true;      
   }

   /**
    * Resolve the prefix in an XMLName.
    *
    * <p>This method resolves the prefix in the current qualified name based on
    * a hashtable mapping prefixes to URIs. It sets the local name, universal name,
    * prefix, and URI accordingly, as well as setting namespace-awareness to true.
    * It overrides any current settings of those fields.</p>

    * <p>If the current qualified name does not have a prefix, then (a) if the
    * hashtable provides a URI for the default namespace, the prefix is set to the
    * empty string (""), or (b) if the hashtable does not provide a URI for the
    * default namespace, the prefix is set to null.</p>
    *
    * @param uris Hashtable containing prefixes as keys and namespace URIs as values. Use
    *    an empty string ("") for the prefix of the default namespace. Must be non-null.
    *
    * @exception XMLMiddlewareException Thrown if the local name contains a colon or
    *    the prefix cannot be resolved.
    */
   public void resolveNamespace(Hashtable<String, String> uris)
      throws XMLMiddlewareException
   {
      // 11/07, Ronald Bourret
      // New method. This method calls create(qualified, uris) rather than
      // duplicate the code in that method.

      XMLName temp;

      if (uris == null)
         throw new IllegalArgumentException("uris argument cannot be null.");

      try
      {
         temp = create(qualified, uris);
      }
      catch (IllegalArgumentException e)
      {
         throw new XMLMiddlewareException(e.getMessage());
      }

      isNamespaceAware = true;
      local = temp.getLocalName();
      qualified = temp.getQualifiedName();
      universal = temp.getUniversalName();
      prefix = temp.getPrefix();
      uri = temp.getURI();
   }

   //***********************************************************************
   // Equals and hashCode methods
   //***********************************************************************

   /**
    * Overrides Object.equals(Object).
    *
    * <p>An object is equal to this XMLName object if: (1) it is an XMLName object
    * and (2) it has the same URI and local name. Note that two XMLName objects are
    * considered equal even if they have different namespace prefixes.</p>
    *
    * @param obj The reference object with which to compare.
    * @return true if this object is the same as the obj argument; false otherwise. 
    */
   public boolean equals(Object obj)
   {
      String objectURI;

      // Return false if the object is not an XMLName object.

      if (!(obj instanceof XMLName)) return false;

      // Return false if the object has a different URI.

      objectURI = ((XMLName)obj).getURI();
      if (uri == null)
      {
         if (objectURI != null) return false;
      }
      else
      {
         if (!uri.equals(objectURI)) return false;
      }

      // Return true or false depending on whether the objects have the same local name.

      return local.equals( ((XMLName)obj).getLocalName() );
   }

   /**
    * Overrides Object.hashCode().
    *
    * <p>Two XMLName objects that are equal according to the equals method return
    * the same hash code.</p>
    *
    * @return The hash code
    */
   public int hashCode()
   {
      return universal.hashCode();
   }

   //***********************************************************************
   // Check methods
   //***********************************************************************

   private static void checkLocalName(String localName, boolean isNamespaceAware)
   {
      // 11/07, Ronald Bourret
      // Added isNamespaceAware parameter. If isNamespaceAware is true, local
      // names cannot contain a colon.

      // Check for valid characters not implemented

      if (localName == null)
         throw new IllegalArgumentException("Local name cannot be null.");

      if (localName.length() == 0)
         throw new IllegalArgumentException("Local name must have non-zero length.");

      if (isNamespaceAware && localName.indexOf(COLON) != -1)
         throw new IllegalArgumentException("Local name contains a colon: " + localName);

      if (localName.indexOf(SEPARATOR) != -1)
         throw new IllegalArgumentException("Local name contains a caret: " + localName);
   }

   private static void checkPrefix(String prefix)
   {
      // Check for valid characters not implemented

      if (prefix == null) return;

      if (prefix.indexOf(COLON) != -1)
         throw new IllegalArgumentException("Prefix contains a colon: " + prefix);

      if (prefix.indexOf(SEPARATOR) != -1)
         throw new IllegalArgumentException("Prefix contains a caret: " + prefix);
   }

   private static void checkURI(String uri)
   {
      // Check for valid characters not implemented

      if (uri == null) return;

      if (uri.length() == 0)
         throw new IllegalArgumentException("Namespace URI must have non-zero length.");

      if (uri.indexOf(SEPARATOR) != -1)
         throw new IllegalArgumentException("Namespace URI contains a caret: " + uri);
   }

   private static void checkQualifiedName(String qualifiedName, boolean isNamespaceAware)
   {
      // 11/07, Ronald Bourret
      // Added isNamespaceAware parameter.

      int colon;

      if (qualifiedName == null)
         throw new IllegalArgumentException("Qualified name cannot be null.");

      if (qualifiedName.length() == 0)
         throw new IllegalArgumentException("Qualified name must have non-zero length.");

      colon = qualifiedName.indexOf(COLON);
      if (colon == -1)
      {
         checkLocalName(qualifiedName, isNamespaceAware);
      }
      else
      {
         checkPrefix(qualifiedName.substring(0, colon));
         checkLocalName(qualifiedName.substring(colon + 1), isNamespaceAware);
      }
   }

   private static void checkUniversalName(String universalName)
   {
      int separator;

      if (universalName == null)
         throw new IllegalArgumentException("Universal name cannot be null.");

      if (universalName.length() == 0)
         throw new IllegalArgumentException("Universal name must have non-zero length.");

      // 11/07, Ronald Bourret
      // Add true as second argument to checkLocalName. By definition,
      // universal names are namespace aware.

      separator = universalName.indexOf(SEPARATOR);
      if (separator == -1)
      {
         checkLocalName(universalName, true);
      }
      else
      {
         checkURI(universalName.substring(0, separator));
         checkLocalName(universalName.substring(separator + 1), true);
      }
   }
}

