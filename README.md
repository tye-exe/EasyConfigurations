## EasyConfigurations

___
### Synopsis:
This java project provides an easy & simplistic way to implement a highly customizable config & lang system into your java 8+ projects.


___
### Content:

- [Notable Features](#notable-features)
- [Setting up Configs](#setting-up-configs)
- [Setting up Lang](#setting-up-lang)
- [Defining Yaml values](#defining-the-values)
- [Setting up Keys](#setting-up-keys)
- [How to use](#using-the-configurations)
- [Annotation information](#annotation-information)
- [TLDR](#tldr)

___
### Notable features:
Moves the language strings & your programs configuration options into Yaml files bundled within the jar. This reduces the programming
knowledge required to aid in translations to nothing.

EasyConfigurations is built on top of java enums, this eliminates the possibility of syntactical errors.

The language format allows for the replacement of key-values (customizable) with the string representation of any lang response.

On start up the code performs validation checks on the language & config files to ensure that every enum registered is present in the
default files & that it can be parsed as the chosen object.


___
### Setting up configs:
Let’s look at a bare-bones implementation for configuration, observe the following code snippet:

```java
import io.github.tye.easyconfigs.instances.ConfigInstance;

public enum Configs implements ConfigInstance {

  USERNAME(String.class, "username");

Configs(Class<?> instanceOf, String yamlPath) {
  init(instanceOf, yamlPath);
}
}
```

There are a few basic requirements of a config enum for it to be used by EasyConfigurations. The interface
`ConfigInstance` **must** be implemented. This interface contains the core methods for retrieving configs. You
**must not** override any methods within this interface, as they are default methods containing the code for
retrieving configs.

Due to the constraints of interfaces within java you’ll have to manually create the constructors within the enum
classes. The created config constructor has three requirements:

- The first argument must be of type `Class<?>`. It can have any argument name.
- The second argument must be a String. It can have any argument name.
- The constructor must call `init(Class<?>, String)` at some point.

The first argument of the constructor is the class the object should be parsed as. This allows
EasyConfigurations to ensure that the object can be parsed from the internal Yaml as its intended class. This is
checked on program initialization by EasyConfigurations & if the value can’t be parsed as the given object an 
exception is thrown at configuration registration. This ensures the config values can always be parsed.

Several classes are supported by EasyConfigurations to be used as the assigned class. The list can be seen
[here](#supported-parsing-classes). You **cannot** assign a config as a class that isn’t listed.

___
### Setting up Lang:

A basic implementation of lang would appear as follows:

```java
import io.github.tye.easyconfigs.instances.LangInstance;

public enum Langs implements LangInstance {

  WELCOME("welcome");

Langs(String yamlPath) {
  init(yamlPath);
}
}
```

You will recognise most of the code here from the earlier sections on configs. The few notable differences are that
a Lang implementation will implement the `LangInstance` instead of the `ConfigInstance`. And that the Lang
constructor only takes one parameter, which is the yaml path to the lang value. This is because every lang value is
parsed as a string. The constructor **must** still the `init(String)` method.

___
### Defining the values:
Here is a small section of a yaml file.

```yaml
username: "taffy"
```

The `yamlPath` passed into the constructor is the path to the value within the yaml file. The path passed **must not**
include the colon ':' character at the end of the path. So for this example the path would be "username".

<br>

For nested values the sub keys should be joined with a fullstop '.' character. See bellow example:

```yaml
users:
  name: "taffy"
```

Would result in a YAML path of "users.name".

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

Would both have the YAML path of "usernames".

___
### Setting up keys:

```java
import io.github.tye.easyconfigs.instances.KeyInstance;

public enum Keys implements KeyInstance {

  USER("user");

Keys(String toReplace) {
  init(toReplace);
}
}
```

The implementation & use of keys differs significantly from lang or config. As keys are used to replace _key_
sequences of text within a lang response with any string at runtime.

Referencing the above code you can see that the constructor only takes the string "toReplace". This is the sequence
of text that the key will be replacing within a lang response. However, it won’t replace just any text that matches
the sequence of characters. The lang response **must** surround a key with the default key markers. The markers
default to "{" for the start of the key & "}" for the end of a key. Future examples will use the default markers.
However, the default key markers can be changed to any string sequence
([example](#registering-the-created-configurations)).

For example take this theoretical lang response. `Welcome {user}!`. Using a key we could change `{user}` to any
string value, such as a persons’ username. This would modify the lang response to become `Welcome taffy!`.

However, there are a few things to keep in mind when using keys:

- You **should not** replace one key with another key. This is **not** supported.
- Try to replace a non-existent key within a lang response will have no effect.
- Not replacing a key within a lang response will cause the raw key to be output in the lang response.

___
### Registering the created configurations:
Before being able to use any lang or configs, you’d first need to register them. This is simple:

```java
import io.github.tye.easyconfigs.EasyConfigurations;
import internalConfigs.io.github.tye.easyconfigs.Config;

public class Main() {
public static void main(String[] args) {

  // Registers Configs
  EasyConfigurations.registerConfig(myConfigs.class, "/myResourceConfigPath/configs.yml");

  // Registers Lang
  EasyConfigurations.registerLang(myLangs.class, "/myResourceLangPath/eng.yml");

  // Registers Keys
  EasyConfigurations.registerKey(myKeys.class);

  // Optional //

  // Sets default language. (see below for more info).
  EasyConfigurations.setEasyConfigurationLanguage(Config.InternalLanguages.ENGLISH);

  // Sets the default key markers. (see below for more info).
  EasyConfigurations.setKeyCharacters("{", "}");
}
}
```

Registering Lang & Config both require the class of the lang/config enum as a first parameter, with the string path
to the location of the yaml files starting from the resource folder within the jar. For keys, only the key class
parameter is needed.

The method `setEasyConfigurationLanguage()` changes the internal language that EasyConfigurations logs in. It will
default to English if it is not set. See [supported languages](#supported-languages) for a list of the languages 
EasyConfigurations can output logs in.

The method `setKeyCharacters()` changes what strings surround a key within the lang yaml file. Using the default
value, ("{" to open a key, "}" to close a key) an example yaml entry would look like `welcome: "Welcome {user}!`.
With the "{user}" part of the text being replaced before being returned.
___
### Using the Configurations:

// Examples will be implemented in the future.

___
### Annotation information:
Every method within EasyConfigurations is annotated with one of four annotations. If you hate reading the short of
it is only use elements marked with "@ExternalUse". A more descriptive explanation will follow:

- @InternalUse – These methods should only be used within the EasyConfigurations project & not outside of it. As
  doing so could have unforeseen effects.

- @ExternalUse – These methods are safe to use as you see fit, anywhere & in anyway. This is because these methods
  implement guards to prevent arbitrary values from being passed into places they shouldn’t.

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
To parse an unsupported class from a config file, you will need to implement a method to convert the
class to & from a String.

If there is a class you think should be supported, please open an issue on GitHub.
<br>

#### Supported languages:

Only one language is supported by EasyConfigurations for logging, English.  
  
To clarify, EasyConfigurations can be used to parse text in any language. The supported language only effects the 
language of the logs that EasyConfigurations outputs. 

##### Future plans:

- When there is an invalid Yaml file give the text on the invalid line & the line number of the invalid line.
- Create option to copy config / lang files to an external location to allow for user customization. (Internal ones
  will still be used for fallback purposes).
- Adding support for parsing binary strings from config files to allow for data serialization.
- Adding example code.
- Add way to redirect logger output.
- More? Open an issue if you have a suggestion!

___
### TLDR:

- Only use methods/classes annotated with "@ExternalUse".
- When creating an enum class for Configs, Keys or, Lang always implement its respective interface.
  (`ConfigInstance`, `LangInstance`, `KeyInstance`).
- The constructors in any of the enums must always call the "init" method provided by the interface.