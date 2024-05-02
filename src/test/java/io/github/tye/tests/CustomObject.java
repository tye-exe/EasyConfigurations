package io.github.tye.tests;

import io.github.tye.easyconfigs.ConfigObject;
import io.github.tye.easyconfigs.exceptions.InvalidDataException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomObject implements ConfigObject {

private String name;
private int value;

public CustomObject(@NotNull String name) throws IllegalArgumentException {
  if (name.contains(":")) {
    throw new IllegalArgumentException("Name cannot contain \":\".");
  }

  this.name = name;
  this.value = 1;
}

public void nameFound() {
  this.value++;
}

public @NotNull String getName() {
  return name;
}

public int getValue() {
  return value;
}

@Override
public boolean equals(Object o) {
  if (this == o) return true;
  if (o == null || getClass() != o.getClass()) return false;
  CustomObject that = (CustomObject) o;
  return value == that.value && Objects.equals(name, that.name);
}

/**
 This method will return a string representation of the custom config object for writing to the yaml
 config file.
 @return A string representation of the custom config object. */
@Override
public @NotNull String getConfigString() {
  return name + ':' + value;
}

/**
 This method will take a string representation of the custom config object & constructs a new
 instance of the custom config with the string data.
 <p>
 If the given data is malformed, then an {@link InvalidDataException} must be thrown.
 @param configString The string representation of the custom config object.
 @return A new instance of the custom config object based upon the given string data.
 @throws InvalidDataException Will be thrown if the given string data is malformed. */
@Override
public @NotNull ConfigObject parseConfigString(@NotNull String configString) throws InvalidDataException {
  try {
    String[] split = configString.split(":");
    String name = split[0];
    int value = Integer.parseInt(split[1]);

    return new CustomObject(name, value);
  }
  catch (Exception ignore) {
    throw new InvalidDataException();
  }
}

public CustomObject() {}

public CustomObject(String name, int value) {
  this.name = name;
  this.value = value;
}
}
