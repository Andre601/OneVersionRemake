# Supported Protocols

Due to BungeeCord not offering a way to convert a player's protocol version into a readable MC version, does OneVersionRemake have its own system in place to do so.  
This system is used to replace `{version}` and `{userVersion}` with readable MC versions. Former will display a comma-separated list of the configured Protocol Versions while the later will display the Player's MC version.

To achieve this feature does OneVersionRemake pull from a JSON file which contains the necessary info. The source may differ depending on the version used:

- `v3.11.0` and newer: https://andre601.ch/oneversionremake/protocol_versions.json ([Source](https://codeberg.org/Andre601/website/src/branch/main/docs/oneversionremake/protocol_versions.json))
- `v3.9.0` through `v3.10.0`: https://andre601.ch/oneversionremake/versions.json ([Source](https://codeberg.org/Andre601/website/src/branch/main/docs/oneversionremake/versions.json))
- `v3.8.2` and older: https://raw.githubusercontent.com/Andre601/OneVersionRemake/master/versions.json ([Source](https://github.com/Andre601/OneVersionRemake/blob/master/versions.json))

You are free to customize the file to your liking by adding or removing entries from it and you can even [change the source URL to pull from](config.md#versionsurl), just keep the structure of the file in mind.

/// note
Any protocol version not listed in the JSON file will be displayed as `?`
///

## Versions

Below is a table of supported Protocol Versions which will be translated into their respective MC version or Major MC Version for the `{version}` and `{userVersion}` placeholder.  

/// note
The table is automatically created using the latest [`protocol_versions.json` file](https://andre601.ch/oneversionremake/protocol_versions.json) as source.  
Should you not see any table, make sure that you allow Javascript to be executed. Otherwise check above link for the current JSON file.
///

<div data-md-component="versions-table">
</div>