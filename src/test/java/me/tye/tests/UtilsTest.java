package me.tye.tests;

import me.tye.easyconfigs.utils.ArrayManipulation;
import me.tye.easyconfigs.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class UtilsTest {


@Test
void notNull() {
  assertThrowsExactly(NullPointerException.class, () -> Utils.notNull(null ,"null"), "Null should throw Null pointer exception");
  assertDoesNotThrow(() -> Utils.notNull("test!" ,"not-null"), "Not-null should not throw Null pointer exception");
  assertDoesNotThrow(() -> Utils.notNull(new char[]{'a', 'e', 'o', 'u'} ,"non-null"), "Not-null should not throw Null pointer exception");
}


private static Stream<Arguments> array_provider() {
  return Stream.of(
      arguments(new String[]{"Testing", "A", "Util", "Method"}, "A", true),
      arguments(new String[]{"Testing", "A", "Util", "Method"}, "Please work.", false),
      arguments(new char[]{'h', 'e', 'l', 'p'}, ";-;", false),
      arguments(new int[]{2, 74, -1}, 3, false),
      arguments(new int[]{2, 74, -1}, -1, true)
  );
}

@ParameterizedTest
@MethodSource("array_provider")
void arrayContains(Object array, Object valueToCheck, boolean isContained) {
  boolean determinedToContain = ArrayManipulation.arrayContains(array, valueToCheck);

  if (isContained) {
    assertTrue(determinedToContain ,"Array should contain the provided value.");
  }
  else {
    assertFalse(determinedToContain, "Array should not contain the provided value.");
  }
}


private static Stream<Arguments> clazz_provider() {
  return Stream.of(
      arguments(Object.class, true),
      arguments(int[].class, true),
      arguments(Integer.class, false),
      arguments(LocalDateTime.class, false)
  );
}

@ParameterizedTest
@MethodSource("clazz_provider")
void usesDefaultToString(Class<?> clazzToCheck, boolean expectedResult) {
  boolean result = Utils.usesDefaultToString(clazzToCheck);
  assertEquals(result, expectedResult, "Result & expected result should match.");
}


}