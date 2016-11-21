LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

Based on the LJD 16-bit Computer
[Specification](https://github.com/lj-ditrapani/16-bit-computer-specification).

This is a work in progress.

Start this program from the command line.

See the help text for usage [src/main/resources/help.txt](src/main/resources/help.txt)


TODO
----

- create concrete types instead of type alias for collections
    - Colors as a class instead of alias to Vector[Color8]
- make specs for buffer method

Overall steps
- video, built-in video rom
- video buffer
- gamepad (multi-threaded?)
- cpu
