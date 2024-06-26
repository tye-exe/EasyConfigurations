package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.InvalidDataException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 This enum contains an entry for each class EasyConfigurations can parse.<br> If an unsupported class
 is given, an error will be thrown. */
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
  CONFIG_OBJECT(ConfigObject.class),

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
  ZONED_DATE_TIME_LIST(ZonedDateTime[].class),
  CONFIG_OBJECT_LIST(ConfigObject[].class);

/**
 Contains the class values for classes represented by this enum. */
@InternalUse
private final @NotNull Class<?>[] classes;

/**
 Creates an enum that represents the given classes.
 @param classes The given classes. */
@InternalUse
SupportedClasses(@NotNull Class<?>... classes) {
  this.classes = classes;
}

/**
 Gets the classes that this enum represents.
 @return The classes that this enum represents. */
@Contract(pure=true)
@InternalUse
public @NotNull Class<?>[] getClasses() {
  return classes;
}

/**
 Checks if this enum represents an array.
 @return True is the enum represents a form of array. False otherwise. */
@Contract(pure=true)
@InternalUse
public boolean representsArray() {
  return getClasses()[0].isArray();
}


/**
 Checks if the class this enum represents can parse the given value as the represented class.
 @param rawValue The given value.
 @return True if it can be parsed.<br> False only if the value cannot be parsed as its intended class
 or if the value is one EasyConfigurations hasn't accounted for. */
@Contract(pure=true)
@InternalUse
public boolean canParse(@NotNull Object rawValue) {
  // Parsing is handled differently for array & non-array values.
  if (this.representsArray()) {
    return canParseArray(rawValue);
  }
  else {
    return canParseNonArray(rawValue);
  }
}


/**
 If this method is used on an array enum then it will always return false.<br> Checks if the given
 value can be parsed as the class this enum represents.
 @param rawValue The given value.
 @return True if it can be parsed.<br> False only if the value cannot be parsed as its intended class
 or if the value is one EasyConfigurations hasn't accounted for. */
@SuppressWarnings("ResultOfMethodCallIgnored")
// Just checks if they can be parsed.
@Contract(pure=true)
@InternalUse
private boolean canParseNonArray(@NotNull Object rawValue) {
  // Tries to parse the string value of the object based on the enum.
  // If there is an error parsing, then it's caught & false is returned.
  // If it parses successfully, then true is returned.
  try {
    String value = rawValue.toString();

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

  }
  // If there was an error parsing the value then it isn't a valid value.
  catch (Exception ignore) {}

  return false;
}

/**
 If this method is used on a non-array enum then it will always return false.<br> Checks if the given
 value can be parsed as the class this enum represents.
 @param rawValue The given array or List value.
 @return True if it can be parsed.<br> False only if the value cannot be parsed as its intended class
 or if the value is one EasyConfigurations hasn't accounted for. */
@Contract(pure=true)
@InternalUse
private boolean canParseArray(@NotNull Object rawValue) {
  List<String> value;

  try {
    value = toList(rawValue);

    // If there was an error attempting to parse the input then it wasn't an array or list.
  }
  catch (Exception ignore) {
    return false;
  }


  // Checks that all the objects within the list can be parsed as their intended class.
  for (String stringValue : value) {
    boolean couldParseValue = false;

    switch (this) {

    case STRING_LIST: {
      couldParseValue = STRING.canParseNonArray(stringValue);
      break;
    }
    case BOOLEAN_LIST: {
      couldParseValue = BOOLEAN.canParseNonArray(stringValue);
      break;
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

  // True is returned since to make it to here parsing must be successful every time.
  // Or the list was empty.
  return true;
}


/**
 Checks if the given class is a class {@link #CONFIG_OBJECT} or {@link #CONFIG_OBJECT_LIST} enums
 represent. And if the given value can be parsed as the represented class.
 @param customClazz The marked class of the value.
 @param rawValue    The given value.
 @return True if it can be parsed.<br> False only if the value cannot be parsed as its intended class
 or if the value is one EasyConfigurations hasn't accounted for.
 @throws ConfigurationException If the given class doesn't extend {@link ConfigObject} and doesn't
 have a public default constructor. */
@SuppressWarnings({"UnreachableCode", "unchecked"})
// The code is reachable as the unchecked cast may fail.
@InternalUse
public boolean canParseCustom(@NotNull Class<?> customClazz, @NotNull Object rawValue) throws ConfigurationException {
  // If the class is an array then it won't get the correct constructor.
  customClazz = Classes.getComponent(customClazz);

  // Does manual casting here so input sanitation doesn't need to be performed.
  // Either way this error shouldn't be thrown as validation is performed before.
  Class<? extends ConfigObject> configClass;
  try {
    configClass = (Class<? extends ConfigObject>) customClazz;
  }
  catch (ClassCastException e) {
    throw new ConfigurationException(Lang.classNotSupported(Classes.getName(customClazz)));
  }

  // If this enum doesn't represent a custom object, it can't be parsed as such.
  if (this != CONFIG_OBJECT && this != CONFIG_OBJECT_LIST) {
    return false;
  }

  // Creates a new instance of the config class.
  // If it can't be created then there is an unrecoverable error.
  ConfigObject configObject;
  try {
    configObject = configClass.getConstructor().newInstance();
  }
  catch (Exception e) {
    throw new ConfigurationException(Lang.missingDefaultConstructor(Classes.getName(configClass)));
  }

  // Parsing is handled differently for array & non-array values.
  if (this.representsArray()) {
    return this.canParseCustomArray(configObject, rawValue);
  }
  else {
    return this.canParseCustomNonArray(configObject, rawValue);
  }
}

/**
 If this method is used on an array enum then it will always return false.
 <p>
 Checks if the given value can be parsed as a {@link #CONFIG_OBJECT}.
 @param configObject An initialized object of the value's marked class.
 @param rawValue     The given value.
 @return True if it can be parsed.<br> False only if the value cannot be parsed as its intended class
 or if the value is one EasyConfigurations hasn't accounted for. */
private boolean canParseCustomNonArray(@NotNull ConfigObject configObject, @NotNull Object rawValue) {
  if (rawValue instanceof ConfigObject) return true;

  try {
    configObject.parseConfigString(rawValue.toString());
    return true;
  }
  catch (InvalidDataException ignore) {
    return false;
  }
}

/**
 If this method is used on a non-array enum then it will always return false.
 <p>
 Checks if the given value can be parsed as a {@link #CONFIG_OBJECT_LIST}.
 @param configObject An initialized object of the value's marked class.
 @param rawValue     The given array or List value.
 @return True if it can be parsed.<br> False only if the value cannot be parsed as its intended class
 or if the value is one EasyConfigurations hasn't accounted for. */
private boolean canParseCustomArray(@NotNull ConfigObject configObject, @NotNull Object rawValue) {
  try {
    List<String> stringList = toList(rawValue);

    for (String value : stringList) {
      canParseCustomNonArray(configObject, value);
    }

    return true;
  }
  catch (Exception ignore) {
    return false;
  }
}

/**
 Parses the give value to the class specified by this enum.<br>
 <br>
 Note: {@link #canParse(Object)} should be performed first to check if the object can be parsed.
 @param rawValue The given value.
 @return The value as its intended object. Or just the object if someone (me) forgot to add an enum
 to this method.
 @throws NotOfClassException If the object passed in isn't of a class this enum represents. */
@InternalUse
public @NotNull Object parse(@NotNull Object rawValue) throws NotOfClassException {
  // Parsing is handled differently for array & non-array values.
  if (this.representsArray()) {
    return parseArray(rawValue);
  }
  else {
    return parseNonArray(rawValue);
  }
}

/**
 If this method is used on an array enum then it will always throw {@link NotOfClassException}.
 <p>
 Parses the given value as the class this enum represents.
 @param rawValue The given value.
 @return The value as its intended object.
 @throws NotOfClassException If the object passed in isn't of a class this enum represents. */
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
      throw new RuntimeException(); // The boolean value can't be parse so throw an error that will be caught & formatted.
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

  }
  catch (RuntimeException e) {
    throw new NotOfClassException(Lang.notOfClass(value, this.toString()));
  }

  return value;
}

/**
 If this method is used on a non-array enum then it will always throw {@link NotOfClassException}.
 <p>
 Parses the given value as the class this enum represents.
 @param value The given array or List value.
 @return The value as its intended object.
 @throws NotOfClassException If the object passed in isn't of a class this enum represents. */
@InternalUse
private @NotNull Object parseArray(@NotNull Object value) throws NotOfClassException {
  try {
    List<String> stringsToParse;
    stringsToParse = toList(value);

    List<Object> outputList = new ArrayList<>(stringsToParse.size());

    // Parses all the objects within the list.
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

      // If the switch statement can't match any of the above conditions then the object is invalid.
      default: {
        String className = Classes.getName(value.getClass());
        throw new NotOfClassException(Lang.notOfClass(className, this.toString()));
      }
      }
    }

    return outputList;
  }
  // Catches any exceptions & then formats them.
  catch (NotOfClassException | IllegalArgumentException e) {

    // If it's a NotOfClassException thrown by one of the parse methods then just throw that.
    if (e.getClass().equals(NotOfClassException.class)) throw e;

    // Else create a new NotOfClassException exception.
    String className = Classes.getName(value.getClass());
    throw new NotOfClassException(Lang.notOfClass(className, this.toString()));
  }
}


/**
 Parses the given value as a custom {@link ConfigObject}.
 <p>
 Note: {@link #canParseCustom(Class, Object)} should be performed first to check if the object can be
 parsed.
 @param customClazz The marked class of the value.
 @param rawValue    The given value.
 @return The value as its intended object.
 @throws NotOfClassException    If the object passed in isn't of a class this enum represents.
 @throws ConfigurationException If the given class doesn't implement {@link ConfigObject}. */
@SuppressWarnings({"UnreachableCode", "unchecked"})
// The code is reachable as the unchecked cast may fail.
@InternalUse
public Object parseCustom(@NotNull Class<?> customClazz, @NotNull Object rawValue) throws NotOfClassException, ConfigurationException {
  // If the class is an array then it won't get the correct constructor.
  customClazz = Classes.getComponent(customClazz);

  // Does manual casting here so input sanitation doesn't need to be performed.
  // Either way this error shouldn't be thrown as validation is performed before.
  Class<? extends ConfigObject> configClass;
  try {
    configClass = (Class<? extends ConfigObject>) customClazz;
  }
  catch (ClassCastException e) {
    throw new ConfigurationException(Lang.classNotSupported(Classes.getName(customClazz)));
  }

  // If this enum doesn't represent a custom object, it can't be parsed as such.
  if (this != CONFIG_OBJECT && this != CONFIG_OBJECT_LIST) {
    throw new NotOfClassException(Lang.notOfClass(Classes.getName(customClazz), CONFIG_OBJECT + " or " + CONFIG_OBJECT_LIST));
  }

  // Creates a new instance of the config class.
  // If it can't be created then there is an unrecoverable error.
  ConfigObject configObject;
  try {
    configObject = configClass.newInstance();
  }
  catch (InstantiationException | IllegalAccessException e) {
    throw new ConfigurationException(Lang.missingDefaultConstructor(Classes.getName(configClass)));
  }

  // Parsing is handled differently for array & non-array values.
  if (this.representsArray()) {
    return this.parseCustomArray(configObject, rawValue);
  }
  else {
    return this.parseCustomNonArray(configObject, rawValue);
  }
}

/**
 If this method is used on an array enum then it will always throw {@link NotOfClassException}.
 <p>
 Parses the given value as the class this enum represents.
 @param configObject An initialized object of the value's marked class.
 @param rawValue     The given value.
 @return The value as its intended object.
 @throws NotOfClassException If the object passed in isn't of a class this enum represents. */
private ConfigObject parseCustomNonArray(@NotNull ConfigObject configObject, @NotNull Object rawValue) throws NotOfClassException {
  if (rawValue instanceof ConfigObject) return (ConfigObject) rawValue;

  try {
    return configObject.parseConfigString(rawValue.toString());
  }
  catch (Exception ignore) {
    String className = Classes.getName(rawValue.getClass());
    throw new NotOfClassException(Lang.notOfClass(className, CONFIG_OBJECT.toString()));
  }
}

/**
 If this method is used on a non-array enum then it will always throw {@link NotOfClassException}.
 <p>
 Parses the given value as the class this enum represents.
 @param configObject An initialized object of the value's marked class.
 @param rawValue     The given value.
 @return The value as its intended object.
 @throws NotOfClassException If the object passed in isn't of a class this enum represents. */
private List<ConfigObject> parseCustomArray(@NotNull ConfigObject configObject, @NotNull Object rawValue) {
  try {
    List<String> stringList = toList(rawValue);
    ArrayList<ConfigObject> configObjects = new ArrayList<>(stringList.size());

    for (String value : stringList) {
      configObjects.add(parseCustomNonArray(configObject, value));
    }

    return configObjects;
  }
  catch (Exception e) {
    // If the error was thrown by the parsing method then just throw that
    if (e instanceof NotOfClassException) throw e;

    // Else thrown a generic exception
    String className = Classes.getName(rawValue.getClass());
    throw new NotOfClassException(Lang.notOfClass(className, CONFIG_OBJECT.toString()));
  }
}


/**
 Checks if the given class is represented by any existing enum in {@link SupportedClasses}
 @param classToMatch The given class.
 @return True if the given class is represented. False otherwise. */
@Contract(pure=true)
@InternalUse
public static boolean existsAsEnum(@NotNull Class<?> classToMatch) {
  SupportedClasses[] supportedClasses = SupportedClasses.class.getEnumConstants();

  // Loops over this class to find if any enums support the class to match.
  for (SupportedClasses supportedClass : supportedClasses) {
    for (Class<?> alikeClass : supportedClass.getClasses()) {
      if (classToMatch.equals(alikeClass)) return true;
    }
  }

  // If the class is an array then it won't get the correct interfaces.
  Class<?> componentClass = Classes.getComponent(classToMatch);

  // If it implements the interface then it's supported.
  for (Class<?> interfaces : componentClass.getInterfaces()) {
    if (interfaces.equals(ConfigObject.class)) return true;
  }

  return false;
}

/**
 Matches the given class to an enum inside {@link SupportedClasses}.
 @param classToMatch The given class.
 @return The enum that represents the given class.
 @throws ConfigurationException If the given class doesn't match any supported classes. */
@InternalUse
public static @NotNull SupportedClasses getAsEnum(@NotNull Class<?> classToMatch) throws ConfigurationException {
  SupportedClasses[] supportedClasses = SupportedClasses.class.getEnumConstants();

  // Loops over this class to find if any enums support the class to match.
  for (SupportedClasses supportedClass : supportedClasses) {
    for (Class<?> alikeClass : supportedClass.getClasses()) {
      if (classToMatch.equals(alikeClass)) return supportedClass;
    }
  }

  // If the class is an array then it won't get the correct interfaces.
  Class<?> componentClass = Classes.getComponent(classToMatch);

  // If it implements the interface then it's supported.
  for (Class<?> interfaces : componentClass.getInterfaces()) {
    if (!interfaces.equals(ConfigObject.class)) continue;

    if (classToMatch.isArray()) {return CONFIG_OBJECT_LIST;}
    else {return CONFIG_OBJECT;}
  }

  String className = Classes.getName(classToMatch);
  throw new ConfigurationException(Lang.classNotSupported(className));
}


/**
 Converts the given value to {@literal List<String>}.
 @param value The given value. It must be an instance of {@link List} or an array of any type.
 @return The given value converted to an object list.
 @throws IllegalArgumentException If the given value isn't a list or an array. */
@InternalUse
private static @NotNull List<String> toList(@NotNull Object value) throws IllegalArgumentException {
  ArrayList<String> stringList;

  // Converts the List or array into a String list.
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
