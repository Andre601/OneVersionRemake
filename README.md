[OneVersion]: https://github.com/johnnywoof/OneVersion
[Spigot]: https://spigotmc.org/resources/71727/
[Wiki]: https://github.com/Andre601/OneVersionRemake/wiki
[Jenkins]: https://ci.codemc.io/view/Author/job/Andre601/job/OneVersionRemake/

# OneVersionRemake
BungeeCord plugin based on the original [OneVersion] with improvements in terms of configuration and coding standards.

## Changes compared to OneVersion
- **Improved code**  
  OneVersionRemake completely rewrote the original plugin and improved it to also support Velocity!
- **Changed configuration**  
  The configuration has been rewritten to provide useful information alongside several options to display text.
- **Support for selected MC releases**  
  The plugin adds a new `{version}` and `{clientVersion}` placeholder which when using it with a supported protocol version displays the supported/used MC version.  
  E.g. the protocol 575 would change `{version}` to `1.15.1`
- **Only allow the versions YOU want!**  
  You can list as many protocol versions as you want. Any player not having any of these will be denied access.

## Build it yourself
If you want to build the plugin yourself can you just clone this repository to your Desktop using following command:

```
git clone https://github.com/Andre601/OneVersionRemake
```

After that head over to the new folder using `cd OneVersionRemake` and then execute `mvn clean Install`  
You should find the plugin jars in `bungeecord/target`, `velocity-legacy/target` and `velocity/target` respectively (Make sure to NOT use the ones having "original" in their name).

## Contribute
Any contribution is welcome when it helps improving the plugin's performance.  
Just make sure the code is readable and easy to understand for others.

### Modules
The project is split into 4 different modules which all have their own purpose.

#### `core`
The `core` module contains platform-independent code that will be used by the other modules.  
Whenever you add a new feature that should be used on BungeeCord AND Velocity should you first try to make it platform-independent by adding it to this module.

#### `bungeecord`
BungeeCord related code.  
The jar build in this module *should* support BungeeCord, Waterfall, Travertine and FlameCord.  
No guarantee is given for the last two forks.

#### `velocity`
This module contains the code required to build a Jar supporting Velocity 3.0.0  
This version of Velocity is still under heavy development, and you're not recommended using it for production.

#### `velocity-legacy`
This module contains the code originally made for Velocity 1.x  
It is recommended to use Jars for this version of Velocity for Production enviroments.

## Links
- [Spigot]
- [Wiki]
- [Jenkins (Dev builds)][Jenkins]
