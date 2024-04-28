package io.github.tye.tests.persistentInstanceClasses;

import io.github.tye.easyconfigs.instances.persistent.PersistentConfigInstance;

public enum Config_Default implements PersistentConfigInstance {
  hmm(String.class, "test.hmm"),
  nah(String.class, "test.nah"),
  eh(String.class, "eh"),
  ehh(String.class, "ehh"),
  ehhhhhh(String.class, "ehhhhhh"),
  This(String[].class, "this"),
  multiKey(String.class, "multiple.keys.in.one"),
  number(Integer.class, "number"),
  numbers(Integer[].class, "numbers"),

  ;


Config_Default(Class<?> instanceOf, String yamlPath) {
  init(instanceOf, yamlPath);
}
}
