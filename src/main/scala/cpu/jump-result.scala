package info.ditrapani.ljdcomputer.cpu

sealed abstract class JumpResult {
  override def toString: String = this match {
    case DontJump => {
      "DontJump"
    }
    case TakeJump(address) => {
      val s = Integer.toHexString(address.toInt).toUpperCase
      "TakeJump($" + s"$s)"
    }
  }
}
final case class TakeJump(address: Char) extends JumpResult
final object DontJump extends JumpResult

object JumpResult {
  def fromBool(b: Boolean, address: Char): JumpResult = b match {
    case false => DontJump
    case true => TakeJump(address)
  }
}
