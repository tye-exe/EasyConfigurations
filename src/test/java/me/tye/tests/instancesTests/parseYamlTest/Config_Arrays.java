package me.tye.tests.instancesTests.parseYamlTest;

import me.tye.easyconfigs.ConfigInstance;

public enum Config_Arrays implements ConfigInstance {
  arrayTime(String[].class, "arrayTime"),
  numbers(int[].class, "moreArrays");


Config_Arrays(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
