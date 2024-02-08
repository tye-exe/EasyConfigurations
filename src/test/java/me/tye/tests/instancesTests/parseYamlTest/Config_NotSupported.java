package me.tye.tests.instancesTests.parseYamlTest;

import me.tye.easyconfigs.ConfigInstance;

import java.util.List;

public enum Config_NotSupported implements ConfigInstance {

EEEEEE(String.class, "e"),
AAAAAAAAAAAAAAAAAAAAAAAA(List.class, "errors"); // Should throw a not supported exception.


Config_NotSupported(Class<?> markedClass, String yamlPath) {
  init(markedClass, yamlPath);
}
}
