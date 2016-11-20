LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

Based on the LJD 16-bit Computer
[Specification](https://github.com/lj-ditrapani/16-bit-computer-specification).

This is a work in progress.

Start this program from the command line.

See the help text for usage [src/main/resources/help.txt](src/main/resources/help.txt)


TODO
----

- Only use apply method as aux constructors in companion objects

- video object built_in_tiles has a lot of duplicate parsing code with
  bin-file-reader.  Refactor
- Complete VideoState buffer method
- Clean up Video buffer method
- makes specs for buffer method?

Overall steps
- video, built-in video
- video buffer
- gamepad
- cpu
