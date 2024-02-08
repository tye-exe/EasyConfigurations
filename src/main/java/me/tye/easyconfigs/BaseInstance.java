package me.tye.easyconfigs;

import me.tye.easyconfigs.utils.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static me.tye.easyconfigs.utils.Utils.notNull;

/**Contains the vars & methods that {@link ConfigInstance} & {@link LangInstance} require.*/
@InternalUse
public interface BaseInstance {

/**The class that the object stored in the Yaml should be parsed as.*/
@InternalUse
@NotNull
static HashMap<BaseInstance, Class<?>> markedClass = new HashMap<>();

/**The path to parse the object from in the Yaml.*/
@InternalUse
@NotNull
static HashMap<BaseInstance, String> yamlPath = new HashMap<>();

/**
 Creates a new instance of a config or lang enum.
 * @param markedClass The class to parse the yaml value as.
 * @param yamlPath The key path of the yaml value to parse.
 */
@InternalUse
default void init(@NotNull Class<?> markedClass, @NotNull String yamlPath) {
  notNull(markedClass, "Instance of class");
  notNull(yamlPath, "Yaml path");

  this.markedClass.put(this, markedClass);
  this.yamlPath.put(this, yamlPath);
}


/**
 * @return The class this instance should be parsed as.
 */
@InternalUse
default Class<?> getMarkedClass() {
  return BaseInstance.markedClass.get(this);
}

/**
 * @return The path to this class in the Yaml file.
 */
@InternalUse
default String getYamlPath() {
  return BaseInstance.yamlPath.get(this);
}

}
