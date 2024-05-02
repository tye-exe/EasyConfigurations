package io.github.tye.tests;

import io.github.tye.easyconfigs.ConfigObject;
import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.instances.persistent.PersistentInstanceHandler;
import io.github.tye.easyconfigs.instances.reading.ReadingInstanceHandler;
import io.github.tye.easyconfigs.logger.LogType;
import io.github.tye.easyconfigs.yamls.ReadYaml;
import io.github.tye.tests.readingInstanceClasses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ReadYamlTests {

@BeforeEach
public void reset_environment() {
  EasyConfigurations.persistentConfigInstance = new PersistentInstanceHandler();
  EasyConfigurations.persistentLangInstance = new PersistentInstanceHandler();
  EasyConfigurations.readOnlyLangInstance = new ReadingInstanceHandler();
  EasyConfigurations.readOnlyConfigInstance = new ReadingInstanceHandler();
}

/**
 Empty file should throw exception. */
@Test
public void empty() throws IOException {
  InputStream yamlData = EasyConfigurations.class.getResourceAsStream("/tests/Yamls/internalYamls/Config_Empty.yml");
  assert yamlData != null;

  // Snake yaml return null on an empty yaml, so an exception is thrown.
  assertThrowsExactly(
      ConfigurationException.class,
      () -> new ReadYaml(yamlData));

  yamlData.close();
}

/**
 Yaml with null in it should throw exception */
@Test
public void containsNull() {
  // Nulls aren't allowed by EasyConfigurations.
  assertThrowsExactly(
      ConfigurationException.class,
      () -> new ReadingInstanceHandler("/tests/Yamls/internalYamls/Config_HasNull.yml", ReadingConfig_HasNull.class));
}

/**
 A warning should be output for the extra keys */
@Test
public void warnExtra() throws IOException, ConfigurationException {
  // Redirects the logs to a custom logger.
  DebugLogger debugLogger = new DebugLogger();
  EasyConfigurations.overrideEasyConfigurationsLogger(debugLogger);

  EasyConfigurations.registerReadOnlyConfig(ReadingConfig_General.class, "/tests/Yamls/internalYamls/Config_ExtraKey.yml");

  // Gets the log output & verifies that it was the correct log.
  DebugLogger.LogContainer logged = debugLogger.output.get(0);
  assertEquals(LogType.INTERNAL_UNUSED_PATH, logged.logType);
  assertEquals(
      "The yaml path \"extra\" in \"/tests/Yamls/internalYamls/Config_ExtraKey.yml\"" +
      " is a path that isn't being used by EasyConfigurations.",
      logged.logMessage);
}

/**
 Error should be thrown if values are missing from the yaml file. */
@Test
public void missing() {
  assertThrowsExactly(
      ConfigurationException.class,
      () -> EasyConfigurations.registerReadOnlyConfig(ReadingConfig_General.class, "/tests/Yamls/internalYamls/Config_Missing.yml"));
}

/**
 Error should be thrown if the file can't be parsed as a yaml file. */
@Test
public void invalidData() {
  ByteArrayInputStream invalidData = new ByteArrayInputStream("This string does not AT ALL follow the yaml format :).".getBytes());

  assertThrowsExactly(
      ConfigurationException.class,
      () -> new ReadYaml(invalidData));
}


/**
 Error should be thrown if a class is marked as one EasyConfigs doesn't support. */
@Test
public void unsupportedClass() {
  assertThrowsExactly(
      ConfigurationException.class,
      () -> EasyConfigurations.registerReadOnlyConfig(ReadingConfig_Unsupported.class, "/tests/Yamls/internalYamls/Config_UnsupportedClass.yml"));
}


/**
 The parsed content of the general file. */
private static final HashMap<String, Object> config_General;

static {
  HashMap<String, Object> preFormattedValues = new HashMap<>();
  preFormattedValues.put("nuhuh", false);
  preFormattedValues.put("another.one", "one!");
  preFormattedValues.put("mhm", Boolean.TRUE);
  preFormattedValues.put("another.two", "two!");
  preFormattedValues.put("example", 4);
  preFormattedValues.put("quotes", "\"quoted\" <- it's quoted.");
  preFormattedValues.put("timeIsSlipping", LocalDateTime.parse("2024-02-06T17:15:53.315"));
  preFormattedValues.put("nested.arrays", Arrays.asList("no one", "expected the", "spanish", "inquisition!"));
  preFormattedValues.put("floatingAround", 83.93f);
  preFormattedValues.put("note", Arrays.asList("running out", "of ideas"));
  config_General = preFormattedValues;
}

/**
 Tests if the values are parsed into their correct classes. */
@Test
public void genericConfig() throws IOException, ConfigurationException {
  EasyConfigurations.registerReadOnlyConfig(ReadingConfig_General.class, "/tests/Yamls/internalYamls/Config_General.yml");

  for (ReadingConfig_General value : ReadingConfig_General.values()) {
    assertEquals(
        config_General.get(value.getYamlPath()),
        value.getValue());
  }
}

/**
 Content of lang file */
private static final HashMap<String, Object> lang_General;

static {
  HashMap<String, Object> preFormattedValues = new HashMap<>();
  preFormattedValues.put("lyrics.never", "gonna");
  preFormattedValues.put("lyrics.give", "you");
  preFormattedValues.put("lyrics.up", ".");
  preFormattedValues.put("or.this.works", "or well it should");
  preFormattedValues.put("backToBasics", "");
  lang_General = preFormattedValues;
}

/**
 Tests parsing the lang file correctly */
@Test
public void genericLang() throws IOException, ConfigurationException {
  EasyConfigurations.registerReadOnlyLang(ReadingLang_General.class, "/tests/Yamls/internalYamls/Lang_General.yml");

  for (ReadingLang_General value : ReadingLang_General.values()) {
    assertEquals(
        lang_General.get(value.getYamlPath()),
        value.get());

  }
}

/**
 Arrays in lang should throw errors */
@Test
public void lang_arrayError() {
  assertThrowsExactly(
      ConfigurationException.class,
      () -> EasyConfigurations.registerReadOnlyLang(ReadingLang_ArrayFail.class, "/tests/Yamls/internalYamls/Lang_ArrayFail.yml"));
}

/**
 Tests using the keys within the lang. */
@Test
public void lang_keys() throws IOException, ConfigurationException {
  EasyConfigurations.registerReadOnlyLang(ReadingLang_Keys.class, "/tests/Yamls/internalYamls/Lang_Keys.yml");

  assertEquals(
      "I know a good joke! {joke}",
      ReadingLang_Keys.joke.get()
              );

  assertEquals(
      "I know a good joke! ",
      ReadingLang_Keys.joke.get(Keys.joke)
              );

  assertEquals(
      "I know a good joke! My life!",
      ReadingLang_Keys.joke.get(Keys.joke.replaceWith("My life!"))
              );

  assertEquals(
      "I know a good joke! Big oxygen!",
      ReadingLang_Keys.joke.get(Keys.joke.replaceWith("Big oxygen!"))
              );

  // Keys don't persist.
  assertEquals(
      "I know a good joke! ",
      ReadingLang_Keys.joke.get(Keys.joke)
              );

  assertEquals(
      "I know a bad joke! Potato",
      ReadingLang_Keys.unJoke.get(Keys.unJoke.replaceWith("Potato"))
              );

  assertEquals(
      "I know a bad joke! ",
      ReadingLang_Keys.unJoke.get(Keys.unJoke)
              );

}

/**
 Tests if the config hasn't been initiated it should throw the respective exception. */
@Test
public void notInitiated() {
  assertThrowsExactly(NotInitiatedException.class, ReadingConfig_General.time::getValue);
}

/**
 Tests if a config is got as the incorrect type the respective exception should be thrown. */
@Test
public void wrongClazz() throws IOException, ConfigurationException {
  EasyConfigurations.registerReadOnlyConfig(ReadingConfig_General.class, "/tests/Yamls/internalYamls/Config_General.yml");

  assertThrowsExactly(NotOfClassException.class, ReadingConfig_General.floats::getAsString);
  assertThrowsExactly(NotOfClassException.class, ReadingConfig_General.floats::getAsFloatList);
  assertDoesNotThrow(ReadingConfig_General.floats::getAsFloat);
}

/**
 Tests if a {@link ConfigObject} &amp; a config object array can be parsed successfully. */
@Test
public void customObject() throws ConfigurationException, IOException {
  EasyConfigurations.registerReadOnlyConfig(ReadingConfig_Custom.class, "/tests/Yamls/Config_Custom.yml");

  // Checks if a single value can be parsed
  assertEquals(
      new CustomObject("Bob", 3),
      ReadingConfig_Custom.NAME.getAsConfigObject());

  // Checks if the array could be parsed
  assertEquals(
      new CustomObject("Anne", 0),
      ReadingConfig_Custom.NAMES.getAsConfigObjectList().get(0));

  assertEquals(
      new CustomObject("Jacob", 0),
      ReadingConfig_Custom.NAMES.getAsConfigObjectList().get(1));
}
}
