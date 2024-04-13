package io.github.tye.tests.instancesTests.parseInternalYamlFormattingTest;

import io.github.tye.easyconfigs.instances.ConfigInstance;

import java.time.LocalDateTime;

public enum Config_General implements ConfigInstance {
  example(int.class, "example"),
  another_one(String.class, "another.one"),
  another_two(String.class, "another.two"),
  quotes(String.class, "quotes"),
  nuhuh(Boolean.class, "nuhuh"),
  mhm(boolean.class, "mhm"),
  time(LocalDateTime.class, "timeIsSlipping"),
  nestedArrays(String[].class, "nested.arrays"),
  floats(float.class, "floatingAround");

Config_General(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
