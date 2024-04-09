package io.github.tye.easyconfigs.exceptions;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.instances.ConfigInstance;
import io.github.tye.easyconfigs.instances.KeyInstance;
import io.github.tye.easyconfigs.instances.LangInstance;

/**
 This error is thrown when a class doesn't implement its intended interface (see bellow) is
 registered as a config/lang/key enum.<br>
 Configs - {@link ConfigInstance}<br>
 Lang - {@link LangInstance}<br>
 keys - {@link KeyInstance} */
@ExternalUse
public class MissingInterfaceException extends RuntimeException {

/**
 @param message The error message to display to the user.
 @see MissingInterfaceException */
@InternalUse
public MissingInterfaceException(String message) {
  super(message);
}

}
