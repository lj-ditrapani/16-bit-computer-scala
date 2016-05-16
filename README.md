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

- Split VideoState file into video package sub files (tile, cell, sprite color)
- Make base Spec class that all spec classes inherit from; refactor
- VideoState.make(tiles, cells, colors, sprites)
- components of VideoState
    - large_tiles: Vector[LargeTile],
    - small_tiles: Vector[SmallTile],
    - text_char_tiles: Vector[TextCharTile],
    - bg_cells: Vector[Vector[BgCell]],
    - text_cells: Vector[Vector[TextCell]],
    - bg_colors: Vector[(Color8, Color8)],
    - fg_colors: Vector[(Color8, Color8)],
    - large_sprites: Vector[Sprite],
    - small_sprites: Vector[Sprite]
- VideoState.make
    - expload ram into ram blocks
    - make Vector of components from ram block
      (makeVector method on component object)
    - make component from ram cells
      (make method on component object)
