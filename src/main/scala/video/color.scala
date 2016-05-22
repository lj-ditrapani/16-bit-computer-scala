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
  def apply(char: Char): Color8 = {
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
