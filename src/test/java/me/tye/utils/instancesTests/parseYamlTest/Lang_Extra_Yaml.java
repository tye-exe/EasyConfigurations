package me.tye.utils.instancesTests.parseYamlTest;

import me.tye.LangInstance;

public enum Lang_Extra_Yaml implements LangInstance {
  lang0("lang0"),
  lang1("lang1"),
  lang2("lang2"),
  // Should be warn for extra lang3 in lang.
  ;

Lang_Extra_Yaml(String yamlPath) {
  init(yamlPath);
}
}
