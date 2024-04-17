package io.github.tye.easyconfigs.test;

import io.github.tye.easyconfigs.exceptions.ConfigurationException;
import io.github.tye.easyconfigs.yamls.WriteYaml;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Test {

/*
static {
  try {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("org.yaml.snakeyaml.nodes.Node");
    CtNewMethod.make("public void say() { System.out.println(\"Hello world!\"); }", cc);
    Class<?> c = cc.toClass();
    Object o = c.newInstance();
    o.getClass().getMethod("say").invoke(o);
  }
  catch (NotFoundException e) {
    throw new RuntimeException(e);
  }
  catch (CannotCompileException e) {
    throw new RuntimeException(e);
  }
  catch (InvocationTargetException e) {
    throw new RuntimeException(e);
  }
  catch (InstantiationException e) {
    throw new RuntimeException(e);
  }
  catch (IllegalAccessException e) {
    throw new RuntimeException(e);
  }
  catch (NoSuchMethodException e) {
    throw new RuntimeException(e);
  }
}
 */

public static void main(String[] args) throws IOException, ConfigurationException {

  LoaderOptions loaderOptions = new LoaderOptions();
  loaderOptions = loaderOptions.setProcessComments(true);
  DumperOptions dumperOptions = new DumperOptions();
  dumperOptions.setProcessComments(true);

  Yaml yaml = new Yaml(new SafeConstructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);

  String data = "# I wonder what this will do\n" +
                "test: \n" +
                "  hmm: \"Weee\"\n" +
                "  nah: \"...\" #No comment\n" +
                "\n" +
                "eh: \"idk\"\n" +
                "\n" +
                "this:\n" +
                "- \"IS\"\n" +
                "- \"SPARTA\"\n" +
                "\n" +
                "more.tests.ee: 5";

  ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getBytes());
  //MappingNode composed = (MappingNode) yaml.compose(new InputStreamReader(byteArrayInputStream));

  WriteYaml parsedYaml = new WriteYaml(new ByteArrayInputStream(data.getBytes()));

  System.out.println(parsedYaml.getYaml());

  parsedYaml.setValue("more.tests.ee", "e. n. d.");

  System.out.println(parsedYaml.getYaml());

}

}
