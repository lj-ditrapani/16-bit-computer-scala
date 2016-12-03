package info.ditrapani.ljdcomputer

object BitHelper {
  def b(s: String): Int = Integer.parseInt(s, 2)

  def bool2int(b: Boolean): Int = b match {
    case false => 0
    case true => 1
  }

  def pprint(i: Int): String = "$" + Integer.toHexString(i).toUpperCase
}
