package info.ditrapani.ljdcomputer.video

object Cell {
  case class BgCell(
    color_pair_1: Byte,
    color_pair_2: Byte,
    x_flip: Boolean,
    y_flip: Boolean,
    large_tile_index: Byte
  )

  def makeBgCell(word: Char): BgCell = {
    val (color_pair_1, color_pair_2, x_flip, y_flip, tile_index) =
      VideoState.getColorXYandIndex(word)
    BgCell(color_pair_1, color_pair_2, x_flip, y_flip, tile_index)
  }

  case class TextCharCell(
    on: Boolean,
    text_char_tile_index: Byte
  )

  def makeTextCharCell(byte: Int): TextCharCell = {
    assert(byte < 256)
    assert(byte > -1)
    TextCharCell((byte >> 7) > 0, (byte & 0x7F).toByte)
  }
}
