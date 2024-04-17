package io.github.tye.tests;

import io.github.tye.easyconfigs.logger.EasyConfigurationsLogger;
import io.github.tye.easyconfigs.logger.LogType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DebugLogger implements EasyConfigurationsLogger {

public ArrayList<LogContainer> output = new ArrayList<>();

/**
 Logs a message to the EasyConfigurations logger.
 @param logType    The type of log message to output.
 @param logMessage The text that will be included in the log message. */
@Override
public void log(@NotNull LogType logType, @NotNull String logMessage) {
  output.add(new LogContainer(logType, logMessage));
}


public static class LogContainer {
  public LogType logType;
  public String logMessage;

  public LogContainer(LogType logType, String logMessage) {
    this.logType = logType;
    this.logMessage = logMessage;
  }
}
}
