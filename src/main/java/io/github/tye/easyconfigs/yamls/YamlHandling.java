package io.github.tye.easyconfigs.yamls;

import io.github.tye.easyconfigs.ClassName;
import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.BadYamlException;
import io.github.tye.easyconfigs.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.instances.Instance;
import io.github.tye.easyconfigs.instances.InstanceHandler;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.logger.LogType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger.logger;

/**
 This class is a utility class for EasyConfigurations that handles parsing &amp; formatting internal
 Yaml files. */
@InternalUse
public class YamlHandling {

/**
 Formats the Map returned from Yaml.load() into a hashmap where the exact key corresponds to the
 value.<br>
 E.G: key: "example.response" value: "test".
 @param baseMap The Map from Yaml.load().
 @return The formatted Map. */
@Contract(pure=true)
@InternalUse
private static @NotNull HashMap<String, Object> getKeysRecursive(@Nullable Map<?, ?> baseMap) {
  // In more words, this method takes the nested sub-maps that snakeYaml returns
  // & converts it into a non-nested HashMap, with the key being the exact key from the Yaml file, but with the ":" removed.

  HashMap<String, Object> map = new HashMap<>();
  if (baseMap == null) return map;

  for (Object key : baseMap.keySet()) {
    Object value = baseMap.get(key);

    // snakeYaml returns nested Yaml values as sub-maps.
    if (value instanceof Map<?, ?>) {
      Map<?, ?> subMap = (Map<?, ?>) value;

      map.putAll(getKeysRecursive(String.valueOf(key), subMap));
    }
    else {
      map.put(String.valueOf(key), value);
    }

  }

  return map;
}

/**
 Formats the Map returned from Yaml.load() into a hashmap where the exact key corresponds to the
 value.
 @param keyPath The path to append to the starts of the key. (Should only be called recursively).
 @param baseMap The Map from Yaml.load().
 @return The formatted Map. */
@Contract(pure=true)
@InternalUse
private static @NotNull HashMap<String, Object> getKeysRecursive(@NotNull String keyPath, @NotNull Map<?, ?> baseMap) {
  if (!keyPath.isEmpty()) keyPath += ".";

  HashMap<String, Object> map = new HashMap<>();
  for (Object key : baseMap.keySet()) {
    Object value = baseMap.get(key);

    // Calls this method recursively until all sub-maps have been parsed.
    if (value instanceof Map<?, ?>) {
      Map<?, ?> subMap = (Map<?, ?>) value;
      map.putAll(getKeysRecursive(keyPath + key, subMap));
    }
    else {
      map.put(keyPath + key, value);
    }

  }

  return map;
}


/**
 Parses the data from an internal YAML file.
 @param resourcePath     The path to the file from the resource location.
 @param internalInstance The internal enum class to check the parsed yaml against.
 @return The parsed values in the format key: "test1.log" value: "works!".
 @throws DefaultConfigurationException If there was an error parsing the given Yaml file.
 @throws FileNotFoundException         If the given resource path doesn't lead to a file. */
@Contract(pure=true)
@InternalUse
public static @NotNull HashMap<String, Object> parseInternalYaml(@NotNull Class<?> internalInstance, @NotNull String resourcePath) throws DefaultConfigurationException, FileNotFoundException {
  try (InputStream resourceInputStream = internalInstance.getResourceAsStream(resourcePath)) {

    // Checks if a file exists at the given path.
    if (resourceInputStream == null) {
      throw new FileNotFoundException(Lang.internalYamlFail(resourcePath));
    }

    HashMap<String, Object> parsedYaml = parseYaml(resourceInputStream);
    resourceInputStream.close();

    warnUnusedKeys(parsedYaml, internalInstance, resourcePath);

    return processYamlData(parsedYaml, internalInstance, resourcePath);

  }
  catch (IOException e) {
    if (e.getClass() == FileNotFoundException.class) {
      throw (FileNotFoundException) e;
    }

    throw new DefaultConfigurationException(Lang.internalYamlFail(resourcePath));
  }
}


/**
 Parses &amp; formats data from the given inputStream to a Yaml resource.
 <br>
 This method does not close the given resource steam.
 @param yamlInputStream The given inputStream to a Yaml resource.
 @return The parsed values in the format key: "test1.log" value: "works!"
 @throws IOException                   If the data couldn't be read from the given inputStream.
 @throws DefaultConfigurationException If the parsed yaml contained a null value or key. */
@Contract(pure=true)
@InternalUse
private static @NotNull HashMap<String, Object> parseYaml(@NotNull InputStream yamlInputStream) throws IOException, DefaultConfigurationException {
  // Loads the Yaml file into a HashMap
  HashMap<String, Object> yamlData = getKeysRecursive(new Yaml().load(yamlInputStream));

  // Checks that the Map doesn't contain any null values.
  for (Map.Entry<String, Object> entry : yamlData.entrySet()) {
    String key = entry.getKey();
    Object value = entry.getValue();

    if (key == null || value == null) throw new DefaultConfigurationException(Lang.badYaml());

    // Checks if an list contains a null value.
    if (value instanceof List) {
      boolean result = recursiveListNullCheck((List<?>) value);
      if (result) throw new DefaultConfigurationException(Lang.badYaml());
    }
  }

  return yamlData;
}

/**
 Recursively checks a list for null values.
 @param list The list to check recursively.
 @return True if any object was null. */
@Contract(pure=true)
@InternalUse
private static boolean recursiveListNullCheck(@NotNull List<?> list) {
  for (Object listObject : list) {
    if (listObject == null) return true;

    if (!(listObject instanceof List)) continue;

    boolean result = recursiveListNullCheck((List<?>) listObject);
    if (result) return true;

  }
  return false;
}


/**
 Outputs a warning message to the logger if the given map contains keys that aren't used by the given
 internal instance.
 @param mapToCheck       The given map to check the key values of.
 @param internalInstance The given internal instance to check.
 @param resourcePath     The path to the Yaml file. This is used purely for logging. */
@InternalUse
private static void warnUnusedKeys(@NotNull HashMap<String, Object> mapToCheck, @NotNull Class<?> internalInstance, @NotNull String resourcePath) {
  // Checks if any default values in the file are missing from the enum.
  for (String yamlPath : mapToCheck.keySet()) {

    // Checks if the enum contains the key outlined in the file.
    boolean contains = false;

    for (Instance instanceEnum : (Instance[]) internalInstance.getEnumConstants()) {
      if (!yamlPath.equals(instanceEnum.getYamlPath())) continue;

      contains = true;
      break;
    }

    // Logs a warning if there's an unused path.
    if (contains) continue;

    logger.log(LogType.INTERNAL_UNUSED_PATH, Lang.unusedYamlPath(yamlPath, resourcePath));
  }
}


/**
 Validates that the values in the given HashMap can be parsed as the value indicated by its
 corresponding enum.<br>
 If it can be parsed then the value in the given map is replaced with the parsed value.
 @param mapToFormat      The given map, which contains the keys &amp; values to check against the
 given enum.
 @param internalInstance The given enum to check the given map against.
 @param resourcePath     The path to the internal Yaml file. This is purely used for logging purposes
 during exceptions.
 @return The given HashMap with the values parsed into the classes specified by the given enum.
 @throws DefaultConfigurationException If the map doesn't contain a key that is present in the enum.
 Or if the map contains a value that cannot be parsed as it's
 intended class. */
@Contract()
@InternalUse
private static @NotNull HashMap<String, Object> processYamlData(@NotNull HashMap<String, Object> mapToFormat, @NotNull Class<?> internalInstance, @NotNull String resourcePath) throws DefaultConfigurationException {
  Instance[] enums = (Instance[]) internalInstance.getEnumConstants();

  for (Instance instanceEnum : enums) {

    // Checks if the value exists in the default file.
    String keyPath = instanceEnum.getYamlPath();
    if (!mapToFormat.containsKey(keyPath)) {
      throw new DefaultConfigurationException(Lang.notInDefaultYaml(keyPath, resourcePath));
    }

    // Checks if the class is one supported by EasyConfigurations.
    if (!SupportedClasses.existsAsEnum(instanceEnum.getAssingedClass())) {
      String className = ClassName.getName(instanceEnum.getAssingedClass());
      throw new DefaultConfigurationException(Lang.classNotSupported(className));
    }

    SupportedClasses enumRepresentation = SupportedClasses.getAsEnum(instanceEnum.getAssingedClass());

    // Checks if the value can be parsed as its intended class.
    Object rawValue = mapToFormat.get(keyPath);
    boolean canParse = enumRepresentation.canParse(rawValue);
    if (!canParse) {
      throw new DefaultConfigurationException(Lang.notAssignedClass(keyPath, resourcePath, rawValue.getClass(), instanceEnum.getAssingedClass().getName()));
    }

    // Parses the value as it's intended class & replaces it within that HashMap.
    Object parsedValue = enumRepresentation.parse(rawValue);
    mapToFormat.put(keyPath, parsedValue);
  }

  return mapToFormat;
}


public static @NotNull HashMap<String, Object> parseExternalYaml(@NotNull File externalYamlFile, @NotNull InstanceHandler initiatedInstance) throws DefaultConfigurationException, IOException, NotInitiatedException, BadYamlException {
  if (!initiatedInstance.isInitiated()) throw new NotInitiatedException();

  InputStream internalInputStream = initiatedInstance.getClazz().getResourceAsStream(initiatedInstance.getPath());
  InputStream externalInputStream = new FileInputStream(externalYamlFile);

  // Checks if a file exists at the given path.
  if (internalInputStream == null) {
    throw new FileNotFoundException(Lang.internalYamlFail(initiatedInstance.getPath()));
  }


  WriteYaml internalYaml = new WriteYaml(internalInputStream);
  WriteYaml externalYaml = new WriteYaml(externalInputStream);

  if (internalYaml.equals(externalYaml)) return initiatedInstance.getMap();


  externalYaml.addMissingKeys(internalYaml);
  return initiatedInstance.getMap();
}


}