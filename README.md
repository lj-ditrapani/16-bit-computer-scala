LJD 16-bit Computer in Scala & JavaFX

Just started; not working yet.

Planned cli interface

    java -jar ljd-computer.jar --f=binary-executable.bin

Show help

    java -jar ljd-computer.jar --help


TODO
----

- Use immutable Vector instead of Array for program binary
- Use immutable Vector for ROM (duh!)
- RAM: Vector or Array...not sure which.  Each instruction that writes to RAM would generate

    512 bytes on 32 bit machine (4 nodes * 32 word * 4 bytes)
    1 KB      on 64 bit machine (4 nodes * 32 words * 8 bytes)
    of garbage collection
    100 ms -> 195 MB or 390 MB

- How do I set the name of the assembly jar?
- write help text/usage in main/resources/help.txt


2 approaches:

[Approach 1] Have a separate worker thread, distinct from the javaFX thread, to do the actual work
- tstart = current time
- run 400 K instructions
- swap video ram
- render 240 x 256 matrix of 24-bit pixels
- call run later with final buffer
- schedule next job to run in 100 ms - (current time - tstart)


[Approach 2] Do everything on javaFX thread
- Use animation loop, whenever time_delta >= 100 ms; reset start_time and do:
- run 400 K instructions
- swap video ram
- render 240 x 256 matrix of 24-bit pixels
- call run later with final buffer
