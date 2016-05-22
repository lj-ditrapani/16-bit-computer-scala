package info.ditrapani.ljdcomputer.video

object Tile {
  def toBits(c: Char): Seq[Boolean] = {
    15 to 0 by -1 map { (i) => ((c >> i) & 1) > 0 }
  }
}

object LargeTileSet {
  def apply(ram: Ram): LargeTileSet = {
    assert(ram.size == 2048)
    ram.grouped(32).map(LargeTile(_)).to[Vector]
  }
}

object LargeTile {
  def apply(tile_ram: Ram): LargeTile = {
    assert(tile_ram.size == 32)

    def toPixelRow(c: Char): Iterator[(Boolean, Boolean)] = {
      Tile.toBits(c).grouped(2).map { (v) => (v(0), v(1)) }
    }

    tile_ram.grouped(2).map { (pair) =>
      (toPixelRow(pair(0)) ++ toPixelRow(pair(1))).toVector
    }.toVector
  }
}

object SmallTileSet {
  def apply(ram: Ram): SmallTileSet = {
    assert(ram.size == 512)
    ram.grouped(8).map(SmallTile(_)).to[Vector]
  }
}

object SmallTile {
  def apply(tile_ram: Ram): SmallTile = {
    assert(tile_ram.size == 8)

    def toPixelRow(c: Char): Iterator[(Boolean, Boolean)] = {
      Tile.toBits(c).grouped(2).map { (v) => (v(0), v(1)) }
    }

    tile_ram.map { Vector() ++ toPixelRow(_) }.toVector
  }
}

object TextCharTileSet {
  def apply(ram: Ram): TextCharTileSet = {
    assert(ram.size == 512)
    ram.grouped(4).map(TextCharTile(_)).to[Vector]
  }
}

object TextCharTile {
  def apply(tile_ram: Ram): TextCharTile = {
    assert(tile_ram.size == 4)

    def toPixelRow(c: Int): Vector[Boolean] = {
      (7 to 0 by -1 map { (i: Int) => ((c >> i) & 1) > 0 }).to[Vector]
    }

    tile_ram.flatMap {
      (char) => Vector(toPixelRow(char >> 8), toPixelRow(char >> 0))
    }.toVector
  }
}
