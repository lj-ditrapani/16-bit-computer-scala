LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

Based on the LJD 16-bit Computer
[Specification](https://github.com/lj-ditrapani/16-bit-computer-specification).

This is a work in progress.

Start this program from the command line.

See the help text for usage [src/main/resources/help.txt](src/main/resources/help.txt)


TODO
----

- Fix Cell
    - tile index is a byte; which .toInt does not return 0-255; (ex: 255 -> -1)
    - Change class to store byte internally, but give api that returns proper int
    - same for bg & fg colors
- make specs for buffer method
- make a nice test program with video

Overall steps
- video, built-in video rom
- video buffer
- Computer step function
    - swap ram
- gamepad (multi-threaded?)
- cpu
