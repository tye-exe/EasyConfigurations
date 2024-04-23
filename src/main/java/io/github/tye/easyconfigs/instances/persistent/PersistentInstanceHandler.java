package io.github.tye.easyconfigs.instances.persistent;

import io.github.tye.easyconfigs.ClassName;
import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.yamls.ReadYaml;
import io.github.tye.easyconfigs.yamls.WriteYaml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class PersistentInstanceHandler {
/**
 Contains the class an instance stored in a yaml should be parsed as. */
@InternalUse
@NotNull
public static final HashMap<PersistentInstance, Class<?>> assignedClass = new HashMap<>();

/**
 Contains the path to parse an instance from in a yaml. */
@InternalUse
@NotNull
public static final HashMap<PersistentInstance, String> yamlPath = new HashMap<>();

/**
 The string to the location of the internal configuration file. */
@InternalUse
private @NotNull String internalPath = "";

@InternalUse
private @Nullable File externalFile;

/**
 The yaml parsed from a default file. */
@InternalUse
private @Nullable WriteYaml yaml;

public PersistentInstanceHandler() {}

@InternalUse
public PersistentInstanceHandler(@NotNull String internalPath, @NotNull File externalFile, @NotNull Class<? extends PersistentInstance> clazz) throws IOException, ConfigurationException {
  checkInternalResourceValid(internalPath, clazz);

  if (!isExternalFileValid(externalFile)) {
    renameFileAsInvalid(externalFile);
  }

  if (!externalFile.exists()) {
    copyContent(internalPath, externalFile, clazz);
  }

  // Initializes the yaml
  WriteYaml yaml = new WriteYaml(internalPath, externalFile, clazz);
  yaml.parseValues(clazz, internalPath, externalFile.getPath());

  this.internalPath = internalPath;
  this.externalFile = externalFile;
  this.yaml = yaml;

}

/**
 Tests if the internal resource is in a valid yaml format.
 @param internalPath The path to the internal resource.
 @param clazz        The class to get the path with.
 @throws DefaultConfigurationException If the internal resource isn't in a valid yaml format. */
public static void checkInternalResourceValid(String internalPath, Class<?> clazz) throws DefaultConfigurationException {
  try (InputStream inputStream = clazz.getResourceAsStream(internalPath)) {
    if (inputStream == null) throw new IOException(Lang.configNotReadable(internalPath));

    // If it is a valid yaml don't throw an exception.
    if (ReadYaml.isValidYaml(inputStream)) return;
  }
  catch (Exception ignore) {}

  throw new DefaultConfigurationException(Lang.errorWhileParsingYaml());
}

/**
 Copies the file at the given internal path to the given external file.
 <p>
 <strong>Any existing file will be overwritten!</strong>
 @param internalPath The path to the internal resource.
 @param externalFile The file to write the content of the internal resource to.
 @param clazz        The class to get the resource from.
 @throws IOException If there was an error reading from the internal resource or writing to the
 external file. */
private static void copyContent(@NotNull String internalPath, @NotNull File externalFile, @NotNull Class<?> clazz) throws IOException {
  try (InputStream defaultYamlStream = clazz.getResourceAsStream(internalPath)) {
    if (defaultYamlStream == null) throw new IOException(Lang.configNotReadable(internalPath));

    Files.copy(defaultYamlStream, externalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }
}

/**
 Tests if an external file contains valid yaml data.
 <p>
 If the external file doesn't exist true is returned.
 @param externalFile The external file to check the content of.
 @return True if the file is a valid yaml file. Otherwise, false.
 @throws IOException If there was an error reading the content of the external file. */
private static boolean isExternalFileValid(@NotNull File externalFile) throws IOException {
  if (!externalFile.exists()) return true; // File will be created by later method.

  try (FileInputStream fileInputStream = new FileInputStream(externalFile)) {
    return ReadYaml.isValidYaml(fileInputStream);
  }
}

/**
 Appends the invalid yaml label to the end of the given file's name.
 <p>
 The invalid label contains a time stamp, so previous invalid files won't be overridden.
 @param externalFile The external file to rename.
 @throws IOException If there was an error renaming the external file. */
private static void renameFileAsInvalid(@NotNull File externalFile) throws IOException {
  //The time is parsed manually, as the default output contains chars that would be invalid in most filesystems.
  LocalDateTime now = LocalDateTime.now();

  StringBuilder fileName = new StringBuilder(externalFile.getName())
      .append('_')
      .append(Lang.invalidYaml())
      .append("_")
      .append(now.getYear())
      .append('-')
      .append(now.getMonthValue())
      .append('-')
      .append(now.getDayOfMonth())
      .append("_")
      .append(now.getHour())
      .append('-')
      .append(now.getMinute())
      .append('-')
      .append(now.getSecond())
      .append(".yml");

  Path parentPath = externalFile.toPath().normalize().getParent();
  Path newPath = Paths.get(parentPath.toString() + File.separatorChar + fileName);

  Files.move(externalFile.toPath(), newPath);
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


public void replaceValue(@NotNull PersistentInstance instance, @NotNull Object newValue) throws NotOfClassException, NullPointerException, NotInitiatedException {
  if (yaml == null) throw new NotInitiatedException();

  NullCheck.notNull(instance, "instance");
  NullCheck.notNull(newValue, "newValue");

  // If the value is a list, check each element in the list is the correct class.
  // And yes this will let any list of size 0 through. This is fine.
  if (newValue instanceof List) {
    for (Object listValue : ((List<?>) newValue)) {

      // Gets the component class if applicable, as the list values are marked as arrays.
      Class<?> clazz = getComponent(instance.getAssingedClass());
      if (clazz.equals(listValue)) {
        continue;
      }

      throw new NotOfClassException(Lang.notOfClass(newValue.toString(), ClassName.getName(instance.getAssingedClass())));
    }
  }

  if (!instance.getAssingedClass().equals(newValue.getClass())) {
    throw new NotOfClassException(Lang.notOfClass(newValue.toString(), ClassName.getName(instance.getAssingedClass())));
  }


  yaml.replaceValue(instance.getYamlPath(), newValue);
}


private static Class<?> getComponent(@NotNull Class<?> clazz) {
  if (!clazz.isArray()) return clazz;
  return clazz.getComponentType();
}

}
