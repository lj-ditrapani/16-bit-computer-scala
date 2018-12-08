package info.ditrapani.ljdcomputer.video

class TileSet(ram: Ram) {
  assert(ram.size == 1024 + 512)
  val vector: Vector[Tile] = ram.grouped(6).map(new Tile(_)).to[Vector]
}

class Tile(tile_ram: Ram) {
  assert(tile_ram.size == 6)
  val rows: Vector[Vector[Boolean]] = tile_ram.flatMap {
    (char) => Vector(toPixelRow(char >> 8), toPixelRow(char >> 0))
  }.toVector

  private def toPixelRow(c: Int): Vector[Boolean] = {
    (7 to 0 by -1 map { (i: Int) => ((c >> i) & 1) > 0 }).to[Vector]
  }
}
