package io.github.tye.easyconfigs.utils.exceptions;

/**
 The name of this exception is self-explanatory, when it is used no circumstance (that I'm aware of) could lead to it being thrown.<br>
 <b>IF</b> you ever see this method being thrown please open an issue on GitHub or get in contact with me. */
public class NeverThrownExceptions extends RuntimeException {

/**
 @param cause The exception that should never happen.
 @see NeverThrownExceptions */
public NeverThrownExceptions(Throwable cause) {
  super(cause);
}
}
