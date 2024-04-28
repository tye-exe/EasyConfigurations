package io.github.tye.tests.persistentInstanceClasses;

import io.github.tye.easyconfigs.instances.persistent.PersistentLangInstance;

public enum Lang_Default implements PersistentLangInstance {

  word("word"),
  words("words"),
  ;

Lang_Default(String yamlPath) {
  init(yamlPath);
}
}
