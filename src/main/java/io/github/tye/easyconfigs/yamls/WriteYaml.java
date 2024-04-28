package io.github.tye.easyconfigs.yamls;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.instances.Instance;
import io.github.tye.easyconfigs.instances.persistent.PersistentInstance;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.logger.LogType;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger.logger;

/**
 This class is for reading &amp; writing values from <a href="https://yaml.org/">yaml</a> data. */
public class WriteYaml extends ReadYaml {

/**
 Takes the given input steam &amp; parses it into a yaml format.
 @param internalPath The path to the internal file the yaml data is read from.
 @param externalFile The external file the yaml data is read from.
 @param yamlEnum     The enum that represents the yaml file.
 @throws IOException            If there was an error reading the input stream.
 @throws ConfigurationException If there was an error in the yamls.
 @throws NullPointerException   If either of the given input streams are null. */
public WriteYaml(@NotNull String internalPath, @NotNull File externalFile, @NotNull Class<? extends Instance> yamlEnum) throws IOException, ConfigurationException, NullPointerException {
  // Should never throw an exception as the file is checked if it is valid before this constructor is called.
  super(Files.newInputStream(externalFile.toPath()));

  try (InputStream internalInputStream = yamlEnum.getResourceAsStream(internalPath)) {
    if (internalInputStream == null) throw new IOException(Lang.configNotReadable(internalPath));

    ReadYaml internalYaml = new ReadYaml(internalInputStream);
    internalYaml.warnUnusedKeys(yamlEnum, internalPath);
    removeUnusedKeys(internalYaml, externalFile);
    addMissingKeys(internalYaml, externalFile);
  }
}

/**
 Removes any keys from this yaml that aren't in the given default yaml.
 @param defaultYaml  The default yaml to check keys against.
 @param externalFile The external file that is having the keys removed. (This is purely for logging
 purposes). */
@InternalUse
private void removeUnusedKeys(@NotNull ReadYaml defaultYaml, @NotNull File externalFile) {
  if (this.equals(defaultYaml)) return;

  ArrayList<String> toRemove = new ArrayList<>();

  for (String key : this.getKeys()) {
    if (defaultYaml.yamlMap.containsKey(key)) continue;

    toRemove.add(key);
    // Logs that a key was removed
    logger.log(LogType.EXTERNAL_UNUSED_PATH, Lang.removingExternalUnusedKey(externalFile.getPath(), key));
  }

  // Performed in separate loop to avoid concurrent modification.
  for (String key : toRemove) {
    yamlMap.remove(key);
  }

  parsedYaml = removeKeysRecursive(parsedYaml, "", toRemove);
}


/**
 Removes keys from the nested yaml data that represents the parsed yaml.
 @param rootNode     The root node to remove keys relative to.
 @param currentKey   The current key leading to the root node, from the initial root node.
 @param keysToRemove The list of keys that will be removed from the yaml.
 @return The new yaml data with the specified keys removed. */
@InternalUse
private @NotNull MappingNode removeKeysRecursive(@NotNull MappingNode rootNode, @NotNull String currentKey, @NotNull ArrayList<String> keysToRemove) {
  ArrayList<NodeTuple> newValues = new ArrayList<>();

  for (NodeTuple currentTuple : rootNode.getValue()) {
    // Create the key to this node by combining the key of this node & the key path to this node.
    String nodeKey = currentKey + getNodeKey(currentTuple);
    Node nodeValue = currentTuple.getValueNode();

    // If the node is a MappingNode then it contains sub-keys.
    if (nodeValue instanceof MappingNode) {
      NodeTuple nodeTuple = new NodeTuple(
          currentTuple.getKeyNode(),
          removeKeysRecursive((MappingNode) nodeValue, nodeKey + ".", keysToRemove));

      newValues.add(nodeTuple);
      continue;
    }

    // If it's a node to remove then don't add it.
    if (keysToRemove.contains(nodeKey)) continue;

    newValues.add(currentTuple);

  }

  rootNode.setValue(newValues);
  return rootNode;
}


/**
 Adds any missing keys from the given yaml to this yaml.
 <p>
 This method doesn't add any missing comments. The only comments that will be added are those that
 belong to a missing key.
 @param fullYaml     The yaml to get the keys from.
 @param externalFile The external file that is having the keys added. (This is purely for logging
 purposes). */
private void addMissingKeys(@NotNull ReadYaml fullYaml, @NotNull File externalFile) {
  if (this.equals(fullYaml)) return;

  ArrayList<String> keys = new ArrayList<>();
  ArrayList<Object> values = new ArrayList<>();
  ArrayList<Node> nodeValues = new ArrayList<>();

  for (String key : fullYaml.getKeys()) {
    if (yamlMap.containsKey(key)) continue;

    keys.add(key);
    values.add(fullYaml.yamlMap.get(key).parsedValue);

    // Gets the node containing the value from the full yaml.
    NodeTuple nodeTuple = fullYaml.getNodeTuple(key);
    NullCheck.notNull(nodeTuple, "node tuple");
    nodeValues.add(nodeTuple.getValueNode());
  }

  for (int i = 0; i < keys.size(); i++) {
    String key = keys.get(i);
    Object value = values.get(i);
    Node tuple = nodeValues.get(i);

    parsedYaml = addKeysRecursive(parsedYaml, "", key, value, tuple, new ArrayList<>());

    // Logs that a missing key was added.
    logger.log(LogType.EXTERNAL_MISSING_PATH, Lang.addingExternalMissingKey(externalFile.getPath(), key));
  }
}

/**
 Adds the given key &amp; it's given value to the given root node.
 <p>
 This method does not support replacing existing keys.
 @param rootNode        The root node to add keys relative to.
 @param currentKey      The current key leading to the root node, from the initial root node.
 @param key             The key being added to the yaml.
 @param value           The value being added to the yaml, as its intended class.
 @param copiedNodeValue The value node from the internal yaml that will be added to the root node.
 @param indexPath       The index path to the new key.
 @return The root node with the given key &amp; value added. */
private @NotNull MappingNode addKeysRecursive(@NotNull MappingNode rootNode, @NotNull String currentKey, @NotNull String key, @NotNull Object value, @NotNull Node copiedNodeValue, @NotNull ArrayList<Integer> indexPath) {
  ArrayList<NodeTuple> newValues = new ArrayList<>();
  boolean noneMatching = true;

  List<NodeTuple> rootNodeValue = rootNode.getValue();
  for (int index = 0; index < rootNodeValue.size(); index++) {
    NodeTuple currentTuple = rootNodeValue.get(index);

    // Create the key to this node by combining the key of this node & the key path to this node.
    String nodeKey = currentKey + getNodeKey(currentTuple);
    Node nodeValue = currentTuple.getValueNode();

    // The fullstop is added to ensure it doesn't match part of a key.
    if (!key.startsWith(nodeKey + ".")) {
      newValues.add(currentTuple);
      continue;
    }

    // One node key matched so don't add the new value on this "layer".
    noneMatching = false;

    // If the node is a MappingNode then it contains sub-keys.
    if (!(nodeValue instanceof MappingNode)) continue;

    // Can be suppressed since the copy will return the same class.
    @SuppressWarnings("unchecked") ArrayList<Integer> indexPathClone = (ArrayList<Integer>) indexPath.clone();
    indexPathClone.add(index);

    NodeTuple nodeTuple = new NodeTuple(
        currentTuple.getKeyNode(),
        addKeysRecursive((MappingNode) nodeValue, nodeKey + ".", key, value, nodeValue, indexPathClone));

    newValues.add(nodeTuple);
  }


  if (noneMatching) {
    // Calculates the new key for the node.
    String tupleKey = key.substring(currentKey.length());
    ScalarNode keyNode = new ScalarNode(Tag.STR, tupleKey, null, null, DumperOptions.ScalarStyle.PLAIN);

    newValues.add(new NodeTuple(keyNode, copiedNodeValue));

    indexPath.add(newValues.size());
    yamlMap.put(key, new Value<>(indexPath, value));
  }

  rootNode.setValue(newValues);
  return rootNode;
}


/**
 Replaces the value at the given key with the given value. The replacement value needs to be an
 instance of the class it is marked as.
 @param key              The key to replace the value at.
 @param replacementValue The new value of the key. */
@InternalUse
public void replaceValue(@NotNull String key, @NotNull Object replacementValue) {
  if (!yamlMap.containsKey(key)) return;

  Value<?> value = yamlMap.get(key);
  replaceMapValueRecursive(parsedYaml, new ArrayList<>(value.yamlIndexPath), replacementValue);

  yamlMap.put(key, new Value<>(value.yamlIndexPath, replacementValue));
}


/**
 Sets the value of an existing key to the new value.
 @param rootNode     The node to replace the value in.
 @param indexPath    The path to the value being replaced.
 @param replaceValue The value to override the old value with.
 @return The root node with the specified value replaced. */
@InternalUse
private @NotNull MappingNode replaceMapValueRecursive(@NotNull MappingNode rootNode, @NotNull ArrayList<Integer> indexPath, @NotNull Object replaceValue) {
  // The list will always have elements to remove, as recursion only occurs on mapping nodes,
  // and the list is a path though the mapping nodes.
  Integer nextIndex = indexPath.remove(0);

  List<NodeTuple> nodes = rootNode.getValue();
  NodeTuple pathTuple = nodes.get(nextIndex);
  Node pathValue = pathTuple.getValueNode();

  if (pathValue instanceof MappingNode) {
    return replaceMapValueRecursive((MappingNode) pathValue, indexPath, replaceValue);
  }

  // Non-recursive section //

  ArrayList<NodeTuple> newTuples = new ArrayList<>();
  for (int i = 0; i < nodes.size(); i++) {
    NodeTuple oldTuple = nodes.get(i);

    // If it isn't the value to replace don't change it.
    if (i != nextIndex) {
      newTuples.add(oldTuple);
      continue;
    }


    // If it's a scalar node then just replace the value.
    if (pathValue instanceof ScalarNode) {
      ScalarNode value = (ScalarNode) pathValue;

      ScalarNode newValue = new ScalarNode(
          value.getTag(),
          replaceValue.toString(),
          value.getStartMark(),
          value.getEndMark(),
          value.getScalarStyle()
      );

      newTuples.add(new NodeTuple(oldTuple.getKeyNode(), newValue));
      continue;
    }


    if (pathValue instanceof SequenceNode) {
      SequenceNode value = (SequenceNode) pathValue;

      // Create Scalar nodes from the replacement values.
      ArrayList<Node> newNodes = new ArrayList<>();
      for (String replacingValue : toStringList(replaceValue)) {

        ScalarNode newNode = new ScalarNode(
            Tag.STR, // Write the value as a string.
            replacingValue,
            null,
            null,
            DumperOptions.ScalarStyle.DOUBLE_QUOTED
        );

        newNodes.add(newNode);
      }

      // Create a new Sequence node with the new elements.
      SequenceNode newValue = new SequenceNode(
          value.getTag(),
          newNodes,
          value.getFlowStyle()
      );

      newTuples.add(new NodeTuple(oldTuple.getKeyNode(), newValue));
    }
  }

  // Update the content of the root node with the modified value.
  rootNode.setValue(newTuples);
  return rootNode;
}

/**
 Gets the string representation of all the values in a list.
 @param list The list to get the string representation of the values from.
 @return A list containing the string representation of the values. */
private static @NotNull List<String> toStringList(@NotNull Object list) {
  ArrayList<String> stringList = new ArrayList<>();

  for (Object value : (List<?>) list) {
    stringList.add(value.toString());
  }

  return stringList;
}


/**
 Parses the values within the yaml to the classes specified by the given yamlEnum.
 <p>
 If a value can't be parsed as the class it is marked as in the yaml enum, then a warning is output
 to the logger &amp; the value inside the internal yaml is used as a fallback.
 @param yamlEnum     The enum that corresponds to the parsed yaml.
 @param externalYaml The path to the parsed file. (only used for logging purposes) */
public void parseValues(@NotNull Class<? extends PersistentInstance> yamlEnum, @NotNull String internalYaml, @NotNull String externalYaml) throws ConfigurationException {
  // Parses the values from the default yaml first.
  parseValues(yamlEnum, internalYaml);

  PersistentInstance[] enums = yamlEnum.getEnumConstants();
  for (PersistentInstance instanceEnum : enums) {
    String keyPath = instanceEnum.getYamlPath();

    // An error should never be thrown from this as parsing the default values would throw it.
    SupportedClasses enumRepresentation = SupportedClasses.getAsEnum(instanceEnum.getAssingedClass());

    // Checks if the value can be parsed as its intended class.
    Object rawValue = yamlMap.get(keyPath).parsedValue;
    boolean canParse = enumRepresentation.canParse(rawValue);
    // If it can't then leave the default value parsed from the internal yaml.
    if (!canParse) {
      logger.log(LogType.USING_FALLBACK_VALUE, Lang.invalidExternalKey(keyPath, externalYaml, rawValue.getClass()));
      continue;
    }

    // Replaces the value from the internal yaml with the one from the external yaml.
    Object parsedValue = enumRepresentation.parse(rawValue);
    List<Integer> yamlIndexPath = yamlMap.get(keyPath).yamlIndexPath;
    yamlMap.put(keyPath, new Value<>(yamlIndexPath, parsedValue));
  }
}
}
