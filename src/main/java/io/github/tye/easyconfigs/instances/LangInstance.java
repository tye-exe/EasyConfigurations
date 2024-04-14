package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import org.jetbrains.annotations.NotNull;

import static io.github.tye.easyconfigs.EasyConfigurations.langInstance;

/**
 This interface must be implemented by an enum to define it as an enum containing the different lang
 options.<br>
 Please reference the
 <a
 href="https://github.com/tye-exe/EasyConfigurations?tab=readme-ov-file#setting-up-lang">README</a>
 file on GitHub for "EasyConfigurations" for usage information. */
// These methods are intended for use projects using Easy Configurations as a dependency.
@SuppressWarnings("unused")
@ExternalUse
public interface LangInstance extends Instance {

/**
 Gets the string response for the selected enum.
 @param keys The keys to modify the response with.
 @return The modified string. */
@ExternalUse
default @NotNull String get(KeyInstance... keys) {

  String response = langInstance.getMap().get(getYamlPath()).toString();

  // Replaces the keys within the response with their set replace value.
  for (KeyInstance registeredKey : keys) {
    int replacementIndex = response.indexOf(registeredKey.getReplacementValue());

    // Replaces every instance of the found key within the response string.
    while (replacementIndex != -1) {
      String stringStart = response.substring(0, replacementIndex);
      String stringEnd = response.substring(replacementIndex + registeredKey.getReplacementValue().length());

      response = stringStart + registeredKey.getReplacementValue() + stringEnd;

      replacementIndex = response.indexOf(registeredKey.getReplacementValue());
    }
  }

  return response;
}

/**
 Creates a new instance of a Lang enum.
 @param yamlPath The key path of the yaml value to parse as the lang. */
@ExternalUse
default void init(@NotNull String yamlPath) {
  Instance.super.init(String.class, yamlPath);
}
}
