package io.github.tye.easyconfigs.instances;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class InstanceHandler {

/**
 The class the object stored in the Yaml should be parsed as. */
@InternalUse
@NotNull
public static final HashMap<Instance, Class<?>> assignedClass = new HashMap<>();
/**
 The path to parse the object from in the Yaml. */
@InternalUse
@NotNull
public static final HashMap<Instance, String> yamlPath = new HashMap<>();


/**
 The parsed instance responses. */
@InternalUse
private @NotNull HashMap<String, Object> map;

/**
 If the instance enum has already been initiated. */
@InternalUse
private boolean initiated;

/**
 The string to the location of the internal configuration file. */
@InternalUse
private @NotNull String path;

/**
 The clazz of the instance. */
@InternalUse
private @NotNull Class<?> clazz;


public InstanceHandler() {
  this.map = null;
  this.initiated = false;
  this.path = null;
  this.clazz = null;
}


public @NotNull HashMap<String, Object> getMap() throws NotInitiatedException {
  if (map == null) throw new NotInitiatedException();

  return map;
}

public InstanceHandler setMap(HashMap<String, Object> map) {
  this.map = map;
  return this;
}

public boolean isInitiated() {
  return initiated;
}

public InstanceHandler setInitiated(boolean initiated) {
  this.initiated = initiated;
  return this;
}

public @NotNull String getPath() throws NotInitiatedException {
  if (path == null) throw new NotInitiatedException();

  return path;
}

public InstanceHandler setPath(String path) {
  this.path = path;
  return this;
}

public @NotNull Class<?> getClazz() throws NotInitiatedException {
  if (path == null) throw new NotInitiatedException();

  return clazz;
}

public InstanceHandler setClazz(Class<?> clazz) {
  this.clazz = clazz;
  return this;
}
}
