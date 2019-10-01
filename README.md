[OneVersion]: https://github.com/johnnywoof/OneVersion

# NewOneVersion
Improved BungeeCord plugin of the original [OneVersion] that improves in terms of configuration and coding standards.

## Differences to the original
This version has a few differences to the original.

### Config
The config has some minor changes. Mainly the kick message which is now a list instead of a string, getting rid of the `.newline.` placeholder.  
It also implements the `{version}` placeholder which tells the player what (minimum) version they need to use.

### Code
The code got slightly improved, making it not a "Single class" plugin.  
It also has some fail-saves in place to prevent issues if f.e. the config couldn't be loaded/generated or the Protocol is set to -1.