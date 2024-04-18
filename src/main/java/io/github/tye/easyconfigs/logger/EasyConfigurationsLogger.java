package io.github.tye.easyconfigs.logger;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import org.jetbrains.annotations.NotNull;

/**
 This interface contains the methods necessary to override the default logger for
 EasyConfigurations.
 <p>
 To override the logger, implement this method in a class, then call
 {@link
 io.github.tye.easyconfigs.EasyConfigurations#overrideEasyConfigurationsLogger(EasyConfigurationsLogger)
 overrideEasyConfigurationsLogger} in
 {@link io.github.tye.easyconfigs.EasyConfigurations EasyConfigurations}.
 <p>
 To see the default logging behaviour view {@link EasyConfigurationsDefaultLogger}. */
@ExternalUse
public interface EasyConfigurationsLogger {

/**
 Logs a message to the EasyConfigurations logger.
 @param logType    The type of log message to output.
 @param logMessage The text that will be included in the log message. */
@ExternalUse
void log(@NotNull LogType logType, @NotNull String logMessage);

}
