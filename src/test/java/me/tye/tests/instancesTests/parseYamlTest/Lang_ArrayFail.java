package me.tye.tests.instancesTests.parseYamlTest;

import me.tye.easyconfigs.LangInstance;

public enum Lang_ArrayFail implements LangInstance {

test1("testing"),
testingLists("fail"); // Should fails as lang doesn't support arrays

Lang_ArrayFail(String yamlPath) {
  init(yamlPath);
}
}
