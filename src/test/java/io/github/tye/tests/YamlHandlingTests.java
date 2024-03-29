package io.github.tye.tests;

import io.github.tye.easyconfigs.YamlHandling;
import io.github.tye.easyconfigs.utils.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.utils.exceptions.NotSupportedException;
import io.github.tye.easyconfigs.utils.exceptions.YamlParseException;
import io.github.tye.tests.instancesTests.parseInternalYamlFormattingTest.Config_Empty;
import io.github.tye.tests.instancesTests.parseInternalYamlFormattingTest.Config_General;
import io.github.tye.tests.instancesTests.parseInternalYamlFormattingTest.Config_HasNull;
import io.github.tye.tests.instancesTests.parseYamlTest.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.yaml.snakeyaml.Yaml;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.tye.easyconfigs.YamlHandling.parseInternalYaml;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class YamlHandlingTests {


private static final String yaml_Empty = "";
private static final String yaml_General = "example: 4\n" + "\n" + "another:\n" + "  one: \"one!\"\n" + "  two: \"two!\"\n" + "\n" + "quotes: \"\\\"quoted\\\" <- it's quoted.\"\n" + "\n" + "nuhuh: false\n" + "mhm: true\n" + "\n" + "timeIsSlipping: \"2024-02-06T17:15:53.315\"\n" + "\n" + "nested.arrays: [\"no one\", \"expected the\", \"spanish\", \"inquisition!\"]\n" + "\n" + "floatingAround: \"83.93\"\n";
private static final String yaml_HasNull = "arrayTime:\n" + "  - \"first!\"\n" + "  - \"second!\"\n" + "  - \"last!\"\n" + "  - unquoted\n" + "\n" + "empties:\n" + "\n" + "evenMoreEmpty: []\n" + "\n" + "moreArrays: [3, 2, 1]";

private static final HashMap<String, Object> map_Empty = new HashMap<>();
private static final HashMap<String, Object> map_General = createMap_General();
private static final HashMap<String, Object> map_HasNull = createMap_HasNull(); // Contains a null value.

private static HashMap<String, Object> createMap_General() {
  ArrayList<Object> inquisition = new ArrayList<>();
  inquisition.add("no one"); inquisition.add("expected the"); inquisition.add("spanish"); inquisition.add("inquisition!");

  HashMap<String, Object> preFormattedValues = new HashMap<>();
  preFormattedValues.put("nuhuh", false);
  preFormattedValues.put("another.one", "one!");
  preFormattedValues.put("mhm", Boolean.TRUE);
  preFormattedValues.put("another.two", "two!");
  preFormattedValues.put("example", 4);
  preFormattedValues.put("quotes", "\"quoted\" <- it's quoted.");
  preFormattedValues.put("timeIsSlipping", "2024-02-06T17:15:53.315");
  preFormattedValues.put("nested.arrays", inquisition);
  preFormattedValues.put("floatingAround", "83.93");
  return preFormattedValues;
}
private static HashMap<String, Object> createMap_HasNull() {
  HashMap<String, Object> preFormattedValues = new HashMap<>();
  preFormattedValues.put("arrayTime", Arrays.asList("first!", "second!", "last!", "unquoted"));
  preFormattedValues.put("evenMoreEmpty", new ArrayList<>());
  preFormattedValues.put("moreArrays", Arrays.asList(3, 2, 1));
  preFormattedValues.put("empties", null);
  return preFormattedValues;
}


private static Stream<Arguments> yaml_provider() {
  return Stream.of(
      arguments(new Yaml().load(yaml_Empty), map_Empty),
      arguments(new Yaml().load(yaml_General), map_General),
      arguments(new Yaml().load(yaml_HasNull), map_HasNull)
  );
}

@ParameterizedTest
@MethodSource("yaml_provider")
void getKeysRecursive(Map<?,?> parsedYaml, HashMap<String, Object> preFormattedValues) {
  HashMap<String, Object> returnedFormatting = YamlHandling.getKeysRecursive(parsedYaml);
  assertEquals(returnedFormatting, preFormattedValues, "Maps should match.");
}


private static final HashMap<String, Object> formattedMap_General = createFormattedMap_General();
private static HashMap<String, Object> createFormattedMap_General() {
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
  return preFormattedValues;
}

private static Stream<Arguments> yaml_string_provider() {
  return Stream.of(
      arguments("/tests/Yamls/parseInternalYamlFormattingTest/Config_Empty.yml", Config_Empty.class, map_Empty),
      arguments("/tests/Yamls/parseInternalYamlFormattingTest/Config_General.yml", Config_General.class, formattedMap_General)
  );
}

@ParameterizedTest
@MethodSource("yaml_string_provider")
void parseInternalYamlFormatting(String yamlPath, Class<?> configInstance, HashMap<String, Object> preFormattedValues) {
  HashMap<String, Object> parsedYaml = parseInternalYaml(configInstance, yamlPath);
  assertEquals(parsedYaml, preFormattedValues, "Maps should match.");
}

@Test
void parseInternalYamlFormattingWithNull() {
  assertThrowsExactly(YamlParseException.class, () -> parseInternalYaml(
                          Config_HasNull.class, "/tests/Yamls/parseInternalYamlFormattingTest/Config_HasNull.yml"),
      "This method should throw a YamlParseException as the yaml file contains a null value.");
}


private static Stream<Arguments> instance_provider() {
  return Stream.of(
      arguments(io.github.tye.tests.instancesTests.parseYamlTest.Config_Empty.class, "/tests/Yamls/parseYamlTest/Config_Empty.yml", true, null),
      arguments(io.github.tye.tests.instancesTests.parseYamlTest.Config_General.class, "/tests/Yamls/parseYamlTest/Config_General.yml", true, null),
      arguments(Config_Arrays.class, "/tests/Yamls/parseYamlTest/Config_Arrays.yml", true, null),
      arguments(Config_DefaultConfiguration.class, "/tests/Yamls/parseYamlTest/Config_DefaultConfiguration.yml", false, DefaultConfigurationException.class),
      arguments(Lang_ArrayFail.class, "/tests/Yamls/parseYamlTest/Lang_ArrayFail.yml", false, DefaultConfigurationException.class),
      arguments(Lang_General.class, "/tests/Yamls/parseYamlTest/Lang_General.yml", true, null),
      arguments(Lang_Extra_Yaml.class, "/tests/Yamls/parseYamlTest/Lang_Extra_Yaml.yml", true, null),
      arguments(Config_NotSupported.class, "/tests/Yamls/parseYamlTest/Config_NotSupported.yml", false, NotSupportedException.class),
      arguments(io.github.tye.tests.instancesTests.parseYamlTest.Config_HasNull.class, "/tests/Yamls/parseYamlTest/Config_HasNull.yml", false, YamlParseException.class)
  );
}

@ParameterizedTest
@MethodSource("instance_provider")
<T extends Throwable> void parseYamlTest(@NotNull Class<?> configInstance, @NotNull String yamlPath, boolean shouldParse, @Nullable Class<T> expectedException) {
  if (shouldParse) {
    assertDoesNotThrow(() -> parseInternalYaml(configInstance, yamlPath), "The yaml file should be parsed successfully.");
  }
  else {
    assertNotNull(expectedException, "If a parse is expected to throw an exception the exact exception class should be supplied.");
    assertThrowsExactly(expectedException, () -> parseInternalYaml(configInstance, yamlPath), "The yaml file should have thrown an error when parsed.");
  }
}

}