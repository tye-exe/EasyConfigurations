package io.github.tye.tests.readingInstanceClasses;

public enum Keys implements io.github.tye.easyconfigs.keys.Keys {

  joke("joke"),

  unJoke("unJoke");

Keys(String toReplace) {
  init(toReplace);
}

}
