package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

case class VideoState(
  large_tiles: Vector[VideoState.LargeTile],
  small_tiles: Vector[VideoState.SmallTile],
  text_char_tiles: Vector[VideoState.TextCharTile],
  bg_cells: Vector[Vector[VideoState.BgCell]],
  text_cells: Vector[Vector[VideoState.TextCharCell]],
  bg_colors: Vector[(VideoState.Color8, VideoState.Color8)],
  fg_colors: Vector[(VideoState.Color8, VideoState.Color8)],
  large_sprites: Vector[VideoState.Sprite],
  small_sprites: Vector[VideoState.Sprite]
) {
  type VideoBuffer = Vector[Vector[Color]]
  def buffer: VideoBuffer = Vector(Vector())
}

object VideoState {
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

  case class BgCell(
    color_pair_1: Byte,
    color_pair_2: Byte,
    x_flip: Boolean,
    y_flip: Boolean,
    large_tile_index: Byte
  )

  def makeBgCell(word: Char): BgCell = {
    val color_pair_1 = (word >> 12).toByte
    val color_pair_2 = ((word >> 8) & 0xF).toByte
    val x_flip = ((word >> 7) & 1) > 0
    val y_flip = ((word >> 6) & 1) > 0
    val tile_index = (word & 0x3F).toByte
    BgCell(color_pair_1, color_pair_2, x_flip, y_flip, tile_index)
  }

  case class TextCharCell(
    on: Boolean,
    text_char_tile_index: Byte
  )

  def makeTextCharCell(byte: Int): TextCharCell = {
    TextCharCell((byte >> 7) > 0, (byte & 0x7F).toByte)
  }

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
