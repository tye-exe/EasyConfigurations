package me.tye.utils.instancesTests.parseYamlTest;

import me.tye.ConfigInstance;

public enum Config_DefaultConfiguration implements ConfigInstance {

  test0(Integer.class, "test0"),
  test1(Integer.class, "test1"),
  test2(Integer.class, "test2"),
  test3(Integer.class, "test3"); // Should fail as it doesn't exist in config

Config_DefaultConfiguration(Class<?> markedClass, String yamlPath) {
  init(markedClass, yamlPath);
}
}
