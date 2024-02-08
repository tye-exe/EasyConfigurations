package me.tye.utils;

import me.tye.internalConfigs.Lang;
import me.tye.utils.annotations.InternalUse;
import me.tye.utils.exceptions.NotSupportedException;
import me.tye.utils.exceptions.NotOfClassException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static me.tye.utils.Utils.toList;

/**
 This enum contains an entry for each type of class that EasyConfigurations can parse.<br>
 Trying to parse the value of an object from a string is a nightmare, since each class implements parsing slightly differently.
 */
@InternalUse
public enum SupportedClasses {

  STRING(String.class),
  BOOLEAN(Boolean.class, boolean.class),
  INTEGER(Integer.class, int.class),
  DOUBLE(Double.class, double.class),
  FLOAT(Float.class, float.class),
  SHORT(Short.class, short.class),
  LONG(Long.class, long.class),
  BYTE(Byte.class, byte.class),
  CHAR(Character.class, char.class),
  LOCAL_DATE_TIME(LocalDateTime.class),
  OFFSET_DATE_TIME(OffsetDateTime.class),
  ZONED_DATE_TIME(ZonedDateTime.class),

  STRING_LIST(String[].class),
  BOOLEAN_LIST(Boolean[].class, boolean[].class),
  INTEGER_LIST(Integer[].class, int[].class),
  DOUBLE_LIST(Double[].class, double[].class),
  FLOAT_LIST(Float[].class, float[].class),
  SHORT_LIST(Short[].class, short[].class),
  LONG_LIST(Long[].class, long[].class),
  BYTE_LIST(Byte[].class, byte[].class),
  CHAR_LIST(Character[].class, char[].class),
  LOCAL_DATE_TIME_LIST(LocalDateTime[].class),
  OFFSET_DATE_TIME_LIST(OffsetDateTime[].class),
  ZONED_DATE_TIME_LIST(ZonedDateTime[].class);

/**
 Contains the class values for classes represented by this enum.
 */
private final @NotNull Class<?>[] classes;

/**
 Creates an enum that represents the given classes.
 * @param classes The given classes.
 */
@InternalUse
SupportedClasses(@NotNull Class<?>... classes) {
  this.classes = classes;
}

/**
 * @return The classes that this enum represents.
 */
@Contract(pure = true)
@InternalUse
public @NotNull Class<?>[] getClasses() {
  return classes;
}

/**
 * @return True is the enum represents a form of array. False otherwise.
 */
public boolean representsArray() {
  return getClasses()[0].isArray();
}


/**
 Checks if the class that this enum represents can parse the given value as represented class.
 * @param rawValue The given value.
 * @return True if it can be parsed.<br>
 * False only if the value cannot be parsed as its intended class or if the value is one that EasyConfigurations hasn't accounted for.
 */
public boolean canParse(@NotNull Object rawValue) {
  // Parsing is handled differently for array & non-array values.
  if (this.representsArray()) {
    return canParseArray(rawValue);
  } else {
    return canParseNonArray(rawValue);
  }
}



/**
 If this method is used on an array enum then it will always return false.<br>
 Checks if the class that this enum represents can parse the given value as represented class.
 * @param rawValue The given value.
 * @return True if it can be parsed.<br>
 * False only if the value cannot be parsed as its intended class or if the value is one that EasyConfigurations hasn't accounted for.
 */
@Contract(pure = true)
@InternalUse
@SuppressWarnings ("ResultOfMethodCallIgnored")
private boolean canParseNonArray(@NotNull Object rawValue) {
  String value = rawValue.toString();

  // Tries to parse the string value of the object in accordance with the enum.
  // If their is an error parsing, then it's caught & false is returned.
  // If it parses successfully, then true is returned.
  try {
    switch (this) {

    // String & Boolean are handled uniquely due to the behaviour or their respective parsing method.
    case STRING: {
      if (rawValue instanceof String) {
        return true;
      }
    }
    case BOOLEAN: {
      return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    case INTEGER: {
      Integer.parseInt(value);
      return true;
    }
    case DOUBLE: {
      Double.parseDouble(value);
      return true;
    }
    case FLOAT: {
      Float.parseFloat(value);
      return true;
    }
    case SHORT: {
      Short.parseShort(value);
      return true;
    }
    case LONG: {
      Long.parseLong(value);
      return true;
    }
    case BYTE: {
      Byte.parseByte(value);
      return true;
    }
    case CHAR: {
      return value.length() == 1;
    }
    case LOCAL_DATE_TIME: {
      LocalDateTime.parse(value);
      return true;
    }
    case OFFSET_DATE_TIME: {
      OffsetDateTime.parse(value);
      return true;
    }
    case ZONED_DATE_TIME: {
      ZonedDateTime.parse(value);
      return true;
    }
    }

    // If there was an error parsing the value then it isn't a valid value
  } catch (Exception ignore) {}

  return false;
}

/**
 If this method is used on a non-array enum then it will always return false.<br>
 Checks if the class that this enum represents can parse the given value as represented class.
 * @param rawValue The given array or List value.
 * @return True if it can be parsed.<br>
 * False only if the value cannot be parsed as its intended class or if the value is one that EasyConfigurations hasn't accounted for.
 */
@Contract(pure = true)
@InternalUse
private <T> boolean canParseArray(@NotNull T rawValue) {
  List<String> value;

  try {
    value = toList(rawValue);

    // If there was an error attempting to parse the input then it wasn't an array or list.
  } catch (Exception ignore) {
    return false;
  }

  boolean couldParseValue = false;

  // Checks that each of the objects within the list can be parsed as it's intended class.
  for (String stringValue : value) {
    switch (this) {

    // Can always be parsed
    case STRING_LIST:
    case BOOLEAN_LIST: {
      return true;
    }

    case INTEGER_LIST: {
      couldParseValue = INTEGER.canParseNonArray(stringValue);
      break;
    }
    case DOUBLE_LIST: {
      couldParseValue = DOUBLE.canParseNonArray(stringValue);
      break;
    }
    case FLOAT_LIST: {
      couldParseValue = FLOAT.canParseNonArray(stringValue);
      break;
    }
    case SHORT_LIST: {
      couldParseValue = SHORT.canParseNonArray(stringValue);
      break;
    }
    case LONG_LIST: {
      couldParseValue = LONG.canParseNonArray(stringValue);
      break;
    }
    case BYTE_LIST: {
      couldParseValue = BYTE.canParseNonArray(stringValue);
      break;
    }
    case CHAR_LIST: {
      couldParseValue = CHAR.canParseNonArray(stringValue);
      break;
    }
    case LOCAL_DATE_TIME_LIST: {
      couldParseValue = LOCAL_DATE_TIME.canParseNonArray(stringValue);
      break;
    }
    case OFFSET_DATE_TIME_LIST: {
      couldParseValue = OFFSET_DATE_TIME.canParseNonArray(stringValue);
      break;
    }
    case ZONED_DATE_TIME_LIST: {
      couldParseValue = ZONED_DATE_TIME.canParseNonArray(stringValue);
      break;
    }
    }

    if (!couldParseValue) return false;
  }

  // True is returned since to make it to here the parse must have been successful every time.
  return couldParseValue;
}

/**
 Parses the give value to the class specified by this enum.<br>
 <br>
 Note: {@link #canParse(Object)} should be performed first to check if the object can be parsed.
 * @param rawValue The given value.
 * @return The value as its intended object. Or just the object if someone (me) forgot to add an enum to this method.
 * @throws NotOfClassException If the object passed in isn't of a class that this enum represents
 */
@InternalUse
public @NotNull Object parse(@NotNull Object rawValue) throws NotOfClassException {
  // Parsing is handled differently for array & non-array values.
  if (this.representsArray()) {
    return parseArray(rawValue);
  } else {
    return parseNonArray(rawValue);
  }
}

/**
 If this method is used on an array enum then it will always throw {@link NotOfClassException}.<br>
 Parses the give value to the class specified by this enum.
 * @param rawValue The given value.
 * @return The value as its intended object.
 * @throws NotOfClassException If the object passed in isn't of a class that this enum represents.
 */
@InternalUse
private @NotNull Object parseNonArray(@NotNull Object rawValue) throws NotOfClassException {
  String value = rawValue.toString();

  try {
    // Parses the non-array values //
    switch (this) {

    // String & Boolean are handled uniquely due to the behaviour or their respective parsing method.
    case STRING: {
      if (rawValue instanceof String) {
        return value;
      }
    }
    case BOOLEAN: {
      if (value.equalsIgnoreCase("true")) return true;
      if (value.equalsIgnoreCase("false")) return false;
    }

    case INTEGER: {
      return Integer.parseInt(value);
    }
    case DOUBLE: {
      return Double.parseDouble(value);
    }
    case FLOAT: {
      return Float.parseFloat(value);
    }
    case SHORT: {
      return Short.parseShort(value);
    }
    case LONG: {
      return Long.parseLong(value);
    }
    case BYTE: {
      return Byte.parseByte(value);
    }
    case CHAR: {
      return value.charAt(0);
    }
    case LOCAL_DATE_TIME: {
      return LocalDateTime.parse(value);
    }
    case OFFSET_DATE_TIME: {
      return OffsetDateTime.parse(value);
    }
    case ZONED_DATE_TIME: {
      return ZonedDateTime.parse(value);
    }
    }

  } catch (RuntimeException e) {
    throw new NotOfClassException(Lang.notOfClass(value, this.toString()));
  }

  return value;
}

/**
 If this method is used on a non-array enum then it will always throw {@link NotOfClassException}.<br>
 Parses the give value to the class specified by this enum.
 * @param value The given array or List value.
 * @return The value as its intended object.
 * @throws NotOfClassException If the object passed in isn't of a class that this enum represents.
 */
@InternalUse
private @NotNull Object parseArray(@NotNull Object value) throws NotOfClassException {
  try {
    List<String> stringsToParse;
    stringsToParse = toList(value);

    List<Object> outputList = new ArrayList<>(stringsToParse.size());

    for (String str : stringsToParse) {

      switch (this) {

      case STRING_LIST: {
        outputList.add(STRING.parse(str));
        break;
      }
      case BOOLEAN_LIST: {
        outputList.add(BOOLEAN.parse(str));
        break;
      }
      case INTEGER_LIST: {
        outputList.add(INTEGER.parse(str));
        break;
      }
      case DOUBLE_LIST: {
        outputList.add(DOUBLE.parse(str));
        break;
      }
      case FLOAT_LIST: {
        outputList.add(FLOAT.parse(str));
        break;
      }
      case SHORT_LIST: {
        outputList.add(SHORT.parse(str));
        break;
      }
      case LONG_LIST: {
        outputList.add(LONG.parse(str));
        break;
      }
      case BYTE_LIST: {
        outputList.add(BYTE.parse(str));
        break;
      }
      case CHAR_LIST: {
        outputList.add(CHAR.parse(str));
        break;
      }
      case LOCAL_DATE_TIME_LIST: {
        outputList.add(LOCAL_DATE_TIME.parse(str));
        break;
      }
      case OFFSET_DATE_TIME_LIST: {
        outputList.add(OFFSET_DATE_TIME.parse(str));
        break;
      }
      case ZONED_DATE_TIME_LIST: {
        outputList.add(ZONED_DATE_TIME.parse(str));
        break;
      }
      }
    }

    // If the outputList is empty then then given array or list isn't one supported by EasyConfigurations.
    if (outputList.isEmpty()) {
      throw new Exception();
    }

    return outputList;

    // Catches any exceptions & then formats them.
  } catch (Exception e) {

    // If it's a NotOfClassException thrown by one of the parse methods then just throw that.
    if (e.getClass().equals(NotOfClassException.class)) throw new NotOfClassException(e);

    // Tries to get the canonical name of the class, but a canonical name doesn't exist for every class.
    // So the regular name is used as a fall bac
    String className = value.getClass().getCanonicalName();
    if (className == null) {
      className = value.getClass().getName();
    }

    throw new NotOfClassException(Lang.notOfClass(className, this.toString()));
  }
}


/**
 Checks if the given class is represented by any existing enum in {@link SupportedClasses}
 * @param classToMatch The given class.
 * @return True if the given class is represented. False otherwise.
 */
@InternalUse
public static boolean existsAsEnum(@NotNull Class<?> classToMatch) {
  SupportedClasses[] supportedClasses = SupportedClasses.class.getEnumConstants();

  // Loops over this class to find if any enums support the class to match
  for (SupportedClasses supportedClass : supportedClasses) {
    for (Class<?> alikeClass : supportedClass.getClasses()) {
      if (classToMatch.equals(alikeClass)) return true;
    }
  }

  return false;
}

/**
 Matches the given class to an enum inside {@link SupportedClasses}.
 * @param classToMatch The given class.
 * @return The enum that represents the given class.
 * @throws NotSupportedException If the given classes doesn't match any supported classes.
 */
@InternalUse
public static @NotNull SupportedClasses getAsEnum(@NotNull Class<?> classToMatch) throws NotSupportedException {
  SupportedClasses[] supportedClasses = SupportedClasses.class.getEnumConstants();

  // Loops over this class to find if any enums support the class to match
  for (SupportedClasses supportedClass : supportedClasses) {
    for (Class<?> alikeClass : supportedClass.getClasses()) {
      if (classToMatch.equals(alikeClass)) return supportedClass;
    }
  }

  throw new NotSupportedException(Lang.classNotSupported(classToMatch.getName()));
}

}
