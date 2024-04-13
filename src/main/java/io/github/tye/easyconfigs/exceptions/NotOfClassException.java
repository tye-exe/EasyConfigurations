package io.github.tye.easyconfigs.exceptions;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;

/**
 This exception is thrown when you try &amp; retrieve a config as a class it wasn't assigned as in
 the enum.<br>
 <br>
 For example:<br>
 enum : "USERNAME(String.class, "name");"<br>
 <br>
 USERNAME.getAsString(); // <b>Doesn't</b> thrown the exception.<br>
 USERNAME.getAsDouble(); // <b>Does</b> thrown the exception.<br>
 <br>
 The methods ".getAsObject()", ".getAsString()", &amp; ".getAsBoolean()" can always be used
 regardless of the marked class in the enum. */
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
