package io.github.tye.tests.readingInstanceClasses;

import io.github.tye.easyconfigs.instances.reading.ReadingLangInstance;

public enum ReadingLang_Keys implements ReadingLangInstance {

  joke("joke"),
  unJoke("unJoke");

ReadingLang_Keys(String yamlPath) {
  init(yamlPath);
}
}
