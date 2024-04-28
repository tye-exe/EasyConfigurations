package io.github.tye.easyconfigs.exceptions;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;

/**
 This error is thrown when there was a mistake within a default Yaml file parsed by
 EasyConfigurations. Common cases in with this exception is thrown are:
 <p>
 — If an enum in a config or lang file doesn't have a matching value in the Yaml file.
 <p>
 — If the Yaml file contains a null value.
 <p>
 — If a Yaml value can't be parsed as it's intended class.
 <p>
 — If a config is assigned as an unsupported class. */
@ExternalUse
public class ConfigurationException extends Exception {

/**
 @param message The message to display to the user.
 @see ConfigurationException */
@InternalUse
public ConfigurationException(String message) {
  super(message);
}

/**
 @param message The message to display to the user.
 @param cause   The cause of the exception.
 @see ConfigurationException */
@InternalUse
public ConfigurationException(String message, Throwable cause) {
  super(message, cause);
}
}
