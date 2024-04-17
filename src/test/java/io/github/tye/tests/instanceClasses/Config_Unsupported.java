package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.ConfigInstance;

import java.util.List;

public enum Config_Unsupported implements ConfigInstance {
  unsupported(List.class, "unsupported");

Config_Unsupported(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
