package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.BitHelper

sealed abstract class JumpResult {
  override def toString: String = this match {
    case DontJump => {
      "DontJump"
    }
    case TakeJump(address) => {
      val s = BitHelper.pprint(address.toInt)
      s"TakeJump($s)"
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
