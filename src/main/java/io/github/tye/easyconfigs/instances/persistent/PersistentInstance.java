package io.github.tye.easyconfigs.instances.persistent;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.instances.Instance;
import org.jetbrains.annotations.NotNull;

/**
 Contains the methods that {@link PersistentConfigInstance} &amp; {@link PersistentLangInstance}
 require. */
@InternalUse
public interface PersistentInstance extends Instance {

/**
 Replaces the value denoted by this instance with the given value.
 @param value The new value to overwrite the old value with.
 @throws NotOfClassException   If the given value isn't of the same class as the marked class.
 @throws NullPointerException  If the given value is null.
 @throws NotInitiatedException If the persistent instance hasn't been registered yet. */
@ExternalUse
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
