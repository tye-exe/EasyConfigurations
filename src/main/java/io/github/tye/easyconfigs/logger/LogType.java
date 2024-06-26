package io.github.tye.easyconfigs.logger;

import io.github.tye.easyconfigs.annotations.ExternalUse;
import io.github.tye.easyconfigs.annotations.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 This enum contains the possible situations in which EasyConfigurations might output a log message
 &amp; the severity of the output message. */
@InternalUse
public enum LogType {
  /**
   This log is output when there are unused paths in the internal config files for the implementing
   program. */
  INTERNAL_UNUSED_PATH(Level.WARNING),

  /**
   This log is output when there are unused paths in the external config files for the implementing
   program. */
  EXTERNAL_UNUSED_PATH(Level.INFO),

  /**
   This log is output when an external yaml was missing keys compared to the internal yaml, so the
   missing keys are appended to the external file, using the default value. */
  EXTERNAL_MISSING_PATH(Level.INFO),

  /**
   This log is output when a value from an external config file cannot be parsed, so the default
   internal value is used as a fallback. */
  USING_FALLBACK_VALUE(Level.SEVERE),

  /**
   This log is output when a value is replaced within a persistent yaml &amp; the updated value is
   unable to be written to the external file. */
  FAILED_EXTERNAL_UPDATE(Level.WARNING);

/**
 The severity of the log message. */
@InternalUse
private final @NotNull Level logLevel;

/**
 Constructs a new enum that represents a situation in which EasyConfigurations might output a log
 message &amp; the severity of the output message.
 @param logLevel The severity of the log message. */
@InternalUse
LogType(@NotNull Level logLevel) {
  this.logLevel = logLevel;
}

/**
 Gets the severity for this log message.
 @return The severity of this log message. */
@ExternalUse
public @NotNull Level getLogLevel() {
  return logLevel;
}

}
