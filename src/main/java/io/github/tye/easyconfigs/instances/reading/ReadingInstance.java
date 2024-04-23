package io.github.tye.easyconfigs.instances.reading;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.instances.Instance;

/**
 Contains the methods that {@link ReadingConfigInstance} &amp; {@link ReadingLangInstance} require. */
@InternalUse
public interface ReadingInstance extends Instance {

/**
 Gets the class the enum was assigned to.
 @return The class this instance should be parsed as. */
@InternalUse
@Override
default Class<?> getAssingedClass() {
  return ReadingInstanceHandler.assignedClass.get(this);
}

/**
 Gets the path to the enum within the Yaml file.
 @return The path to this class in the Yaml file. */
@InternalUse
@Override
default String getYamlPath() {
  return ReadingInstanceHandler.yamlPath.get(this);
}

}
