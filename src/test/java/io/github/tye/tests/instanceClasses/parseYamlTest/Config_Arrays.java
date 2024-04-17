package io.github.tye.tests.instanceClasses.parseYamlTest;

import io.github.tye.easyconfigs.instances.ConfigInstance;

public enum Config_Arrays implements ConfigInstance {
  arrayTime(String[].class, "arrayTime"),
  numbers(int[].class, "moreArrays");


Config_Arrays(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
