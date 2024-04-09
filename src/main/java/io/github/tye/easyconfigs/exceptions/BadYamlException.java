package io.github.tye.easyconfigs.exceptions;

/**
 This error is thrown when there was an error thrown while parsing a yaml file. */
public class BadYamlException extends Exception {

public BadYamlException(String message, Throwable cause) {
  super(message, cause);
}

}
