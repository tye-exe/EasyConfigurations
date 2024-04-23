package io.github.tye.easyconfigs.instances.reading;

import io.github.tye.easyconfigs.ClassName;
import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static io.github.tye.easyconfigs.EasyConfigurations.readOnlyConfigInstance;

/**
 This interface must be implemented by an enum to define it as an enum containing the different
 config options.
 <p>
 Please reference the <a
 href="https://github.com/tye-exe/EasyConfigurations?tab=readme-ov-file#setting-up-configs">README</a>
 file on GitHub for more usage information. */
@SuppressWarnings({"unused", "unchecked"})
// Unused – These methods are intended for use projects using Easy Configurations as a dependency.
// Unchecked – Suppresses the cast warnings for the Lists. As they are being cast to the correct class. This is ensured by the classCheck() preceding the cast & at config initiation.
@ExternalUse
public interface ReadingConfigInstance extends ReadingInstance {

/**
 Creates a new instance of a config enum.
 @param markedClass The class to parse the yaml value for the config as.
 @param yamlPath    The key path of the yaml value to parse as the config. */
@Contract(pure=true)
@ExternalUse
default void init(@NotNull Class<?> markedClass, @NotNull String yamlPath) {
  NullCheck.notNull(markedClass, "Instance of class");
  NullCheck.notNull(yamlPath, "Yaml path");

  ReadingInstanceHandler.assignedClass.put(this, markedClass);
  ReadingInstanceHandler.yamlPath.put(this, yamlPath);
}

/**
 @return The object in the config HashMap that this enum represents.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@InternalUse
default @NotNull Object getValue() throws NotInitiatedException {
  Object configValue = readOnlyConfigInstance.getValue(getYamlPath());

  // Value will never be null as it is ensured by checks when initializing.
  NullCheck.notNull(configValue, "configValue");
  return configValue;
}

/**
 @return Gets a {@link String} config response.
 @throws NotOfClassException   If the selected config isn't a String value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull String getAsString() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.STRING, this);
  return (String) getValue();
}

/**
 @return Gets a {@link Boolean} config response.
 @throws NotOfClassException   If the selected config isn't a Boolean value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default boolean getAsBoolean() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.BOOLEAN, this);
  return (boolean) getValue();
}

/**
 @return Gets a {@link Integer} config response.
 @throws NotOfClassException   If the selected config isn't an Integer value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default int getAsInteger() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.INTEGER, this);
  return (int) getValue();
}

/**
 @return Gets a {@link Double} config response.
 @throws NotOfClassException   If the selected config isn't a Double value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default double getAsDouble() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.DOUBLE, this);
  return (double) getValue();
}

/**
 @return Gets a {@link Float} config response.
 @throws NotOfClassException   If the selected config isn't a Float value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default float getAsFloat() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.FLOAT, this);
  return (float) getValue();
}

/**
 @return Gets a {@link Short} config response.
 @throws NotOfClassException   If the selected config isn't a Short value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default short getAsShort() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.SHORT, this);
  return (short) getValue();
}

/**
 @return Gets a {@link Long} config response.
 @throws NotOfClassException   If the selected config isn't a Long value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default long getAsLong() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.LONG, this);
  return (long) getValue();
}

/**
 @return Gets a {@link Byte} config response.
 @throws NotOfClassException   If the selected config isn't a Byte value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default byte getAsByte() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.BYTE, this);
  return (byte) getValue();
}

/**
 @return Gets a {@link Character} config response.
 @throws NotOfClassException   If the selected config isn't a Char value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default char getAsChar() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.CHAR, this);
  return (char) getValue();
}

/**
 @return Gets a {@link LocalDateTime} config response.
 @throws NotOfClassException   If the selected config isn't a LocalDateTime value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull LocalDateTime getAsLocalDateTime() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.LOCAL_DATE_TIME, this);
  return (LocalDateTime) getValue();
}

/**
 @return Gets a {@link OffsetDateTime} config response.
 @throws NotOfClassException   If the selected config isn't an OffsetDateTime value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull OffsetDateTime getAsOffsetDateTime() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.OFFSET_DATE_TIME, this);
  return (OffsetDateTime) getValue();
}

/**
 @return Gets a {@link ZonedDateTime} config response.
 @throws NotOfClassException   If the selected config isn't a ZonedDateTime value.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull ZonedDateTime getAsZonedDateTime() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.ZONED_DATE_TIME, this);
  return (ZonedDateTime) getValue();
}


/**
 @return Gets a {@link String} list config response.
 @throws NotOfClassException   If the selected config isn't a string list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<String> getAsStringList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.STRING_LIST, this);
  return (List<String>) getValue();
}

/**
 @return Gets an {@link Boolean} list config response.
 @throws NotOfClassException   If the selected config isn't a boolean list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Boolean> getAsBooleanList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.BOOLEAN_LIST, this);
  return (List<Boolean>) getValue();
}

/**
 @return Gets an {@link Integer} list config response.
 @throws NotOfClassException   If the selected config isn't an integer list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Integer> getAsIntegerList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.INTEGER_LIST, this);
  return (List<Integer>) getValue();
}

/**
 @return Gets a {@link Double} list config response.
 @throws NotOfClassException   If the selected config isn't a double list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Double> getAsDoubleList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.DOUBLE_LIST, this);
  return (List<Double>) getValue();
}

/**
 @return Gets a {@link Float} list config response.
 @throws NotOfClassException   If the selected config isn't a float list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Float> getAsFloatList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.FLOAT_LIST, this);
  return (List<Float>) getValue();
}

/**
 @return Gets a {@link Short} list config response.
 @throws NotOfClassException   If the selected config isn't a short list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Short> getAsShortList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.SHORT_LIST, this);
  return (List<Short>) getValue();
}

/**
 @return Gets a {@link Long} list config response.
 @throws NotOfClassException   If the selected config isn't a long list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Long> getAsLongList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.LONG_LIST, this);
  return (List<Long>) getValue();
}

/**
 @return Gets a {@link Byte} list config response.
 @throws NotOfClassException   If the selected config isn't a byte list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Byte> getAsByteList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.BYTE_LIST, this);
  return (List<Byte>) getValue();
}

/**
 @return Gets a {@link Character} list config response.
 @throws NotOfClassException   If the selected config isn't a char list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<Character> getAsCharList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.CHAR_LIST, this);
  return (List<Character>) getValue();
}

/**
 @return Gets a {@link LocalDateTime} list config response.
 @throws NotOfClassException   If the selected config isn't a local date time list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<LocalDateTime> getAsLocalDateTimeList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.LOCAL_DATE_TIME_LIST, this);
  return (List<LocalDateTime>) getValue();
}

/**
 @return Gets a {@link OffsetDateTime} list config response.
 @throws NotOfClassException   If the selected config isn't an offset date time list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<OffsetDateTime> getAsOffsetDateTimeList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.OFFSET_DATE_TIME_LIST, this);
  return (List<OffsetDateTime>) getValue();
}

/**
 @return Gets a {@link ZonedDateTime} list config response.
 @throws NotOfClassException   If the selected config isn't a zoned date time list.
 @throws NotInitiatedException If a config is retrieved before it is registered with
 {@link EasyConfigurations#registerReadOnlyConfig(Class, String)}. */
@ExternalUse
default @NotNull List<ZonedDateTime> getAsZonedDateTimeList() throws NotOfClassException, NotInitiatedException {
  classCheck(SupportedClasses.ZONED_DATE_TIME_LIST, this);
  return (List<ZonedDateTime>) getValue();
}


/**
 Checks if the intendedType matches the actual object type in the map that the instance points to.
 @param readingInstance The instance of config or lang to check against.
 @param intendedType    The {@link SupportedClasses} that the map value should be.
 @throws NotOfClassException If the intended type doesn't match the actual type in the map. */
@InternalUse
static void classCheck(@NotNull SupportedClasses intendedType, @NotNull ReadingInstance readingInstance) throws NotOfClassException {
  Class<?> assignedClass = ReadingInstanceHandler.assignedClass.get(readingInstance);
  StringBuilder classNames = new StringBuilder();

  for (Class<?> clazz : intendedType.getClasses()) {
    if (assignedClass.equals(clazz)) return;

    classNames.append(ClassName.getName(clazz))
              .append(" ");
  }

  throw new NotOfClassException(Lang.notOfClass(readingInstance.getYamlPath(), classNames.toString()));
}
}
