package io.github.tye.easyconfigs.instances.persistent;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.keys.Keys;
import org.jetbrains.annotations.NotNull;

import static io.github.tye.easyconfigs.EasyConfigurations.persistentLangInstance;

public interface PersistentLangInstance extends PersistentInstance {

/**
 Creates a new instance of a Lang enum.
 @param yamlPath The key path of the yaml value to parse as the lang. */
@ExternalUse
default void init(@NotNull String yamlPath) {
  PersistentInstanceHandler.yamlPath.put(this, yamlPath);
}

@Override
default Class<?> getAssingedClass() {
  return String.class;
}

@Override
default void replaceValue(@NotNull Object value) throws NotOfClassException, NullPointerException, NotInitiatedException {
  persistentLangInstance.replaceValue(this, value);
}

/**
 Gets the string response for the selected enum, with any matching keys replaced.
 @param keys The keys to modify the response with.
 @return The modified string.
 @throws NotInitiatedException If a lang is retrieved before it is registered with
 {@link
 io.github.tye.easyconfigs.EasyConfigurations#registerPersistentLang(Class,
     String)} EasyConfigurations#registerReadOnlyLang(Class, String)} */
@ExternalUse
default @NotNull String get(@NotNull Keys... keys) throws NotInitiatedException {
  NullCheck.notNull(keys, "keys");

  // Won't be null as the lang will be initialized.
  String response = persistentLangInstance.getValue(getYamlPath()).toString();

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
