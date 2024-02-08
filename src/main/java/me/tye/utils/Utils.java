package me.tye.utils;

import me.tye.BaseInstance;
import me.tye.internalConfigs.Lang;
import me.tye.utils.annotations.InternalUse;
import me.tye.utils.annotations.Utilities;
import me.tye.utils.exceptions.DefaultConfigurationException;
import me.tye.utils.exceptions.NeverThrownExceptions;
import me.tye.utils.exceptions.NotOfClassException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Level;

import static me.tye.utils.Consts.logger;

/**
 This class is a general utility class for EasyConfigurations. */
@Utilities
public final class Utils {

/**
 This class is a utility class & should not be instantiated. */
private Utils() {}

/**
 If the given object is null throws a {@link NullPointerException} with the default lang message.
 @param object     The given object.
 @param objectName The name of the given object.
 @throws NullPointerException If the given object is null */
@Contract ("null, _ -> fail; !null, _ -> _")
@Utilities
public static void notNull(@Nullable Object object, @NotNull String objectName) throws NullPointerException {
  if (object != null) return;

  throw new NullPointerException(me.tye.internalConfigs.Lang.notNull(objectName));
}

/**
 Checks if the given object is contained within the given array.
 * @param array The given array to check.
 * @param object The given object to check if it's present in the array.
 * @return True if any object in the given array is equal (using it's respective #equals() method) to the given object.
 */
@Contract(pure = true)
@Utilities
public static boolean arrayContains(@NotNull Object array, @NotNull Object object) {
  notNull(array ,"array");
  notNull(array ,"object");

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
 * @param value The given value. It must be an instance of {@link List} or an array of any type.
 * @return The given value converted to an object list.
 * @throws IllegalArgumentException If the given value isn't a list or an array.
 */
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

/**
 @param clazz The given class to check.
 @return True if the given class uses the default {@link Object#toString()} method. */
@Contract (pure=true)
@Utilities
public static boolean usesDefaultToString(@NotNull Class<?> clazz) {
  try {
    return clazz.getMethod("toString").getDeclaringClass() == Object.class;

  } catch (NoSuchMethodException e) {
    throw new NeverThrownExceptions(e);
  }
}

/**
 Checks if the intendedType matches the actual object type in the map that the instance points to.
 * @param instance The instance of config or lang to check against.
 * @param intendedType The {@link SupportedClasses} that the map value is expected to be.
 * @throws NotOfClassException If the intended type doesn't match the actual type in the map.
 */
@Utilities
public static void classCheck(@NotNull SupportedClasses intendedType, @NotNull BaseInstance instance) throws NotOfClassException {
  // Will contain the names of classes that the were given in the case of them not matching the intended class.
  StringBuilder classes = new StringBuilder();

  for (Class<?> clazz : intendedType.getClasses()) {
    if (instance.getMarkedClass().equals(clazz)) return;

    // Not all classes have canonical names
    if (clazz.getCanonicalName() != null) {
      classes.append(clazz.getCanonicalName());
    } else {
      classes.append(clazz.getName());
    }

    classes.append(" ");
  }

  throw new NotOfClassException(Lang.notOfClass(instance.getYamlPath(), classes.toString()));
}

}
