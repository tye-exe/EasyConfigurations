package io.github.tye.tests.instanceClasses;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;

public class MyFile extends File {
public MyFile(@NotNull String pathname) {
  super(pathname);
}

public MyFile(String parent, @NotNull String child) {
  super(parent, child);
}

public MyFile(File parent, @NotNull String child) {
  super(parent, child);
}

public MyFile(@NotNull URI uri) {
  super(uri);
}
}
