package io.github.tye.easyconfigs.yamls;

import io.github.tye.easyconfigs.NullCheck;
import io.github.tye.easyconfigs.exceptions.BadYamlException;
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

import static io.github.tye.easyconfigs.NullCheck.notNull;
import static io.github.tye.easyconfigs.logger.EasyConfigurationsDefaultLogger.logger;

public class ReadYaml {


/**
 Contains the parser for the comment yaml. */
protected static final @NotNull org.yaml.snakeyaml.Yaml commentYaml;

// Initializes the comment yaml parser.
static {
  // Parses the Yaml with the comments.
  LoaderOptions loaderOptions = new LoaderOptions();
  loaderOptions = loaderOptions.setProcessComments(true);
  DumperOptions dumperOptions = new DumperOptions();
  dumperOptions.setProcessComments(true);

  commentYaml = new org.yaml.snakeyaml.Yaml(new SafeConstructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
}


/**
 Contains the parsed yaml without comments. */
protected @NotNull MappingNode parsedYaml;
/**
 Contains the parsed yaml with comments. */
protected @NotNull MappingNode parsedCommentYaml;

/**
 Contains the keys in the yaml. With each key being stored as the full yaml path. E.g.
 "test.first.thingy" */
protected @NotNull HashMap<String, List<Integer>> keys;


/**
 Creates an immutable yaml representation of the given yamlInputStream.
 @param yamlInputStream The input stream containing the data of the yaml.
 @throws IOException If there was an error reading the input stream.
 @throws BadYamlException If there was an error parsing the given inputStream as a yaml. */
public ReadYaml(@NotNull InputStream yamlInputStream) throws IOException, BadYamlException {
  if (yamlInputStream == null) throw new IOException(Lang.notNull("yamlInputStream"));

  // Allows the stream to be duplicated
  byte[] bytes = streamToBytes(yamlInputStream);

  try {
    // Parses the Yaml without the comments.
    Yaml yaml = new Yaml();
    parsedYaml = (MappingNode) yaml.compose(new InputStreamReader(new ByteArrayInputStream(bytes)));

    // Parses the Yaml with the comments.
    parsedCommentYaml = (MappingNode) commentYaml.compose(new InputStreamReader(new ByteArrayInputStream(bytes)));

    // If there was an error / runtime error parsing the yaml data then format it into an exception.
  }
  catch (Exception e) {
    throw new BadYamlException(Lang.errorWhileParsingYaml(), e.getCause());
  }

  // Gets the keys from the yaml
  keys = getKeys(parsedYaml);
}


/**
 Reads the given input stream &amp; returns a byte array containing the content of the given stream.
 This allows a stream to be initiated multiple times from the byte array using {@link ByteArrayInputStream}
 * @param yamlInputStream The input stream to convert.
 * @return The bytes that make up the given input stream.
 * @throws IOException If there was an error reading data from the input stream.
 */
private static byte[] streamToBytes(@NotNull InputStream yamlInputStream) throws IOException {
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
  return bytes;
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
private @NotNull HashMap<String, List<String>> getMapRecursive(@NotNull MappingNode rootNode, @NotNull String currentKey) {
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
 Gets the key from the given {@link NodeTuple}
 @param node The node to get the key from.
 @return The key from the node. */
protected @NotNull String getNodeKey(@NotNull NodeTuple node) throws NullPointerException {
  NullCheck.notNull(node, "node");
  Node keyNode = node.getKeyNode();

  if (!(keyNode instanceof ScalarNode)) {
    logger.log(LogType.NOT_SCALAR_KEY_NODE, Lang.noneScalarNode(keyNode.toString()));
    return "";
  }

  return ((ScalarNode) keyNode).getValue();
}

/**
 Gets the unique key path to every sub-node within the given rootNode recursively.
 @param rootNode The given mapping node to get the keys from relatively.
 @return A {@link HashMap} containing the yaml keys in the keys & the values containing a list with
 the indexes to take to get to the keys in the yaml node. */
protected @NotNull HashMap<String, List<Integer>> getKeys(@NotNull MappingNode rootNode) throws NullPointerException {
  NullCheck.notNull(rootNode, "rootNode");
  return getKeysRecursive(rootNode, "", new ArrayList<>());
}

/**
 Gets the string keys relatively from the given mapping node.
 <p>
 <strong>This method should not be used outside itself or {@link #getKeys(MappingNode)}</strong>
 @param rootNode   The given mapping node to get the keys from relatively.
 @param currentKey The current string key path that leads to the given mapping node.
 @param indexPath  Contains a list the stores the path of indexes to take to get to the value of the
 key.
 @return A {@link HashMap} containing the yaml keys in the keys & the values containing a list with
 the indexes to take to get to the keys in the yaml node. */
private @NotNull HashMap<String, List<Integer>> getKeysRecursive(@NotNull MappingNode rootNode, @NotNull String currentKey, @NotNull ArrayList<Integer> indexPath) {
  HashMap<String, List<Integer>> keys = new HashMap<>();

  List<NodeTuple> value = rootNode.getValue();
  for (int index = 0; index < value.size(); index++) {
    // Create the key to this node by combining the key of this node & the key path to this node.
    String nodeKey = currentKey + getNodeKey(value.get(index));
    Node valueNode = value.get(index).getValueNode();

    // Can be suppressed since the copy will return the same class & generic.
    @SuppressWarnings("unchecked") ArrayList<Integer> indexPathClone = (ArrayList<Integer>) indexPath.clone();
    indexPathClone.add(index);

    // If the node is a MappingNode then it contains sub-keys.
    if (valueNode instanceof MappingNode) {
      keys.putAll((getKeysRecursive((MappingNode) valueNode, nodeKey + ".", indexPathClone)));
    }

    // The only other types of nodes used to store a yaml are "ScalarNode"s & "SequenceNode"s.
    // Both nodes have no sub-keys.
    keys.put(nodeKey, indexPathClone);
  }

  return keys;
}


/**
 Gets the {@link NodeTuple} based upon its key path relative to the root node of this yaml.
 @param key The key of the NodeTuple relative to the root node.
 @return The NodeTuple at the given key path. If there is no node at the given path then null will be
 returned. */
@Nullable
protected NodeTuple getNodeTuple(@NotNull String key) {
  // If the node doesn't exist return null.
  if (!keys.containsKey(key)) return null;

  List<Integer> path = keys.get(key);
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

private @NotNull List<String> getValue(@NotNull String key) {
  if (!keys.containsKey(key)) return Collections.emptyList();

  NodeTuple nodeTuple = getNodeTuple(key);
  // It should never be null
  if (nodeTuple == null) return Collections.emptyList();

  Node valueNode = nodeTuple.getValueNode();

  // If the key has a scalar value then return the one value.
  if (valueNode instanceof ScalarNode) {
    ScalarNode value = (ScalarNode) valueNode;
    return Collections.singletonList(value.getValue());
  }

  if (valueNode instanceof SequenceNode) {
    SequenceNode nodeValues = (SequenceNode) valueNode;
    ArrayList<String> values = new ArrayList<>();

    // A Sequence Node only contains Scalar Nodes.
    for (Node node : nodeValues.getValue()) {
      ScalarNode value = (ScalarNode) node;
      values.add(value.getValue());
    }

    return values;
  }

  return Collections.emptyList();
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

  WriteYaml identicalYaml = (WriteYaml) otherYaml;
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

  WriteYaml equalYaml = (WriteYaml) otherYaml;
  return this.keys.equals(equalYaml.keys);
}

/**
 @see Object#hashCode() */
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

  for (Event event : new org.yaml.snakeyaml.Yaml().serialize(parsedYaml)) {
    emitter.emit(event);
  }

  return stream.toString();
}

/**
 Gets the string representation of the comment yaml.
 <p>
 This method is only useful for debuggers. To handle the string yaml properly use {@link #getYaml()}
 to get the yaml string with comments. Or use {@link #getRawYaml()} to get the yaml string without
 comments.
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
