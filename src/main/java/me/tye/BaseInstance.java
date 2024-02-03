package me.tye;

import me.tye.utils.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import static me.tye.utils.Utils.notNull;

/**Contains the vars & methods that {@link ConfigInstance} & {@link LangInstance} require.*/
@InternalUse
public interface BaseInstance {

/**The class that the object stored in the Yaml should be parsed as.*/
@InternalUse
@NotNull
Class<?>[] markedClass = new Class<?>[1];

/**The path to parse the object from in the Yaml.*/
@InternalUse
@NotNull
String[] yamlPath = new String[1];

/**
 Creates a new instance of a config or lang enum.
 * @param markedClass The class to parse the yaml value as.
 * @param yamlPath The key path of the yaml value to parse.
 */
@InternalUse
default void init(@NotNull Class<?> markedClass, @NotNull String yamlPath) {
  notNull(markedClass, "Instance of class");
  notNull(yamlPath, "Yaml path");

  this.markedClass[0] = markedClass;
  this.yamlPath[0] = yamlPath;
}

}
