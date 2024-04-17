package io.github.tye.easyconfigs;

import org.jetbrains.annotations.NotNull;


public interface ConfigObject {

@NotNull
String getConfigString();

@NotNull
ConfigObject getConfigObject(String configString);

}
