package me.tye.easyconfigs.internalConfigs;

import me.tye.easyconfigs.utils.annotations.InternalUse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 Contains the internal configs for EasyConfigurations. */
@InternalUse
public class Config {

/**
 The languages that are supported for logging. */
@InternalUse
public enum InternalLanguages {
  ENGLISH()
}


/**
 Stores the current language that is being used for logging. (Defaults to english). */
@InternalUse
private static @NotNull InternalLanguages lang = InternalLanguages.ENGLISH;


/**
 @return The current language being used for logging. */
@Contract (pure=true)
@InternalUse
public static @NotNull InternalLanguages getLanguage() {
  return lang;
}

/**
 Sets the current language being used for logging. English is the default.
 @param lang Which language to use for logging. */
@InternalUse
public static void setLanguage(@NotNull InternalLanguages lang) {
  Config.lang = lang;
}


}
