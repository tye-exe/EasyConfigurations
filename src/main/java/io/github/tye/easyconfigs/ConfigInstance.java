package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.utils.Consts;
import io.github.tye.easyconfigs.utils.Utils;
import io.github.tye.easyconfigs.utils.annotations.ExternalUse;
import io.github.tye.easyconfigs.utils.annotations.InternalUse;
import io.github.tye.easyconfigs.utils.exceptions.NotOfClassException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 This interface is designed to be implemented by an enum to define it as an enum containing the different config options for the program implementing this dependency.<br>
 Please reference the
 <a href="https://github.com/Mapty231/EasyConfigurations?tab=readme-ov-file#setting-up-configs">README.md</a>
 file on GitHub for "EasyConfigurations" for usage information.
 */
@SuppressWarnings ({"unused", "unchecked"})
// Unchecked – These methods are intended for use projects using Easy Configurations as a dependency.
// Unused – Suppresses the cast warnings for the Lists. As they are being cast to the correct class. This is ensured by the classCheck() preceding the cast & at config initiation.
@ExternalUse
public interface ConfigInstance extends BaseInstance {

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
 * @return The object in the config HashMap that this enum represents.
 */
@Contract(pure = true)
@InternalUse
default @NotNull Object getValue() {
  return Consts.configMap.get(getYamlPath());
}

/**
 * @return Gets a {@link String} config response.
 * @throws NotOfClassException If the selected config isn't a String value.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull String getAsString() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.STRING, this);
  return (String) getValue();
}

/**
 * @return Gets a {@link Boolean} config response.
 * @throws NotOfClassException If the selected config isn't a Boolean value.
 */
@Contract(pure = true)
@ExternalUse
default boolean getAsBoolean() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.BOOLEAN, this);
  return (boolean) getValue();
}

/**
 * @return Gets a {@link Integer} config response.
 * @throws NotOfClassException If the selected config isn't an Integer value.
 */
@Contract(pure = true)
@ExternalUse
default int getAsInteger() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.INTEGER, this);
  return (int) getValue();
}

/**
 * @return Gets a {@link Double} config response.
 * @throws NotOfClassException If the selected config isn't a Double value.
 */
@Contract(pure = true)
@ExternalUse
default double getAsDouble() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.DOUBLE, this);
  return (double) getValue();
}

/**
 * @return Gets a {@link Float} config response.
 * @throws NotOfClassException If the selected config isn't a Float value.
 */
@Contract(pure = true)
@ExternalUse
default float getAsFloat() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.FLOAT, this);
  return (float) getValue();
}

/**
 * @return Gets a {@link Short} config response.
 * @throws NotOfClassException If the selected config isn't a Short value.
 */
@Contract(pure = true)
@ExternalUse
default short getAsShort() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.SHORT, this);
  return (short) getValue();
}

/**
 * @return Gets a {@link Long} config response.
 * @throws NotOfClassException If the selected config isn't a Long value.
 */
@Contract(pure = true)
@ExternalUse
default long getAsLong() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.LONG, this);
  return (long) getValue();
}

/**
 * @return Gets a {@link Byte} config response.
 * @throws NotOfClassException If the selected config isn't a Byte value.
 */
@Contract(pure = true)
@ExternalUse
default byte getAsByte() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.BYTE, this);
  return (byte) getValue();
}

/**
 * @return Gets a {@link Character} config response.
 * @throws NotOfClassException If the selected config isn't a Char value.
 */
@Contract(pure = true)
@ExternalUse
default char getAsChar() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.CHAR, this);
  return (char) getValue();
}

/**
 * @return Gets a {@link LocalDateTime} config response.
 * @throws NotOfClassException If the selected config isn't a LocalDateTime value.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull LocalDateTime getAsLocalDateTime() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.LOCAL_DATE_TIME, this);
  return (LocalDateTime) getValue();
}

/**
 * @return Gets a {@link OffsetDateTime} config response.
 * @throws NotOfClassException If the selected config isn't an OffsetDateTime value.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull OffsetDateTime getAsOffsetDateTime() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.OFFSET_DATE_TIME, this);
  return (OffsetDateTime) getValue();
}

/**
 * @return Gets a {@link ZonedDateTime} config response.
 * @throws NotOfClassException If the selected config isn't a ZonedDateTime value.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull ZonedDateTime getAsZonedDateTime() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.ZONED_DATE_TIME, this);
  return (ZonedDateTime) getValue();
}


/**
 * @return Gets a {@link String} list config response.
 * @throws NotOfClassException If the selected config isn't a string list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<String> getAsStringList() {
  Utils.classCheck(SupportedClasses.STRING_LIST, this);
  return (List<String>) getValue();
}

/**
 * @return Gets an {@link Boolean} list config response.
 * @throws NotOfClassException If the selected config isn't a boolean list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Boolean> getAsBooleanList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.BOOLEAN_LIST, this);
  return (List<Boolean>) getValue();
}

/**
 * @return Gets an {@link Integer} list config response.
 * @throws NotOfClassException If the selected config isn't an integer list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Integer> getAsIntegerList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.INTEGER_LIST, this);
  return (List<Integer>) getValue();
}

/**
 * @return Gets a {@link Double} list config response.
 * @throws NotOfClassException If the selected config isn't a double list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Double> getAsDoubleList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.DOUBLE_LIST, this);
  return (List<Double>) getValue();
}

/**
 * @return Gets a {@link Float} list config response.
 * @throws NotOfClassException If the selected config isn't a float list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Float> getAsFloatList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.FLOAT_LIST, this);
  return (List<Float>) getValue();
}

/**
 * @return Gets a {@link Short} list config response.
 * @throws NotOfClassException If the selected config isn't a short list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Short> getAsShortList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.SHORT_LIST, this);
  return (List<Short>) getValue();
}

/**
 * @return Gets a {@link Long} list config response.
 * @throws NotOfClassException If the selected config isn't a long list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Long> getAsLongList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.LONG_LIST, this);
  return (List<Long>) getValue();
}

/**
 * @return Gets a {@link Byte} list config response.
 * @throws NotOfClassException If the selected config isn't a byte list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Byte> getAsByteList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.BYTE_LIST, this);
  return (List<Byte>) getValue();
}

/**
 * @return Gets a {@link Character} list config response.
 * @throws NotOfClassException If the selected config isn't a char list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<Character> getAsCharList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.CHAR_LIST, this);
  return (List<Character>) getValue();
}

/**
 * @return Gets a {@link LocalDateTime} list config response.
 * @throws NotOfClassException If the selected config isn't a local date time list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<LocalDateTime> getAsLocalDateTimeList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.LOCAL_DATE_TIME_LIST, this);
  return (List<LocalDateTime>) getValue();
}

/**
 * @return Gets a {@link OffsetDateTime} list config response.
 * @throws NotOfClassException If the selected config isn't an offset date time list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<OffsetDateTime> getAsOffsetDateTimeList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.OFFSET_DATE_TIME_LIST, this);
  return (List<OffsetDateTime>) getValue();
}

/**
 * @return Gets a {@link ZonedDateTime} list config response.
 * @throws NotOfClassException If the selected config isn't a zoned date time list.
 */
@Contract(pure = true)
@ExternalUse
default @NotNull List<ZonedDateTime> getAsZonedDateTimeList() throws NotOfClassException {
  Utils.classCheck(SupportedClasses.ZONED_DATE_TIME_LIST, this);
  return (List<ZonedDateTime>) getValue();
}
}