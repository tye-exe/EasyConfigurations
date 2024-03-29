package io.github.tye.tests.instancesTests.parseYamlTest;

import io.github.tye.easyconfigs.ConfigInstance;

import java.time.LocalDateTime;

public enum Config_General implements ConfigInstance {
example(int.class, "example"),
another_one(String.class, "another.one"),
another_two(String.class, "another.two"),
quotes(String.class, "quotes"),
nuhuh(Boolean.class, "nuhuh"),
mhm(boolean.class, "mhm"),
time(LocalDateTime.class, "timeIsSlipping");

Config_General(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
