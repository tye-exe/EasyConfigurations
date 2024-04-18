package io.github.tye.easyconfigs.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 Elements marked with this annotation are the correct way to interact with EasyConfigurations.
 <p>
 They can be safely used anywhere by anything. */
@Retention(RetentionPolicy.SOURCE)
@Documented
// These methods are intended for use projects using Easy Configurations as a dependency.
@SuppressWarnings("unused")
public @interface ExternalUse {}
