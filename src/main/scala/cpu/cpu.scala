package info.ditrapani.ljdcomputer.cpu

final case class Cpu(instruction_counter: Char, registers: Registers, rom: Vector[Char]) {
  def run(n: Int, ram: Vector[Char]): (Vector[Char], Cpu) = {
    val controller = new Controller(this, ram)
    controller.run(n)
    (controller.ramAsVector, controller.getCpu)
  }
}

object Cpu {
  def initialize(rom: Vector[Char]): Cpu =
    Cpu(0.toChar, new Registers(Vector.fill(16)(0.toChar)), rom)
}
