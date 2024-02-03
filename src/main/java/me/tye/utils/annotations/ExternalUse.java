package me.tye.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**Elements marked with this annotation are intended for use in programs that implement EasyConfigurations as a dependency.<br>
 They can be safely used anywhere by anything.*/
@Retention (RetentionPolicy.SOURCE)
@Documented
public @interface ExternalUse {}
