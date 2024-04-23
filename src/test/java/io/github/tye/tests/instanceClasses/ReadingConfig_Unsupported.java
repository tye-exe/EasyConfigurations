package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.reading.ReadingConfigInstance;

import java.util.List;

public enum ReadingConfig_Unsupported implements ReadingConfigInstance {
  unsupported(List.class, "unsupported");

ReadingConfig_Unsupported(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
