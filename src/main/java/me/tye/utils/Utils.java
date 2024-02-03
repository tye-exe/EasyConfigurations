package me.tye.utils;

import me.tye.BaseInstance;
import me.tye.internalConfigs.Lang;
import me.tye.utils.annotations.InternalUse;
import me.tye.utils.annotations.NotImplemented;
import me.tye.utils.annotations.Utilities;
import me.tye.utils.exceptions.DefaultConfigurationException;
import me.tye.utils.exceptions.NeverThrownExceptions;
import me.tye.utils.exceptions.YamlParseException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static me.tye.utils.Consts.configMap;
import static me.tye.utils.Consts.logger;

/**
 This class is a general utility class for EasyConfigurations. */
@Utilities
public final class Utils {

/**
 This class is a utility class & should not be instantiated. */
private Utils() {}


/**
 Formats the Map returned from Yaml.load() into a hashmap where the exact key corresponds to the value.<br>
 E.G: key: "example.response" value: "test".
 @param baseMap The Map from Yaml.load().
 @return The formatted Map. */
@Contract (pure=true)
@Utilities
public static @NotNull HashMap<String, Object> getKeysRecursive(@Nullable Map<?, ?> baseMap) {
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
      map.put(String.valueOf(key), String.valueOf(value));
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
@Utilities
public static @NotNull HashMap<String, Object> getKeysRecursive(@NotNull String keyPath, @NotNull Map<?, ?> baseMap) {
  if (!keyPath.isEmpty()) keyPath += ".";

  HashMap<String, Object> map = new HashMap<>();
  for (Object key : baseMap.keySet()) {
    Object value = baseMap.get(key);

    // Calls this method recursively until all sub-maps have been parsed.
    if (value instanceof Map<?, ?>) {
      Map<?, ?> subMap = (Map<?, ?>) value;
      map.putAll(getKeysRecursive(keyPath + key, subMap));
    } else {
      map.put(keyPath + key, String.valueOf(value));
    }

  }

  return map;
}


/**
 Parses & formats data from the given inputStream to a Yaml resource.
 @param yamlInputStream The given inputStream to a Yaml resource.
 @return The parsed values in the format key: "test1.log" value: "works!"<br>
 Or an empty hashMap if the given inputStream is null.
 @throws IOException If the data couldn't be read from the given inputStream. */
@Contract (pure=true)
@Utilities
private static @NotNull HashMap<String, Object> parseYaml(@Nullable InputStream yamlInputStream) throws IOException {
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
    if (key == null || value == null) throw new YamlParseException(me.tye.internalConfigs.Lang.badYaml());

    // Checks if an list contains a null value.
    if (value instanceof List) {
      boolean result = recursiveListNullCheck((List<?>) value);
      if (result) throw new YamlParseException(me.tye.internalConfigs.Lang.badYaml());
    }
  });

  return yamlData;
}

/**
 Recursively checks a list for null values.
 @param list The list to check recursively.
 @return True if any object was null. */
@Contract (pure=true)
@Utilities
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
 Parses the data from an internal YAML file.
 @param resourcePath The path to the file from /src/main/resource/
 @return The parsed values in the format key: "test1.log" value: "works!" <br>
 Or an empty hashMap if the file couldn't be found or read. */
@Contract (pure=true)
@Utilities
public static @NotNull HashMap<String, Object> parseInternalYaml(@NotNull String resourcePath) {
  try (InputStream resourceInputStream = Utils.class.getResourceAsStream(resourcePath)) {

    if (resourceInputStream == null) throw new NullPointerException(Lang.internalYamlFail());

    return parseYaml(resourceInputStream);

  } catch (IOException e) {
    Consts.logger.log(Level.SEVERE, Lang.internalYamlFail(), e);
    return new HashMap<>();
  }

}


/**
 Parses the given external file into a hashMap. If the internal file contained keys that the external file didn't then the key-value pare is added to the external file.
 @param externalFile           The external file to parse.
 @param pathToInternalResource The path to the internal resource to repair it with or fallback on if the external file is broken.
 @return The key-value pairs from the external file. If any keys were missing from the external file then they are put into the hashMap with their default value. */
@Utilities
@NotImplemented
public static @NotNull HashMap<String, Object> parseAndRepairExternalYaml(@NotNull File externalFile, @Nullable String pathToInternalResource) {
  HashMap<String, Object> externalYaml;

  //tries to parse the external file.
  try (InputStream externalInputStream = new FileInputStream(externalFile)) {
    externalYaml = parseYaml(externalInputStream);

  } catch (FileNotFoundException e) {
    //logger.log(Level.SEVERE, InternalLang.noFile.get(Keys.path.replaceWith(externalFile.getAbsolutePath())), e);

    //returns an empty hashMap or the internal values if present.
    return pathToInternalResource == null ? new HashMap<>() : parseInternalYaml(pathToInternalResource);

  } catch (IOException e) {
    //logger.log(Level.SEVERE, InternalLang.parseYaml.get(Keys.path.replaceWith(externalFile.getAbsolutePath())), e);

    //returns an empty hashMap or the internal values if present.
    return pathToInternalResource == null ? new HashMap<>() : parseInternalYaml(pathToInternalResource);
  }


  //if there is no internal resource to compare against then only the external file data is returned.
  if (pathToInternalResource == null)
    return externalYaml;

  HashMap<String, Object> internalYaml = parseInternalYaml(pathToInternalResource);

  //gets the values that the external file is missing;
  HashMap<String, Object> missingPairsMap = new HashMap<>();
  internalYaml.forEach((String key, Object value) -> {
    if (externalYaml.containsKey(key))
      return;

    missingPairsMap.put(key, value);
  });

  //if no values are missing return
  if (missingPairsMap.keySet().isEmpty())
    return externalYaml;

  //Adds all the missing key-value pairs to a stringBuilder.
  StringBuilder missingPairs = new StringBuilder("\n");
  missingPairsMap.forEach((String key, Object value) -> {
    missingPairs.append(key).append(": \"").append(preserveEscapedQuotes(value)).append("\"\n");
  });

  //Adds al the missing pairs to the external Yaml.
  externalYaml.putAll(missingPairsMap);


  //Writes the missing pairs to the external file.
  try (FileWriter externalFileWriter = new FileWriter(externalFile, true)) {
    externalFileWriter.append(missingPairs.toString());

  } catch (IOException e) {
    //Logs a warning
    //Consts.logger.log(Level.WARNING, Lang.fileRestore.get(Keys.path.replaceWith(externalFile.getAbsolutePath())), e);

    //Logs the keys that couldn't be appended.
    missingPairsMap.forEach((String key, Object value) -> {
      Consts.logger.log(Level.WARNING, key + ": " + value);
    });
  }

  return externalYaml;
}

/**
 Object.toString() changes \" to ". This method resolves this problem.
 @param value The object to get the string from.
 @return The correct string from the given object. */
@Contract (pure=true)
@Utilities
private static String preserveEscapedQuotes(@NotNull Object value) {
  char[] valueCharArray = value.toString().toCharArray();
  StringBuilder correctString = new StringBuilder();


  for (char character : valueCharArray) {
    if (character != '"') {
      correctString.append(character);
      continue;
    }

    correctString.append('\\');
    correctString.append('"');
  }

  return correctString.toString();
}

/**
 @param clazz The given class to check.
 @return True if the given class uses the default {@link Object#toString()} method. */
@Contract (pure=true)
@Utilities
public static boolean usesDefaultToString(@NotNull Class<?> clazz) {
  try {
    return clazz.getMethod("toString").getDeclaringClass() == Object.class;

  } catch (NoSuchMethodException e) {
    throw new NeverThrownExceptions(e);
  }
}

/**
 If the given object is null throws a {@link NullPointerException} with the default lang message.
 @param object     The given object.
 @param objectName The name of the given object.
 @throws NullPointerException If the given object is null */
@Contract ("null, _ -> fail; !null, _ -> _")
@Utilities
public static void notNull(@Nullable Object object, @NotNull String objectName) throws NullPointerException {
  if (object != null) return;

  throw new NullPointerException(me.tye.internalConfigs.Lang.notNull(objectName));
}


/**
 Performs validation checks on the data from the internal enum & the data parsed from the default config value.
 @param internalInstance The internal enum class to check against.
 @param resourcePath     The path to the chosen internal resource.
 @throws DefaultConfigurationException If an enum present has no matching yaml value, if the response cannot be parsed as its assigned class, or if the response is null. */
@InternalUse
public static void validateInstance(@NotNull Class<?> internalInstance, @NotNull String resourcePath) throws DefaultConfigurationException {
  BaseInstance[] enums = (BaseInstance[]) internalInstance.getEnumConstants();

  // Checks that all the values declared in the enum class are valid.
  for (BaseInstance instanceEnum : enums) {
    String path = instanceEnum.yamlPath[0];

    // Checks if the value exists in the default file.
    if (!configMap.containsKey(path)) {
      throw new DefaultConfigurationException(Lang.notInDefaultYaml(path, resourcePath));
    }

    Object configFileValue = configMap.get(path);

    // Ensures that the value isn't null (a null value should never reach here but yk).
    if (configFileValue == null) {
      throw new DefaultConfigurationException(Lang.notNull(path));
    }

    // Checks that the value can be parsed as it's intended object.
    if (!validate(instanceEnum, configFileValue)) {
      throw new DefaultConfigurationException(Lang.notMarkedClass(path, resourcePath, instanceEnum.markedClass[0].getName()));
    }
  }

  // Checks if any default values in the file are missing from the enum.
  for (String configFilePaths : configMap.keySet()) {

    // Checks if the enum contains the key outlined in the file.
    boolean contains = false;
    for (BaseInstance instanceEnum : enums) {
      if (!configFilePaths.equals(instanceEnum.yamlPath[0])) continue;

      contains = true;
      break;
    }

    // Logs a warning if there's an unused path.
    if (contains) continue;

    logger.log(Level.WARNING, Lang.unusedYamlPath(configFilePaths, resourcePath));
  }
}


/**
 Checks if instances can be parsed as its intended object.
 @param instances The instances to check.
 @param value     The value of the instances.
 @return True if the instances can be parsed as its intended object. False if it can't. */
@Contract (pure=true)
@InternalUse
public static boolean validate(@NotNull BaseInstance instances, @NotNull Object value) {
  return SupportedClasses.getAsEnum(instances.markedClass[0]).canParse(value);
}

}
