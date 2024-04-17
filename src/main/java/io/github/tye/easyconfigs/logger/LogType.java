package io.github.tye.easyconfigs.logger;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 This enum contains the possible situations in which EasyConfigurations might output a log message &
 the severity of the output message. */
public enum LogType {
  /**
   This log is output when there are unused paths in the internal config files for the implementing
   program. */
  INTERNAL_UNUSED_PATH(Level.WARNING),

  /**
   This log is output when there are unused paths in the external config files for the implementing
   program. */
  EXTERNAL_UNUSED_PATH(Level.WARNING),

  /**
   This log is output when a value from an external config file cannot be parsed, so the default
   internal value is used as a fallback. */
  USING_FALLBACK_VALUE(Level.SEVERE),
  ;

/**
 The severity of the log message. */
private final @NotNull Level logLevel;

/**
 Constructs a new enum that represents a situation in which EasyConfigurations might output a log
 message & the severity of the output message.
 @param logLevel The severity of the log message. */
LogType(@NotNull Level logLevel) {
  this.logLevel = logLevel;
}

/**
 Gets the severity for this log message.
 @return The severity of this log message. */
public @NotNull Level getLogLevel() {
  return logLevel;
}

}
