# Introduction to DTD Parser
 
The DTD Parser is a set of Java packages for exploring DTDs. For example, it could be used by software that generates database schema or Java classes from a DTD created by Ronald Bourret and which is now loosely maintained by Carl Eric Codere.

# System Requirements

To run the DTD parser, you need the following software:

* DTD parser
* JDK (Java Development Kit) 1.6 or higher
* SAX (Simple API for XML) version 1 or 2
    * Only the InputSource class is required. If you have an XML parser on your system, you probably already have this.


# Class Overview

The main classes are:

* DTD, ElementType, Attribute, Particle, Group, Reference: Classes that model the "logical" part of a DTD. Particle is the base class for Group and Reference, which are used to model content models.
* Entity, ParameterEntity, ParsedGeneralEntity, UnparsedEntity: Classes that model the entities declared in a DTD. 

# Source Code

You can find complete source code for the DTD parser at https://github.com/ccodere/DTDParser

# Licensing

The DTD Parser is in the public domain.

The DTD Parser is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose, and noninfringement. In no event shall the author(s) be liable for any claim, damages, or other liability, whether in an action of contract, tort, or otherwise, arising from, out of, or in connection with the DTD Parser or the use or other dealings in the DTD Parser.

# Support

If you have questions about how to use the DTD parser, the first thing you should do is read the documentation and look at the sample program (ClassGenerator). If you want to check for known defects, you can find them on github. Any fixes through pull requests are welcome.

# Building

Building is done through maven. 
