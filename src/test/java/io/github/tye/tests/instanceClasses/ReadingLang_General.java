package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.reading.ReadingLangInstance;

public enum ReadingLang_General implements ReadingLangInstance {
  lyrics_never("lyrics.never"),
  lyrics_give("lyrics.give"),
  lyrics_up("lyrics.up"),

  hehe("or.this.works"),

  basics("backToBasics");

ReadingLang_General(String yamlPath) {
  init(yamlPath);
}
}
