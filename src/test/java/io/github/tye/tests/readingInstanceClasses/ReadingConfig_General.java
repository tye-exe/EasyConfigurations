package io.github.tye.tests.readingInstanceClasses;

import io.github.tye.easyconfigs.instances.reading.ReadingConfigInstance;

import java.time.LocalDateTime;

public enum ReadingConfig_General implements ReadingConfigInstance {
  example(int.class, "example"),
  another_one(String.class, "another.one"),
  another_two(String.class, "another.two"),
  quotes(String.class, "quotes"),
  nuhuh(Boolean.class, "nuhuh"),
  mhm(boolean.class, "mhm"),
  time(LocalDateTime.class, "timeIsSlipping"),
  nestedArrays(String[].class, "nested.arrays"),
  floats(float.class, "floatingAround"),
  note(String[].class, "note");

ReadingConfig_General(Class<?> markedClazz, String yamlPath) {
  init(markedClazz, yamlPath);
}
}
