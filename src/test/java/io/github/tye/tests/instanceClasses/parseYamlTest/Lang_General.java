package io.github.tye.tests.instanceClasses.parseYamlTest;

import io.github.tye.easyconfigs.instances.LangInstance;

public enum Lang_General implements LangInstance {
  lyrics_never("lyrics.never"),
  lyrics_give("lyrics.give"),
  lyrics_up("lyrics.up"),

  hehe("or.this.works"),

  basics("backToBasics");

Lang_General(String yamlPath) {
  init(yamlPath);
}
}
