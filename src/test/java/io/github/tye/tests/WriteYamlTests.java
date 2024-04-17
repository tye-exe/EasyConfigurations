package io.github.tye.tests;

import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.yamls.WriteYaml;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class WriteYamlTests {


private InputStream getResource(String path) {
  return EasyConfigurations.class.getResourceAsStream(path);
}

private InputStream inputData;

@BeforeEach
public void populateInputStream() {
  inputData = getResource("/tests/Yamls/externalYamls/DefaultYaml.yml");
}

@AfterEach
public void clearInputStream() throws IOException {
  inputData.close();
}


@Test
public void addMissingKeys_nodeTest() throws IOException, ConfigurationException {
  WriteYaml fullYaml = new WriteYaml(inputData);
  WriteYaml missingYaml = new WriteYaml(getResource("/tests/Yamls/externalYamls/missingTests/MissingNode.yml"));

  // Shouldn't be equal or identical as the yaml is missing a key.
  assertNotEquals(fullYaml, missingYaml);
  assertFalse(fullYaml.identical(missingYaml));

  missingYaml.addMissingKeys(fullYaml);

  assertEquals(fullYaml, missingYaml);
  assertTrue(fullYaml.identical(missingYaml));
}

@Test
public void addMissingKeys_commentTest() throws IOException, ConfigurationException {
  WriteYaml fullYaml = new WriteYaml(inputData);
  WriteYaml missingYaml = new WriteYaml(getResource("/tests/Yamls/externalYamls/missingTests/MissingComment.yml"));

  // Will be equal & identical since only a comment is missing.
  assertEquals(fullYaml, missingYaml);
  assertTrue(fullYaml.identical(missingYaml));

  missingYaml.addMissingKeys(fullYaml);

  assertEquals(fullYaml, missingYaml);
  assertTrue(fullYaml.identical(missingYaml));
}

}
