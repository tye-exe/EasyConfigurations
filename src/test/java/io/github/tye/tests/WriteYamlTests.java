package io.github.tye.tests;

import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.instances.persistent.PersistentInstanceHandler;
import io.github.tye.easyconfigs.instances.reading.ReadingInstanceHandler;
import io.github.tye.easyconfigs.yamls.ReadYaml;
import io.github.tye.easyconfigs.yamls.WriteYaml;
import io.github.tye.tests.persistentInstanceClasses.Config_Default;
import io.github.tye.tests.persistentInstanceClasses.Lang_Default;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class WriteYamlTests {

@BeforeEach
public void reset_environment() {
  EasyConfigurations.persistentConfigInstance = new PersistentInstanceHandler();
  EasyConfigurations.persistentLangInstance = new PersistentInstanceHandler();
  EasyConfigurations.readOnlyLangInstance = new ReadingInstanceHandler();
  EasyConfigurations.readOnlyConfigInstance = new ReadingInstanceHandler();
}

/**
 Gets an input stream from an internal resource. */
private InputStream getResource(String path) {
  return EasyConfigurations.class.getResourceAsStream(path);
}

@TempDir
File tempDir;

/**
 Writes the content of an internal resource to a temp file & returns the temp file. */
private File getFile(String internalPath) throws IOException {
  try (InputStream inputStream = EasyConfigurations.class.getResourceAsStream(internalPath)) {
    assert inputStream != null;

    File file = new File(tempDir, "file");
    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return file;
  }
}

/**
 Tests removing extra keys from the external file. */
@Test
public void extra() throws IOException, ConfigurationException {
  DebugLogger debugLogger = new DebugLogger();
  EasyConfigurations.overrideEasyConfigurationsLogger(debugLogger);

  ReadYaml fullYaml = new ReadYaml(getResource("/tests/Yamls/externalYamls/Config_DefaultYaml.yml"));
  WriteYaml yaml = new WriteYaml(
      "/tests/Yamls/externalYamls/Config_DefaultYaml.yml", // Full yaml
      getFile("/tests/Yamls/externalYamls/Config_Extra.yml"), // Broken yaml
      Config_Default.class
  );

  assertEquals(fullYaml, yaml);
  assertTrue(fullYaml.identical(yaml));

  int unusedKeys = 0;
  int missingKeys = 0;
  for (DebugLogger.LogContainer logContainer : debugLogger.output) {
    switch (logContainer.logType) {
    case EXTERNAL_UNUSED_PATH: unusedKeys++;
      break;
    case EXTERNAL_MISSING_PATH: missingKeys++;
    }
  }

  // Should be five unused external keys
  assertEquals(5, unusedKeys);
  // Should be two external missing keys
  assertEquals(2, missingKeys);
}

/**
 Test fixing a file that is oh so broken. */
@Test
public void messedUp() throws IOException, ConfigurationException {
  DebugLogger debugLogger = new DebugLogger();
  EasyConfigurations.overrideEasyConfigurationsLogger(debugLogger);

  ReadYaml fullYaml = new ReadYaml(getResource("/tests/Yamls/externalYamls/Config_DefaultYaml.yml"));
  WriteYaml yaml = new WriteYaml(
      "/tests/Yamls/externalYamls/Config_DefaultYaml.yml", // Full yaml
      getFile("/tests/Yamls/externalYamls/Config_MessedUp.yml"), // External yaml
      Config_Default.class
  );

  assertEquals(fullYaml, yaml);
  assertTrue(fullYaml.identical(yaml));

  int unusedKeys = 0;
  int missingKeys = 0;
  for (DebugLogger.LogContainer logContainer : debugLogger.output) {
    switch (logContainer.logType) {
    case EXTERNAL_UNUSED_PATH: unusedKeys++;
      break;
    case EXTERNAL_MISSING_PATH: missingKeys++;
    }
  }

  // Should be one unused external key
  assertEquals(1, unusedKeys);
  // Should be six external missing keys
  assertEquals(6, missingKeys);
}

/**
 Test if an invalid internal file will get caught */
@Test
public void invalidInternal() {
  File invalidTest = new File(tempDir, "beep.test"); // Exception will be thrown before anything is done with the file.

  assertThrowsExactly(
      ConfigurationException.class,
      () -> EasyConfigurations.registerPersistentConfig(Config_Default.class, "/tests/Yamls/Invalid.yml", invalidTest)
                     );
}

/**
 The correctly parsed string data for the default yaml. */
String correctData = "# I wonder what this will do\n" +
                     "test:\n" +
                     "  hmm: \"Weee\"\n" +
                     "  nah: \"...\" #No comment\n" +
                     "\n" +
                     "eh: \"idk\"\n" +
                     "ehh: \"idk\" # guess i still don't know\n" +
                     "\n" +
                     "# lovely...\n" +
                     "ehhhhhh: \"idkkkkkkkkkk\"\n" +
                     "\n" +
                     "#eiaou\n" +
                     "this:\n" +
                     "  - \"Is\" #Not\n" +
                     "  - \"Sparta\"\n" +
                     "\n" +
                     "multiple.keys.in.one: \"Cats\"\n" +
                     "\n" +
                     "# I almost forgot numbers existed\n" +
                     "number: 1\n" +
                     "numbers: [ 1, 2 ]";

/**
 Tests if an invalid external file ahs been renamed & a new valid one created. */
@Test
public void invalidExternal() throws IOException, ConfigurationException {
  File invalidTest = new File(tempDir, "invalid.yml");

  try (FileWriter fileWriter = new FileWriter(invalidTest)) {
    fileWriter.write("This is really not valid data.");
  }

  EasyConfigurations.registerPersistentConfig(Config_Default.class, "/tests/Yamls/externalYamls/Config_DefaultYaml.yml", invalidTest);

  String replacedData = new String(Files.readAllBytes(invalidTest.toPath()));
  assertEquals(correctData, replacedData);

  // Checks if the original content was moved to the new file properly.
  for (File file : Objects.requireNonNull(tempDir.listFiles())) {
    if (file.getName().equals("invalid.yml")) continue;

    String movedData = new String(Files.readAllBytes(file.toPath()));
    assertEquals("This is really not valid data.", movedData);
  }
}

/**
 Tests creating the external yaml file & copying the data from the internal yaml to it. */
@Test
public void completeCopy() throws IOException, ConfigurationException {
  File copyTest = new File(tempDir, "completeCopy.test");

  EasyConfigurations.registerPersistentConfig(Config_Default.class, "/tests/Yamls/externalYamls/Config_DefaultYaml.yml", copyTest);

  String fileData = new String(Files.readAllBytes(copyTest.toPath()));
  assertEquals(correctData, fileData);


}

/**
 The values that should be parsed from the default yaml. */
private static final HashMap<String, Object> preFormattedValues;

static {
  HashMap<String, Object> preFormatted = new HashMap<>();
  preFormatted.put("test.hmm", "Weee");
  preFormatted.put("test.nah", "...");
  preFormatted.put("eh", "idk");
  preFormatted.put("ehh", "idk");
  preFormatted.put("ehhhhhh", "idkkkkkkkkkk");
  preFormatted.put("this", Arrays.asList("Is", "Sparta"));
  preFormatted.put("multiple.keys.in.one", "Cats");
  preFormatted.put("number", 1);
  preFormatted.put("numbers", Arrays.asList(1, 2));
  preFormattedValues = preFormatted;
}

/**
 Tests if the correct values were parsed as their respective classes from the default yaml. */
@Test
public void parsedClazz() throws IOException, ConfigurationException {
  EasyConfigurations.registerPersistentConfig(
      Config_Default.class,
      "/tests/Yamls/externalYamls/Config_DefaultYaml.yml",
      getFile("/tests/Yamls/externalYamls/Config_DefaultYaml.yml"));

  for (Config_Default yamlValue : Config_Default.values()) {
    Object correctValue = preFormattedValues.get(yamlValue.getYamlPath());

    assertEquals(correctValue, yamlValue.getValue());
  }

}

/**
 Test lang parsing. */
@Test
public void parsingLang() throws IOException, ConfigurationException {
  EasyConfigurations.registerPersistentLang(Lang_Default.class, "/tests/Yamls/externalYamls/Lang_Default.yml", getFile("/tests/Yamls/externalYamls/Lang_Default.yml"));

  assertEquals("Many, a many word!", Lang_Default.word.get());
  assertEquals("Many, a many more words!", Lang_Default.words.get());
}


@Test
public void replaceTest() throws IOException, ConfigurationException, InterruptedException {
  File externalFile = getFile("/tests/Yamls/externalYamls/Config_DefaultYaml.yml");

  EasyConfigurations.registerPersistentConfig(
      Config_Default.class,
      "/tests/Yamls/externalYamls/Config_DefaultYaml.yml",
      externalFile);

  // Tests replacing invalid values
  assertThrowsExactly(NotOfClassException.class, () -> Config_Default.hmm.replaceValue(2));
  assertThrowsExactly(NotOfClassException.class, () -> Config_Default.numbers.replaceValue(9));

  // Tests replacing a value
  assertEquals(
      preFormattedValues.get("test.nah"),
      Config_Default.nah.getAsString());

  Config_Default.nah.replaceValue("Yar in fact");

  assertEquals(
      "Yar in fact",
      Config_Default.nah.getAsString());

  // Tests replacing an array value
  assertEquals(
      preFormattedValues.get("numbers"),
      Config_Default.numbers.getAsIntegerList());

  Config_Default.numbers.replaceValue(Arrays.asList(1, 2, 3));

  assertEquals(
      Arrays.asList(1, 2, 3),
      Config_Default.numbers.getAsIntegerList());


  // Sleep for one second, so the thread can update the file.
  Thread.sleep(1000);

  // Tests writing the value to the file
  EasyConfigurations.registerPersistentConfig(
      Config_Default.class,
      "/tests/Yamls/externalYamls/Config_DefaultYaml.yml",
      externalFile);

  assertEquals(
      "Yar in fact",
      Config_Default.nah.getAsString());

  assertEquals(
      Arrays.asList(1, 2, 3),
      Config_Default.numbers.getAsIntegerList());
}
}
