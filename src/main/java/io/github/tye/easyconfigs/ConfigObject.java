package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.exceptions.InvalidDataException;
import org.jetbrains.annotations.NotNull;

/**
 This interface must be implemented by a class that is a custom config object.
 <p>
 The interface provides the necessary methods for a custom config object. As well as these methods,
 the implementing class must include a public constructor with no arguments. This constructor does
 not have to do anything. */
public interface ConfigObject {

/**
 This method will return a string representation of the custom config object for writing to the yaml
 config file.
 @return A string representation of the custom config object. */
@NotNull
String getConfigString();

/**
 This method will take a string representation of the custom config object &amp; constructs a new instance
 of the custom config with the string data.
 <p>
 If the given data is malformed, then an {@link InvalidDataException} must be thrown.
 @param configString The string representation of the custom config object.
 @return A new instance of the custom config object based upon the given string data.
 @throws InvalidDataException Will be thrown if the given string data is malformed. */
@NotNull
ConfigObject parseConfigString(@NotNull String configString) throws InvalidDataException;

}
