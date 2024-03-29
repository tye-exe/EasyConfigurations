package io.github.tye.easyconfigs.utils;

import io.github.tye.easyconfigs.utils.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.logging.Logger;

/**Contains constant or static values that EasyConfigurations uses.*/
@InternalUse()
@SuppressWarnings ({"NotNullFieldNotInitialized"})
public final class Consts {

/**This class is a utility class & should not be instantiated.*/
private Consts() {}


/**Contains the default logger for the EasyConfigurations dependency.*/
@InternalUse
public static final @NotNull Logger logger = Logger.getLogger("io.github.tye.easyconfigs.EasyConfigurations");


/**Stores the parsed config options.*/
@InternalUse
public static @NotNull HashMap<String, Object> langMap;
/**Stores the parsed language responses.*/
@InternalUse
public static @NotNull HashMap<String, Object> configMap;

/**Stores the string that proceeds a key*/
@InternalUse
public static @NotNull String keyStart = "{";
/**Stores the string that terminates a key*/
@InternalUse
public static @NotNull String keyEnd = "}";

}
