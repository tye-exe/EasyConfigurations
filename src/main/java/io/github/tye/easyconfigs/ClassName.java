package io.github.tye.easyconfigs;

import org.jetbrains.annotations.NotNull;

/**
 Contains a method to get the canonical name of a class. */
public class ClassName {

/**
 Tries to get the canonical name of the given class.
 @param givenClass The class to attempt to get the canonical name of.
 @return The canonical name of the class. If the class doesn't have a canonical name then the raw
 name of the class is returned. */
public static @NotNull String getName(@NotNull Class<?> givenClass) {
  // Tries to get the canonical name of the class, but a canonical name doesn't exist for every class.
  // So the regular name is used as a fallback.
  String className = givenClass.getCanonicalName();
  if (className == null) {
    className = givenClass.getName();
  }
  return className;
}

}
