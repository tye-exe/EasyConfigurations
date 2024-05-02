package io.github.tye.tests.persistentInstanceClasses;

import io.github.tye.easyconfigs.instances.persistent.PersistentConfigInstance;
import io.github.tye.tests.CustomObject;

public enum PersistentConfig_Custom implements PersistentConfigInstance {

  NAME(CustomObject.class, "name"),
  NAMES(CustomObject[].class, "names");

PersistentConfig_Custom(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
