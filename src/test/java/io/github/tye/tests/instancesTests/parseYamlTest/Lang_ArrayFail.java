package io.github.tye.tests.instancesTests.parseYamlTest;

import io.github.tye.easyconfigs.instances.LangInstance;

public enum Lang_ArrayFail implements LangInstance {

  test1("testing"),
  testingLists("fail"); // Should fails as lang doesn't support arrays

Lang_ArrayFail(String yamlPath) {
  init(yamlPath);
}
}
