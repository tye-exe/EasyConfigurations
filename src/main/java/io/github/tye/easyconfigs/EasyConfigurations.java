package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.exceptions.MissingInterfaceException;
import io.github.tye.easyconfigs.instances.ConfigInstance;
import io.github.tye.easyconfigs.instances.KeyInstance;
import io.github.tye.easyconfigs.instances.LangInstance;
import io.github.tye.easyconfigs.internalConfigs.Config;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.yamls.YamlHandling;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;

/**
 This is the main class for EasyConfigurations. It contains the basic methods that control core
 elements within EasyConfigurations.<br>
 <br>
 EasyConfigurations provides an easy &amp; simplistic way to implement a highly customizable config
 &amp; lang system into your java 8+ projects.<br>
 Read the
 <a href="https://github.com/tye-exe/EasyConfigurations?tab=readme-ov-file">README</a>
 file on GitHub for more information! */
@ExternalUse
public class EasyConfigurations {


public static Instance configInstance = new Instance();

/**
 Registers the given enum as the config enum. Without using this method configs won't work.<br>
 Only one class can be registered as the config enum. If you try to register a second class, the
 second class will be discarded.<br>
 <br>
 Configs are used to store data persistently between a program, closing &amp; re-opening.
 It specializes in storing values that are often used as settings within programs.
 @param configEnum   The class of your config enum.
 @param resourcePath The path to the yaml file within the resource folder that contains the config
 values.
 @throws MissingInterfaceException     If the given class doesn't implement {@link ConfigInstance}.
 @throws FileNotFoundException         If the file at the given resourcePath doesn't exist.
 @throws DefaultConfigurationException If the yaml file doesn't conform to config enum. */
@ExternalUse
public static void registerConfig(@NotNull Class<?> configEnum, @NotNull String resourcePath) throws MissingInterfaceException, FileNotFoundException, DefaultConfigurationException {
  if (!configInstance.isInitiated()) return;

  NullCheck.notNull(configEnum, "Config enum");
  NullCheck.notNull(resourcePath, "Resource path");

  if (!doesImplement(configEnum, ConfigInstance.class)) {
    throw new MissingInterfaceException(Lang.missingInterface(configEnum.getName(), ConfigInstance.class.getName()));
  }

  configInstance
      .setMap(YamlHandling.parseInternalYaml(configEnum, resourcePath))
      .setPath(resourcePath)
      .setInitiated(true);
}


public static Instance langInstance = new Instance();

/**
 Registers the given enum as the lang enum. Without using this method lang won't work.<br>
 Only one class can be registered as the lang enum. If you try to register a second class, the second
 class will be discarded.<br>
 <br>
 Lang is used to provide the user with a centralized response based upon the variables at runtime.
 @param langEnum     The class of your lang enum.
 @param resourcePath The path to the yaml file within the resource folder that contains the lang
 values.
 @throws MissingInterfaceException     If the given class doesn't implement {@link LangInstance}.
 @throws FileNotFoundException         If the file at the given resourcePath doesn't exist.
 @throws DefaultConfigurationException If the yaml file doesn't conform to config enum. */
@ExternalUse
public static void registerLang(@NotNull Class<?> langEnum, @NotNull String resourcePath) throws MissingInterfaceException, FileNotFoundException, DefaultConfigurationException {
  if (!langInstance.isInitiated()) return;

  NullCheck.notNull(langEnum, "Lang enum");
  NullCheck.notNull(resourcePath, "Resource path");

  if (!doesImplement(langEnum, LangInstance.class)) {
    throw new MissingInterfaceException(Lang.missingInterface(langEnum.getName(), LangInstance.class.getName()));
  }

  langInstance
      .setMap(YamlHandling.parseInternalYaml(langEnum, resourcePath))
      .setPath(resourcePath)
      .setInitiated(true);
}


/**
 Stores if the key enum has already been initiated. */
@InternalUse
private static boolean keyInitiated = false;

/**
 Registers the given enum as the key enum. Without using this method keys won't work.<br>
 Only one class can be registered as the key enum. If you try to register a second class, the second
 class will be discarded.<br>
 <br>
 Keys are used to replace key sections of a lang response with a variable at runtime.
 This allows for lang responses to contain information that isn't pre-determined.
 @param keyEnum The class of your key enum.
 @throws MissingInterfaceException If the given class doesn't implement {@link KeyInstance}. */
@ExternalUse
public static void registerKey(@NotNull Class<?> keyEnum) throws MissingInterfaceException {
  if (keyInitiated) return;
  NullCheck.notNull(keyEnum, "Key enum");

  if (!doesImplement(keyEnum, KeyInstance.class)) {
    throw new MissingInterfaceException(Lang.missingInterface(keyEnum.getName(), KeyInstance.class.getName()));
  }

  keyInitiated = true;
}


/**
 Checks if a class implements the correct interface.
 @param givenClazz     The given clazz to check.
 @param givenInterface The given interface to check.
 @return True if the given clazz implements the given interface. */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
private static boolean doesImplement(@NotNull Class<?> givenClazz, @NotNull Class<?> givenInterface) {
  for (Class<?> implemented : givenClazz.getInterfaces()) {
    if (!implemented.equals(givenInterface)) continue;
    return true;
  }

  return false;
}


public static void registerExternalConfig(@NotNull File externalConfigFile) throws FileNotFoundException {
  // Checks that the file exists & that it is a file.
  if (externalConfigFile.exists()) {
    throw new FileNotFoundException(Lang.externalConfigNotFound(externalConfigFile.getPath()));
  }
  if (!externalConfigFile.isFile()) {
    throw new FileNotFoundException(Lang.externalConfigNotFile(externalConfigFile.getPath()));
  }

  //parseExternalYaml
}


/**
 Stores the string that proceeds a key */
@InternalUse
public static @NotNull String keyStart = "{";
/**
 Stores the string that terminates a key */
@InternalUse
public static @NotNull String keyEnd = "}";

/**
 Sets the strings of text that appear at the start &amp; at the end of a key within a lang
 response.<br>
 By default, the keyStart is "{" &amp; the keyEnd is "}".
 @param keyStart The string sequence that indicated the start of a key.
 @param keyEnd   The string sequence that indicated the end of a key. */
@ExternalUse
public static void setKeyCharacters(@NotNull String keyStart, @NotNull String keyEnd) {
  NullCheck.notNull(keyStart, "Starting key string");
  NullCheck.notNull(keyEnd, "Ending key string");

  EasyConfigurations.keyStart = keyStart;
  EasyConfigurations.keyEnd = keyEnd;
}


/**
 Sets the internal language that EasyConfigurations should output its log messages in.<br>
 A list of languages can be seen at {@link Config.InternalLoggingLanguages}.
 @param language The language that EasyConfigurations should log messages as. */
@ExternalUse
public static void setEasyConfigurationLanguage(@NotNull Config.InternalLoggingLanguages language) {
  NullCheck.notNull(language, "EasyConfigurations language");

  Config.setLanguage(language);
}
}