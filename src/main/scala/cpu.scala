package info.ditrapani.ljdcomputer

case class Cpu(rom: Vector[Char]) {
  def step(n: Int, ram: Vector[Char]): (Vector[Char], Cpu) = {
    (ram, this)
  }
}
