package me.tye;

import me.tye.utils.annotations.ExternalUse;
import org.jetbrains.annotations.NotNull;

import static me.tye.utils.Consts.langMap;

/**
 This interface is designed to be implemented by an enum to define it as a enum containing the different lang options for the program implementing this dependency.<br>
 Please reference the
 <a href="https://github.com/Mapty231/EasyConfigurations?tab=readme-ov-file#setting-up-lang">README.md</a>
 file on github for "EasyConfigurations" for usage information.
 */
@ExternalUse
public interface LangInstance extends BaseInstance {

/**
 Gets the string response for the selected enum.
 * @param keys The keys to modify the response with.
 * @return The modified string.
 */
@ExternalUse
default @NotNull String get(KeyInstance... keys) {

  String response = langMap.get(getYamlPath()).toString();

  // Replaces the keys within the response with their set replace value.
  for (KeyInstance registeredKey : keys) {
    int replacementIndex = response.indexOf(registeredKey.getReplacementValue());

    // Replaces every instance of the found key within the response string.
    while (replacementIndex != -1) {
      String stringStart = response.substring(0, replacementIndex);
      String stringEnd = response.substring(replacementIndex+registeredKey.getReplacementValue().length());

      response = stringStart + registeredKey.getReplacementValue() + stringEnd;

      replacementIndex = response.indexOf(registeredKey.getReplacementValue());
    }
  }

  return response;
}

/**
 Creates a new instance of a Lang enum.
 * @param yamlPath The key path of the yaml value to parse as the lang.
 */
@ExternalUse
default void init(@NotNull String yamlPath) {
  BaseInstance.super.init(String.class, yamlPath);
}
}
