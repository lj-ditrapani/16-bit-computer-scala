package info.ditrapani.ljdcomputer.video

case class Cell(
  background_color: Byte,
  foreground_color: Byte,
  tile_index: Byte
)

object Cell {
  def apply(word: Char): Cell = {
    val (background_color, foreground_color, tile_index) =
      getColorsAndIndex(word)
    Cell(background_color, foreground_color, tile_index)
  }

  def getColorsAndIndex(word: Char): (Byte, Byte, Byte) = {
    val background_color = (word >> 12).toByte
    val foreground_color = ((word >> 8) & 0xF).toByte
    val tile_index = (word & 0xFF).toByte
    (background_color, foreground_color, tile_index)
  }
}

object CellGrid {
  def apply(ram: Ram): CellGrid = {
    assert(ram.size == 640)
    ram.map(Cell(_)).grouped(32).to[Vector]
  }
}
