package io.github.tye.tests;

import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.yamls.ReadYaml;
import io.github.tye.easyconfigs.yamls.WriteYaml;
import io.github.tye.tests.persistentInstanceClasses.DefaultConfig;
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

  ReadYaml fullYaml = new ReadYaml(getResource("/tests/Yamls/externalYamls/DefaultYaml.yml"));
  WriteYaml yaml = new WriteYaml(
      "/tests/Yamls/externalYamls/DefaultYaml.yml", // Full yaml
      getFile("/tests/Yamls/externalYamls/Extra.yml"), // Broken yaml
      DefaultConfig.class
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

  ReadYaml fullYaml = new ReadYaml(getResource("/tests/Yamls/externalYamls/DefaultYaml.yml"));
  WriteYaml yaml = new WriteYaml(
      "/tests/Yamls/externalYamls/DefaultYaml.yml", // Full yaml
      getFile("/tests/Yamls/externalYamls/MessedUp.yml"), // External yaml
      DefaultConfig.class
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
      DefaultConfigurationException.class,
      () -> EasyConfigurations.registerPersistentConfig(DefaultConfig.class, "/tests/Yamls/Invalid.yml", invalidTest)
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

  EasyConfigurations.registerPersistentConfig(DefaultConfig.class, "/tests/Yamls/externalYamls/DefaultYaml.yml", invalidTest);

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

  EasyConfigurations.registerPersistentConfig(DefaultConfig.class, "/tests/Yamls/externalYamls/DefaultYaml.yml", copyTest);

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
  EasyConfigurations.registerPersistentConfig(DefaultConfig.class, "/tests/Yamls/externalYamls/DefaultYaml.yml", getFile("/tests/Yamls/externalYamls/DefaultYaml.yml"));

  for (DefaultConfig yamlValue : DefaultConfig.values()) {
    Object correctValue = preFormattedValues.get(yamlValue.getYamlPath());

    assertEquals(correctValue, yamlValue.getValue());
  }

}


}
