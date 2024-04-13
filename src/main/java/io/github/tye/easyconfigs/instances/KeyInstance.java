package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.ClassName;
import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// These methods are intended for use projects using Easy Configurations as a dependency.
@SuppressWarnings("unused")
@ExternalUse
public interface KeyInstance {

/**
 Stores the string that will get replaced with {@link #replaceWith}. */
@InternalUse
String[] toReplace = new String[1];

/**
 Stores the string that will be replaced by {@link #toReplace}. */
@InternalUse
String[] replaceWith = new String[1];


/**
 Initiates a KeyInstance.
 @param toReplace The value that should be replaced. The
 {@link EasyConfigurations#setKeyCharacters(String, String) values set} that define
 a key <b>must not</b> be included here. */
@ExternalUse
default void init(@NotNull String toReplace) {
  this.toReplace[0] = toReplace;
}


/**
 Sets the string value that this key will replace to the string from the given object.<br>
 If the given object isn't a string or doesn't have a string representation then the class name of
 the object will be used instead.
 @param object The object to get the string value of.
 @return The modified key object.
 @throws NullPointerException If the given object was null */
@ExternalUse
default @NotNull KeyInstance replaceWith(@NotNull Object object) throws NullPointerException {
  NullCheck.notNull(object, "Replacement object");

  if (usesDefaultToString(object.getClass())) {
    this.replaceWith[0] = ClassName.getName(object.getClass());
  }
  else {
    this.replaceWith[0] = object.toString();
  }

  return this;
}


/**
 Gets the value that the key will replace.
 @return The key value (with starting &amp; ending markers included) that will be replaced. */
@Contract(pure=true)
@InternalUse
default @NotNull String getReplacementValue() {
  return EasyConfigurations.keyStart + this.replaceWith[0] + EasyConfigurations.keyEnd;
}


/**
 Checks if the given class has overridden the default {@link Object#toString() #toString()} method
 provided by Object.
 @param clazz The given class to check.
 @return True if the given class uses the default {@link Object#toString()} method. */
@Contract(pure=true)
@InternalUse
static boolean usesDefaultToString(@NotNull Class<?> clazz) {
  try {
    return clazz.getMethod("toString").getDeclaringClass() == Object.class;
  }
  // This error should never be thrown, as all classes extend the Object class, which implements the "toString" method.
  catch (NoSuchMethodException e) {
    throw new RuntimeException(e);
  }
}
}
