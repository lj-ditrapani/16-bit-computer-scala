package info.ditrapani.ljdcomputer.video

object TileSet {
  def apply(ram: Ram): TileSet = {
    assert(ram.size == 256 * 6)
    ram.grouped(6).map(TextCharTile(_)).to[Vector]
  }
}

object Tile {
  def apply(tile_ram: Ram): Tile = {
    assert(tile_ram.size == 6)

    def toPixelRow(c: Int): Vector[Boolean] = {
      (7 to 0 by -1 map { (i: Int) => ((c >> i) & 1) > 0 }).to[Vector]
    }

    tile_ram.flatMap {
      (char) => Vector(toPixelRow(char >> 8), toPixelRow(char >> 0))
    }.toVector
  }
}
