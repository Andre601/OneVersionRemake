[oneversion]: https://github.com/johnnywoof/OneVersion
[wiki]: https://github.com/Andre601/OneVersionRemake/wiki
[releases]: https://github.com/Andre601/OneVersionRemake/releases

<a href="https://modrinth.com/plugin/oneversionremake" target="_blank">
<img src="https://rawcdn.githack.com/Andre601/OneVersionRemake/997bd22eaa8f57b26e44ca17f86251b43c425ac6/wiki/images/ovr.png" width="200" align="right" alt="OneVersionRemake">
</a>

# OneVersionRemake

OneVersionRemake is a plugin for Velocity and BungeeCord based on the original concept from [OneVersion] but with a lot of changes and improved code.

## Changes from the original

OneVersionRemake has a few specifici differences that make it better than the original:

- **Improved Code**  
  The Code has been improved. The plugin no longer has everything in a single class and instead has things split up for better readability and understanding.  
  Additionally are commonly used parts shared between the different plugin variants to reduce duplicate code.
- **Better configuration**  
  The configuration received a general overhaul.  
  Not only is Configurate used for better config handling, but new features and settings have been added for you to use. No longer is a weird placeholder needed to add a line-break.
- **Placeholders to display MC Versions**  
  OneVersionRemake uses a JSON file containing known MC protocols and their versions.  
  This allows you to use placeholders such as `{version}` and `{clientVersion}` in messages to display the Network's supported version and the client's used version respectively.  
  The JSON file is automatically updated on proxy restarts (Can be disabled) and you can modify it to change the text displayed or add your own MC versions.
- **Allow the versions you want**  
  The configuration is very simple to understand. Just add the version protocols you want to allow on your network, reload OneVersionRemake and enjoy.  
  The [Wiki] has a page listing all supported version protocols.

## Downloads
<a href="https://modrinth.com/plugin/oneversionremake" target="_blank">
<img alt="modrinth" title="Download from Modrinth" height="64" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/available/modrinth_64h.png">
</a>
<a href="https://spigotmc.org/resources/71727/" target="_blank">
<img alt="spigot" title="Download from Spigot" height="64" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/supported/spigot_vector.svg">
</a>
<a href="https://ci.codemc.io/view/Author/job/Andre601/job/OneVersionRemake/" target="_blank">
<img alt="codemc" title="Dev builds on CodeMC" height="64" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/available/codemc_vector.svg">
</a>

## Build it yourself

You can build your own version of OneVersionRemake from this source.

### Requirements

You need:

- Java (Obviously)
- Maven
- Git-based terminal (i.e. Git-scm for windows)

### Clone the repository

To clone the repository, first choose where you want to store the folder and open your terminal in there (For windows will you need to use Git Bash).  
Next, run the following command:

```bash
git clone https://github.com/Andre601/OneVersionRemake
```

After everything has been cloned, head over to the now generated `OneVersionRemake` folder by using `cd OneVersionRemake` in your terminal.

### Build the jars

Now you can build the jar files. Just execute `mvn clean install` and the jars should be build within a few minutes (Depends on your download speed).  
Afterwards can you obtain the final jar from either `bungeecord/target/` or `velocity/target/`.

Note: Make sure to use the jar file either labeled `OneVersionRemake-BungeeCord-<version>.jar` or `OneVersionRemake-Velocity-<version>.jar`. Do not use jars containing `original` in their name!

## Contribute

Contributions towards OneVersionRemake are always welcome when it helps improving the plugin in one way or another.

### Modules

The project is split into 4 different modules which all have their own purpose.

#### `core`

The `core` module contains all code that is used on either version of OneVersionRemake (That is platform-agnostic).

Your primary goal when adding new stuff to OneVersionRemake should be to make the code as platform-independand as possible. This helps to reduce duplicate code. So whenever possible, add new code to this module first and only to the other ones when it can't be avoided.

#### `bungeecord`

This module contains all the code used on BungeeCord and that can't be put into the core module itself.

When adding new BungeeCord features to OneVersionRemake, make sure that it also supports popular forks such as Waterfall, Flamecord, etc.

#### `velocity`

This module contains code used for making the Velocity version of OneVersionRemake.  
The currently used major API version of Velocity is v3 and the old v1 API is not supported anymore, so please avoid adding v1 support here.

## Links
<a href="https://github.com/Andre601/OneVersionRemake/wiki">
<img alt="ghpages" title="Read the wiki" height="64" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/documentation/ghpages_vector.svg">
</a>
<a href="https://discord.gg/6dazXp6" target="_blank">
<img alt="discord" title="Join my Discord Server" height="64" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/discord-singular_vector.svg">
</a>
<a href="https://app.revolt.chat/invite/74TpERXA" target="_blank">
<img alt="revolt" title="Join my Revolt Server" height="64" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/revolt-singular_vector.svg">
</a>
<a href="https://blobfox.coffee/@andre_601" target="_blank">
<img alt="mastodon" title="Chat with me on Mastodon" height="64" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/mastodon-singular_vector.svg">
</a>
