package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.exceptions.YamlParseException;
import io.github.tye.easyconfigs.instances.BaseInstance;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;

/**
 This class is a utility class for EasyConfigurations that handles parsing &amp; formatting internal Yaml files. */
@InternalUse
public class YamlHandling {

/**
 This class is a utility class &amp; should not be instantiated. */
private YamlHandling() {}


/**
 Formats the Map returned from Yaml.load() into a hashmap where the exact key corresponds to the value.<br>
 E.G: key: "example.response" value: "test".
 @param baseMap The Map from Yaml.load().
 @return The formatted Map. */
@Contract (pure=true)
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
    } else {
      map.put(String.valueOf(key), value);
    }

  }

  return map;
}

/**
 Formats the Map returned from Yaml.load() into a hashmap where the exact key corresponds to the value.
 @param keyPath The path to append to the starts of the key. (Should only be called internally).
 @param baseMap The Map from Yaml.load().
 @return The formatted Map. */
@Contract (pure=true)
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
    } else {
      map.put(keyPath + key, value);
    }

  }

  return map;
}


/**
 Parses the data from an internal YAML file.
 @param resourcePath The path to the file from /src/main/resource/
 @param internalInstance The internal enum class to check the parsed yaml against.
 @return The parsed values in the format key: "test1.log" value: "works!" <br>
 Or an empty hashMap if the file couldn't be found or read.
 @throws YamlParseException If there was an error parsing the given Yaml file.
 @throws DefaultConfigurationException If the parsed map doesn't contain a key that is present in the enum. Or if the parsed map contains a value that cannot be parsed as it's intended class.*/
@Contract (pure=true)
@InternalUse
public static @NotNull HashMap<String, Object> parseInternalYaml(@NotNull Class<?> internalInstance, @NotNull String resourcePath) throws YamlParseException, DefaultConfigurationException {
  try (InputStream resourceInputStream = EasyConfigurations.class.getResourceAsStream(resourcePath)) {

    Objects.requireNonNull(resourceInputStream);

    HashMap<String, Object> parsedYaml = parseYaml(resourceInputStream);

    warnUnusedKeys(parsedYaml, internalInstance, resourcePath);

    return processYamlData(parsedYaml, internalInstance, resourcePath);

  } catch (IOException e) {
    throw new YamlParseException(Lang.internalYamlFail(resourcePath));
  }
}


/**
 Parses &amp; formats data from the given inputStream to a Yaml resource.
 @param yamlInputStream The given inputStream to a Yaml resource.
 @return The parsed values in the format key: "test1.log" value: "works!"<br>
 Or an empty hashMap if the given inputStream is null.
 @throws IOException If the data couldn't be read from the given inputStream.
 @throws YamlParseException If the parsed yaml contained a null value or key.
 */
@Contract (pure=true)
@InternalUse
private static @NotNull HashMap<String, Object> parseYaml(@Nullable InputStream yamlInputStream) throws IOException, YamlParseException {
  if (yamlInputStream == null) return new HashMap<>();

  // Parses content from the resource stream.
  ArrayList<Byte> content = new ArrayList<>();
  int currentByte;
  while ((currentByte = yamlInputStream.read()) != -1) {
    content.add((byte) currentByte);
  }

  // Converts it to a primitive byte array.
  byte[] bytes = new byte[content.size()];
  for (int i = 0; i < content.size(); i++) {
    bytes[i] = content.get(i);
  }

  // Loads the Yaml file into a HashMap
  String resourceContent = new String(bytes, Charset.defaultCharset());
  HashMap<String, Object> yamlData = getKeysRecursive(new Yaml().load(resourceContent));

  // Checks that the Map doesn't contain any null values.
  yamlData.forEach((key, value) -> {
    if (key == null || value == null) throw new YamlParseException(Lang.badYaml());

    // Checks if an list contains a null value.
    if (value instanceof List) {
      boolean result = recursiveListNullCheck((List<?>) value);
      if (result) throw new YamlParseException(Lang.badYaml());
    }
  });

  return yamlData;
}

/**
 Recursively checks a list for null values.
 @param list The list to check recursively.
 @return True if any object was null. */
@Contract (pure=true)
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
 Outputs a warning logger messages if the given map contains keys that aren't used by the given internal instance.
 * @param mapToCheck The given map to check the key values of.
 * @param internalInstance The given internal instance to check.
 * @param resourcePath The path to the Yaml file. This is used purely for logging.
 */
@InternalUse
private static void warnUnusedKeys(@NotNull HashMap<String, Object> mapToCheck, @NotNull Class<?> internalInstance, @NotNull String resourcePath) {
  // Checks if any default values in the file are missing from the enum.
  for (String yamlPath : mapToCheck.keySet()) {

    // Checks if the enum contains the key outlined in the file.
    boolean contains = false;

    for (BaseInstance instanceEnum : (BaseInstance[]) internalInstance.getEnumConstants()) {
      if (!yamlPath.equals(instanceEnum.getYamlPath())) continue;

      contains = true;
      break;
    }

    // Logs a warning if there's an unused path.
    if (contains) continue;

    EasyConfigurations.logger.log(Level.WARNING, Lang.unusedYamlPath(yamlPath, resourcePath));
  }
}


/**
 Validates that the values in the given HashMap can be parsed as the value indicated by its corresponding enum.<br>
 If it can be parsed then the value in the given map is replaced with the parsed value.
 * @param mapToFormat The given map, which contains the keys &amp; values to check against the given enum.
 * @param internalInstance The given enum to check the given map against.
 * @param resourcePath The path to the internal Yaml file. This is purely used for logging purposes during exceptions.
 * @return The given HashMap with the values parsed into the classes specified by the given enum.
 * @throws DefaultConfigurationException If the map doesn't contain a key that is present in the enum. Or if the map contains a value that cannot be parsed as it's intended class.
 */
@Contract()
@InternalUse
private static @NotNull HashMap<String, Object> processYamlData(@NotNull HashMap<String, Object> mapToFormat, @NotNull Class<?> internalInstance, @NotNull String resourcePath) throws DefaultConfigurationException {
  BaseInstance[] enums = (BaseInstance[]) internalInstance.getEnumConstants();

  for (BaseInstance instanceEnum : enums) {

    // Checks if the value exists in the default file.
    String keyPath = instanceEnum.getYamlPath();
    if (!mapToFormat.containsKey(keyPath)) {
      throw new DefaultConfigurationException(Lang.notInDefaultYaml(keyPath, resourcePath));
    }

    // Checks if the value can be parsed as its intended class.
    Object rawValue = mapToFormat.get(keyPath);
    SupportedClasses enumRepresentation = SupportedClasses.getAsEnum(instanceEnum.getMarkedClass());

    boolean canParse = enumRepresentation.canParse(rawValue);
    if (!canParse) {
      throw new DefaultConfigurationException(Lang.notMarkedClass(keyPath, resourcePath, rawValue.getClass(), instanceEnum.getMarkedClass().getName()));
    }

    // Parses the value as it's intended class & replaces it within that HashMap.
    Object parsedValue = enumRepresentation.parse(rawValue);
    mapToFormat.put(keyPath, parsedValue);
  }

  return mapToFormat;
}
}
