package me.tye.easyconfigs.utils;

import me.tye.easyconfigs.BaseInstance;
import me.tye.easyconfigs.SupportedClasses;
import me.tye.easyconfigs.internalConfigs.Lang;
import me.tye.easyconfigs.utils.annotations.Utilities;
import me.tye.easyconfigs.utils.exceptions.NeverThrownExceptions;
import me.tye.easyconfigs.utils.exceptions.NotOfClassException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  throw new NullPointerException(Lang.notNull(objectName));
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
