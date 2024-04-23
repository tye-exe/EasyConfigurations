package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.reading.ReadingLangInstance;

public enum ReadingLang_ArrayFail implements ReadingLangInstance {

  testingLists("fail"); // Should fails as lang doesn't support arrays

ReadingLang_ArrayFail(String yamlPath) {
  init(yamlPath);
}
}
