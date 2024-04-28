package io.github.tye.easyconfigs.instances.reading;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.keys.Keys;
import org.jetbrains.annotations.NotNull;

import static io.github.tye.easyconfigs.EasyConfigurations.readOnlyLangInstance;

/**
 This interface must be implemented by an enum to define it as an enum containing the different read
 only lang options.
 <p>
 Please reference the <a
 href="https://github.com/tye-exe/EasyConfigurations?tab=readme-ov-file#setting-up-lang">README</a>
 file on GitHub for more usage information. */
@SuppressWarnings("unused")
// These methods are intended for use projects using Easy Configurations as a dependency.
@ExternalUse
public interface ReadingLangInstance extends ReadingInstance {

/**
 Creates a new instance of a Lang enum.
 @param yamlPath The key path of the yaml value to parse as the lang. */
@ExternalUse
default void init(@NotNull String yamlPath) {
  ReadingInstanceHandler.yamlPath.put(this, yamlPath);
}


/**
 @return {@link String#getClass()} as all langs are strings. */
@InternalUse
@Override
default Class<?> getAssingedClass() {
  return String.class;
}

/**
 Gets the string response for the selected enum, with any matching keys replaced.
 @param keys The keys to modify the response with.
 @return The modified string.
 @throws NullPointerException If the given argument is null.
 @throws NotInitiatedException If a lang is retrieved before it is registered with
 {@link
 io.github.tye.easyconfigs.EasyConfigurations#registerReadOnlyLang(Class,
     String) EasyConfigurations#registerReadOnlyLang(Class, String)} */
@ExternalUse
default @NotNull String get(@NotNull Keys... keys) throws NotInitiatedException, NullPointerException {
  NullCheck.notNull(keys, "keys");

  // Won't be null as the lang will be initialized.
  String response = readOnlyLangInstance.getValue(getYamlPath()).toString();

  // Replaces the keys within the response with their set replace value.
  for (Keys registeredKey : keys) {
    int replacementIndex = response.indexOf(registeredKey.getToReplace());

    // Replaces every instance of the found key within the response string.
    while (replacementIndex != -1) {
      String stringStart = response.substring(0, replacementIndex);
      String stringEnd = response.substring(replacementIndex + registeredKey.getToReplace().length());

      response = stringStart + registeredKey.getReplaceWith() + stringEnd;

      replacementIndex = response.indexOf(registeredKey.getToReplace());
    }
  }

  return response;
}
}

