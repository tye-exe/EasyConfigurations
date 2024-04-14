package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

/**
 Contains the variables &amp; methods that {@link ConfigInstance} &amp; {@link LangInstance} require. */
@InternalUse
public interface Instance {

/**
 Creates a new instance of a config or lang enum.
 @param markedClass The class to parse the yaml value as.
 @param yamlPath    The key path of the yaml value to parse. */
@InternalUse
default void init(@NotNull Class<?> markedClass, @NotNull String yamlPath) {
  NullCheck.notNull(markedClass, "Instance of class");
  NullCheck.notNull(yamlPath, "Yaml path");

  InstanceHandler.assignedClass.put(this, markedClass);
  InstanceHandler.yamlPath.put(this, yamlPath);
}


/**
 Gets the class the enum was assigned to.
 @return The class this instance should be parsed as. */
@InternalUse
default Class<?> getAssingedClass() {
  return InstanceHandler.assignedClass.get(this);
}

/**
 Gets the path to the enum within the Yaml file.
 @return The path to this class in the Yaml file. */
@InternalUse
default String getYamlPath() {
  return InstanceHandler.yamlPath.get(this);
}

}
