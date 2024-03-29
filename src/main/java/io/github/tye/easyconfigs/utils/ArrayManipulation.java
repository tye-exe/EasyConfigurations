package io.github.tye.easyconfigs.utils;

import io.github.tye.easyconfigs.utils.annotations.Utilities;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 This class contains methods that are use while working with arrays in EasyConfigurations.
 */
public final class ArrayManipulation {

/**
 This class is a utility class &amp; should not be instantiated. */
private ArrayManipulation() {}

/**
 Checks if the given object is contained within the given array.
 @param array  The given array to check.
 @param object The given object to check if it's present in the array.
 @return True if any object in the given array is equal (using its respective #equals() method) to the given object. */
@Contract(pure=true)
@Utilities
public static boolean arrayContains(@NotNull Object array, @NotNull Object object) {
  Utils.notNull(array, "array");
  Utils.notNull(array, "object");

  if (!array.getClass().isArray()) return false;

  int length = Array.getLength(array);
  for (int i = 0; i < length; i++) {
    Object arrayObject = Array.get(array, i);
    if (arrayObject.equals(object)) return true;
  }

  return false;
}

/**
 Converts the given value to {@literal List<Object>}.
 @param value The given value. It must be an instance of {@link List} or an array of any type.
 @return The given value converted to an object list.
 @throws IllegalArgumentException If the given value isn't a list or an array. */
@Utilities
public static @NotNull List<String> toList(@NotNull Object value) throws IllegalArgumentException {
  ArrayList<String> stringList;

  // Converts the List or array into an Object list.
  if (value instanceof List) {
    List<?> valueList = (List<?>) value;
    stringList = new ArrayList<>(valueList.size());

    for (Object obj : valueList) {
      stringList.add(obj.toString());
    }
  }
  else {
    int length = Array.getLength(value);
    stringList = new ArrayList<>(length);

    for (int i = 0; i < length; ++i) {
      stringList.add(Array.get(value, i).toString());
    }
  }

  return stringList;
}
}
