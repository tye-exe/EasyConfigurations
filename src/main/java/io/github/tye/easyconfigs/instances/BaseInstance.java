package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**Contains the vars &amp; methods that {@link ConfigInstance} &amp; {@link LangInstance} require.*/
@SuppressWarnings ("unused") // These methods are intended for use projects using Easy Configurations as a dependency.
@InternalUse
public interface BaseInstance {

/**The class the object stored in the Yaml should be parsed as.*/
@InternalUse
@NotNull HashMap<BaseInstance, Class<?>> markedClass = new HashMap<>();

/**The path to parse the object from in the Yaml.*/
@InternalUse
@NotNull HashMap<BaseInstance, String> yamlPath = new HashMap<>();

/**
 Creates a new instance of a config or lang enum.
 * @param markedClass The class to parse the yaml value as.
 * @param yamlPath The key path of the yaml value to parse.
 */
@InternalUse
default void init(@NotNull Class<?> markedClass, @NotNull String yamlPath) {
  NullCheck.notNull(markedClass, "Instance of class");
  NullCheck.notNull(yamlPath, "Yaml path");

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
