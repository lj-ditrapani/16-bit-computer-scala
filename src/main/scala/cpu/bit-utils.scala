package info.ditrapani.ljdcomputer.cpu

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

}
