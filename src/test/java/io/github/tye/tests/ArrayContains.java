package io.github.tye.tests;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;

public class ArrayContains {


/**
 Checks if the given object is contained within the given array.
 @param array  The given array to check.
 @param object The given object to check if it's present in the array.
 @return True if any object in the given array is equal (using its respective #equals() method) to the given object. */
@Contract(pure=true)
@InternalUse
protected static boolean arrayContains(@NotNull Object array, @NotNull Object object) {
  NullCheck.notNull(array, "array");
  NullCheck.notNull(array, "object");

  if (!array.getClass().isArray()) return false;

  int length = Array.getLength(array);
  for (int i = 0; i < length; i++) {
    Object arrayObject = Array.get(array, i);
    if (arrayObject.equals(object)) return true;
  }

  return false;
}

}