package info.ditrapani.ljdcomputer.video

case class BgCell(
  color_pair_1: Byte,
  color_pair_2: Byte,
  x_flip: Boolean,
  y_flip: Boolean,
  large_tile_index: Byte
)

object BgCell {
  def apply(word: Char): BgCell = {
    val (color_pair_1, color_pair_2, x_flip, y_flip, tile_index) =
      VideoState.getColorXYandIndex(word)
    BgCell(color_pair_1, color_pair_2, x_flip, y_flip, tile_index)
  }
}

object BgCellGrid {
  def apply(ram: Ram): BgCellGrid = {
    assert(ram.size == 240)
    ram.map(BgCell(_)).grouped(16).to[Vector]
  }
}

case class TextCharCell(
  on: Boolean,
  text_char_tile_index: Byte
)

object TextCharCell {
  def apply(byte: Int): TextCharCell = {
    assert(byte < 256)
    assert(byte > -1)
    TextCharCell((byte >> 7) > 0, (byte & 0x7F).toByte)
  }
}

object TextCharCellGrid {
  def apply(ram: Ram): TextCharCellGrid = {
    assert(ram.size == 480)
    val bytes = ram.flatMap((char) => Vector(char >> 8, char & 0xFF))
    bytes.map(TextCharCell(_)).grouped(32).to[Vector]
  }
}
