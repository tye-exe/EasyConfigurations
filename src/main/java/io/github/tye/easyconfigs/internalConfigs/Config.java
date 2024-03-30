package io.github.tye.easyconfigs.internalConfigs;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 Contains the internal configs for EasyConfigurations. */
@InternalUse
public class Config {

/**
 The languages that are supported for logging. */
@ExternalUse
public enum InternalLoggingLanguages {
  ENGLISH()
}


/**
 Stores the current language that is being used for logging. (Defaults to english). */
@InternalUse
private static @NotNull Config.InternalLoggingLanguages lang = InternalLoggingLanguages.ENGLISH;


/**
 Gets the current language being used for logging inside EasyConfigurations.
 @return The current language being used for logging. */
@Contract(pure=true)
@InternalUse
public static @NotNull Config.InternalLoggingLanguages getLanguage() {
  return lang;
}

/**
 Sets the current language being used for logging. English is the default.<br>
 See {@link InternalLoggingLanguages} for available languages.
 @param lang Which language to use for logging. */
@InternalUse
public static void setLanguage(@NotNull Config.InternalLoggingLanguages lang) {
  Config.lang = lang;
}


}
