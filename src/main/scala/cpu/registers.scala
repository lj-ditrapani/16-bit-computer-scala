package info.ditrapani.ljdcomputer.cpu

final case class Registers(vector: Vector[Char]) {
  assert(vector.size == 16)
}
