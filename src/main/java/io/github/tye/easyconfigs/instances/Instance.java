package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.annotations.InternalUse;

/**
 An interface that all reading &amp; persistent instances extend. It's primarily used to allow either
 a reading or persistent instance to be used interchangeably. */
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
