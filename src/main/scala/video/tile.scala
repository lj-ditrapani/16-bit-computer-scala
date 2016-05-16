package info.ditrapani.ljdcomputer.video

object Tile {
  type LargeTile = Vector[Vector[(Boolean, Boolean)]]
  type SmallTile = Vector[Vector[(Boolean, Boolean)]]
  type TextCharTile = Vector[Vector[Boolean]]

  def toBits(c: Char): Seq[Boolean] = {
    15 to 0 by -1 map { (i) => ((c >> i) & 1) > 0 }
  }

  def makeLargeTile(tile_ram: Vector[Char]): LargeTile = {
    assert(tile_ram.size == 32)

    def toPixelRow(c: Char): Iterator[(Boolean, Boolean)] = {
      toBits(c).grouped(2).map { (v) => (v(0), v(1)) }
    }

    tile_ram.grouped(2).map { (pair) =>
      (toPixelRow(pair(0)) ++ toPixelRow(pair(1))).toVector
    }.toVector
  }

  def makeSmallTile(tile_ram: Vector[Char]): SmallTile = {
    assert(tile_ram.size == 8)

    def toPixelRow(c: Char): Iterator[(Boolean, Boolean)] = {
      toBits(c).grouped(2).map { (v) => (v(0), v(1)) }
    }

    tile_ram.map { Vector() ++ toPixelRow(_) }.toVector
  }

  def makeTextCharTile(tile_ram: Vector[Char]): TextCharTile = {
    assert(tile_ram.size == 4)

    def toPixelRow(c: Int): Vector[Boolean] = {
      (7 to 0 by -1 map { (i: Int) => ((c >> i) & 1) > 0 }).to[Vector]
    }

    tile_ram.flatMap {
      (char) => Vector(toPixelRow(char >> 8), toPixelRow(char >> 0))
    }.toVector
  }
}
