package info.ditrapani.ljdcomputer.video

final case class Cell(
  private val background_color: Byte,
  private val foreground_color: Byte,
  private val tile_index: Byte
) {
  def getBackgroundColor: Int = background_color & 0xFF

  def getForegroundColor: Int = foreground_color & 0xFF

  def getTileIndex: Int = tile_index & 0xFF
}

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

class CellGrid(ram: Ram) {
  assert(ram.size == 640)
  val rows = ram.map(Cell(_)).grouped(32).to[Vector]
}
