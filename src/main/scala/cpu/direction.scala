package info.ditrapani.ljdcomputer.cpu

sealed abstract class Direction {
  override def toString: String = this match {
    case DLeft => "left"
    case DRight => "right"
  }
}
object DLeft extends Direction
object DRight extends Direction
object Direction {
  def fromNibble(immd4: Int): Direction = (immd4 >> 3) > 0 match {
    case false => DLeft
    case true => DRight
  }
}
