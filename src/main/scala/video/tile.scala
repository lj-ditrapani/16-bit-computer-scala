package info.ditrapani.ljdcomputer.video

object Tile {
  type LargeTileSet = Vector[LargeTile]
  type SmallTileSet = Vector[SmallTile]
  type TextCharTileSet = Vector[TextCharTile]
  type LargeTile = Vector[Vector[(Boolean, Boolean)]]
  type SmallTile = Vector[Vector[(Boolean, Boolean)]]
  type TextCharTile = Vector[Vector[Boolean]]
  type Ram = Vector[Char]

  def makeLargeTileSet(ram: Ram): LargeTileSet = {
    assert(ram.size == 2048)
    ram.grouped(32).map(makeLargeTile(_)).to[Vector]
  }

  def makeSmallTileSet(ram: Ram): SmallTileSet = {
    assert(ram.size == 512)
    ram.grouped(8).map(makeSmallTile(_)).to[Vector]
  }

  def makeTextCharTileSet(ram: Ram): TextCharTileSet = {
    assert(ram.size == 512)
    ram.grouped(4).map(makeTextCharTile(_)).to[Vector]
  }

  def makeLargeTile(tile_ram: Ram): LargeTile = {
    assert(tile_ram.size == 32)

    def toPixelRow(c: Char): Iterator[(Boolean, Boolean)] = {
      toBits(c).grouped(2).map { (v) => (v(0), v(1)) }
    }

    tile_ram.grouped(2).map { (pair) =>
      (toPixelRow(pair(0)) ++ toPixelRow(pair(1))).toVector
    }.toVector
  }

  def makeSmallTile(tile_ram: Ram): SmallTile = {
    assert(tile_ram.size == 8)

    def toPixelRow(c: Char): Iterator[(Boolean, Boolean)] = {
      toBits(c).grouped(2).map { (v) => (v(0), v(1)) }
    }

    tile_ram.map { Vector() ++ toPixelRow(_) }.toVector
  }

  def makeTextCharTile(tile_ram: Ram): TextCharTile = {
    assert(tile_ram.size == 4)

    def toPixelRow(c: Int): Vector[Boolean] = {
      (7 to 0 by -1 map { (i: Int) => ((c >> i) & 1) > 0 }).to[Vector]
    }

    tile_ram.flatMap {
      (char) => Vector(toPixelRow(char >> 8), toPixelRow(char >> 0))
    }.toVector
  }

  def toBits(c: Char): Seq[Boolean] = {
    15 to 0 by -1 map { (i) => ((c >> i) & 1) > 0 }
  }
}
