package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

case class VideoState(
  large_tiles: Vector[LargeTile],
  small_tiles: Vector[SmallTile],
  text_char_tiles: Vector[TextCharTile],
  bg_cells: Vector[Vector[BgCell]],
  text_cells: Vector[Vector[TextCharCell]],
  bg_colors: Vector[(Color8, Color8)],
  fg_colors: Vector[(Color8, Color8)],
  large_sprites: Vector[Sprite],
  small_sprites: Vector[Sprite]
) {
  def buffer: VideoBuffer = Vector(Vector())
}

object VideoState {
  type Ram = Vector[Char]

  /*
  def make(tiles: Ram, cells: Ram, colors: Ram, sprites: Ram): VideoState = {
    VideoState(Vector(), Vector(), Vector(), Vector(Vector())
  }
  */

  def getColorXYandIndex(word: Char): (Byte, Byte, Boolean, Boolean, Byte) = {
    val color_pair_1 = (word >> 12).toByte
    val color_pair_2 = ((word >> 8) & 0xF).toByte
    val x_flip = ((word >> 7) & 1) > 0
    val y_flip = ((word >> 6) & 1) > 0
    val tile_index = (word & 0x3F).toByte
    (color_pair_1, color_pair_2, x_flip, y_flip, tile_index)
  }
}
