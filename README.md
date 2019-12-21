[OneVersion]: https://github.com/johnnywoof/OneVersion
[Spigot]: https://spigotmc.org/resources/71727/
[Wiki]: https://github.com/Andre601/OneVersionRemake/wiki
[Jenkins]: https://ci.codemc.io/view/Author/job/Andre601/job/OneVersionRemake/

# OneVersionRemake
BungeeCord plugin based on the original [OneVersion] with improvements in terms of configuration and coding standards.

## Changes compared to OneVersion
- Improved code.  
OneVersion used some questionable methods and ways to perform things and was just a "single class" plugin, which is never good.  
This plugin improves that.
- Changed configuration.  
OneVersion used a useless placeholder for new lines which made the config look weird.  
OneVersionRemake changed this and has an easier way to set messages.
- New options to display text to.  
OneVersionRemake allows to display text in the MOTD, player count and the hover of said player count.
- Support for selected MC releases.  
The plugin adds a new `{version}` placeholder which when using it with a supported protocol version displays the supported/used MC version.  
E.g. the protocol 575 would change `{version}` to `1.15.1`
- Deny any version that isn't version X.  
Do you only want to allow players with 1.12? Set the protocol and enable the "Exact" mode and you're done!

# Links
- [Spigot]
- [Wiki]
- [Jenkins (Dev builds)][Jenkins]