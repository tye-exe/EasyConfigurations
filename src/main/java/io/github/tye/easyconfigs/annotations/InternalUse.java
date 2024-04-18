package io.github.tye.easyconfigs.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 Elements marked with this annotation are for internal use within EasyConfigurations. They should not
 be used outside the EasyConfigurations jar.
 <p>
 Methods that are intended for external use are marked with {@link ExternalUse}. */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface InternalUse {}
