package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.LangInstance;

public enum Lang_ArrayFail implements LangInstance {

  testingLists("fail"); // Should fails as lang doesn't support arrays

Lang_ArrayFail(String yamlPath) {
  init(yamlPath);
}
}
