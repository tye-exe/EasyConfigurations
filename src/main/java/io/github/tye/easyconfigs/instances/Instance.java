package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.annotations.InternalUse;

public interface Instance {

/**
 Gets the class the enum was assigned to.
 @return The class this instance should be parsed as. */
@InternalUse
Class<?> getAssingedClass();

/**
 Gets the path to the enum within the Yaml file.
 @return The path to this class in the Yaml file. */
@InternalUse
String getYamlPath();

}
