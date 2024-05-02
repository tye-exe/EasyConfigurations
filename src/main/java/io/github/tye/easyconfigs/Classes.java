package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

/**
 Contains methods that operate on {@link Class} objects. */
@InternalUse
public class Classes {

/**
 Tries to get the canonical name of the given class.
 @param givenClass The class to attempt to get the canonical name of.
 @return The canonical name of the class. If the class doesn't have a canonical name then the raw
 name of the class is returned. */
@InternalUse
public static @NotNull String getName(@NotNull Class<?> givenClass) {
  // Tries to get the canonical name of the class, but a canonical name doesn't exist for every class.
  // So the regular name is used as a fallback.
  String className = givenClass.getCanonicalName();
  if (className == null) {
    className = givenClass.getName();
  }
  return className;
}

/**
 Gets the component of the class, if applicable. Otherwise, the given class is returned.
 @param clazz The given class
 @return The component of the class, if applicable. Otherwise, the given class is returned. */
public static @NotNull Class<?> getComponent(@NotNull Class<?> clazz) {
  if (!clazz.isArray()) return clazz;
  return clazz.getComponentType();
}
}
