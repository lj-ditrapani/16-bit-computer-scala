package info.ditrapani.ljdcomputer

final case class Cpu(rom: Vector[Char]) {
  def step(n: Int, ram: Vector[Char]): (Vector[Char], Cpu) = {
    (ram, this)
  }
}
