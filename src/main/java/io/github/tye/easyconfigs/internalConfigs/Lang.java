package io.github.tye.easyconfigs.internalConfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Contains the internal lang responses for EasyConfigurations.<br> Each method contains a text
 response that is returned in the configured language.<br> Methods that take arguments replace
 certain parts of the response with the given arguments. */
// More languages might be added in the future.
@SuppressWarnings("SwitchStatementWithTooFewBranches")
@InternalUse
public class Lang {

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String configNotReadable(@NotNull String path) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "Unable to read configuration file at \"" + path + "\".";
  default: return "";
  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String externalConfigNotFound(@NotNull String path) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "Unable to locate external configuration file at \"" + path + "\".";
  default: return "";
  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String externalConfigNotFile(@NotNull String path) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "Item at \"" + path + "\" must be a file.";
  default: return "";
  }
}


/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String notNull(@Nullable String objectName) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "\"" + objectName + "\" cannot be null.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String containsNull(@Nullable String objectName) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "\"" + objectName + "\" contains null object.";
  default: return "";

  }
}


/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String notInstantiated(@Nullable String objectName) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "\"" + objectName + "\" hasn't been instantiated.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String nullInYaml(@NotNull String yamlPath) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "Null value found at \"" + yamlPath + "\". Null values are not allowed.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String unusedYamlPath(@NotNull String yamlPath, @NotNull String filePath) {
  switch (Config.getLanguage()) {

  case
      ENGLISH: return "The yaml path \"" + yamlPath + "\" in \"" + filePath + "\" is a path that isn't being used by EasyConfigurations.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String notOfClass(@NotNull String value, @NotNull String classes) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "\"" + value + "\" is not of type(s) \"" + classes + "\".";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String notInDefaultYaml(@NotNull String path, @NotNull String resourcePath) {
  switch (Config.getLanguage()) {

  case
      ENGLISH: return "\"" + path + "\" isn't in default file \"" + resourcePath + "\"." + " Either add it to the default file or remove it from the internal enum.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String notAssignedClass(@NotNull String path, @NotNull String resourcePath, @NotNull Class<?> rawValueClass, @NotNull String clazzName) {
  switch (Config.getLanguage()) {

  case
      ENGLISH: return "\"" + path + "\" in \"" + resourcePath + "\" is of class \"" + rawValueClass + "\". Therefore, cannot be parsed as given class, \"" + clazzName + "\".";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String internalYamlFail(@NotNull String resourcePath) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "Unable to parsed required yaml file at \"" + resourcePath + "\".";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String classNotSupported(String clazzName) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "\"" + clazzName + "\" is not supported.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String errorWhileParsingYaml() {
  switch (Config.getLanguage()) {

  case ENGLISH: return "An error was thrown when parsing the yaml.";
  default: return "";

  }
}
}
