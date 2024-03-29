package io.github.tye.easyconfigs.exceptions;

/**
 This error is thrown when there was a mistake within a Yaml file parsed by EasyConfigurations.<br>
 Common cases in with this exception is thrown are:<br>
 — If an enum in a config or lang file doesn't have a matching value in the Yaml file.<br>
 — If the Yaml file contains a null value.<br>
 — If a Yaml value can't be parsed as it's intended class. */
public class DefaultConfigurationException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see DefaultConfigurationException */
public DefaultConfigurationException(String message) {
  super(message);
}

}
