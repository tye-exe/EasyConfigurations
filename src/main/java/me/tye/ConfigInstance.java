package me.tye;

import me.tye.internalConfigs.Lang;
import me.tye.utils.Consts;
import me.tye.utils.SupportedClasses;
import me.tye.utils.annotations.ExternalUse;
import me.tye.utils.annotations.InternalUse;
import me.tye.utils.exceptions.NotOfClassException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static me.tye.utils.SupportedClasses.OBJECT_ARRAY;

/**
 This interface is designed to be implemented by an enum to define it as a enum containing the different config options for the program implementing this dependency.<br>
 Please reference the README.md file on github for "EasyConfigurations" for usage information.
 */
@SuppressWarnings ("unchecked") // I had to use unchecked casts to tell java that a certain object is indeed the class i say it is.
@ExternalUse
public interface  ConfigInstance extends BaseInstance {

/**
  Creates a new instance of a config enum.
 * @param markedClass The class to parse the yaml value for the config as.
 * @param yamlPath The key path of the yaml value to parse as the config.
 */
@Override
@ExternalUse
default void init(@NotNull Class<?> markedClass, @NotNull String yamlPath) {
  BaseInstance.super.init(markedClass, yamlPath);
}

/**
 Checks if the enum is of the given classes.
 * @param isInstancesOf The given classes.
 * @throws NotOfClassException If it's not of the given classes.
 */
@InternalUse
default void classCheck(@NotNull Class<?>... isInstancesOf) throws NotOfClassException {
  StringBuilder classes = new StringBuilder();

  for (Class<?> isInstanceOf : isInstancesOf) {
    if (markedClass[0].equals(isInstanceOf)) return;

    classes.append(isInstanceOf.getName()).append(" ");
  }

  throw new NotOfClassException(Lang.notOfClass(yamlPath[0], classes.toString()));
}


/**
 This method can be used regardless of the marked class.
 * @return Gets the config response for the selected enum.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull Object getAsObject() {
  return Consts.configMap.get(yamlPath[0]);
}

/**
 This method can be used regardless of the marked class.
 Gets the config response for the selected enum wrapped with {@link String#valueOf(Object)}.
 * @return The string parsed from the internal file that represents this enum.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull String getAsString() {
  return String.valueOf(getAsObject());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Boolean#parseBoolean(String)}.
 * @throws NotOfClassException If the selected config isn't a Boolean value.
 */
@Contract(pure = true)
@ExternalUse
default boolean getAsBoolean() throws NotOfClassException {
  classCheck(Boolean.class, boolean.class);
  return Boolean.parseBoolean(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Integer#parseInt(String)}.
 * @throws NotOfClassException If the selected config isn't a Integer value.
 */
@Contract(pure = true)
@ExternalUse
default int getAsInteger() throws NotOfClassException {
  classCheck(Integer.class, int.class);
  return Integer.parseInt(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Double#parseDouble(String)}.
 * @throws NotOfClassException If the selected config isn't a Double value.
 */
@Contract(pure = true)
@ExternalUse
default double getAsDouble() throws NotOfClassException {
  classCheck(Double.class, double.class);
  return Double.parseDouble(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Float#parseFloat(String)}.
 * @throws NotOfClassException If the selected config isn't a Float value.
 */
@Contract(pure = true)
@ExternalUse
default float getAsFloat() throws NotOfClassException {
  classCheck(Float.class, float.class);
  return Float.parseFloat(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Short#parseShort(String)}.
 * @throws NotOfClassException If the selected config isn't a Short value.
 */
@Contract(pure = true)
@ExternalUse
default short getAsShort() throws NotOfClassException {
  classCheck(Short.class, short.class);
  return Short.parseShort(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Long#parseLong(String)}.
 * @throws NotOfClassException If the selected config isn't a Long value.
 */
@Contract(pure = true)
@ExternalUse
default long getAsLong() throws NotOfClassException {
  classCheck(Long.class, long.class);
  return Long.parseLong(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Byte#parseByte(String)}.
 * @throws NotOfClassException If the selected config isn't a Byte value.
 */
@Contract(pure = true)
@ExternalUse
default byte getAsByte() throws NotOfClassException {
  classCheck(Byte.class, byte.class);
  return Byte.parseByte(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link Byte#parseByte(String)}.
 * @throws NotOfClassException If the selected config isn't a Char value.
 */
@Contract(pure = true)
@ExternalUse
default char getAsChar() throws NotOfClassException {
  classCheck(Character.class, char.class);
  return getAsString().toCharArray()[0];
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link LocalDateTime#parse(CharSequence)}.
 * @throws NotOfClassException If the selected config isn't a LocalDateTime value.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull LocalDateTime getAsLocalDateTime() throws NotOfClassException {
  classCheck(LocalDateTime.class);
  return LocalDateTime.parse(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link OffsetDateTime#parse(CharSequence)}}.
 * @throws NotOfClassException If the selected config isn't an OffsetDateTime value.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull OffsetDateTime getAsOffsetDateTime() throws NotOfClassException {
  classCheck(OffsetDateTime.class);
  return OffsetDateTime.parse(getAsString());
}

/**
 * @return Gets the config response for the selected enum wrapped with {@link ZonedDateTime#parse(CharSequence)}.
 * @throws NotOfClassException If the selected config isn't a ZonedDateTime value.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull ZonedDateTime getAsZonedDateTime() throws NotOfClassException {
  classCheck(ZonedDateTime.class);
  return ZonedDateTime.parse(getAsString());
}

/**
 * @return Gets the config response from the selected enum as an object list.
 * @throws NotOfClassException If the selected config isn't an object list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Object> getAsObjectArray() {
  classCheck(Object[].class, List.class);
  return (List<Object>) OBJECT_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a string list.
 * @throws NotOfClassException If the selected config isn't a string list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<String> getAsStringArray() {
  classCheck(String[].class);
  return (List<String>) SupportedClasses.STRING_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a boolean list.
 * @throws NotOfClassException If the selected config isn't a boolean list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Boolean> getAsBooleanArray() throws NotOfClassException {
  classCheck(boolean[].class, Boolean[].class);
  return (List<Boolean>) SupportedClasses.BOOLEAN_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as an integer list.
 * @throws NotOfClassException If the selected config isn't an integer list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Integer> getAsIntegerArray() throws NotOfClassException {
  classCheck(Integer[].class, int[].class);
  return (List<Integer>) SupportedClasses.INTEGER_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a double list.
 * @throws NotOfClassException If the selected config isn't a double list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Double> getAsDoubleArray() throws NotOfClassException {
  classCheck(Double[].class, double[].class);
  return (List<Double>) SupportedClasses.DOUBLE_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a float list.
 * @throws NotOfClassException If the selected config isn't a float list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Float> getAsFloatArray() throws NotOfClassException {
  classCheck(Float[].class, float[].class);
  return (List<Float>) SupportedClasses.FLOAT_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a short list.
 * @throws NotOfClassException If the selected config isn't a short list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Short> getAsShortArray() throws NotOfClassException {
  classCheck(Short[].class, short[].class);
  return (List<Short>) SupportedClasses.SHORT_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a long list.
 * @throws NotOfClassException If the selected config isn't a long list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Long> getAsLongArray() throws NotOfClassException {
  classCheck(Long[].class, long[].class);
  return (List<Long>) SupportedClasses.LONG_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a byte list.
 * @throws NotOfClassException If the selected config isn't a byte list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Byte> getAsByteArray() throws NotOfClassException {
  classCheck(Byte[].class, byte[].class);
  return (List<Byte>) SupportedClasses.BYTE_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a char list.
 * @throws NotOfClassException If the selected config isn't a char list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Character> getAsCharArray() throws NotOfClassException {
  classCheck(Character[].class, char[].class);
  return (List<Character>) SupportedClasses.CHAR_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a {@link LocalDateTime} list.
 * @throws NotOfClassException If the selected config isn't a {@link LocalDateTime} list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<LocalDateTime> getAsLocalDateTimeArray() throws NotOfClassException {
  classCheck(LocalDateTime[].class);
  return (List<LocalDateTime>) SupportedClasses.LOCAL_DATE_TIME_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a {@link OffsetDateTime} list.
 * @throws NotOfClassException If the selected config isn't a {@link OffsetDateTime} list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<OffsetDateTime> getAsOffsetDateTimeArray() throws NotOfClassException {
  classCheck(OffsetDateTime[].class);
  return (List<OffsetDateTime>) SupportedClasses.OFFSET_DATE_TIME_ARRAY.parse(getAsObject());
}

/**
 * @return Gets the config response from the selected enum as a {@link ZonedDateTime} list.
 * @throws NotOfClassException If the selected config isn't a {@link ZonedDateTime} list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<ZonedDateTime> getAsZonedDateTimeArray() throws NotOfClassException {
  classCheck(ZonedDateTime[].class);
  return (List<ZonedDateTime>) SupportedClasses.ZONED_DATE_TIME_ARRAY.parse(getAsObject());
}
}
