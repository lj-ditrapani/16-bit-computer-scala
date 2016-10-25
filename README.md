LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

[Secification](https://github.com/lj-ditrapani/16-bit-computer-specification)

Just started working on this.

cli interface

    java -jar ljd-16-bit-computer-x.x.x.jar --f=binary-executable.bin

Show help

    java -jar ljd-16-bit-computer-x.x.x.jar --help

Help text [src/main/resources/help.txt](src/main/resources/help.txt)


TODO
----

- Remove use of === in specs
- Only use apply method as aux constructors in companion objects

- video object built_in_tiles has a lot of duplicate parsing code with
  bin-file-reader.  Refactor
- video object built_in_tiles actually should be loaded from resources
  like in game-of-life.
- Complete VideoState buffer method
- Clean up Video buffer method
- makes specs for buffer method?

Overall steps
- video, built-in tiles
- video buffer
- gamepad
- cpu
