package info.ditrapani.ljdcomputer.cpu

sealed abstract class Direction
object Left extends Direction
object Right extends Direction

object BitUtils {
  def getNibbles(word: Char): (Int, Int, Int, Int) = {
    val op_code = word >> 12
    val a = word >> 8 & 0xF
    val b = word >> 4 & 0xF
    val c = word & 0xF
    (op_code, a, b, c)
  }

  def isPositiveOrZero(word: Char): Boolean = word < 32768

  def isNegative(word: Char): Boolean = word >= 32768

  def isTruePositive(word: Char): Boolean = word != 0 && isPositiveOrZero(word)

  def hasOverflowedOnAdd(a: Char, b: Char, sum: Char): Boolean =
  ((isNegative(a) && isNegative(b) && isPositiveOrZero(sum)) ||
   (isPositiveOrZero(a) && isPositiveOrZero(b) && isNegative(sum)))

  def positionOfLastBitShifted(direction: Direction, amount: Int): Int =
    direction match {
      case Left => 16 - amount
      case Right => amount - 1
    }
}
