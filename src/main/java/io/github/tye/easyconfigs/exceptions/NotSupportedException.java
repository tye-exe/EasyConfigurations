package io.github.tye.easyconfigs.exceptions;

import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.instances.ConfigInstance;

/**
 This exception is thrown when the class of an object that EasyConfigurations doesn't support is provided as an argument within the {@link ConfigInstance}.<br>
 For a list of supported classes see {@link SupportedClasses} or the <a href="https://github.com/tye-exe/EasyConfigurations?tab=readme-ov-file#supported-parsing-classes">README.md</a> one GitHub.
 */
public class NotSupportedException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see NotSupportedException
 */
public NotSupportedException(String message) {
  super(message);
}

}
