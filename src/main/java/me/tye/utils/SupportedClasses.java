package me.tye.utils;

import me.tye.internalConfigs.Lang;
import me.tye.utils.annotations.InternalUse;
import me.tye.utils.exceptions.NotOfClassException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 This enum contains an entry for each type of class that EasyConfigurations can parse.<br>
 Trying to parse the value of an object from a string is a nightmare, since each class implements parsing slightly differently.
 */
@InternalUse
public enum SupportedClasses {

  OBJECT(Object.class),
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

  OBJECT_ARRAY(Object[].class),
  STRING_ARRAY(String[].class),
  BOOLEAN_ARRAY(Boolean[].class, boolean[].class),
  INTEGER_ARRAY(Integer[].class, int[].class),
  DOUBLE_ARRAY(Double[].class, double[].class),
  FLOAT_ARRAY(Float[].class, float[].class),
  SHORT_ARRAY(Short[].class, short[].class),
  LONG_ARRAY(Long[].class, long[].class),
  BYTE_ARRAY(Byte[].class, byte[].class),
  CHAR_ARRAY(Character[].class, char[].class),
  LOCAL_DATE_TIME_ARRAY(LocalDateTime[].class),
  OFFSET_DATE_TIME_ARRAY(OffsetDateTime[].class),
  ZONED_DATE_TIME_ARRAY(ZonedDateTime[].class),

  NOT_SUPPORTED();

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
 Checks if the class that this enum represents can parse the given value as represented class.
 * @param value The given value.
 * @return True if it can be parsed or if it is of a class that EasyConfigurations doesn't account for.<br>
 * False only if the value cannot be parsed as its intended class that EasyConfigurations supports.
 */
@Contract(pure = true)
@InternalUse
@SuppressWarnings ("ResultOfMethodCallIgnored")
public boolean canParse(@NotNull Object value) {
  String stringValue = value.toString();

  // Tries to parse the string value of the object in accordance with the enum.
  // If their is an error parsing, then it's caught & false is returned.
  // If it parses successfully, then true is returned.
  try {
    // Parses the non-array values //
    switch (this) {

    // Can always be parsed
    case NOT_SUPPORTED:
    case OBJECT:
    case STRING:
    case BOOLEAN: {
      return true;
    }

    case INTEGER: {
      Integer.parseInt(stringValue);
      return true;
    }
    case DOUBLE: {
      Double.parseDouble(stringValue);
      return true;
    }
    case FLOAT: {
      Float.parseFloat(stringValue);
      return true;
    }
    case SHORT: {
      Short.parseShort(stringValue);
      return true;
    }
    case LONG: {
      Long.parseLong(stringValue);
      return true;
    }
    case BYTE: {
      Byte.parseByte(stringValue);
      return true;
    }
    case CHAR: {
      return stringValue.length() == 1;
    }
    case LOCAL_DATE_TIME: {
      LocalDateTime.parse(stringValue);
      return true;
    }
    case OFFSET_DATE_TIME: {
      OffsetDateTime.parse(stringValue);
      return true;
    }
    case ZONED_DATE_TIME: {
      ZonedDateTime.parse(stringValue);
      return true;
    }
    }

    // If there was an error parsing the value then it isn't a valid value
  } catch (NullPointerException | NumberFormatException | DateTimeException e) {
    return false;
  }

  // If it's not of a List or Object[] then it can't be parsed by any of following cases.
  if (!(value instanceof List || value instanceof Object[])) {
    return true;
  }


  // Parses the array values //
  List<Object> objectList = toList(value);

  // Checks that each of the objects within the list can be parsed as it's intended class.
  for (Object containedObject : objectList) {
    switch (this) {

    // Can always be parsed
    case OBJECT_ARRAY:
    case STRING_ARRAY:
    case BOOLEAN_ARRAY: {
      return true;
    }

    case INTEGER_ARRAY: {
      return INTEGER.canParse(containedObject);
    }
    case DOUBLE_ARRAY: {
      return DOUBLE.canParse(containedObject);
    }
    case FLOAT_ARRAY: {
      return FLOAT.canParse(containedObject);
    }
    case SHORT_ARRAY: {
      return SHORT.canParse(containedObject);
    }
    case LONG_ARRAY: {
      return LONG.canParse(containedObject);
    }
    case BYTE_ARRAY: {
      return BYTE.canParse(containedObject);
    }
    case CHAR_ARRAY: {
      return CHAR.canParse(containedObject);
    }
    case LOCAL_DATE_TIME_ARRAY: {
      return LOCAL_DATE_TIME.canParse(containedObject);
    }
    case OFFSET_DATE_TIME_ARRAY: {
      return OFFSET_DATE_TIME.canParse(containedObject);
    }
    case ZONED_DATE_TIME_ARRAY: {
      return ZONED_DATE_TIME.canParse(containedObject);
    }
    }
  }

  // Returns true if the class isn't one known to EasyConfigurations.
  return true;
}

/**
 Parses the give value to the class specified by this enum.
 * @param value The given value.
 * @return The value as its intended object. Or just the object if someone (me) forgot to add an enum to this method.
 * @throws NotOfClassException If the object passed in isn't of a class that this enum represents
 */
@Contract(pure = true)
@InternalUse
public @NotNull Object parse(@NotNull Object value) throws NotOfClassException {
  String stringValue = value.toString();

  try {
    // Parses the non-array values //
    switch (this) {

    case NOT_SUPPORTED:
    case OBJECT: {
      return value;
    }
    case STRING: {
      return String.valueOf(value);
    }
    case BOOLEAN: {
      return Boolean.valueOf(stringValue);
    }
    case INTEGER: {
      return Integer.parseInt(stringValue);
    }
    case DOUBLE: {
      return Double.parseDouble(stringValue);
    }
    case FLOAT: {
      return Float.parseFloat(stringValue);
    }
    case SHORT: {
      return Short.parseShort(stringValue);
    }
    case LONG: {
      return Long.parseLong(stringValue);
    }
    case BYTE: {
      return Byte.parseByte(stringValue);
    }
    case CHAR: {
      return stringValue.charAt(0);
    }
    case LOCAL_DATE_TIME: {
      return LocalDateTime.parse(stringValue);
    }
    case OFFSET_DATE_TIME: {
      return OffsetDateTime.parse(stringValue);
    }
    case ZONED_DATE_TIME: {
      return ZonedDateTime.parse(stringValue);
    }
    }


    // Parses the array values //
    List<Object> objectList = toList(value);

    // Checks that each of the objects within the list can be parsed as it's intended class.
    switch (this) {

    // Can always be parsed
    case OBJECT_ARRAY: {
      List<Object> objects = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        objects.add(OBJECT.parse(o));
      }
      return objects;
    }
    case STRING_ARRAY: {
      List<String> strings = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        strings.add((String) STRING.parse(o));
      }
      return strings;
    }
    case BOOLEAN_ARRAY: {
      List<Boolean> booleans = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        booleans.add((Boolean) BOOLEAN.parse(o));
      }
      return booleans;
    }
    case INTEGER_ARRAY: {
      List<Integer> ints = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        ints.add((Integer) INTEGER.parse(o));
      }
      return ints;
    }
    case DOUBLE_ARRAY: {
      List<Double> doubles = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        doubles.add((Double) DOUBLE.parse(o));
      }
      return doubles;
    }
    case FLOAT_ARRAY: {
      List<Float> floats = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        floats.add((Float) FLOAT.parse(o));
      }
      return floats;
    }
    case SHORT_ARRAY: {
      List<Short> shorts = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        shorts.add((Short) SHORT.parse(o));
      }
      return shorts;
    }
    case LONG_ARRAY: {
      List<Long> longs = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        longs.add((Long) LONG.parse(o));
      }
      return longs;
    }
    case BYTE_ARRAY: {
      List<Byte> bytes = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        bytes.add((Byte) BYTE.parse(o));
      }
      return bytes;
    }
    case CHAR_ARRAY: {
      List<Character> chars = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        chars.add((Character) CHAR.parse(o));
      }
      return chars;
    }
    case LOCAL_DATE_TIME_ARRAY: {
      List<LocalDateTime> time = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        time.add((LocalDateTime) LOCAL_DATE_TIME.parse(o));
      }
      return time;
    }
    case OFFSET_DATE_TIME_ARRAY: {
      List<OffsetDateTime> time = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        time.add((OffsetDateTime) OFFSET_DATE_TIME.parse(o));
      }
      return time;
    }
    case ZONED_DATE_TIME_ARRAY: {
      List<ZonedDateTime> time = new ArrayList<>(objectList.size());
      for (Object o : objectList) {
        time.add((ZonedDateTime) ZONED_DATE_TIME.parse(o));
      }
      return time;
    }
    }
  } catch (RuntimeException e) {
    throw new NotOfClassException(Lang.notOfClass(stringValue, this.toString()));
  }

  return value;
}

/**
 Converts the given value to {@literal List<Object>}.
 * @param value The given value. It must be an instance of {@link List} or an Object[].
 * @return The given value converted to an object list.
 */
@InternalUse
private @NotNull List<Object> toList(@NotNull Object value) {
  ArrayList<Object> objectList;

  // Converts the List or array into an Object list.
  if (value instanceof List) {
    List<?> valueList = (List<?>) value;
    objectList = new ArrayList<>(valueList.size());
    objectList.addAll(valueList);
  }
  else {
    Object[] objArray = ((Object[]) value);
    objectList = new ArrayList<>(objArray.length);
    objectList.addAll(Arrays.asList(objArray));
  }

  return objectList;
}

/**
 Matches the given class to an enum inside {@link SupportedClasses}.
 * @param classToMatch The given class.
 * @return The enum that represents the given class. If none match then SupportedClasses.NOT_SUPPORTED is returned.
 */
@InternalUse
public static @NotNull SupportedClasses getAsEnum(@NotNull Class<?> classToMatch) {
  SupportedClasses[] supportedClasses = SupportedClasses.class.getEnumConstants();

  // Loops over this class to find if any enums support the class to match
  for (SupportedClasses supportedClass : supportedClasses) {
    for (Class<?> alikeClass : supportedClass.getClasses()) {
      if (classToMatch.equals(alikeClass)) return supportedClass;
    }
  }

  return NOT_SUPPORTED;
}

}
