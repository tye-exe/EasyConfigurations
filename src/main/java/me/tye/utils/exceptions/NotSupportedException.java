package me.tye.utils.exceptions;

/**
 This exception is thrown when the class of an object that EasyConfigurations doesn't support is provided as an argument within the {@link me.tye.ConfigInstance}.<br>
 For a list of supported classes see {@link me.tye.utils.SupportedClasses} or the <a href="https://github.com/Mapty231/EasyConfigurations?tab=readme-ov-file#supported-parsing-classes">README.md</a> one GitHub.
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
