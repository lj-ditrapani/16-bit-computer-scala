LJD 16-bit Computer in Scala & JavaFX
-------------------------------------

Based on the LJD 16-bit Computer
[Specification](https://github.com/lj-ditrapani/16-bit-computer-specification).

This is a work in progress.

Start this program from the command line.

See the help text for usage [src/main/resources/help.txt](src/main/resources/help.txt)


TODO
----

Overall steps
- cpu needs carry & overflow flags (and Controller/Executor as well)
- cpu
- Computer step function
    - interrupt enable & vector (not double buffered)
- gamepad (separate thread?)
- may need to put computation in a service or task
  that runs in a separate thread?
