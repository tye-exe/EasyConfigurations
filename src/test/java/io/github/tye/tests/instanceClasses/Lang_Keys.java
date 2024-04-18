package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.LangInstance;

public enum Lang_Keys implements LangInstance {

  joke("joke"),
  unJoke("unJoke");

Lang_Keys(String yamlPath) {
  init(yamlPath);
}
}
