package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// These methods are intended for use projects using Easy Configurations as a dependency.
@SuppressWarnings("unused")
@ExternalUse
public interface KeyInstance {

/**
 Stores the string that will get replaced with {@link #replaceWith}. */
@InternalUse
String[] toReplace = new String[1];

/**
 Stores the string that will be replaced by {@link #toReplace}. */
@InternalUse
String[] replaceWith = new String[]{""};


/**
 Initiates a KeyInstance.
 @param toReplace The value that should be replaced. The
 {@link EasyConfigurations#setKeyCharacters(String, String) values set} that define
 a key <b>must not</b> be included here. */
@ExternalUse
default void init(@NotNull String toReplace) {
  this.toReplace[0] = toReplace;
}


/**
 Sets the string value that this key will replace to the given string.
 <p>
 The set value will persist until it is explicitly replaced. If no value is set then it will default
 to an empty string.
 @param string The string to replace the key with.
 @return The modified key string.
 @throws NullPointerException If the given string was null */
@ExternalUse
default @NotNull KeyInstance replaceWith(@NotNull String string) throws NullPointerException {
  NullCheck.notNull(string, "Replacement string");
  this.replaceWith[0] = string;
  return this;
}


/**
 Gets the value that the key will replace.
 @return The key value (with starting &amp; ending markers included) that will be replaced. */
@Contract(pure=true)
@InternalUse
default @NotNull String getToReplace() {
  return EasyConfigurations.keyStart + this.toReplace[0] + EasyConfigurations.keyEnd;
}

/**
 Gets the value that the key will be replaced with.
 @return The value that the key will be replaced with. */
@Contract(pure=true)
@InternalUse
default @NotNull String getReplaceWith() {
  return this.replaceWith[0];
}
}
