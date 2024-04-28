package io.github.tye.easyconfigs.yamls;

import io.github.tye.easyconfigs.ClassName;
import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.instances.Instance;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.logger.LogType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.*;

import static io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger.logger;

/**
 This class is for reading values from <a href="https://yaml.org/">yaml</a> data.
 <p>
 If you want to read &amp; write yaml data use {@link WriteYaml}. */
@InternalUse
public class ReadYaml {


/**
 Contains the parser for the comment yaml. */
@InternalUse
protected static final @NotNull Yaml commentYaml;

// Initializes the comment yaml parser.
static {
  // Parses the Yaml with the comments.
  LoaderOptions loaderOptions = new LoaderOptions();
  loaderOptions = loaderOptions.setProcessComments(true);

  DumperOptions dumperOptions = new DumperOptions();
  dumperOptions.setProcessComments(true);

  commentYaml = new Yaml(new SafeConstructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
}

/**
 Contains the parsed yaml with comments. */
@InternalUse
protected @NotNull MappingNode parsedYaml;

/**
 Contains the processed values of the {@link #parsedYaml}. This purpose of this object is like that
 of a cache.
 <p>
 The keys in the HashMap are the raw keys that lead to their corresponding value within the yaml.
 <p>
 The values in the HashMap are instances of {@link Value}. See those javadocs for more info. */
@SuppressWarnings("NotNullFieldNotInitialized")
// It is initialized within a method in the constructor.
@InternalUse
protected @NotNull HashMap<String, Value<?>> yamlMap;


/**
 Small wrapper class to hold the index route to the value in the {@link #parsedYaml} &amp; once
 computed, the value instantiated as the correct class.
 @param <T> The class of the parsed value. */
@InternalUse
protected static class Value<T> {
  public @NotNull List<Integer> yamlIndexPath;
  public @NotNull T parsedValue;

  public Value(@NotNull List<Integer> yamlIndexPath, @NotNull T parsedValue) {
    this.yamlIndexPath = yamlIndexPath;
    this.parsedValue = parsedValue;
  }
}


/**
 Tests if a file is a valid yaml file.
 @param yamlInputStream An inputStream containing the data of the yaml file.
 @return True if the given inputStream contains a valid yaml file. Otherwise, false is returned.
 @throws NullPointerException If the given input stream was null. */
@InternalUse
public static boolean isValidYaml(@NotNull InputStream yamlInputStream) throws NullPointerException {
  NullCheck.notNull(yamlInputStream, "yaml input stream");

  try {
    Node composed = commentYaml.compose(new InputStreamReader(yamlInputStream));
    // If it's not a mapping node then it's not counted as valid.
    // This also acts as a null check.
    return composed instanceof MappingNode;
  }
  catch (Exception ignore) {
    return false;
  }
}


/**
 Creates an immutable yaml representation of the given yamlInputStream.
 @param yamlInputStream The input stream containing the data of the yaml.
 @throws IOException            If there was an error reading the input stream.
 @throws ConfigurationException If there was an error parsing the given inputStream as a yaml. */
@SuppressWarnings("ConstantValue")
// Safeguards against if a null value was somehow given.
@InternalUse
public ReadYaml(@NotNull InputStream yamlInputStream) throws IOException, ConfigurationException {
  if (yamlInputStream == null) throw new IOException(Lang.notNull("yamlInputStream"));

  Node composed;
  try {
    // Parses the Yaml with the comments.
    composed = commentYaml.compose(new InputStreamReader(yamlInputStream));
  }
  catch (Exception e) {
    // If there was an error / runtime error parsing the yaml data then format it into an exception.
    throw new ConfigurationException(Lang.errorWhileParsingYaml(), e.getCause());
  }

  // If the returned value is null or not a MappingNode, then that should also be counted as an error.
  if (!(composed instanceof MappingNode)) {
    throw new ConfigurationException(Lang.errorWhileParsingYaml());
  }

  parsedYaml = (MappingNode) composed;

  // Computes a HashMap representation of the yaml.
  createMap();

  // If the yaml contains any null values throw a ConfigurationException.
  nullCheck();
}


/**
 Computes a HashMap representation of the {@link #parsedYaml}.
 @see #yamlMap */
@InternalUse
protected void createMap() {
  yamlMap = getMapRecursive(parsedYaml, "", new ArrayList<>());
}

/**
 Computes a HashMap representation of the {@link #parsedYaml}.
 <p>
 <strong>This method should not be used outside itself or {@link #createMap()}</strong>
 @param rootNode   The given mapping node to get the keys from relatively.
 @param currentKey The current string key path that leads to the given mapping node.
 @param indexPath  Contains a list the stores the path of indexes to take to get to the value of the
 key.
 @return A {@link HashMap} containing the yaml keys in the keys &amp; the values containing a list
 with the indexes to take to get to the keys in the yaml node.
 @see #yamlMap */
@InternalUse
private @NotNull HashMap<String, Value<?>> getMapRecursive(@NotNull MappingNode rootNode, @NotNull String currentKey, @NotNull ArrayList<Integer> indexPath) {
  HashMap<String, Value<?>> map = new HashMap<>();

  List<NodeTuple> value = rootNode.getValue();
  for (int index = 0; index < value.size(); index++) {
    // Create the key to this node by combining the key of this node & the key path to this node.
    String nodeKey = currentKey + getNodeKey(value.get(index));
    Node valueNode = value.get(index).getValueNode();

    // Can be suppressed since the copy will return the same class.
    @SuppressWarnings("unchecked") ArrayList<Integer> indexPathClone = (ArrayList<Integer>) indexPath.clone();
    indexPathClone.add(index);

    // If the node is a MappingNode then it contains sub-keys.
    if (valueNode instanceof MappingNode) {
      map.putAll((getMapRecursive((MappingNode) valueNode, nodeKey + ".", indexPathClone)));
      continue;
    }

    // The only other types of nodes used to store a yaml are "ScalarNode"s & "SequenceNode"s.
    // Both nodes have no sub-keys.
    Object unparsedValue = getNodeValue(valueNode);
    map.put(nodeKey, new Value<>(indexPathClone, unparsedValue));
  }

  return map;
}

/**
 Gets the key from the given {@link NodeTuple}
 @param node The node to get the key from.
 @return The key from the node.
 @throws NullPointerException If the given node was null. */
@InternalUse
protected @NotNull String getNodeKey(@NotNull NodeTuple node) throws NullPointerException {
  NullCheck.notNull(node, "node");
  Node keyNode = node.getKeyNode();
  return ((ScalarNode) keyNode).getValue();
}

/**
 Gets the raw value from given node the node.
 <p>
 The given node must be either a {@link SequenceNode} or {@link ScalarNode}.
 @param valueNode The node to get the value of.
 @return A string or List of strings that is the value of the given node. */
@InternalUse
protected static @NotNull Object getNodeValue(@NotNull Node valueNode) {
  Object unparsedValue;

  // If a node is a ScalarNode then it only has one value.
  if (valueNode instanceof ScalarNode) {
    ScalarNode scalarNode = (ScalarNode) valueNode;
    unparsedValue = scalarNode.getValue();
  }
  // Else it is a SequenceNode, which has multiple values.
  else {
    SequenceNode sequenceNode = (SequenceNode) valueNode;
    List<Node> nodeValues = sequenceNode.getValue();

    ArrayList<String> values = new ArrayList<>(nodeValues.size());

    // Gets the string value from the nodes
    for (Node scalarNode : nodeValues) {
      // All value nodes should be ScalarNodes.
      // This is because they only contain values, not keys.
      if (!(scalarNode instanceof ScalarNode)) continue;

      ScalarNode scalarValue = (ScalarNode) scalarNode;
      values.add(scalarValue.getValue());
    }

    unparsedValue = values;
  }

  return unparsedValue;
}


/**
 Gets the value at the given key as it's specified class from the parsed yaml.
 @param key The given key to get the value at.
 @return The value at the given key as it's specified class. If the key isn't in the parsed yaml,
 then null is returned. */
@InternalUse
public @Nullable Object getValue(@NotNull String key) {
  if (!yamlMap.containsKey(key)) return null;
  return yamlMap.get(key).parsedValue;
}

/**
 Returns a Set view of the keys contained in this yaml. The set is backed by the yaml, so changes to
 the map are reflected in the set, and vice-versa. If the yaml is modified while an iteration over
 the set is in progress (except through the iterator's own remove operation), the results of the
 iteration are undefined. The set supports element removal, which removes the corresponding keys from
 the yaml, via the Iterator.remove, Set.remove, removeAll, retainAll, and clear operations. It does
 not support the add or addAll operations. */
@InternalUse
public Set<String> getKeys() {
  return yamlMap.keySet();
}


/**
 Tests if another yaml has the same keys &amp; values as this yaml.
 <p>
 To test if only the keys are the same use {@link #equals(Object)}.
 @param otherYaml The other yaml to check the keys against.
 @return True if both yamls have the same keys &amp; values. Otherwise, false is returned. */
@InternalUse
public boolean identical(@Nullable Object otherYaml) {
  if (this == otherYaml) return true;
  if (!(otherYaml instanceof ReadYaml)) return false;

  ReadYaml identicalYaml = (ReadYaml) otherYaml;
  return this.getKeys().equals(identicalYaml.getKeys());
}

/**
 Tests if another yaml has the same keys as this yaml.
 <p>
 To test if both the keys &amp; values are the same use {@link #identical(Object)}.
 @param otherYaml The other yaml to check the keys against.
 @return True if both yamls have the same keys. Otherwise, false is returned. */
@InternalUse
@Override
public boolean equals(@Nullable Object otherYaml) {
  if (this == otherYaml) return true;
  if (!(otherYaml instanceof ReadYaml)) return false;

  ReadYaml equalYaml = (ReadYaml) otherYaml;
  return this.getKeys().equals(equalYaml.getKeys());
}

/**
 @see Object#hashCode() */
@Override
public int hashCode() {
  return Objects.hash(parsedYaml);
}

/**
 Gets the string representation of the stored comment yaml, this representation is formatted &amp;
 includes comments.
 @return The formatted string that represents the current yaml.
 @throws IOException If there was an error converting the yaml into a string. */
@InternalUse
public @NotNull String getYaml() throws IOException {
  // Sets the emitter to parse comments.
  DumperOptions dumperOptions = new DumperOptions();
  dumperOptions.setProcessComments(true);

  StringWriter stream = new StringWriter();
  Emitter emitter = new Emitter(stream, dumperOptions);

  for (Event event : commentYaml.serialize(parsedYaml)) {
    emitter.emit(event);
  }

  return stream.toString();
}

/**
 Gets the string representation of the comment yaml.
 <p>
 This method is only useful for debuggers. To handle the string yaml properly use
 {@link #getYaml()}.
 @return The comment yaml as a formatted string. If there was an error converting the yaml to a
 string, an empty string will be returned. */
@InternalUse
@Override
public @NotNull String toString() {
  try {
    return getYaml();
  }
  catch (IOException ignore) {
    return "";
  }
}

/**
 Checks if this yaml contains any null values.
 @throws ConfigurationException If the yaml contains any null values. */
@InternalUse
public void nullCheck() throws ConfigurationException {
  for (String key : getKeys()) {
    // Gets the node
    NodeTuple nodeTuple = getNodeTuple(key);
    if (nodeTuple == null) continue;

    // Checks if the node is a null type
    Node valueNode = nodeTuple.getValueNode();
    if (!valueNode.getTag().getClassName().equals("null")) continue;

    throw new ConfigurationException(Lang.nullInYaml(key));
  }
}

/**
 Gets the {@link NodeTuple} based upon its key path relative to the root node of this yaml.
 @param key The key of the NodeTuple relative to the root node.
 @return The NodeTuple at the given key path. If there is no node at the given path then null will be
 returned. */
@InternalUse
protected @Nullable NodeTuple getNodeTuple(@NotNull String key) {
  // If the node doesn't exist return null.
  if (!yamlMap.containsKey(key)) return null;

  List<Integer> path = yamlMap.get(key).yamlIndexPath;
  // The path shouldn't be empty
  if (path.isEmpty()) return null;

  // Follows the pre-computed path to the nodeTuple
  NodeTuple nodeTuple = parsedYaml.getValue().get(path.get(0));
  for (int i = 1; i < path.size(); i++) {
    Integer index = path.get(i);

    MappingNode mapNode = (MappingNode) nodeTuple.getValueNode();
    nodeTuple = mapNode.getValue().get(index);
  }

  return nodeTuple;
}


/**
 Outputs a warning message to the logger if the given yaml contains keys that aren't used by the
 given internal instance.
 @param clazz The internal instance.
 @param path  The path to the internal instance, this is purely used for logging purposes. */
@InternalUse
public void warnUnusedKeys(@NotNull Class<? extends Instance> clazz, @NotNull String path) {
  // Checks if any default values in the file are missing from the enum.
  for (String yamlPath : this.getKeys()) {

    // Checks if the enum contains the key outlined in the file.
    boolean contains = false;

    for (Instance instance : clazz.getEnumConstants()) {
      if (!yamlPath.equals(instance.getYamlPath())) continue;

      contains = true;
      break;
    }

    // Logs a warning if there's an unused path.
    if (contains) continue;

    logger.log(LogType.INTERNAL_UNUSED_PATH, Lang.unusedYamlPath(yamlPath, path));
  }
}


/**
 Parses the values within the yaml to the classes specified by the given yamlEnum.
 @param yamlEnum     The enum that corresponds to the parsed yaml.
 @param resourcePath The path to the parsed file. (only used for logging purposes)
 @throws ConfigurationException If:
 <p>
 - There is a key in the yaml enum that isn't in the parsed
 yaml.
 <p>
 - The yaml enum has marked a value as a class
 EasyConfigurations doesn't support.
 <p>
 - A value can't be parsed as the class it is marked as in the
 yaml enum. */
@InternalUse
public void parseValues(@NotNull Class<? extends Instance> yamlEnum, @NotNull String resourcePath) throws ConfigurationException {
  Instance[] enums = yamlEnum.getEnumConstants();

  for (Instance readingInstanceEnum : enums) {

    // Checks if the value exists in the default file.
    String keyPath = readingInstanceEnum.getYamlPath();
    if (!yamlMap.containsKey(keyPath)) {
      throw new ConfigurationException(Lang.notInDefaultYaml(keyPath, resourcePath));
    }

    // Checks if the class is one supported by EasyConfigurations.
    if (!SupportedClasses.existsAsEnum(readingInstanceEnum.getAssingedClass())) {
      String className = ClassName.getName(readingInstanceEnum.getAssingedClass());
      throw new ConfigurationException(Lang.classNotSupported(className));
    }

    SupportedClasses enumRepresentation = SupportedClasses.getAsEnum(readingInstanceEnum.getAssingedClass());

    // Checks if the value can be parsed as its intended class.
    Object rawValue = yamlMap.get(keyPath).parsedValue;
    boolean canParse = enumRepresentation.canParse(rawValue);
    if (!canParse) {
      throw new ConfigurationException(Lang.notAssignedClass(keyPath, resourcePath, rawValue.getClass(), readingInstanceEnum.getAssingedClass().getName()));
    }

    // Parses the value as it's intended class & replaces it within that HashMap.
    Object parsedValue = enumRepresentation.parse(rawValue);
    List<Integer> yamlIndexPath = yamlMap.get(keyPath).yamlIndexPath;
    yamlMap.put(keyPath, new Value<>(yamlIndexPath, parsedValue));
  }
}
}
