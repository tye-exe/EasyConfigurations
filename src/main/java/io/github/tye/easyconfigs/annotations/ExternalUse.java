package io.github.tye.easyconfigs.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**Elements marked with this annotation are intended for use in programs that implement EasyConfigurations as a dependency.<br>
 They can be safely used anywhere by anything.*/
@Retention (RetentionPolicy.SOURCE)
@Documented
@SuppressWarnings ("unused") // These methods are intended for use projects using Easy Configurations as a dependency.
public @interface ExternalUse {}
