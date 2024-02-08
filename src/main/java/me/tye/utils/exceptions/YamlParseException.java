package me.tye.utils.exceptions;

/**
 This exception will be thrown if a Yaml file of configs or lang contains a null value. */
public class YamlParseException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see YamlParseException */
public YamlParseException(String message) {
  super(message);
}
}
