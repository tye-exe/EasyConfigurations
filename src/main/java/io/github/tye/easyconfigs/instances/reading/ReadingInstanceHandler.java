package io.github.tye.easyconfigs.instances.reading;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.yamls.ReadYaml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 Contains information about a parsed yaml file. */
@InternalUse
public class ReadingInstanceHandler {

/**
 Contains the class an instance stored in a yaml should be parsed as. */
@InternalUse
@NotNull
public static final HashMap<ReadingInstance, Class<?>> assignedClass = new HashMap<>();

/**
 Contains the path to parse an instance from in a yaml. */
@InternalUse
@NotNull
public static final HashMap<ReadingInstance, String> yamlPath = new HashMap<>();


/**
 The string to the location of the internal configuration file. */
@InternalUse
private @NotNull String path = "";

/**
 The yaml parsed from a default file. */
@InternalUse
private final @Nullable ReadYaml yaml;


/**
 Creates a placeholder instance. This should be overridden with
 {@link ReadingInstanceHandler#ReadingInstanceHandler(String, Class)} before being used. */
@InternalUse
public ReadingInstanceHandler() {
  this.yaml = null;
}


/**
 Creates a new instance of the given yaml. This method also performs checks for unused keys, missing
 keys, &amp; other incorrect configurations.
 @param path  The path to the default yaml file.
 @param clazz The class of the enum that represents the default yaml file.
 @throws IOException                   If there was an error reading the input stream, or if the
 given path doesn't lead to any files.
 @throws DefaultConfigurationException If:
 <p>
 - There was an error parsing the given inputStream as a yaml.
 <p>
 - There is a key in the yaml enum that isn't in the parsed
 yaml.
 <p>
 - The yaml enum has marked a value as a class
 EasyConfigurations doesn't support.
 <p>
 - A value can't be parsed as the class it is marked as in the
 yaml enum. */
@InternalUse
public ReadingInstanceHandler(@NotNull String path, @NotNull Class<? extends ReadingInstance> clazz) throws IOException, DefaultConfigurationException {
  try (InputStream inputStream = clazz.getResourceAsStream(path)) {
    if (inputStream == null) throw new IOException(Lang.configNotReadable(path));

    // Initializes the yaml
    ReadYaml yaml = new ReadYaml(inputStream);
    yaml.warnUnusedKeys(clazz, path);
    yaml.parseValues(clazz, path);

    this.path = path;
    this.yaml = yaml;
  }
  catch (ConfigurationException exception) {
    if (exception instanceof DefaultConfigurationException) {
      throw (DefaultConfigurationException) exception;
    }

    throw new DefaultConfigurationException(exception.getMessage(), exception.getCause());
  }
}


/**
 Gets the value at the given key from the parsed yaml.
 @param key The key to get the value at.
 @return The value at the given key.
 @throws NotInitiatedException If the value hasn't been initiated. */
@InternalUse
public @NotNull Object getValue(@NotNull String key) throws NotInitiatedException {
  if (yaml == null) throw new NotInitiatedException();

  Object value = yaml.getValue(key);
  // Shouldn't get thrown as this method is only called from instances.
  if (value == null) throw new NotInitiatedException(key);

  return value;
}

}
