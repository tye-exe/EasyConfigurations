package me.tye.utils.exceptions;

/**
 This error is thrown when a class that doesn't implement it's intended interface (see bellow) is registered as a config/lang/key.
 configs - {@link me.tye.ConfigInstance}<br>
 lang - {@link me.tye.LangInstance}<br>
 keys - {@link me.tye.KeyInstance}<br> */
public class MissingInterfaceException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see MissingInterfaceException */
public MissingInterfaceException(String message) {
  super(message);
}

}
