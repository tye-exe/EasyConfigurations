package io.github.tye.easyconfigs.instances.persistent;

import io.github.tye.easyconfigs.Classes;
import io.github.tye.easyconfigs.ConfigObject;
import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.logger.LogType;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger.logger;

/**
 Contains information about a persistent only yaml file. */
@InternalUse
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
 The yaml parsed from a default file. */
@InternalUse
private final @Nullable WriteYaml yaml;

/**
 The external file that contains the yaml */
@InternalUse
private final @Nullable File externalFile;

private final @Nullable YamlWriter writer;

/**
 Constructs an empty {@link PersistentInstanceHandler} with no yaml data. */
@InternalUse
public PersistentInstanceHandler() {
  yaml = null;
  externalFile = null;
  writer = null;
}

/**
 Constructs a new {@link PersistentInstanceHandler} that contains the data of the given yaml files.
 @param internalPath The path to the internal default yaml.
 @param externalFile The path to the external yaml. (The file doesn't need to exist.
 @param clazz        The enum clazz that represents the yaml.
 @throws IOException            If there was an error reading or writing yaml data.
 @throws ConfigurationException If the default yaml can't be parsed. */
@InternalUse
public PersistentInstanceHandler(@NotNull String internalPath, @NotNull File externalFile, @NotNull Class<? extends PersistentInstance> clazz) throws IOException, ConfigurationException {
  checkInternalResourceValid(internalPath, clazz);

  if (!isExternalFileValid(externalFile)) {
    renameFileAsInvalid(externalFile);
  }

  if (!externalFile.exists()) {
    copyContent(internalPath, externalFile, clazz);
  }

  // Initializes the yaml.
  WriteYaml yaml = new WriteYaml(internalPath, externalFile, clazz);
  yaml.parseValues(clazz, internalPath, externalFile.getPath());
  this.yaml = yaml;
  this.externalFile = externalFile;

  // Instantiates the yaml writer.
  writer = new YamlWriter(this.externalFile);
  new Thread(writer).start();

  // Updates the yaml if it had needed to be repaired.
  writer.writeYaml(yaml);
}

/**
 Tests if the internal resource is in a valid yaml format.
 @param internalPath The path to the internal resource.
 @param clazz        The class to get the path with.
 @throws ConfigurationException If the internal resource isn't in a valid yaml format. */
public static void checkInternalResourceValid(String internalPath, Class<?> clazz) throws ConfigurationException {
  try (InputStream inputStream = clazz.getResourceAsStream(internalPath)) {
    if (inputStream == null) throw new IOException(Lang.configNotReadable(internalPath));

    // If it is a valid yaml don't throw an exception.
    if (ReadYaml.isValidYaml(inputStream)) return;
  }
  catch (Exception ignore) {}

  throw new ConfigurationException(Lang.errorWhileParsingYaml());
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

/**
 Replaces the value of the instance with the given new value.
 @param instance The value of the instance to replace.
 @param newValue The new value of the instance.
 @throws NotOfClassException   If the given new value isn't the marked class of the instance.
 @throws NullPointerException  If any of the arguments are null.
 @throws NotInitiatedException If the yaml hasn't been registered. */
public void replaceValue(@NotNull PersistentInstance instance, @NotNull Object newValue) throws NotOfClassException, NullPointerException, NotInitiatedException {
  if (yaml == null || writer == null) throw new NotInitiatedException();

  NullCheck.notNull(instance, "instance");
  NullCheck.notNull(newValue, "newValue");

  Class<?> assingedClass = instance.getAssingedClass();

  // Checks that the given value is the same as the marked value for this instance.
  try {
    List<?> valueList = (List<?>) newValue;
    // Checks that every value within the list is of the correct class.
    for (Object value : valueList) {
      if (value.getClass().equals(Classes.getComponent(assingedClass))) continue;

      throw new NotOfClassException(Lang.notOfClass(newValue.toString(), Classes.getName(assingedClass)));
    }
  }
  catch (ClassCastException ignore) {
    // If it's not a list check that it can just be parsed.
    if (!assingedClass.equals(newValue.getClass())) {
      throw new NotOfClassException(Lang.notOfClass(newValue.toString(), Classes.getName(assingedClass)));
    }
  }

  try {
    // Gets the enum that represents this class
    SupportedClasses asEnum = SupportedClasses.getAsEnum(assingedClass);

    // The string value or string list representation of the new value.
    // By default if it's not a custom object then just use the new value.
    Object stringValue = newValue;

    switch (asEnum) {
    case CONFIG_OBJECT: {
      // Parses the custom object into a string.
      stringValue = ((ConfigObject) newValue).getConfigString();
      break;
    }
    case CONFIG_OBJECT_LIST: {
      // Parses the custom objects into string values.
      ArrayList<String> strings = new ArrayList<>();
      @SuppressWarnings("unchecked") List<ConfigObject> listValue = (List<ConfigObject>) newValue;

      for (ConfigObject configObject : listValue) {
        strings.add(configObject.getConfigString());
      }

      stringValue = strings;
      break;
    }
    }

    // Updates the value within cache & external yaml file
    yaml.replaceValue(instance.getYamlPath(), stringValue, newValue);
    writer.writeYaml(yaml);
  }
  catch (ConfigurationException | ClassCastException e) {
    throw new RuntimeException("Never should happen");
  }
}

/**
 Contains if the external yaml is being written to. */
private boolean isWriting;

/**
 @return True if the external yaml is being written to. */
public boolean isWriting() {
  return isWriting;
}

/**
 This class is used to write the changed data to the external yaml, as processing the yaml structure
 constantly if repeated changes are occurring will be intensive. */
private class YamlWriter implements Runnable {

  /**
   A blocking queue with the yamls to write.
   */
  private final LinkedBlockingQueue<WriteYaml> writingQueue = new LinkedBlockingQueue<>();

  /**
   The file to write the data to.
   */
  private final @NotNull File externalFile;


  public YamlWriter(@NotNull File externalFile) {
    this.externalFile = externalFile;
  }

  /**
   Adds the yaml to write to the queue.
   @param toWrite The yaml to add to the queue.
   */
  public void writeYaml(@NotNull WriteYaml toWrite) {
    try {
      isWriting = true;
      writingQueue.put(toWrite);
    }
    // This error should never happen.
    catch (InterruptedException e) {
      logger.log(LogType.FAILED_EXTERNAL_UPDATE, Lang.failedExternalWrite(externalFile.getPath()));
    }
  }

  @Override
  public void run() {
    // While the thread isn't interrupted write any updates to the external file.
    while (!Thread.interrupted()) {
      try {
        // Blocks until there is new data to write.
        WriteYaml yamlToWrite = writingQueue.take();
        // Writes the data to the file
        Files.write(externalFile.toPath(), yamlToWrite.getYaml().getBytes());
      }
      // If there is an error updating the external yaml output a log about it.
      catch (IOException e) {
        logger.log(LogType.FAILED_EXTERNAL_UPDATE, Lang.failedExternalWrite(externalFile.getPath()));
      }
      // If the thread is interrupted, then terminate the thread.
      catch (InterruptedException e) {
        return;
      }
      finally {
        if (writingQueue.isEmpty()) {
          isWriting = false;
        }
      }
    }
  }

}
}
