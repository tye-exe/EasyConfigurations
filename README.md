## EasyConfigurations

### Warning:
These docs are slightly out of date!

___
### Synopsis:
This java project provides an easy & simplistic way to implement a highly customizable config & lang system into your
java 8+ projects.


___
### Content:

- [Notable Features](#notable-features)
- [Setting up Configs](#setting-up-configs)
- [Setting up Lang](#setting-up-lang)
- [Defining Yaml values](#defining-the-values)
- [Setting up Keys](#setting-up-keys)
- [How to use](#using-the-configurations)
- [Annotation information](#annotation-information)
- [Redirecting logs](#redirecting-logging)
- [Additional information](#additional-information)
- [TLDR](#tldr)

___
### Notable features:
Moves the language strings & your programs configuration options into Yaml files bundled within the jar. This reduces
the barrier to translation & ensures that default configs & language will **always** be available.

EasyConfigurations is built on top of java enums, this eliminates the possibility of syntactical errors.

The language format allows for the replacement of key-values (customizable) with the string representation of any lang
response at runtime.

On runtime initialization, validation checks are performed on the language & config files to ensure that every enum
registered is present in the default files & that it can be parsed correctly.


___
### Setting up configs:
Let’s look at a bare-bones implementation for configuration, observe the following code snippet:

```java
import io.github.tye.easyconfigs.instances.reading.ReadingConfigInstance;

public enum Configs implements ReadingConfigInstance {

  USERNAME(String.class, "username");

Configs(Class<?> instanceOf, String yamlPath) {
  init(instanceOf, yamlPath);
}
}
```

There are a few basic requirements of a config enum for it to be used by EasyConfigurations. The interface
`ConfigInstance` **must** be implemented. This interface contains the core methods for retrieving configs. You
**must not** override any methods within this interface, as they are default methods containing the code for retrieving
configs.

Due to the constraints of interfaces within java you’ll have to manually create the constructors within the enum
classes. The created config constructor has three requirements:

- The first argument must be of type `Class<?>`. It can have any argument name.
- The second argument must be a String. It can have any argument name.
- The constructor must call `init(Class<?>, String)` at some point.

The first argument of the constructor is the class the object should be parsed as. This allows EasyConfigurations to
ensure that the object can be parsed from the internal Yaml as its intended class. This is checked at runtime
initialization by EasyConfigurations & if the value can’t be parsed as the given object an exception is thrown at
configuration registration. This ensures the config values can always be parsed.  
Several classes are supported by EasyConfigurations to be used as the assigned class. The list can be seen
[here](#supported-parsing-classes). You **cannot** assign a config as a class that isn’t listed.

The second value is the path to a value(s) inside the configuration yaml file. The given path **must** be the full path,
if the path traverses any sub-keys, then they **must** be separated by a `.`. The `:` char **must not** be included. An
example: `startup.updates.check`.

The name of the enum readingInstance can be anything, but I'd recommend having it match the yaml key. If you decide to
use the
yaml key, remember to swap `.` for `_`, as `.` aren't allowed in enum names.

___
### Setting up Lang:

A basic implementation of lang would appear as follows:

```java
import io.github.tye.easyconfigs.instances.reading.ReadingLangInstance;

public enum Langs implements ReadingLangInstance {

  WELCOME("welcome");

Langs(String yamlPath) {
  init(yamlPath);
}
}
```

You will recognize most of the code here from the [earlier sections on configs](#setting-up-configs). The few notable
differences are that a Lang implementation will implement the `LangInstance` instead of the `ConfigInstance`. And that
the Lang constructor only takes one parameter, which is the yaml path to the lang value. This is because every lang
value is parsed as a string. The constructor **must** still call the `init(String)` method.

The true utility of Lang withing EasyConfigurations isn't unlocked until [keys](#setting-up-keys) are used. I'd highly
recommend viewing the section on them before implementing any lang.

___
### Defining the values:
Here is a small section of a yaml file.

```yaml
username: "taffy"
```

The `yamlPath` passed into the constructor is the path to the value within the yaml file. The path passed **must not**
include the `:` at the end of the path. So for this example the path would be `username`.

<br>

For nested values the sub keys should be joined with a `.`. See bellow example:

```yaml
users:
  name: "taffy"
```

Would result in a YAML path of `users.name`.

<br>

Paths for YAML arrays aren’t handled differently to any other paths.

```yaml
usernames:
  - "taffy"
  - "zern"
```

```yaml
usernames: [ "taffy", "zern" ]
```

Would both have the YAML path of `usernames`.

___
### Setting up keys:

```java
import io.github.tye.easyconfigs.keys.Keys;

public enum Keys implements Keys {

  USER("user");

Keys(String toReplace) {
  init(toReplace);
}
}
```

The implementation & use of keys differs significantly from lang or config. As keys are used to replace _key_
sequences of text within a lang response at runtime.

Referencing the above code, you can see that the constructor only takes the string "toReplace". This is the sequence of
text that the key will be replacing within a lang response. However, it won’t replace just any text that matches the
sequence of characters. The lang response **must** surround a key with the default key markers. The markers default
to `{` for the start of the key & `}` for the end of a key. Future examples will use the default markers. However, the
default key markers can be changed to any string sequence, [example](#registering-the-created-configurations).

For example, take this theoretical lang response. `Welcome {user}!`. Using a key we could change `{user}` to any string
value, such as a persons’ username. If our user had the name `taffy`, the modified the lang response would
be `Welcome taffy!`.

However, there are a few things to keep in mind when using keys:

- You **should not** replace one key with another key. This is **not** supported.
- Try to replace a non-existent key within a lang response will have no effect.
- Not replacing a key within a lang response will cause the raw key to be output in the lang response.
- Replacing a key without setting a replacement value will cause the key to just be removed.

___
### Registering the created configurations:
Before being able to use any lang or configs, you’d first need to register them.

```java
import io.github.tye.easyconfigs.EasyConfigurations;
import internalConfigs.io.github.tye.easyconfigs.Config;

public class Main() {
public static void main(String[] args) throws Exception {

  // Registers Configs
  EasyConfigurations.registerReadOnlyConfig(myConfigs.class, "/myResourceConfigPath/my_configs.yml");

  // Registers Lang
  EasyConfigurations.registerReadOnlyLang(myLangs.class, "/myResourceLangPath/my_lang.yml");


  // Optional //

  // Sets default language. (see below for more info).
  EasyConfigurations.setEasyConfigurationLanguage(Config.InternalLanguages.ENGLISH);

  // Sets the default key markers. (see below for more info).
  EasyConfigurations.setKeyCharacters("{", "}");
}
}
```

Registering Lang & Config both require the class of the lang/config enum as a first parameter, with the string path to
the location of the yaml files starting from the resource folder within the jar. Keys do not need to be registered.

The method `setEasyConfigurationLanguage()` changes the internal language that EasyConfigurations logs in. It will
default to English if it is not set. See [supported languages](#supported-languages) for a list of the languages
EasyConfigurations can output logs in.

The method `setKeyCharacters()` changes what strings surround a key within the lang yaml file. Using the default
value, (`{` to open a key, `}` to close a key) an example yaml node would look like `welcome: "Welcome {user}!`. With
the "{user}" part of the text being replaced (optionally) at runtime.
___
### Using the Configurations:

Thorough examples will be created for version "2.0". However, it would help to look at
the [unit tests](https://github.com/tye-exe/EasyConfigurations/blob/40fd004a9b01c68f2c1d68da59aec3068edebf02/src/test/java/io/github/tye/tests/ReadingYamlTests.java)
to get a better understanding of the implementation.

___
### Annotation information:
Every method within EasyConfigurations is annotated with one of four annotations. If you hate reading the short of it is
only use elements marked with "@ExternalUse". A more descriptive explanation will follow:

- @InternalUse – These methods should only be used within the EasyConfigurations project & not outside of it. They will
  not be supported if you use them externally & they could change in any version.

- @ExternalUse – These methods are the supported ways to interact with EasyConfigurations.

- @NotImplemented – These methods **should not** be used any circumstances. Either internally or externally as they are
  still being developed.

---
### Redirecting Logging:
EasyConfiguration uses the built-in java logger by default. However, this behaviour can be overridden.

```java
import io.github.tye.easyconfigs.logger.EasyConfigurationsLogger;
import io.github.tye.easyconfigs.logger.LogType;

public class ExampleLogger implements EasyConfigurationsLogger {

/**
 Logs a message to the EasyConfigurations logger.
 @param logType    The type of log message to output.
 @param logMessage The text that will be included in the log message. */
@Override
public void log(@NotNull LogType logType, @NotNull String logMessage) {
  System.out.println(logMessage);
}
}
```

In the above code snippet the `EasyConfigurationsLogger` logger interface is implemented. This interface requires you to
override the `log` method. This is the method that will handle every log output by EasyConfigurations.  
The `LogType` is an enum that represents any situation in which EasyConfigurations might output a log & the severity of
said log. The `logMessage` contains the string output of the log, in set logging language.

The overridden method can do anything, from redirecting the output to whatever else, to just doing nothing & voiding the
log (though this is ill-advised.)

To get EasyConfigurations to use your custom logger you'd need to register it. This is shown bellow:

```java
import io.github.tye.easyconfigs.EasyConfigurations;

public class Main() {
public static void main(String[] args) {

  // Sets EasyConfigurations to use your logger
  EasyConfigurations.overrideEasyConfigurationsLogger(new YourLogger());

}
}
```

There is no limit to the amount of times the logger can be overridden.

___
### Additional information:

##### Supported parsing classes:
Several classes are supported by EasyConfigurations to assign to configs. They are as follows:

- String.class
- Boolean.class & boolean.class
- Integer.class & int.class
- Double.class & double.class
- Float.class & float.class
- Short.class & short.class
- Long.class & long.class
- Byte.class & byte.class
- Char.class & char.class
- LocalDateTime.class
- OffsetDateTime.class
- ZonedDateTime.class
- All the above as an array (e.g. String[].class)

If you try & assign a config to a class that isn’t in the above list, then an error will be thrown.  
To parse an unsupported class from a config file, you will need to implement a method to convert the class to & from a
String. An interface to streamline this will be coming in future versions.

#### Supported languages:

Only one language is supported by EasyConfigurations for logging, English[^1].

If you wish to translate logging to a different language open an issue on GitHub!

[^1]: To clarify, EasyConfigurations can be used to parse text in any language. The supported language only effects the
language of the logs that EasyConfigurations outputs.

##### Future plans:

- Create option to copy config / lang files to an external location to allow for user customization. (Internal ones will
  still be used for fallback purposes).
- Adding example code.
- Streamline process to support a custom objects.
- More? Open an issue if you have a suggestion!

___
### TLDR:

- Only use methods/classes annotated with "@ExternalUse".
- When creating an enum class for Configs, Keys or, Lang always implement its respective interface.
  (`ConfigInstance`, `LangInstance`, `KeyInstance`).
- The constructors in any of the enums must always call the "init" method provided by the interface.