package info.ditrapani.ljdcomputer.cpu

final case class Registers(vector: Vector[Char], carry: Boolean, overflow: Boolean) {
  assert(vector.size == 16)
}

object Registers {
  def initialize: Registers = Registers(Vector.fill(16)(0.toChar), false, false)
}
