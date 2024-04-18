package io.github.tye.easyconfigs.exceptions;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;

/**
 This error is thrown when there was a mistake within a default Yaml file parsed by
 EasyConfigurations.<br>
 <br>
 Common cases in with this exception is thrown are:
 <p>
 — If an enum in a config or lang file doesn't have a matching value in the Yaml file.
 <p>
 — If the Yaml file contains a null value.
 <p>
 — If a Yaml value can't be parsed as it's intended class.
 <p>
 — If a config is assigned as an unsupported class.
 @see ConfigurationException */
@ExternalUse
public class DefaultConfigurationException extends ConfigurationException {

/**
 @param message The error message to display to the user.
 @see DefaultConfigurationException */
@InternalUse
public DefaultConfigurationException(String message) {
  super(message);
}

/**
 @param message The error message to display to the user.
 @param cause   The cause of the error.
 @see DefaultConfigurationException */
@InternalUse
public DefaultConfigurationException(String message, Throwable cause) {
  super(message, cause);
}
}
