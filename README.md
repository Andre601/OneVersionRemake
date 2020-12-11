[OneVersion]: https://github.com/johnnywoof/OneVersion
[Spigot]: https://spigotmc.org/resources/71727/
[Wiki]: https://github.com/Andre601/OneVersionRemake/wiki
[Jenkins]: https://ci.codemc.io/view/Author/job/Andre601/job/OneVersionRemake/

# OneVersionRemake
BungeeCord plugin based on the original [OneVersion] with improvements in terms of configuration and coding standards.

## Changes compared to OneVersion
- Improved code.  
OneVersionRemake completely rewrote the original plugin and improved it to also support Velocity!
- Changed configuration.  
The configuration has been rewritten to provide useful information alongside several options to display text.
- Support for selected MC releases.  
The plugin adds a new `{version}` and `{clientVersion}` placeholder which when using it with a supported protocol version displays the supported/used MC version.  
E.g. the protocol 575 would change `{version}` to `1.15.1`
- Only allow the versions YOU want!  
You can list as many protocol versions as you want. Any player not having any of these will be denied access.

# Links
- [Spigot]
- [Wiki]
- [Jenkins (Dev builds)][Jenkins]
