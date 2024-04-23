package io.github.tye.easyconfigs.exceptions;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;

/**
 This exception is thrown when you try &amp; retrieve or set a config as a class it wasn't assigned
 as in the enum.
 <blockquote><pre>
 enum Configs // Implements // {
 USERNAME(String.class, "name");

 // Rest of enum //
 }

 test() {
 USERNAME.getAsString(); // Doesn't thrown the exception.
 USERNAME.getAsDouble(); // Does thrown the exception.

 USERNAME.replaceValue("bob"); // Doesn't thrown the exception.
 USERNAME.replaceValue(2.03D); // Does thrown the exception.
 }
 </pre></blockquote> */
@ExternalUse
public class NotOfClassException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see NotOfClassException */
@InternalUse
public NotOfClassException(String message) {
  super(message);
}

}
