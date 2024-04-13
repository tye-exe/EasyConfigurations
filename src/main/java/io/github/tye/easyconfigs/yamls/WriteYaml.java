package io.github.tye.easyconfigs.yamls;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.exceptions.BadYamlException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class WriteYaml extends ReadYaml {

/**
 Takes the given input steam & parses it into a yaml format.
 @param yamlInputStream The input stream containing the data of the yaml.
 @throws IOException If there was an error reading the input stream. */
public WriteYaml(@NotNull InputStream yamlInputStream) throws IOException, BadYamlException {
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
  ArrayList<NodeTuple> commentNodes = new ArrayList<>(parsedCommentYaml.getValue());

  for (String missingKey : missingKeys) {
    NodeTuple missingNode = fullYaml.getNodeTuple(missingKey);
    commentNodes.add(missingNode);
  }

  parsedCommentYaml.setValue(commentNodes);


  // Adds the missing nodes to this yaml.
  ArrayList<NodeTuple> nodes = new ArrayList<>(parsedYaml.getValue());

  for (String missingKey : missingKeys) {
    NodeTuple missingNode = fullYaml.getNodeTuple(missingKey);
    nodes.add(missingNode);
  }

  parsedYaml.setValue(nodes);

  // Recalculates the new keys.
  keys = getKeys(parsedYaml);
}

/**
 Gets the keys that the given yaml has, but this yaml doesn't.
 @param fullYaml The given yaml to get the missing keys from.
 @return The keys that are in the given yaml, but aren't in this yaml. */
private @NotNull ArrayList<String> getMissingKeys(@NotNull WriteYaml fullYaml) {
  ArrayList<String> missingKeys = new ArrayList<>();


  yamlLoop:
  for (String key : fullYaml.keys.keySet()) {
    if (keys.containsKey(key)) continue;

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
  parsedCommentYaml = setValueRecursive(key, parsedCommentYaml, value);
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
}
