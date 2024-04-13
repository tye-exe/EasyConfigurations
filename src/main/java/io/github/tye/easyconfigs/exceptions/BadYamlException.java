package io.github.tye.easyconfigs.exceptions;

/**
 This error is thrown when data that doesn't follow the yaml standards is parsed as a yaml file. */
public class BadYamlException extends Exception {

/**
 @param message The reason that the exception was thrown.
 @param cause   The underlying error thrown by the yaml parser.
 @see BadYamlException */
public BadYamlException(String message, Throwable cause) {
  super(message, cause);
}

}
