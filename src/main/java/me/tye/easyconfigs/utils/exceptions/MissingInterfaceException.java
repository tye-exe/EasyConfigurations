package me.tye.easyconfigs.utils.exceptions;

import me.tye.easyconfigs.ConfigInstance;
import me.tye.easyconfigs.KeyInstance;
import me.tye.easyconfigs.LangInstance;

/**
 This error is thrown when a class that doesn't implement it's intended interface (see bellow) is registered as a config/lang/key.
 configs - {@link ConfigInstance}<br>
 lang - {@link LangInstance}<br>
 keys - {@link KeyInstance}<br> */
public class MissingInterfaceException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see MissingInterfaceException */
public MissingInterfaceException(String message) {
  super(message);
}

}
