# Config.yml

OneVersionRemake's `config.yml` offers various options to configure. This page explains them in detail.

## Contents

- [Settings](#settings)
    - [UpdateVersions](#updateversions)
    - [VersionsUrl](#versionsurl)
- [Protocol](#protocol)
    - [Versions](#versions)
    - [LogDenial](#logdenial)
    - [MajorOnly](#majoronly)
    - [Blacklist](#blacklist)
- [Messages](#messages)
    - [Supported Formatting](#supported-formatting)
    - [Available Placeholders](#available-placeholders)
    - [PlayerCount](#playercount)
    - [Kick](#kick)
    - [Hover](#hover)
    - [Motd](#motd)

## Settings

Main settings of the plugin that don't fit any of the other sections.

### UpdateVersions

/// info |
**Type:** `Boolean`  
**Default:**  
```yaml
Settings:
  UpdateVersions: true
```
///

Wether OneVersionRemake should update the versions file when enabling itself.  
Only updates should there be a newer version at the source.

### VersionsUrl

/// info |
**Type:** `String`  
**Default:**  
```yaml
Settings:
  VersionsUrl: 'https://www.andre601.ch/oneversionremake/protocol_versions.json'
```
///

The URL OneVersionRemake should use to check for an updated file.  
Should you change the URL, make sure the new URL follows these requirements:

- Content-type returned is `application/json`
- The returned Content is a valid JSON file
- [[Since v3.11.0](https://github.com/Andre601/OneVersionRemake/releases/tag/v3.11.0)] The file contains a `file_version` Number option and a `protocols` Array.

## Protocol

Protocol-related options can be found here. This also includes the option to set what Client versions should (not) join your Server.

### Versions

/// info |
**Type:** `List[Integer]`  
**Default:**  
```yaml
Protocol:
  Versions: []
```
///

/// note
You need to use protocol versions and not the MC version.  
As an example, to support all 1.21 versions (State: 15th of february 2025) will you need to use the values `767`, `768` and `769`.

A full list of all available Protocol Versions can be found at https://minecraft.wiki/w/Protocol_version#Java_Edition

Be aware that only a selected number of versions may be translatable into readable MC versions by the plugin. Please see the [Supported Protocols](protocols.md) page for more.
///

Sets the list of allowed client versions that can join the Server.  
Should [Blacklist](#blacklist) be enabled will the list be treated as Client versions that should not join your Server.

### LogDenial

/// info |
**Type:** `Boolean`  
**Default:**  
```yaml
Protocol:
  LogDenial: true
```
///

Wether OneVersionRemake should log denied joins for players with unsupported Protocol Versions.

When enabled will the plugin post the following message for every denied Player:  
```
[OneVersionRemake] Denied login for Player {player} with MC version {version} (Protocol version {protocol})
```

### MajorOnly

/// info |
**Type:** `Boolean`  
**Default:**  
```yaml
Protocol:
  MajorOnly: false
```
///

Wether OneVersionRemake should only display the major version for a set of protocols in the `{version}` placeholder.

/// details | Example
    type: example

//// tab | MajorOnly enabled
**Setup:**  
```yaml
Protocol:
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
  MajorOnly: true
```

**`{version}` Output:**  
```
1.14.x, 1.15.x, 1.16.x
```
////

//// tab | MajorOnly disabled
**Setup:**  
```yaml
Protocol:
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
  MajorOnly: false
```

**`{version}` Output:**  
```
1.14, 1.14.2, 1.14.3, 1.14.4, 1.15, 1.15.1, 1.15.2, 1.16, 1.16.1, 1.16.2, 1.16.3, 1.16.5
```
////
///

### Blacklist

/// info |
**Type:** `Boolean`  
**Default:**  
```yaml
Protocol:
  Blacklist: false
```
///

Wether the Provided Protocol List should be treated as a Blacklist.  
When enabled will OneVersionRemake deny access to your Server should the Player use any of the versions set.

## Messages

The Messages section contains all the options for customizing the different texts displayed to a player with a denied version.

### Supported Formatting

Please note that OneVersionRemake uses `MiniMessage` for formatting, meaning that instead of color and formatting codes such as `&1` and `&l` would you use `<dark_blue>` and `<bold>` respectively.  
This choice was made deliberately, as MiniMessage allows a much more complex formatting without sacrificing readability in the process (See the ugly aproach of making gradients with color codes).

As a final note, be aware that the following options **cannot** be used, no matter where they are used:

- Click actions (`<click:_action_:_value_>`). They won't perform anything on click.
- Custom Fonts (`<font:_name_>`). May work if the client already has the resource pack loaded.
- Hover text (`<hover:_action_:_value_>`). Won't display anything on hover.
- Text insertion (`<insert:_text_>`). Won't do anything on click.

### Available Placeholders

OneVersionRemake provides the following Placeholders:

| Placeholder     | Description                                                                                                                |
|-----------------|----------------------------------------------------------------------------------------------------------------------------|
| `{version}`     | Gets converted to a comma-separated list of MC versions based on the [protocol versions](#versions) you set.               |
|                 | Should [MajorOnly](#majoronly) be true will only the major versions be listed.                                             |
| `{userVersion}` | Displays the Player's MC version. Should the player use a version not in OVR's Version JSON file, will `???` be displayed. |

### PlayerCount

/// info |
**Type:** `String`  
**Default:**  
```yaml
Messages:
  PlayerCount: '<red>Minecraft {version}'
```  
**Supported formatting:**

- Basic colors (`<blue>`, `<red>`, etc.)
- Formatting
///

Modifies the text that usually displays the online and total number of players for the server.  
Set this to an empty String (`#!yaml PlayerCount: ''`) to not modify the Player count.

You can also set it to just a color/formatting code (i.e. `#!yaml PlayerCount: '<red>'`) to hide the player count.

### Kick

/// info |
**Type:** `List[String]`  
**Default:**  
```yaml
Messages:
  Kick:
    - '<red>You are using an unsupported version of Minecraft ({userVersion})!'
    - '<red>This server supports the following Version(s):'
    - '<grey>{version}'
    - ''
    - '<red>Please change your Minecraft Version and try again.' 
```  
**Supported formatting:**

- Hex Colors (`<#rrggbb>`, includes `<gradient:_start_:_end_>`)
- Basic colors (`<blue>`, `<red>`, etc.)
- Formatting (`<bold>`, `<italic>`, etc.)
///

Sets the Message to display when OneVersionRemake kicks the player (Denies the join).

Note that this option can **not** be disabled and will default to the following kick message when removed or set empty:  
```
<red>This Server is running MC {version}! Please change your client version.
```

### Hover

/// info |
**Type:** `List[String]`  
**Default:**  
```yaml
Messages:
  Hover:
    - '<red>You are using an unsupported version of Minecraft ({userVersion})!'
    - '<red>Please change your version to {version}.'
```  
**Supported formatting:**

- Basic colors (`<blue>`, `<red>`, etc.)
- Formatting (`<bold>`, `<italic>`, etc.)
///

This sets the text that is displayed when the player hovers over the Player count, which usually displays players online on the server.  
Set this to an empty List (`#!yaml Hover: []`) to not alter the Hover.

### Motd

/// info |
**Type:** `List[String]`  
**Default:**  
```yaml
Messages:
  Motd:
    - '<red>Unsupported Minecraft Version {userVersion}'
    - '<red>Please use <grey>{version}</grey>.'
```  
**Supported formatting:**

- Hex Colors (`<#rrggbb>`, includes `<gradient:_start_:_end_>`)
- Basic colors (`<blue>`, `<red>`, etc.)
- Formatting (`<bold>`, `<italic>`, etc.)
///

Sets the text to display in the Server's MOTD.  
Only the first two lines are considered and every additional one will be ignored.

Set to an empty List (`#!yaml Motd: []`) to not modify the MOTD.
