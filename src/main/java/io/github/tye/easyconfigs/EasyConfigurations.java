package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.instances.persistent.PersistentConfigInstance;
import io.github.tye.easyconfigs.instances.persistent.PersistentInstanceHandler;
import io.github.tye.easyconfigs.instances.persistent.PersistentLangInstance;
import io.github.tye.easyconfigs.instances.reading.ReadingConfigInstance;
import io.github.tye.easyconfigs.instances.reading.ReadingInstanceHandler;
import io.github.tye.easyconfigs.instances.reading.ReadingLangInstance;
import io.github.tye.easyconfigs.internalConfigs.Config;
import io.github.tye.easyconfigs.keys.KeyHandler;
import io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger;
import io.github.tye.easyconfigs.logger.EasyConfigurationsLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 This is the main class for EasyConfigurations. It contains the basic methods that control core
 elements within EasyConfigurations.<br>
 <br>
 EasyConfigurations provides an easy &amp; simplistic way to implement a highly customizable config
 &amp; lang system into your java 8+ projects.<br> Read the
 <a href="https://github.com/tye-exe/EasyConfigurations?tab=readme-ov-file">README</a>
 file on GitHub for more information! */
@ExternalUse
public class EasyConfigurations {

/**
 Sets the strings of text that appear at the start &amp; at the end of a key within a lang
 response.<br> By default, the keyStart is "{" &amp; the keyEnd is "}".
 @param keyStart The string sequence that indicated the start of a key.
 @param keyEnd   The string sequence that indicated the end of a key. */
@ExternalUse
public static void setKeyCharacters(@NotNull String keyStart, @NotNull String keyEnd) {
  NullCheck.notNull(keyStart, "Starting key string");
  NullCheck.notNull(keyEnd, "Ending key string");

  KeyHandler.keyStart = keyStart;
  KeyHandler.keyEnd = keyEnd;
}


/**
 Sets the internal language that EasyConfigurations should output its log messages in.<br> A list of
 languages can be seen at {@link Config.InternalLoggingLanguages}.
 @param language The language that EasyConfigurations should log messages as. */
@ExternalUse
public static void setEasyConfigurationLanguage(@NotNull Config.InternalLoggingLanguages language) {
  NullCheck.notNull(language, "EasyConfigurations language");

  Config.setLanguage(language);
}


/**
 Replaces the current EasyConfigurations logger with the given logger.
 @param customLogger The given logger to replace the current logger with. */
@ExternalUse
public static void overrideEasyConfigurationsLogger(@NotNull EasyConfigurationsLogger customLogger) {
  EasyConfigurationsDefaultLogger.logger = customLogger;
}


// Read only configurations //

/**
 Stores the information about the parsed read only config yaml. */
@InternalUse
public static @NotNull ReadingInstanceHandler readOnlyConfigInstance = new ReadingInstanceHandler();

/**
 Registers the given enum as the config enum. Without using this method, or
 {@link #registerPersistentConfig(Class, String, File)} configs won't work.
 <p>
 <p>
 When registering a config with this method it will be <strong>READ ONLY</strong>. No changes can be
 made to the configs within the program, or by the end user. To register a config that can be change
 be the user, or the program use {@link #registerPersistentConfig(Class, String, File)}.
 @param configEnum   The class of your config enum.
 @param resourcePath The path to the yaml file within the resource folder that contains the config
 values.
 @throws ConfigurationException If the yaml has an error, or a value couldn't be parsed. enum.
 @throws IOException            If there was an error reading the input stream, or if the given path
 doesn't lead to any files.
 @throws NullPointerException   If any of the arguments are null. */
@ExternalUse
public static void registerReadOnlyConfig(@NotNull Class<? extends ReadingConfigInstance> configEnum, @NotNull String resourcePath) throws ConfigurationException, IOException, NullPointerException {
  NullCheck.notNull(configEnum, "Config enum");
  NullCheck.notNull(resourcePath, "Resource path");

  readOnlyConfigInstance = new ReadingInstanceHandler(resourcePath, configEnum);
}


/**
 Stores the information about the parsed read only lang yaml. */
@InternalUse
public static @NotNull ReadingInstanceHandler readOnlyLangInstance = new ReadingInstanceHandler();

/**
 Registers the given enum as the lang enum. Without using this method lang won't work.<br>
 <br>
 Lang is used to provide the user with a centralized response based upon the variables at runtime.
 @param langEnum     The class of your lang enum.
 @param resourcePath The path to the yaml file within the resource folder that contains the lang
 values.
 @throws ConfigurationException If the yaml has an error, or a value couldn't be parsed.
 @throws IOException            If there was an error reading the input stream, or if the given path
 doesn't lead to any files.
 @throws NullPointerException   If any of the arguments are null. */
@ExternalUse
public static void registerReadOnlyLang(@NotNull Class<? extends ReadingLangInstance> langEnum, @NotNull String resourcePath) throws IOException, ConfigurationException, NullPointerException {
  NullCheck.notNull(langEnum, "Lang enum");
  NullCheck.notNull(resourcePath, "Resource path");

  readOnlyLangInstance = new ReadingInstanceHandler(resourcePath, langEnum);
}


// Persistent configurations //

/**
 Stores the information about the parsed persistent config yaml. */
@InternalUse
public static @NotNull PersistentInstanceHandler persistentConfigInstance = new PersistentInstanceHandler();

/**
 Registers the given enum as the config enum. Without registering a config with this method, or
 {@link #registerReadOnlyConfig(Class, String)} configs won't work.
 <p>
 <p>
 Configs are used to store data persistently between a program, closing &amp; re-opening. It
 specializes in storing values that are often used as settings within programs.
 <p>
 When using this method, configs are editable by both the program &amp; the user. This is achieved by
 having an external config file.
 @param configEnum         The class of your config enum.
 @param resourcePath       The path to the yaml file within the resource folder that contains the
 config values.
 @param externalConfigFile The external config file that will be used to store persistent changes.
 @throws ConfigurationException If there was an error when parsing the default yaml file.
 @throws IOException            If there was an error reading the input stream, or if the given path
 doesn't lead to any files.
 @throws NullPointerException   If any of the arguments are null. */
@ExternalUse
public static void registerPersistentConfig(@NotNull Class<? extends PersistentConfigInstance> configEnum, @NotNull String resourcePath, @NotNull File externalConfigFile) throws IOException, ConfigurationException {
  NullCheck.notNull(configEnum, "config enum");
  NullCheck.notNull(resourcePath, "resource path");
  NullCheck.notNull(externalConfigFile, "external config file");

  persistentConfigInstance = new PersistentInstanceHandler(resourcePath, externalConfigFile, configEnum);
}


/**
 Stores the information about the parsed persistent lang yaml. */
@InternalUse
public static @NotNull PersistentInstanceHandler persistentLangInstance = new PersistentInstanceHandler();

/**
 Registers the given enum as the lang enum. Without registering lang with this method, or
 {@link #registerReadOnlyLang(Class, String)} lang won't work.
 <p>
 <p>
 Lang is used to store lang responses within a program. This allows for easy translation &amp;
 centralization of lang data.
 <p>
 When using this method, lang responses are editable by both the program &amp; the user. This is
 achieved by having an external lang file.
 @param langEnum         The class of your lang enum.
 @param resourcePath     The path to the yaml file within the resource folder that contains the lang
 values.
 @param externalLangFile The external lang file that will be used to store persistent changes.
 @throws ConfigurationException If there was an error when parsing the default yaml file.
 @throws IOException            If there was an error reading the input stream, or if the given path
 doesn't lead to any files.
 @throws NullPointerException   If any of the arguments are null. */
@ExternalUse
public static void registerPersistentLang(@NotNull Class<? extends PersistentLangInstance> langEnum, @NotNull String resourcePath, @NotNull File externalLangFile) throws IOException, ConfigurationException, NullPointerException {
  NullCheck.notNull(langEnum, "Lang enum");
  NullCheck.notNull(resourcePath, "Resource path");
  NullCheck.notNull(externalLangFile, "external config file");

  persistentLangInstance = new PersistentInstanceHandler(resourcePath, externalLangFile, langEnum);
}

}