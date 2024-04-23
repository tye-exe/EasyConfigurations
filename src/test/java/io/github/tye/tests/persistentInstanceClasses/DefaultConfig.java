package io.github.tye.tests.persistentInstanceClasses;

import io.github.tye.easyconfigs.instances.persistent.PersistentConfigInstance;

public enum DefaultConfig implements PersistentConfigInstance {
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


DefaultConfig(Class<?> instanceOf, String yamlPath) {
  init(instanceOf, yamlPath);
}
}
