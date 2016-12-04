LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

Based on the LJD 16-bit Computer
[Specification](https://github.com/lj-ditrapani/16-bit-computer-specification).


Status
------

The cpu and video are working.
The computer can load and run programs and display color output
on the virtual screen.  The gamepad will be implemented next.
Storage and networking are not implemented.


Usage
-----

Start this program from the command line.

See the help text for usage [src/main/resources/help.txt](src/main/resources/help.txt)


TODO
----

- gamepad (separate thread?)
- may need to put computation in a service or task
  that runs in a separate thread?
- automated full program test that manipulates screen (single-frame)
    - should replace need to do manual visual test
- automated full program test that manipulates screen (multi-frame)
