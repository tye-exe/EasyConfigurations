package io.github.tye.easyconfigs.instances.persistent;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.instances.Instance;
import org.jetbrains.annotations.NotNull;

public interface PersistentInstance extends Instance {

void replaceValue(@NotNull Object value) throws NotOfClassException, NullPointerException, NotInitiatedException;

/**
 Gets the class the enum was assigned to.
 @return The class this instance should be parsed as. */
@InternalUse
@Override
default Class<?> getAssingedClass() {
  return PersistentInstanceHandler.assignedClass.get(this);
}

/**
 Gets the path to the enum within the Yaml file.
 @return The path to this class in the Yaml file. */
@InternalUse
@Override
default String getYamlPath() {
  return PersistentInstanceHandler.yamlPath.get(this);
}
}
