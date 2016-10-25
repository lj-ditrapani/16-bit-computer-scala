package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

case class Color8(red: Byte, green: Byte, blue: Byte) {
  def toColor: Color = Color.rgb(
    Color8.convert3bitTo8bit(red),
    Color8.convert3bitTo8bit(green),
    Color8.convert2bitTo8bit(blue)
  )
}

object Color8 {
  def apply(byte: Int): Color8 = {
    assert(byte < 256)
    assert(byte >= 0)
    val r = (byte >> 5).toByte
    val g = ((byte & 28) >> 2).toByte
    val b = (byte & 3).toByte
    Color8(r, g, b)
  }

  def convert3bitTo8bit(byte: Byte): Int =
    (byte << 5) + (byte << 2) + (byte >> 1)

  def convert2bitTo8bit(byte: Byte): Int =
    (byte << 6) + (byte << 4) + (byte << 2) + byte
}

object Colors {
  def make(colors: Vector[Char]): Vector[Color8] = {
    assert(colors.size == 16)
    colors.map((char) => Color8(char.toInt))
  }
}
