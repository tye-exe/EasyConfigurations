package io.github.tye.easyconfigs.internalConfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.annotations.NotImplemented;
import io.github.tye.easyconfigs.instances.BaseInstance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Contains the internal lang responses for EasyConfigurations.<br>
 Each method contains a text response that is returned in the configured language.<br>
 Methods that take arguments replace certain parts of the response with the given arguments. */
// More languages might be added in the future.
@SuppressWarnings("SwitchStatementWithTooFewBranches")
@InternalUse
public class Lang {

/**
 @see Lang */
@Contract(pure=true)
@NotImplemented
@InternalUse
public static @NotNull String noFile() {
  return "WIP";
}

/**
 @see Lang */
@Contract(pure=true)
@NotImplemented
@InternalUse
public static @NotNull String parseYaml() {
  return "WIP";
}

/**
 @see Lang */
@Contract(pure=true)
@NotImplemented
@InternalUse
public static @NotNull String fileRestore() {
  return "WIP";
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
public static @NotNull String missingInterface(@NotNull String clazzName, @NotNull String interfaceName) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "The class \"" + clazzName + "\" doesn't implement \"" + interfaceName + "\".";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String invalidKey(@NotNull String key) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "The key \"" + key + "\" contains invalid characters.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String badYaml() {
  switch (Config.getLanguage()) {

  case ENGLISH: return "Null value parsed from YAML. EasyConfigurations doesn't allow for null objects in lang or configs. (The world \"null\" is valid).";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String unusedYamlPath(@NotNull String yamlPath, @NotNull String filePath) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "The yaml path \"" + yamlPath + "\" in \"" + filePath + "\" is a path that isn't being used by EasyConfigurations.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String unverifiedClass(@NotNull BaseInstance instance, @NotNull Object value) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "No validation could be performed on \"" + instance.getYamlPath() + ": " + value + "\" as the class \"" + instance.getAssingedClass().getName() + "\" is not part of validation test.";
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

  case ENGLISH: return "\"" + path + "\" isn't in default file \"" + resourcePath + "\"." + " Either add it to the default file or remove it from the internal enum.";
  default: return "";

  }
}

/**
 @see Lang */
@Contract(pure=true)
@InternalUse
public static @NotNull String notAssignedClass(@NotNull String path, @NotNull String resourcePath, @NotNull Class<?> rawValueClass, @NotNull String clazzName) {
  switch (Config.getLanguage()) {

  case ENGLISH: return "\"" + path + "\" in \"" + resourcePath + "\" is of class \"" + rawValueClass + "\". Therefore, cannot be parsed as given class, \"" + clazzName + "\".";
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
}
