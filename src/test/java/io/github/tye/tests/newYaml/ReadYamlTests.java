package io.github.tye.tests.newYaml;

import io.github.tye.easyconfigs.EasyConfigurations;
import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.yamls.ReadYaml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReadYamlTests {

protected InputStream getResource(String path) {
  return EasyConfigurations.class.getResourceAsStream(path);
}

private InputStream inputData;
private ReadYaml yaml;

@BeforeEach
public void populateInputStream() throws IOException, ConfigurationException {
  inputData = getResource("/tests/Yamls/externalYamls/DefaultYaml.yml");
  yaml = new ReadYaml(inputData);
}


@Test
public void parsing_Test() throws IOException {
  String commentData = "# I wonder what this will do\ntest:\n  hmm: \"Weee\"\n  nah: \"...\" #No comment\n\neh: \"idk\"\nehh: \"idk\" # guess i still don't know\n\n# lovely...\nehhhhhh: \"idkkkkkkkkkk\"\n\n#eiaou\n\nthis:\n- \"Is\" #Not\n- \"Sparta\"\n";

  assertEquals(commentData, yaml.getYaml());
}

@Test
public void get_Test() {
  assertEquals("idk", yaml.getValue("eh"));
  assertEquals(Arrays.asList("Is", "Sparta"), yaml.getValue("this"));

  assertNull(yaml.getValue("test"));
  assertNull(yaml.getValue(""));
}
}
