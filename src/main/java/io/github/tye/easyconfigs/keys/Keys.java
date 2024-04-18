package io.github.tye.easyconfigs.keys;

import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.github.tye.easyconfigs.keys.KeyHandler.*;

// These methods are intended for use projects using Easy Configurations as a dependency.
@SuppressWarnings("unused")
@ExternalUse
public interface Keys {

/**
 Initiates a KeyInstance.
 @param toReplace The value that should be replaced. The
 {@link EasyConfigurations#setKeyCharacters(String, String) values set} that define
 a key <b>must not</b> be included here. */
@ExternalUse
default void init(@NotNull String toReplace) {
  KeyHandler.toReplace.put(this, toReplace);
}


/**
 Sets the string value that this key will replace to the given string.
 <p>
 If no value is set then it will default to an empty string.
 @param replaceWith The string to replace the key with.
 @return The modified key string.
 @throws NullPointerException If the given string was null */
@ExternalUse
default @NotNull Keys replaceWith(@NotNull String replaceWith) throws NullPointerException {
  NullCheck.notNull(replaceWith, "Replacement string");
  KeyHandler.replaceWith.put(this, replaceWith);
  return this;
}


/**
 Gets the value that the key will replace.
 @return The key value (with starting &amp; ending markers included) that will be replaced. */
@Contract(pure=true)
@InternalUse
default @NotNull String getToReplace() {
  // The string will never be null since the enum has been loaded before this method is reached.
  return keyStart + toReplace.get(this) + keyEnd;
}

/**
 Gets the value that the key will be replaced with.
 @return The value that the key will be replaced with. */
@Contract(pure=true)
@InternalUse
default @NotNull String getReplaceWith() {
  String replaceString = replaceWith.getOrDefault(this, "");
  // Resets the key value.
  replaceWith.put(this, "");
  return replaceString;
}
}
