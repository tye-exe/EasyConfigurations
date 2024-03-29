package io.github.tye.easyconfigs;

import io.github.tye.easyconfigs.annotations.InternalUse;
import io.github.tye.easyconfigs.internalConfigs.Lang;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NullCheck {


/**
 If the given object is null throws a {@link NullPointerException} with the default lang message.
 @param object     The given object.
 @param objectName The name of the given object.
 @throws NullPointerException If the given object is null */
@Contract("null, _ -> fail; !null, _ -> _")
@InternalUse
public static void notNull(@Nullable Object object, @NotNull String objectName) throws NullPointerException {
  if (object != null) return;

  throw new NullPointerException(Lang.notNull(objectName));
}


}
