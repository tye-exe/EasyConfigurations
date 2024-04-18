package io.github.tye.easyconfigs.keys;

import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 A class to store values of keys. */
@InternalUse
public final class KeyHandler {


/**
 Contains the values keys will replace. */
@InternalUse
public static final @NotNull HashMap<Keys, String> toReplace = new HashMap<>();

/**
 Contains the values keys will be replaced with. */
@InternalUse
public static final @NotNull HashMap<Keys, String> replaceWith = new HashMap<>();


/**
 Stores the string that proceeds a key */
@InternalUse
public static @NotNull String keyStart = "{";
/**
 Stores the string that terminates a key */
@InternalUse
public static @NotNull String keyEnd = "}";
}
