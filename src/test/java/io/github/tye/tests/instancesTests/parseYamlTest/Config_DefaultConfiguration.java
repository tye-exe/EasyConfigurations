package io.github.tye.tests.instancesTests.parseYamlTest;

import io.github.tye.easyconfigs.ConfigInstance;

public enum Config_DefaultConfiguration implements ConfigInstance {

  test0(Integer.class, "test0"),
  test1(Integer.class, "test1"),
  test2(Integer.class, "test2"),
  test3(Integer.class, "test3"); // Should fail as it doesn't exist in config

@SuppressWarnings("SameParameterValue") // Including the parameter follows the format of the other enums.
Config_DefaultConfiguration(Class<?> markedClass, String yamlPath) {
  init(markedClass, yamlPath);
}
}
