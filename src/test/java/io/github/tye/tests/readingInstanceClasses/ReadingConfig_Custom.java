package io.github.tye.tests.readingInstanceClasses;

import io.github.tye.easyconfigs.instances.reading.ReadingConfigInstance;
import io.github.tye.tests.CustomObject;

public enum ReadingConfig_Custom implements ReadingConfigInstance {

  NAME(CustomObject.class, "name"),
  NAMES(CustomObject[].class, "names");

ReadingConfig_Custom(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
