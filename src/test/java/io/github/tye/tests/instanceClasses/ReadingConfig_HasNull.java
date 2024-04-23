package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.reading.ReadingConfigInstance;

public enum ReadingConfig_HasNull implements ReadingConfigInstance {
  empty(String.class, "empties"), // Will fail due to null value
  evenMoreEmpty(String[].class, "evenMoreEmpty");

ReadingConfig_HasNull(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
