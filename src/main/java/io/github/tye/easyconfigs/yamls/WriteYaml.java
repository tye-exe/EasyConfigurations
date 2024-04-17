package io.github.tye.easyconfigs.yamls;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.exceptions.DefaultConfigurationException;
import io.github.tye.easyconfigs.instances.Instance;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import io.github.tye.easyconfigs.logger.LogType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger.logger;

public class WriteYaml extends ReadYaml {

/**
 Takes the given input steam & parses it into a yaml format.
 @param yamlInputStream The input stream containing the data of the yaml.
 @throws IOException If there was an error reading the input stream. */
public WriteYaml(@NotNull InputStream yamlInputStream) throws IOException, ConfigurationException {
  super(yamlInputStream);
}


/**
 Adds any missing keys from the given yaml to this yaml.
 <p>
 This method doesn't add any missing comments. The only comments that will be added are those that
 belong to a missing key.
 @param fullYaml The yaml to get the keys from. */
public void addMissingKeys(@NotNull WriteYaml fullYaml) {
  ArrayList<String> missingKeys = getMissingKeys(fullYaml);

  // Adds the missing nodes comment nodes to this yaml.
  ArrayList<NodeTuple> commentNodes = new ArrayList<>(parsedYaml.getValue());

  for (String missingKey : missingKeys) {
    NodeTuple missingNode = fullYaml.getNodeTuple(missingKey);
    commentNodes.add(missingNode);
  }

  parsedYaml.setValue(commentNodes);

  // Recalculates the new keys.
  createMap();
}

/**
 Gets the {@link NodeTuple} based upon its key path relative to the root node of this yaml.
 @param key The key of the NodeTuple relative to the root node.
 @return The NodeTuple at the given key path. If there is no node at the given path then null will be
 returned. */
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
 Gets the keys that the given yaml has, but this yaml doesn't.
 @param fullYaml The given yaml to get the missing keys from.
 @return The keys that are in the given yaml, but aren't in this yaml. */
private @NotNull ArrayList<String> getMissingKeys(@NotNull WriteYaml fullYaml) {
  ArrayList<String> missingKeys = new ArrayList<>();


  yamlLoop:
  for (String key : fullYaml.yamlMap.keySet()) {
    if (yamlMap.containsKey(key)) continue;

    for (int i = 0; i < missingKeys.size(); i++) {
      String missingKey = missingKeys.get(i);

      // If the key is a sub-key of a missing key, then don't add it.
      if (key.startsWith(missingKey)) {
        continue yamlLoop;
      }

      // If a missing key is a sub-key of this key then remove the sub-key.
      if (missingKey.startsWith(key)) {
        missingKeys.remove(i);
        i--;
      }
    }

    missingKeys.add(key);
  }
  return missingKeys;
}

/**
 Sets the given key to the given value within the yaml.
 @param key   The yaml key to set the value of.
 @param value The string representation of the object to set the value as.
 @throws NullPointerException If either the key or value is null. */
public void setValue(@NotNull String key, @NotNull String value) throws NullPointerException {
  NullCheck.notNull(key, "key");
  NullCheck.notNull(value, "value");

  parsedYaml = setValueRecursive(key, parsedYaml, value);
}

/**
 Sets the given key to the given value within the yaml.
 <p>
 <strong>This method should only be called by itself or {@link #setValue(String, String)}</strong>
 @param remainingKey A string containing a section of the key that needs to be matched by nodes
 within rootNode.
 @param rootNode     The node that contains the key &amp; value to set.
 @param value        The string value to set.
 @return A modified {@link MappingNode} that contains the newly set value. */
private @NotNull MappingNode setValueRecursive(@NotNull String remainingKey, @NotNull MappingNode rootNode, @NotNull String value) {
  // Stores the nodes that the root will have set as the value.
  ArrayList<NodeTuple> modifiedNodes = new ArrayList<>();

  // Iterates over the nodes that contain the current fragment of the key.
  List<NodeTuple> nodes = rootNode.getValue();
  for (int i = 0; i < nodes.size(); i++) {

    NodeTuple node = nodes.get(i);
    String nodeKey = getNodeKey(node);

    // Checks if any of the keys match
    if (!remainingKey.startsWith(nodeKey)) {
      modifiedNodes.add(node);
      continue;
    }

    // If the index is bigger than the length of the key then the full key has been reached.
    if (!remainingKey.contains("\\.")) {
      ScalarNode oldValue = (ScalarNode) node.getValueNode();

      ScalarNode newValue = new ScalarNode(
          oldValue.getTag(),
          value,
          // Even tho the marks contains line info in them, it's only used for debugging.
          oldValue.getStartMark(),
          oldValue.getEndMark(),
          oldValue.getScalarStyle()
      );

      modifiedNodes.add(new NodeTuple(node.getKeyNode(), newValue));
      continue;
    }

    // This node will always be a mapping node, since only mapping nodes can have key-fragments
    // and this code will only execute if there are more key-fragments to find.
    MappingNode nextNode = ((MappingNode) node.getValueNode());

    // Iterates over the modified rootNode
    i = 0;
    String subKey = remainingKey.substring(nodeKey.length());
    rootNode = setValueRecursive(subKey, nextNode, value);
  }

  rootNode.setValue(modifiedNodes);
  return rootNode;
}

/**
 Parses the values within the yaml to the classes specified by the given yamlEnum.
 <p>
 <p></p>
 If any of the following scenarios occur, then a warning is logged and values parsed from the default
 yaml are used as a fallback.
 <p>
 - There is a value in the parsed yaml that isn't in the yaml
 enum.
 <p>
 - A value can't be parsed as the class it is marked as in the
 yaml enum.
 @param yamlEnum     The enum that corresponds to the parsed yaml.
 @param resourcePath The path to the parsed file. (only used for logging purposes) */
@Override
public void parseValues(@NotNull Class<? extends Instance> yamlEnum, @NotNull String resourcePath) {
  Instance[] enums = yamlEnum.getEnumConstants();

  for (Instance instanceEnum : enums) {

    // Checks if the value exists in the file.
    String keyPath = instanceEnum.getYamlPath();
    if (!yamlMap.containsKey(keyPath)) {
      logger.log(LogType.EXTERNAL_UNUSED_PATH, Lang.notInDefaultYaml(keyPath, resourcePath));
      continue;
    }

    // Doesn't check if the class will be supported since it'll have been checked before.
    SupportedClasses enumRepresentation;
    try {
      enumRepresentation = SupportedClasses.getAsEnum(instanceEnum.getAssingedClass());
    }
    catch (DefaultConfigurationException ignore) {continue;}

    // Checks if the value can be parsed as its intended class.
    Object rawValue = yamlMap.get(keyPath).parsedValue;
    boolean canParse = enumRepresentation.canParse(rawValue);
    if (!canParse) {
      logger.log(
          LogType.USING_FALLBACK_VALUE,
          Lang.notAssignedClass(keyPath, resourcePath, rawValue.getClass(), instanceEnum.getAssingedClass().getName()));
      continue;
    }

    // Parses the value as it's intended class & replaces it within that HashMap.
    Object parsedValue = enumRepresentation.parse(rawValue);
    List<Integer> yamlIndexPath = yamlMap.get(keyPath).yamlIndexPath;
    yamlMap.put(keyPath, new Value<>(yamlIndexPath, parsedValue));
  }
}
}
