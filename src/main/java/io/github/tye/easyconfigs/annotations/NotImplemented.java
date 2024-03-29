package io.github.tye.easyconfigs.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**Methods marked with this annotation aren't complete &amp; <b>SHOULD NOT</b> be used under any circumstances. Even if they appear to work.<br>
 (Yes, this also applied to the developers that just go "yolo", be warned!)*/
@Retention (RetentionPolicy.SOURCE)
@Documented
public @interface NotImplemented {}
