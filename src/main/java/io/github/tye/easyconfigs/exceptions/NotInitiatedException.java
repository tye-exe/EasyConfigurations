package io.github.tye.easyconfigs.exceptions;

/**
 This exception is thrown if a config or lang value is access before it has been registered with
 {@link io.github.tye.easyconfigs.EasyConfigurations#registerConfig(Class, String)
 EasyConfigurations#registerConfig(Class, String)} or
 {@link io.github.tye.easyconfigs.EasyConfigurations#registerLang(Class, String)
 EasyConfigurations#registerLang(Class, String)} */
public class NotInitiatedException extends RuntimeException {

/**
 @param message The message to display.
 @see NotInitiatedException */
public NotInitiatedException(String message) {
  super(message);
}
}
