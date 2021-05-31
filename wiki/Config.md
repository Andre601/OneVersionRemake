[minimessage]: https://docs.adventure.kyori.net/minimessage.html
[adventure]: https://github.com/KyoriPowered/adventure

[colors]: https://docs.adventure.kyori.net/minimessage.html#color
[colorsVerbose]: https://docs.adventure.kyori.net/minimessage.html#color-verbose
[formatting]: https://docs.adventure.kyori.net/minimessage.html#decoration
[reset]: https://docs.adventure.kyori.net/minimessage.html#reset

[gradient]: https://docs.adventure.kyori.net/minimessage.html#gradient
[keybind]: https://docs.adventure.kyori.net/minimessage.html#keybind
[rainbow]: https://docs.adventure.kyori.net/minimessage.html#rainbow
[translatable]: https://docs.adventure.kyori.net/minimessage.html#translatable

# Config.yml
The config.yml of OneVersionRemake has a lot of settings for you to use.

This page tries to explain each option as detailed as possible!

## Contents
- [Protocol](#protocol)
  - [Versions](#versions)
  - [LogDenial](#logdenial)
  - [MajorOnly](#majoronly)
- [Messages](#messages)
  - [Formatting](#formatting)
    - [Basic Formatting](#basic-formatting)
    - [Advanced Formatting](#advanced-formatting)
    - [Not supported Options](#not-supported-options)
  - [PlayerCount](#playercount)
  - [Kick](#kick)
  - [Hover](#hover)
  - [Motd](#motd)

## Protocol
This section of the config contains the main settings used for things like determining what versions are allowed on the Network.

### Versions
> **Type**: `List (Integer)`  
> **Default**:  
> ```yaml
> Versions: []
> ```

This List is used to set what MC Versions are allowed to join the Network.  
Note that instead of the actual MC version (i.e. 1.15) do you instead set the Protocol Version of it.

Protocol Versions are, in most simplistic terms, unique numbers to determine what MC version is joining the network.

You can find a full list of Protocol Versions on the official Minecraft Wiki:
https://minecraft.gamepedia.com/Protocol_version#Java_Edition_2

Note that only a selected list of versions is supported by the `{version}` and `{userVersion}` placeholders and will be turned into readable MC versions.  
A list of supported Versions can be found in the [[Supported Protocols]] page of this wiki.

### LogDenial
> **Type**: `Boolean`  
> **Default**:  
> ```yaml
> LogDenial: true
> ```

This setting allows you to choose, if OneVersionRemake should log denied logins or not.

When this is set to true will any attempted join with a [not supported version](#versions) be denied and the following message printed into the console:  
```
[OneVersionRemake] Denied login for Player <player> with MC version <mcversion> (Protocol version <protocol>)
```

### MajorOnly
> **Type**: `Boolean`  
> **Default**:  
> ```yaml
> MajorOnly: false
> ```

When this option is set to true, will the `{version}` placeholder only display the Major MC versions of the [defined Protocol versions](#versions).

*Example*:  
- Setup:
  ```yaml
  Versions:
  - 477
  - 480
  - 485
  - 490
  - 498
  - 573
  - 575
  - 578
  - 735
  - 736
  - 751
  - 753
  - 754
  ```
  
  With `MajorOnly` set to `false`:
  ```
  1.14, 1.14.2, 1.14.3, 1.14.4, .1.15, 1.15.1, 1.15.2, 1.16, 1.16.1, 1.16.2, 1.16.3, 1.16.5
  ```
  
  With `MajorOnly` set to `true`:
  ```
  1.14.x, 1.15.x, 1.16.x
  ```

## Messages
The `Messages` section allows you to define the different messages displayed when a player either joins or views a server with an unsupported version.

### Formatting
OneVersionRemake uses the [MiniMessage Add-on][minimessage] from [Kyori Adventure][adventure] to provide basic but also advanced color and text formatting options.  
Depending on the config option are either only [basic](#basic-formatting) or [advanced](#advanced-formatting) options supported.

- #### Basic Formatting

  ##### Colors

  | Option 1                                        | Option 2                                                    |
  | ----------------------------------------------- | ----------------------------------------------------------- |
  | [`<aqua>`][colors]                              | [`<color:aqua>`][colors]                                    |
  | [`<black>`][colors]                             | [`<color:black>`][colors]                                   |
  | [`<blue>`][colors]                              | [`<color:blue>`][colors]                                    |
  | [`<dark_aqua>`][colors]                         | [`<color:dark_aqua>`][colors]                               |
  | [`<dark_blue>`][colors]                         | [`<color:dark_blue>`][colors]                               |
  | [`<dark_gray>`][colors]/[`<dark_grey>`][colors] | [`<color:dark_gray>`][colors]/[`<color:dark_grey>`][colors] |
  | [`<dark_green>`][colors]                        | [`<color:dark_green>`][colors]                              |
  | [`<dark_purple>`][colors]                       | [`<color:dark_purple>`][colors]                             |
  | [`<dark_red>`][colors]                          | [`<color:dark_red>`][colors]                                |
  | [`<gold>`][colors]                              | [`<color:gold>`][colors]                                    |
  | [`<gray>`][colors]                              | [`<color:gray>`][colors]                                    |
  | [`<green>`][colors]                             | [`<color:green>`][colors]                                   |
  | [`<light_purple>`][colors]                      | [`<color:light_purple>`][colors]                            |
  | [`<red>`][colors]                               | [`<color:red>`][colors]                                     |
  | [`<white>`][colors]                             | [`<color:white>`][colors]                                   |
  | [`<yellow>`][colors]                            | [`<color:yellow>`][colors]                                  |

  ##### Formatting

  | Name                            | Alias                 |
  | ------------------------------- | --------------------- |
  | [`<bold>`][formatting]          | [`<b>`][formatting]   |
  | [`<italic>`][formatting]        | [`<em>`][formatting]  |
  |                                 | [`<i>`][formatting]   |
  | [`<obfuscated>`][formatting]    | [`<obf>`][formatting] |
  | [`<reset>`][reset]              | [`<r>`][reset]        |
  | [`<strikethrough>`][formatting] | [`<st>`][formatting]  |
  | [`<underlined>`][formatting]    |                       |

- #### Advanced Formatting
  Includes [Basic Formatting](#basic-formatting) but also some more advanced options:

  | Option         | Format                                         |
  | -------------- | ---------------------------------------------- |
  | RGB Colors     | [`<#rrggbb>`][colors]                          |
  |                | [`<color:#rrggbb>`][colorsVerbose]             |
  | Gradients*     | [`<gradient:#rrggbb:#rrggbb>`][gradient]       |
  | Keybind        | [`<key:_key_>`][keybind]                       |
  | Rainbow        | [`<rainbow>`][rainbow]                         |
  | Translatable** | [`<lang:_key_>`][translatable]                 |
  |                | [`<lang:_key_:_optional1_:...>`][translatable] |

  *Also supports [Color Names](#colors).  
  **`_optionalX_` will be used to replace placeholders in the returned text.

- #### Not supported options
  These options are not supported in any of the messages.

  | Option                     | Reason                                                               |
  | -------------------------- | -------------------------------------------------------------------- |
  | `<click:_action_:_value_>` | Will render but won't perform anything on click.                     |
  | `<font:_name_>`            | Requires an active resource pack for the client to display the font. |
  | `<hover:_action_:_value_>` | Will render but won't show anything on hover.                        |
  | `<insert:_text_>`          | Will render but not perform on click.                                |

----
### PlayerCount
> **Type**: `String`  
> **Default**:
> ```yaml
> PlayerCount: '<red>Minecraft {version}'
> ```
> **Supported Formatting**: [Basic](#basic-formatting)
> 
> - [Basic](#basic-formatting)

Changes the text that usually displays the player count (`<online>/<total>`).  
You can set this to an empty String (`PlayerCount: ''`) to not alter the text or to just a color/formatting code (`PlayerCount: '<red>'`) to hide it completely!

### Kick
> **Type**: `List (String)`  
> **Default**:
> ```yaml
> Kick:
> - '<red>You are using an unsupported version of Minecraft ({userVersion})!'
> - '<red>This server supports the following Versions:'
> - '<gray>{version}'
> - ''
> - '<red>Please change your Version and try again.' 
> ```
> **Supported Formatting**:
>
> - [Advanced](#advanced-formatting)

The text to display when the Player gets kicked for using an unsupported MC version.

This setting can NOT be disabled and will default to the following text when set to an empty list:
```
<red>This Server is running MC {version}! Please change your client version.
```

### Hover
> **Type**: `List (String)`  
> **Default**:
> ```yaml
> Hover:
> - '<red>You are using an unsupported version of Minecraft ({userVersion})!'
> - '<red>Please change your version to {version}.' 
> ```
> **Supported Formatting**:
> 
> - [Basic](#basic-formatting)

This is the text displayed when the player hovers with his cursor over the [Player Count](#playercount).

This text usually displays (random) online players on the network, but we can use it to display this custom text instead.  
You can set it to an empty list (`Hover: []`) to not change the Players shown.

### Motd
> **Type**: `List (String)`  
> **Default**:
> ```yaml
> Motd:
> - '<red>Unsupported Minecraft Version {userVersion}'
> - '<red>Please use <gray>{version}</gray>.'
> ```
> **Supported Formatting**:
> 
> - [Advanced](#advanced-formatting)

This text is shown in the MOTD when the server is displayed in the Server list of the client.

You can set this to an empty list (`Motd: []`) to not change the MOTD displayed.  
Only the first 2 lines will be used from the list.
