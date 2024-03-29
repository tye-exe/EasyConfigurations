package io.github.tye.tests.instancesTests.parseYamlTest;

import io.github.tye.easyconfigs.ConfigInstance;

public enum Config_Arrays implements ConfigInstance {
  arrayTime(String[].class, "arrayTime"),
  numbers(int[].class, "moreArrays");


Config_Arrays(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
