package io.github.tye.tests;

import io.github.tye.easyconfigs.SupportedClasses;
import io.github.tye.easyconfigs.utils.ArrayManipulation;
import io.github.tye.easyconfigs.utils.exceptions.NotOfClassException;
import io.github.tye.easyconfigs.utils.exceptions.NotSupportedException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

import static io.github.tye.easyconfigs.SupportedClasses.existsAsEnum;
import static io.github.tye.easyconfigs.SupportedClasses.getAsEnum;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SupportedClassesTests {

@Order(0)
@ParameterizedTest
@EnumSource(value = SupportedClasses.class)
void Enums_represents_a_class(SupportedClasses clazz) {
  assertNotNull(clazz.getClasses());
  assertTrue(clazz.getClasses().length > 0, "Enums should represent at least one class.");
}


@Order(1)
@ParameterizedTest
@ValueSource(classes = {Object.class, double.class, Integer.class, Float[].class, short[].class, Random.class, Inet4Address[].class, LocalDateTime.class})
void Correct_enum_is_returned_for_class(Class<?> clazzToMatch) {
  boolean exists = existsAsEnum(clazzToMatch);

  if (exists) {
    assertDoesNotThrow(() -> getAsEnum(clazzToMatch), "Classes that are represented should be parsed.");
    SupportedClasses asEnum = getAsEnum(clazzToMatch);

    boolean isRepresented = ArrayManipulation.arrayContains(asEnum.getClasses(), clazzToMatch);
    assertTrue(isRepresented, "The enum should represent the class.");
  }
  else {
    assertThrowsExactly(NotSupportedException.class, () -> getAsEnum(clazzToMatch), "Classes that are not represented should throw an exception.");
  }


}

@SuppressWarnings ("UnnecessaryBoxing") // Used to return non-primitive types.
private static Stream<Arguments> Enums_parsing_respective_class_provider() {
  return Stream.of(
      arguments(20, int.class),
      arguments(6789423579348L, Long.class),
      arguments(Double.valueOf(50.72D), Double.class),
      arguments(new float[]{1.98792345F, 7.321312F, 0.098324875F}, float[].class),
      arguments("Testing times!", String.class),
      arguments(ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]"), ZonedDateTime.class),
      arguments(new Character[]{'s', 'p', 'd'}, char[].class),
      arguments(new Random(), String.class),
      arguments("wrong data", int[].class),
      arguments(new HashMap<>(), String.class),
      arguments(2134234534L, long[].class)
  );
}

@Order(2)
@ParameterizedTest
@MethodSource("Enums_parsing_respective_class_provider")
<T> void Enums_can_parse_respective_class(T valueToParse, Class<?> classToParse) {
  SupportedClasses representingEnum = SupportedClasses.getAsEnum(classToParse);

  boolean isSupposedToBeParsed = ArrayManipulation.arrayContains(representingEnum.getClasses(), valueToParse.getClass());
  boolean couldBeParsed = representingEnum.canParse(valueToParse);

  if (isSupposedToBeParsed) {
    assertTrue(couldBeParsed,  classToParse.getName() + " class should be able to be parsed.");
  } else {
    assertFalse(couldBeParsed, classToParse.getName() + " class should not be able to be parsed.");
  }
}

@Order(3)
@ParameterizedTest
@MethodSource("Enums_parsing_respective_class_provider")
void Enums_parse_respective_classes_correctly(Object valueToParse, Class<?> classToParse) {
  SupportedClasses representingEnum = SupportedClasses.getAsEnum(classToParse);
  boolean canBeParsed = representingEnum.canParse(valueToParse);

  if (canBeParsed) {
    assertDoesNotThrow(() -> representingEnum.parse(valueToParse), "The class should have been parsed.");
  }
  else {
    assertThrowsExactly(NotOfClassException.class ,() -> representingEnum.parse(valueToParse), "The class should not have been parsed.");
  }
}
}