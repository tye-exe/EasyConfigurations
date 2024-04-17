package io.github.tye.tests.instanceClasses;

import io.github.tye.easyconfigs.instances.KeyInstance;

public enum Keys implements KeyInstance {

  joke("joke");

Keys(String toReplace) {
  init(toReplace);
}

}
