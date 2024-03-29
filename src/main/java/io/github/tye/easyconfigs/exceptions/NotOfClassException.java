package io.github.tye.easyconfigs.exceptions;

/**
 This exception is thrown when you try &amp; retrieve a config as a class it wasn't marked as in the enum.<br>
 For example:<br>
 <br>
 enum : "USERNAME(String.class, "name");"
 <br>
 USERNAME.getAsString(); // <b>Doesn't</b> thrown the exception.<br>
 <br>
 USERNAME.getAsDouble(); // <b>Does</b> thrown the exception.<br>
 <br>
 The methods .getAsObject(), .getAsString(), .getAsBoolean() can always be used regardless of the marked class in teh enum. */
public class NotOfClassException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see NotOfClassException */
public NotOfClassException(String message) {
  super(message);
}

/**
 * @param cause The cause of this exception.
 * @see NotOfClassException
 */
public NotOfClassException(Throwable cause) {
  super(cause);
}
}
