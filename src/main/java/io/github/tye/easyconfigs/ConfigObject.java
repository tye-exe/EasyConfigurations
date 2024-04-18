package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.NotImplemented;
import org.jetbrains.annotations.NotNull;


@NotImplemented
public interface ConfigObject {

@NotNull
String getConfigString();

@NotNull
ConfigObject getConfigObject(String configString);

}
