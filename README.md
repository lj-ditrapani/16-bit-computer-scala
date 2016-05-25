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

- bytePair2Char(pair: Array[Byte]): Char
  Broken on negative bytes, for example,
  byte pair Array(0xFF, 0xFF) returns char 0xFEFF
  byte pair Array(0xAA, 0xAA) returns char 0xA9AA

- Use Vector.tabulate for disabled buffer?
- Buffer could use javaFX PixelWrite/PixelReader
- 2 Buffer approach
    - 4 passes: Do each entire layer, then combine the 4 layers
    - 1 pass:   Do 1 pixel at a time; all 4 pixels then combine to 1 pixel, go to next pixel
- Mutable vs immutable work buffer for buffer creation.
    - seems like it would be easier to use a mutable buffer to create the buffer.
      Then can do one entire cell at a time.  Not limited to work on an entire
      buffer row at a time.
    - or do one row in the buffer at a time, selecting the right tile & tile
      row & cell & cell row for each 8 pixels
- Complete VideoState buffer method
- Video:  make built-in tiles
- Clean up Video buffer method
- Extra type defs (tile, etc) in Video class; shouldn't be there?
