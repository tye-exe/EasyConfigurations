package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.LangInstance;

public enum Lang_Keys implements LangInstance {

  keys("keys");

Lang_Keys(String yamlPath) {
  init(yamlPath);
}
}
