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
- cpu
    - branch specs & code refactor
      - both brv brf match on jump_result
    - Controller specs
      - end instruction stops cpu
      - sets the instruction_counter
        (branch to jump location, normal to instruction_counter + 1)
    - Cpu specs
    - Whole program tests
    - cpu.runFrame should not take a n: Int parameter; hard code
- refactor multiple implementations of bool2int
- refactor multiple implementations of b("01010") => Int
- refactor multiple implementations "$" + toHexString.toUpperCase
- Computer step function
    - interrupt enable & vector (not double buffered)
- gamepad (separate thread?)
- may need to put computation in a service or task
  that runs in a separate thread?
