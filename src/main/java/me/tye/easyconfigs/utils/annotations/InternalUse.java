package me.tye.easyconfigs.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 Elements marked with this annotation are for internal use within EasyConfigurations.<br>
 Attempting to use these methods without fulling knowing their effects could result in unforeseen outcomes. */
@Retention (RetentionPolicy.SOURCE)
@Documented
public @interface InternalUse {}
