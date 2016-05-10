package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

case class Video(video_ram: Vector[Char], enable: Boolean, custom_tiles: Boolean) {
  type VideoBuffer = Vector[Vector[Color]]

  def buffer: VideoBuffer =
    if (enable)
      // if custom_tiles use ram tile-set, else use built-in tile-set
      (for (i <- 0 until 240) yield {
        (for (j <- 0 until 256) yield Color.rgb(200, 200, 255)).to[Vector]
      }).to[Vector]
    else
      Video.disabledBuffer
}

object Video {
  type VideoBuffer = Vector[Vector[Color]]
  type LargeTile = Vector[Vector[(Boolean, Boolean)]]
  type SmallTile = Vector[Vector[(Boolean, Boolean)]]
  type TextCharTile = Vector[Vector[Boolean]]

  def make(ram: Vector[Char]): Video = {
    val enable_bits = ram(0xDDF3)
    val enable: Boolean = (enable_bits & 2) > 0
    val custom_tiles: Boolean = (enable_bits & 4) > 0
    val video_ram = ram.slice(0xF000, 0x10000)
    Video(video_ram, enable, custom_tiles)
  }

  val disabledBuffer: VideoBuffer =
    (for (i <- 0 until 240) yield {
      (for (j <- 0 until 256) yield Color.rgb(0, 0, 0)).to[Vector]
    }).to[Vector]

  case class VideoRam(
    large_tiles: Vector[LargeTile],
    small_tiles: Vector[SmallTile],
    text_char_tiles: Vector[TextCharTile],
    bg_cells: Vector[Vector[BgCell]],
    text_cells: Vector[Vector[TextCell]],
    bg_colors: Vector[(Color8, Color8)],
    fg_colors: Vector[(Color8, Color8)],
    large_sprites: Vector[Sprite],
    small_sprites: Vector[Sprite]
  ) {
    def buffer: VideoBuffer = Vector(Vector())
  }

  case class BgCell(
    color_pair_1: Byte,
    color_pair_2: Byte,
    x_flip: Boolean,
    y_flip: Boolean,
    large_tile_index: Byte
  )

  case class TextCell(
    text_char_tile_index: Byte,
    on: Boolean
  )

  case class Sprite(
    color_pair_1: Byte,
    color_pair_2: Byte,
    x_flip: Boolean,
    y_flip: Boolean,
    tile_index: Byte,
    x_position: Byte,
    y_position: Byte,
    on: Boolean
  )

  case class Color8(red: Byte, green: Byte, blue: Byte) {
    def toColor: Color = Color.rgb(
      Color8.convert3bitTo8bit(red),
      Color8.convert3bitTo8bit(green),
      Color8.convert2bitTo8bit(blue)
    )
  }

  object Color8 {
    def make(char: Char): Color8 = {
      val r = (char >> 5).toByte
      val g = ((char & 28) >> 2).toByte
      val b = (char & 3).toByte
      Color8(r, g, b)
    }

    def convert3bitTo8bit(byte: Byte): Int =
      (byte << 5) + (byte << 2) + (byte >> 1)

    def convert2bitTo8bit(byte: Byte): Int =
      (byte << 6) + (byte << 4) + (byte << 2) + byte
  }
}
