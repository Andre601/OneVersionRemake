[OneVersion]: https://github.com/johnnywoof/OneVersion

# OneVersionRemake
Improved BungeeCord plugin of the original [OneVersion] that improves in terms of configuration and coding standards.

## What is different?
First of all is it not just a "Single class" plugin, meaning that event listeners aren't in the same class as the main code.  
The next change is, that it also comes with an improved config, that is a bit easier to understand and use.

This plugin even introduced a new placeholder: `{version}`  
This placeholder can be used in the ProtocolName and KickMessage to display the "friendly name" of a protocol (e.g. 1.14.4)

## Features
- Block/Deny connections to your BungeeCord from players with versions older than your configured protocol version.
- Show the "Outdated Client" message (Next to the ping icon) when the player uses an older version.
- Show a custom Disconnect message.