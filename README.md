LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

Based on the LJD 16-bit Computer
[Specification](https://github.com/lj-ditrapani/16-bit-computer-specification).

This is a work in progress.

Start this program from the command line.

See the help text for usage [src/main/resources/help.txt](src/main/resources/help.txt)


TODO
----

- Check green color conversion in spec for buffer method (8 bit -> 24 bit)
- make a nice test program with video

Overall steps
- video, built-in video rom
- video buffer
- Computer step function
    - swap ram
- gamepad (multi-threaded?)
- cpu
