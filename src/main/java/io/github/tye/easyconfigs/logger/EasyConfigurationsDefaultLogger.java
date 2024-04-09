package io.github.tye.easyconfigs.logger;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 This class is the default logging class for EasyConfigurations. It performs logging based upon the
 default java {@link Logger logger}.
 <p>
 If you want to implement a custom logger (this is usually to redirect the text output)... TODO */
@ExternalUse
public class EasyConfigurationsDefaultLogger implements EasyConfigurationsLogger {


@InternalUse
public static @NotNull EasyConfigurationsLogger logger = new EasyConfigurationsDefaultLogger();


/**
 Contains the default java logger for EasyConfigurations. */
@InternalUse
private static final @NotNull Logger javaLogger = Logger.getLogger("io.github.tye.easyconfigs.EasyConfigurations");


@Override
@InternalUse
public void log(LogType logType, String logMessage) {
  javaLogger.log(logType.getLogLevel(), logMessage);
}

}
