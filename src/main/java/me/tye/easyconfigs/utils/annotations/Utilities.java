package me.tye.easyconfigs.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 Elements marked with this annotation are utility methods that EasyConfigurations uses.<br>
 Using these methods <b>probably</b> won't have any ill effects, however consistency between versions is <b>NOT</b> guaranteed nor taken into consideration.*/
@Retention (RetentionPolicy.SOURCE)
@Documented
public @interface Utilities {}
