package me.tye;

import me.tye.internalConfigs.Config;
import me.tye.internalConfigs.Lang;
import me.tye.utils.Consts;
import me.tye.utils.YamlHandling;
import me.tye.utils.annotations.ExternalUse;
import me.tye.utils.annotations.InternalUse;
import me.tye.utils.exceptions.MissingInterfaceException;
import org.jetbrains.annotations.NotNull;

import static me.tye.utils.Consts.*;
import static me.tye.utils.Utils.*;

/**
 This is the main class for EasyConfigurations. It contains the basic methods that control core elements within EasyConfigurations.<br>
 <br>
 EasyConfigurations provides an easy & simplistic way to implement a highly customizable config & lang system into your java 8+ projects.<br>
 Read the README.md file on github for more information!
 */
@ExternalUse
public class EasyConfigurations {

/**Stores if the config enum has already been initiated.*/
@InternalUse
private static boolean configInitiated = false;

/**Stores if the config enum has already been initiated.*/
@InternalUse
private static boolean langInitiated = false;

/**Stores if the config enum has already been initiated.*/
@InternalUse
private static boolean keyInitiated = false;

/**
 Registers the given enum as the config enum. Without using this method configs won't work.<br>
 Only one class can be registered as the config enum. If you try to register a second class, the second class will be discarded.<br>
 <br>
 Configs are used to store data persistently between a program, closing & ro-opening.
 It specialises in storing values that are often uses as settings within programs.
 * @param configEnum The class of your config enum.
 * @param resourcePath The path to the yaml file within the resource folder that contains the config values.
 * @throws MissingInterfaceException If the given class doesn't implement {@link ConfigInstance}.
 */
@ExternalUse
public static void registerConfig(@NotNull Class<?> configEnum, @NotNull String resourcePath) throws MissingInterfaceException {
  if (configInitiated) return;

  notNull(configEnum, "Config enum");
  notNull(resourcePath, "Resource path");

  if (!doesImplement(configEnum, ConfigInstance.class)) throw new MissingInterfaceException(Lang.missingInterface(configEnum.getName(), ConfigInstance.class.getName()));

  configMap = YamlHandling.parseInternalYaml(configEnum, resourcePath);
  configInitiated = true;
}

/**
 Registers the given enum as the lang enum. Without using this method lang won't work.<br>
 Only one class can be registered as the lang enum. If you try to register a second class, the second class will be discarded.<br>
 <br>
 Lang is used to provide the user with a standardized response based upon the language of the program & variables at runtime.
 * @param langEnum The class of your lang enum.
 * @param resourcePath The path to the yaml file within the resource folder that contains the lang values.
 * @throws MissingInterfaceException If the given class doesn't implement {@link LangInstance}.
 */
@ExternalUse
public static void registerLang(@NotNull Class<?> langEnum, @NotNull String resourcePath) throws MissingInterfaceException {
  if (langInitiated) return;

  notNull(langEnum, "Lang enum");
  notNull(resourcePath, "Resource path");

  if (!doesImplement(langEnum, LangInstance.class)) throw new MissingInterfaceException(Lang.missingInterface(langEnum.getName(), LangInstance.class.getName()));

  langMap = YamlHandling.parseInternalYaml(langEnum, resourcePath);
  langInitiated = true;
}

/**
 Registers the given enum as the key enum. Without using this method keys won't work.<br>
 Only one class can be registered as the key enum. If you try to register a second class, the second class will be discarded.<br>
 <br>
 Keys are used to replace key sections of a lang response with a variable at runtime.
 This allows for lang responses to contain information that isn't pre-determined.
 * @param keyEnum The class of your key enum.
 * @throws MissingInterfaceException If the given class doesn't implement {@link KeyInstance}.
 */
@ExternalUse
public static void registerKey(@NotNull Class<?> keyEnum) throws MissingInterfaceException {
  if (keyInitiated) return;
  notNull(keyEnum, "Key enum");

  if (!doesImplement(keyEnum, KeyInstance.class)) throw new MissingInterfaceException(Lang.missingInterface(keyEnum.getName(), KeyInstance.class.getName()));

  keyInitiated = true;
}

/**
 * @param givenClazz The given clazz to check.
 * @param givenInterface The given interface to check.
 * @return True if the given clazz implements the given interface.
 */
private static boolean doesImplement(@NotNull Class<?> givenClazz, @NotNull Class<?> givenInterface) {
  for (Class<?> implemented : givenClazz.getInterfaces()) {
    if (!implemented.equals(givenInterface)) continue;
    return true;
  }

  return false;
}

/**
 Sets the strings of text that appear at the start & at the end of a key within a lang response.<br>
 By default the keyStart is "{" & the keyEnd is "}".
 * @param keyStart The string sequence that indicated the start of a key.
 * @param keyEnd The string sequence that indicated the end of a key.
 */
@ExternalUse
public static void setKeyCharacters(@NotNull String keyStart, @NotNull String keyEnd) {
  notNull(keyStart, "Starting key string");
  notNull(keyEnd, "Ending key string");

  Consts.keyStart = keyStart;
  Consts.keyEnd = keyEnd;
}


/**
 Sets the internal language that EasyConfigurations should output it's log messages in.<br>
 A list of languages can be seen at {@link Config.InternalLanguages}.
 * @param language The language that EasyConfigurations should log messages as.
 */
@ExternalUse
public static void setEasyConfigurationLanguage(@NotNull Config.InternalLanguages language) {
  notNull(language, "EasyConfigurations language");

  Config.setLanguage(language);
}
}