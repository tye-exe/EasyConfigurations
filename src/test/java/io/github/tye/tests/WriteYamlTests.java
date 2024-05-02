package io.github.tye.tests;

import io.github.tye.easyconfigs.ConfigObject;
import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.instances.persistent.PersistentInstanceHandler;
import io.github.tye.easyconfigs.instances.reading.ReadingInstanceHandler;
import io.github.tye.easyconfigs.logger.LogType;
import io.github.tye.easyconfigs.yamls.ReadYaml;
import io.github.tye.easyconfigs.yamls.WriteYaml;
import io.github.tye.tests.persistentInstanceClasses.Config_Default;
import io.github.tye.tests.persistentInstanceClasses.Lang_Default;
import io.github.tye.tests.persistentInstanceClasses.PersistentConfig_Custom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
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

private void waitForWrite() throws InterruptedException {
  while (EasyConfigurations.persistentLangInstance.isWriting()) {
    Thread.sleep(100);
  }
  while (EasyConfigurations.persistentConfigInstance.isWriting()) {
    Thread.sleep(100);
  }
}


/**
 Tests removing extra keys from the external file. */
@Test
public void extra() throws IOException, ConfigurationException, InterruptedException {
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

  // Waits for any changes to be written to the file
  waitForWrite();
}

/**
 Test fixing a file that is oh so broken. */
@Test
public void messedUp() throws IOException, ConfigurationException, InterruptedException {
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

  // Waits for any changes to be written to the file
  waitForWrite();
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
public void invalidExternal() throws IOException, ConfigurationException, InterruptedException {
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

  // Waits for any changes to be written to the file
  waitForWrite();
}

/**
 Tests creating the external yaml file & copying the data from the internal yaml to it. */
@Test
public void completeCopy() throws IOException, ConfigurationException, InterruptedException {
  File copyTest = new File(tempDir, "completeCopy.test");

  EasyConfigurations.registerPersistentConfig(Config_Default.class, "/tests/Yamls/externalYamls/Config_DefaultYaml.yml", copyTest);

  String fileData = new String(Files.readAllBytes(copyTest.toPath()));
  assertEquals(correctData, fileData);

  // Waits for any changes to be written to the file
  waitForWrite();
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
public void parsedClazz() throws IOException, ConfigurationException, InterruptedException {
  EasyConfigurations.registerPersistentConfig(
      Config_Default.class,
      "/tests/Yamls/externalYamls/Config_DefaultYaml.yml",
      getFile("/tests/Yamls/externalYamls/Config_DefaultYaml.yml"));

  for (Config_Default yamlValue : Config_Default.values()) {
    Object correctValue = preFormattedValues.get(yamlValue.getYamlPath());

    assertEquals(correctValue, yamlValue.getValue());
  }

  // Waits for any changes to be written to the file
  waitForWrite();
}

/**
 Test lang parsing. */
@Test
public void parsingLang() throws IOException, ConfigurationException, InterruptedException {
  EasyConfigurations.registerPersistentLang(Lang_Default.class, "/tests/Yamls/externalYamls/Lang_Default.yml", getFile("/tests/Yamls/externalYamls/Lang_Default.yml"));

  assertEquals("Many, a many word!", Lang_Default.word.get());
  assertEquals("Many, a many more words!", Lang_Default.words.get());

  // Waits for any changes to be written to the file
  waitForWrite();
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

  // Waits for any changes to be written to the file
  waitForWrite();

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


@Test
public void externalShouldOverride() throws IOException, ConfigurationException, InterruptedException {
  String content = "# I wonder what this will do\n" +
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
                   "number: 2\n" +
                   "numbers: [ 1, 2 ]";

  File file = new File(tempDir, "file");
  Files.copy(new ByteArrayInputStream(content.getBytes()), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

  EasyConfigurations.registerPersistentConfig(Config_Default.class, "/tests/Yamls/externalYamls/Config_DefaultYaml.yml", file);

  // Waits for any changes to be written to the file
  waitForWrite();

  assertEquals(
      2,
      Config_Default.number.getAsInteger());
}


/**
 Tests if a {@link ConfigObject} &amp; a config object array can be parsed successfully. */
@Test
public void customObject() throws ConfigurationException, IOException, InterruptedException {
  File externalFile = getFile("/tests/Yamls/Config_Custom.yml");
  EasyConfigurations.registerPersistentConfig(PersistentConfig_Custom.class, "/tests/Yamls/Config_Custom.yml", externalFile);

  // Checks if a single value can be parsed
  assertEquals(
      new CustomObject("Bob", 3),
      PersistentConfig_Custom.NAME.getAsConfigObject());

  // Checks if the array could be parsed
  assertEquals(
      new CustomObject("Anne", 0),
      PersistentConfig_Custom.NAMES.getAsConfigObjectList().get(0));

  assertEquals(
      new CustomObject("Jacob", 0),
      PersistentConfig_Custom.NAMES.getAsConfigObjectList().get(1));

  // Waits for any changes to be written to the file
  waitForWrite();
}

/**
 Tests handling {@link ConfigObject} &amp; config object arrays. */
@Test
public void customMissing() throws ConfigurationException, IOException, InterruptedException {
  DebugLogger debugLogger = new DebugLogger();
  EasyConfigurations.overrideEasyConfigurationsLogger(debugLogger);

  // The file is missing values to test repairing.
  File externalFile = getFile("/tests/Yamls/Config_MissingCustom.yml");
  EasyConfigurations.registerPersistentConfig(PersistentConfig_Custom.class, "/tests/Yamls/Config_Custom.yml", externalFile);

  // Checks if a single value can be parsed
  assertEquals(
      new CustomObject("Bob", 3),
      PersistentConfig_Custom.NAME.getAsConfigObject());

  // Checks if the array could be parsed
  assertEquals(
      new CustomObject("Anne", 0),
      PersistentConfig_Custom.NAMES.getAsConfigObjectList().get(0));

  assertEquals(
      new CustomObject("Jacob", 0),
      PersistentConfig_Custom.NAMES.getAsConfigObjectList().get(1));

  // Waits for any changes to be written to the file
  waitForWrite();

  // Checks that the changes were written correctly
  ReadYaml externalYaml = new ReadYaml(Files.newInputStream(externalFile.toPath()));
  ReadYaml internalYaml = new ReadYaml(getResource("/tests/Yamls/Config_Custom.yml"));

  assertTrue(externalYaml.identical(internalYaml));

  // Tests that the correct logs were output
  assertSame(debugLogger.output.get(0).logType, LogType.EXTERNAL_UNUSED_PATH);
  assertSame(debugLogger.output.get(1).logType, LogType.EXTERNAL_MISSING_PATH);
  assertSame(debugLogger.output.get(2).logType, LogType.EXTERNAL_MISSING_PATH);
}

/**
 Tests replacing {@link ConfigObject} &amp; config objects. */
@Test
public void customReplace() throws ConfigurationException, IOException, InterruptedException {
  File externalFile = getFile("/tests/Yamls/Config_Custom.yml");
  EasyConfigurations.registerPersistentConfig(PersistentConfig_Custom.class, "/tests/Yamls/Config_Custom.yml", externalFile);

  assertEquals(
      new CustomObject("Bob", 3),
      PersistentConfig_Custom.NAME.getAsConfigObject());

  PersistentConfig_Custom.NAME.replaceValue(new CustomObject("Jenny", 10));
  PersistentConfig_Custom.NAMES.replaceValue(Arrays.asList(new CustomObject("Fred", 2), new CustomObject("Tim", 3)));

  // Waits for any changes to be written to the file
  waitForWrite();

  assertEquals(
      new CustomObject("Jenny", 10),
      PersistentConfig_Custom.NAME.getAsConfigObject());

  assertEquals(
      Arrays.asList(new CustomObject("Fred", 2), new CustomObject("Tim", 3)),
      PersistentConfig_Custom.NAMES.getAsConfigObjectList());

  String fileContent = "name: \"Jenny:10\"\n" +
                       "\n" +
                       "names:\n" +
                       "- \"Fred:2\"\n" +
                       "- \"Tim:3\"\n";

  assertEquals(
      fileContent,
      new String(Files.readAllBytes(externalFile.toPath())));
}
}
