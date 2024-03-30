package io.github.tye.easyconfigs.exceptions;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;

/**
 This error is thrown when there was a mistake within a Yaml file parsed by EasyConfigurations.<br>
 <br>
 Common cases in with this exception is thrown are:<br>
 — If an enum in a config or lang file doesn't have a matching value in the Yaml file.<br>
 — If the Yaml file contains a null value.<br>
 — If a Yaml value can't be parsed as it's intended class.
 — If a config is assigned as an unsupported enum. */
@ExternalUse
public class DefaultConfigurationException extends Exception {

/**
 @param message The error message to display to the user.
 @see DefaultConfigurationException */
@InternalUse
public DefaultConfigurationException(String message) {
  super(message);
}

}
