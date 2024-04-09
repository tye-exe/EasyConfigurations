package io.github.tye.tests.newYaml;

import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.exceptions.BadYamlError;
import io.github.tye.easyconfigs.yamls.YamlParsing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExternalYamlTests {


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
public void addMissingKeys_nodeTest() throws IOException, BadYamlError {
  YamlParsing fullYaml = new YamlParsing(inputData);
  YamlParsing missingYaml = new YamlParsing(getResource("/tests/Yamls/externalYamls/missingTests/MissingNode.yml"));

  missingYaml.addMissingKeys(fullYaml);

  assertTrue(fullYaml.identical(missingYaml));
}

}
