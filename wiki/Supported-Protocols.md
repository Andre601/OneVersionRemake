[protocol_versions.json source]: https://github.com/Andre601/andre601.github.io/blob/development/site/oneversionremake/protocol_versions.json
[versions.json source]: https://github.com/Andre601/andre601.github.io/blob/development/site/oneversionremake/versions.json
[old versions.json source]: https://github.com/Andre601/OneVersionRemake/blob/master/versions.json

Due to BungeeCord not providing a way to obtain the MC version from a protocol does OneVersionRemake utilize a separate JSON file containing the MC versions and their major versions for the `{version}` and `{userVersion}` placeholders.

It pulls the JSON from the following sources:

- v3.11.0 and newer: https://andre601.ch/oneversionremake/protocol_versions.json ([Source][protocol_versions.json source])
- v3.9.0 to v3.10.0: https://andre601.ch/oneversionremake/versions.json ([Source][versions.json source])
- v3.8.2 and older: https://raw.githubusercontent.com/Andre601/OneVersionRemake/master/versions.json ([Source][old versions.json source])

You can always add your own protocol versions not present within the JSON file if they exist and if you follow the structure of the JSON file.  
Unknown/Unsupported versions will be displayed as `?`.

## Versions

Here is a complete list of all currently supported protocol versions based on the latest [`protocol_versions.json` file][protocol_versions.json source].

| Protocol: | Displayed version: |
|:---------:|:------------------:|
| 760       | 1.19.2             |
| 759       | 1.19               |
| 758       | 1.18.2             |
| 757       | 1.18.1             |
| 756       | 1.17.1             |
| 755       | 1.17               |
| 754       | 1.16.5             |
| 753       | 1.16.3             |
| 751       | 1.16.2             |
| 736       | 1.16.1             |
| 735       | 1.16               |
| 578       | 1.15.2             |
| 575       | 1.15.1             |
| 573       | 1.15               |
| 498       | 1.14.4             |
| 490       | 1.14.3             |
| 485       | 1.14.2             |
| 480       | 1.14.1             |
| 477       | 1.14               |
| 404       | 1.13.2             |
| 401       | 1.13.1             |
| 393       | 1.13               |
| 340       | 1.12.2             |
| 338       | 1.12.1             |
| 335       | 1.12               |
| 316       | 1.11.2             |
| 315       | 1.11               |
| 210       | 1.10.2             |
| 110       | 1.9.4              |
| 109       | 1.9.2              |
| 108       | 1.9.1              |
| 107       | 1.9                |
| 47        | 1.8.9              |
