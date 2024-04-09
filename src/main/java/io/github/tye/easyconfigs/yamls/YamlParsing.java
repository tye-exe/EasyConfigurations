package io.github.tye.easyconfigs.yamls;

import io.github.tye.easyconfigs.exceptions.BadYamlError;
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

import java.io.*;
import java.util.*;

import static io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger.logger;

public class YamlParsing {

/**
 Contains the parsed yaml without comments. */
private final @NotNull MappingNode parsedYaml;

/**
 Contains the parsed yaml with comments. */
private final @NotNull MappingNode parsedCommentYaml;

/**
 Contains the keys in the yaml. With each key being stored as the full yaml path. E.g.
 "test.first.thingy" */
private @NotNull HashSet<String> keys;

/**
 Contains the parser for the comment yaml. */
private static final @NotNull Yaml commentYaml;

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
 Takes the given input steam & parses it into a yaml format.
 @param yamlInputStream The input stream containing the data of the yaml.
 @throws IOException If there was an error reading the input stream. */
public YamlParsing(@NotNull InputStream yamlInputStream) throws IOException, BadYamlError {
  if (yamlInputStream == null) throw new IOException(Lang.notNull("yamlInputStream"));

  // Reads the data from the given input stream & saves it.
  // This allows two identical streams to be given to both Yaml parsers.
  ArrayList<Byte> byteBuffer = new ArrayList<>();

  byte read;
  while (-1 != (read = (byte) yamlInputStream.read())) {
    byteBuffer.add(read);
  }

  byte[] bytes = new byte[byteBuffer.size()];
  for (int i = 0; i < byteBuffer.size(); i++) {
    bytes[i] = byteBuffer.get(i);
  }

  try {
    // Parses the Yaml without the comments.
    Yaml yaml = new Yaml();
    parsedYaml = (MappingNode) yaml.compose(new InputStreamReader(new ByteArrayInputStream(bytes)));

    // Parses the Yaml with the comments.
    parsedCommentYaml = (MappingNode) commentYaml.compose(new InputStreamReader(new ByteArrayInputStream(bytes)));

    // If there was an error / runtime error parsing the yaml data then format it into an exception.
  }
  catch (Exception e) {
    throw new BadYamlError(Lang.errorWhileParsingYaml(), e.getCause());
  }

  // Gets the keys from the yaml
  keys = getKeysRecursive(parsedYaml, "");
}


/**
 Adds any missing keys from the given yaml to this yaml.
 <p>
 This method doesn't add any missing comments. The only comments that will be added are those that
 belong to a missing key.
 @param fullYaml The yaml to get the keys from. */
public void addMissingKeys(@NotNull YamlParsing fullYaml) {
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
  keys = getKeysRecursive(parsedYaml, "");
}

/**
 Gets the keys that the given yaml has, but this yaml doesn't.
 @param fullYaml The given yaml to get the missing keys from.
 @return The keys that are in the given yaml, but aren't in this yaml. */
private @NotNull ArrayList<String> getMissingKeys(@NotNull YamlParsing fullYaml) {
  ArrayList<String> missingKeys = new ArrayList<>();

  yamlLoop:
  for (String key : fullYaml.keys) {
    if (keys.contains(key)) continue;

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
 Gets the {@link NodeTuple} based upon its key path relative to the root node of this yaml.
 @param key The key of the NodeTuple relative to the root node.
 @return The NodeTuple at the given key path. If there is no node at the given path then null will be
 returned. */
private @Nullable NodeTuple getNodeTuple(@NotNull String key) {
  // If the node doesn't exist return null.
  if (!keys.contains(key)) return null;

  // Splits the key into its fragment (the words between the ".").
  String[] fragments = key.split("\\.");
  int fragmentIndex = 0;

  // Iterates over the nodes that contain the current fragment of the given key.
  // The list being iterated over will be changed to the sub-nodes of the node that matches
  // the key fragment, once found.
  List<NodeTuple> nodes = parsedYaml.getValue();
  for (int i = 0; i < nodes.size(); i++) {

    // Checks the keys match
    NodeTuple node = nodes.get(i);
    if (!fragments[fragmentIndex].equals(getNodeKey(node))) continue;


    fragmentIndex++;
    if (fragmentIndex < fragments.length) {
      i = 0;

      // This node will always be a mapping node, since only mapping nodes can have key-fragments
      // and this code will only execute if there are more key-fragments to find.
      nodes = ((MappingNode) node.getValueNode()).getValue();
      continue;
    }


    return node;
  }

  return null;
}


/**
 Gets the string keys relatively from the given mapping node.
 <p>
 This method should only be called with an empty string for the currentKey unless you know what
 you’re doing.
 @param rootNode   The given mapping node to get the keys from relatively.
 @param currentKey The current string key path that leads to the given mapping node.
 @return A {@link HashSet} containing the string key paths, relative to the given mapping node. */
private static HashSet<String> getKeysRecursive(MappingNode rootNode, String currentKey) {
  HashSet<String> keys = new HashSet<>();

  for (NodeTuple nodeTuple : rootNode.getValue()) {
    // Create the key to this node by combining the key of this node & the key path to this node.
    String nodeKey = currentKey + getNodeKey(nodeTuple);

    Node valueNode = nodeTuple.getValueNode();

    // If the node is a MappingNode then it contains sub-keys.
    if (valueNode instanceof MappingNode) {
      keys.addAll(getKeysRecursive((MappingNode) valueNode, nodeKey + "."));
    }

    // The only other types of nodes used to store a yaml are "ScalarNode"s & "SequenceNode"s.
    // Both nodes have no sub-keys.
    keys.add(nodeKey);
  }

  return keys;
}

/**
 Parses the given mapping node & returns a representation of it as a {@link HashMap}.
 <p>
 The returned HashMap stores its keys in lists as one key can have multiple values. If a node only
 has one value then there will only be one value in a list.
 <p>
 This method should only be called with an empty string for the currentKey unless you know what
 you’re doing.
 @param rootNode   The given mapping node to parse into a HashMap.
 @param currentKey The current string key path that leads to the given mapping node.
 @return A HashMap that represents the given mapping node. */
private static HashMap<String, List<String>> getMapRecursive(MappingNode rootNode, String currentKey) {
  HashMap<String, List<String>> yamlMap = new HashMap<>();

  for (NodeTuple nodeTuple : rootNode.getValue()) {
    // Create the key to this node by combining the key of this node & the key path to this node.
    String nodeKey = currentKey + getNodeKey(nodeTuple);
    Node valueNode = nodeTuple.getValueNode();

    // If the node is a MappingNode then it contains sub-keys.
    if (valueNode instanceof MappingNode) {
      yamlMap.putAll(getMapRecursive((MappingNode) valueNode, nodeKey + "."));
    }

    // If a node is a ScalarNode then it only has one value.
    if (valueNode instanceof ScalarNode) {
      ScalarNode scalarNode = (ScalarNode) valueNode;
      String nodeValue = scalarNode.getValue();

      yamlMap.put(nodeKey, Collections.singletonList(nodeValue));
    }

    // If a node is a SequenceNode then it has multiple values.
    if (valueNode instanceof SequenceNode) {
      SequenceNode sequenceNode = (SequenceNode) valueNode;
      List<Node> nodeValues = sequenceNode.getValue();

      ArrayList<String> values = new ArrayList<>(nodeValues.size());

      // Gets the string value from the nodes
      for (Node value : nodeValues) {
        // All value nodes should be ScalarNodes.
        // This is because they only contain values, not keys.
        if (!(value instanceof ScalarNode)) continue;

        ScalarNode scalarValue = (ScalarNode) value;
        values.add(scalarValue.getValue());
      }

      yamlMap.put(nodeKey, values);
    }
  }

  return yamlMap;
}


/**
 Tests if another yaml has the same keys & values as this yaml.
 <p>
 To test if only the keys are the same use {@link #equals(Object)}.
 @param otherYaml The other yaml to check the keys against.
 @return True if both yamls have the same keys & values. Otherwise, false is returned. */
public boolean identical(Object otherYaml) {
  if (this == otherYaml) return true;
  if (otherYaml == null || getClass() != otherYaml.getClass()) return false;

  YamlParsing identicalYaml = (YamlParsing) otherYaml;
  return getMapRecursive(parsedYaml, "").equals(getMapRecursive(identicalYaml.parsedYaml, ""));
}

/**
 Tests if another yaml has the same keys as this yaml.
 <p>
 To test if both the keys & values are the same use {@link #identical(Object)}.
 @param otherYaml The other yaml to check the keys against.
 @return True if both yamls have the same keys. Otherwise, false is returned. */
@Override
public boolean equals(Object otherYaml) {
  if (this == otherYaml) return true;
  if (otherYaml == null || getClass() != otherYaml.getClass()) return false;

  YamlParsing equalYaml = (YamlParsing) otherYaml;
  return this.keys.equals(equalYaml.keys);
}

@Override
public int hashCode() {
  return Objects.hash(parsedYaml);
}


/**
 Gets the string representation of the stored comment yaml, this representation is formatted &
 includes comments.
 @return The formatted string that represents the current yaml.
 @throws IOException If there was an error converting the yaml into a string. */
public @NotNull String getYaml() throws IOException {
  // Sets the emitter to parse comments.
  DumperOptions dumperOptions = new DumperOptions();
  dumperOptions.setProcessComments(true);

  StringWriter stream = new StringWriter();
  Emitter emitter = new Emitter(stream, dumperOptions);

  for (Event event : commentYaml.serialize(parsedCommentYaml)) {
    emitter.emit(event);
  }

  return stream.toString();
}

/**
 Gets the string representation of the stored yaml, the output is formatted, but is
 <strong>without</strong> comments.
 @return The formatted string that represents the current yaml, <strong>without</strong> comments.
 @throws IOException If there was an error converting the yaml into a string. */
public @NotNull String getRawYaml() throws IOException {
  StringWriter stream = new StringWriter();
  Emitter emitter = new Emitter(stream, new DumperOptions());

  for (Event event : new Yaml().serialize(parsedYaml)) {
    emitter.emit(event);
  }

  return stream.toString();
}

/**
 Gets the key from the given {@link NodeTuple}
 @param node The node to get the key from.
 @return The key from the node. */
private static @NotNull String getNodeKey(NodeTuple node) {
  Node keyNode = node.getKeyNode();

  if (!(keyNode instanceof ScalarNode)) {
    logger.log(LogType.NOT_SCALAR_KEY_NODE, Lang.noneScalarNode(keyNode.toString()));
    return "";
  }

  return ((ScalarNode) keyNode).getValue();
}

/**
 Gets the string representation of the comment yaml.
 @return The comment yaml as a formatted string. If there was an error converting the yaml to a
 string, an empty string will be returned. */
@Override
public @NotNull String toString() {
  try {
    return getYaml();
  }
  catch (IOException ignore) {
    return "";
  }
}
}
