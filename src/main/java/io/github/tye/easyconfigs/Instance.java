package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.NotInitiatedException;
import io.github.tye.easyconfigs.instances.BaseInstance;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Instance {

/**
 The class the object stored in the Yaml should be parsed as. */
@InternalUse
@NotNull
public static final HashMap<BaseInstance, Class<?>> assignedClass = new HashMap<>();
/**
 The path to parse the object from in the Yaml. */
@InternalUse
@NotNull
public static final HashMap<BaseInstance, String> yamlPath = new HashMap<>();


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


public Instance() {
  this.map = null;
  this.initiated = false;
  this.path = null;
  this.clazz = null;
}


public @NotNull HashMap<String, Object> getMap() throws NotInitiatedException {
  if (map == null) throw new NotInitiatedException();

  return map;
}

public Instance setMap(HashMap<String, Object> map) {
  this.map = map;
  return this;
}

public boolean isInitiated() {
  return initiated;
}

public Instance setInitiated(boolean initiated) {
  this.initiated = initiated;
  return this;
}

public @NotNull String getPath() throws NotInitiatedException {
  if (path == null) throw new NotInitiatedException();

  return path;
}

public Instance setPath(String path) {
  this.path = path;
  return this;
}

public @NotNull Class<?> getClazz() throws NotInitiatedException {
  if (path == null) throw new NotInitiatedException();

  return clazz;
}

public Instance setClazz(Class<?> clazz) {
  this.clazz = clazz;
  return this;
}
}
