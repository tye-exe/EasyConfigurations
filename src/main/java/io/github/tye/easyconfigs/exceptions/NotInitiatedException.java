package io.github.tye.easyconfigs.exceptions;

/**
 This exception is thrown if a config or lang value is access before it has been registered within
 {@link io.github.tye.easyconfigs.EasyConfigurations EasyConfigurations} */
public class NotInitiatedException extends RuntimeException {

/**
 @see NotInitiatedException */
public NotInitiatedException() {
  super();
}

/**
 @param message The message to display.
 @see NotInitiatedException */
public NotInitiatedException(String message) {
  super(message);
}
}
