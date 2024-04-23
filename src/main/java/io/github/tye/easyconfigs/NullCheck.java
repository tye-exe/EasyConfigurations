package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;

/**
 Contains a method to check if a given object is null &amp; constructs a NullPointerException if it
 is null. */
@InternalUse
public class NullCheck {

/**
 If the given object is null throws a {@link NullPointerException} with the default lang message.
 @param object     The given object.
 @param objectName The name of the given object.
 @throws NullPointerException If the given object is null */
@Contract(value="null, _ -> fail; _, _ -> _")
@InternalUse
public static void notNull(@Nullable Object object, @NotNull String objectName) throws NullPointerException {
  if (object == null) throw new NullPointerException(Lang.notNull(objectName));

  // If the object is Iterable, check all the values.
  if (object instanceof Iterable) {
    boolean containsNull = recursiveIterableNullCheck(((Iterable<?>) object));

    if (containsNull) throw new NullPointerException(Lang.containsNull(objectName));
  }

  // If the object is an array, check all the values.
  if (object.getClass().isArray()) {
    boolean containsNull = recursiveArrayNullCheck(object);

    if (containsNull) throw new NullPointerException(Lang.containsNull(objectName));
  }
}

/**
 Recursively checks an iterable object for null values.
 @param iterable The iterable to check recursively.
 @return True if any value was null. */
@Contract(pure=true)
@InternalUse
private static boolean recursiveIterableNullCheck(@NotNull Iterable<?> iterable) {
  for (Object iterableObject : iterable) {
    if (iterableObject == null) return true;

    if (!(iterableObject instanceof Iterable)) continue;

    return recursiveIterableNullCheck((Iterable<?>) iterableObject);
  }

  return false;
}

/**
 Recursively checks an array for null values.
 @param array The array to check recursively.
 @return True if any value was null. */
@Contract(pure=true)
@InternalUse
private static boolean recursiveArrayNullCheck(@NotNull Object array) {
  int length = Array.getLength(array);

  for (int i = 0; i < length; ++i) {
    Object value = Array.get(array, i);
    if (value == null) return true;

    if (!value.getClass().isArray()) continue;

    return recursiveArrayNullCheck(value);
  }

  return false;
}

}
