package io.github.tye.easyconfigs.logger;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 This class is the default logging class for EasyConfigurations. It performs logging based upon the
 default java {@link Logger logger}.
 <p>
 If you want to implement a custom logger (this is usually to redirect the text output)... TODO
 <p>
 The default logger is a default java logger created by {@link Logger#getLogger(String)} with a name
 of {@code io.github.tye.easyconfigs.EasyConfigurations} */
@ExternalUse
public class EasyConfigurationsDefaultLogger implements EasyConfigurationsLogger {

/**
 Contains the default logger used by EasyConfigurations. To override this logger use
 {@link
 io.github.tye.easyconfigs.EasyConfigurations#overrideEasyConfigurationsLogger(EasyConfigurationsLogger)
 EasyConfigurations#overrideEasyConfigurationsLogger(EasyConfigurationsLogger)} */
@InternalUse
public static @NotNull EasyConfigurationsLogger logger = new EasyConfigurationsDefaultLogger();


/**
 Contains the default java logger for EasyConfigurations. */
@InternalUse
private static final @NotNull Logger javaLogger = Logger.getLogger("io.github.tye.easyconfigs.EasyConfigurations");


/**
 Logs the message to the default java logger.
 @param logType    The type of log message to output.
 @param logMessage The text that will be included in the log message. */
@Override
@InternalUse
public void log(@NotNull LogType logType, @NotNull String logMessage) {
  javaLogger.log(logType.getLogLevel(), logMessage);
}

}
