package info.ditrapani.ljdcomputer.cpu

final case class Cpu(instruction_counter: Char, registers: Registers, rom: Vector[Char]) {
  def run(ram: Vector[Char]): (Vector[Char], Cpu) = {
    val controller = new Controller(this, ram)
    controller.run(400000)
    (controller.ramAsVector, controller.getCpu)
  }
}

object Cpu {
  def initialize(rom: Vector[Char]): Cpu =
    Cpu(0.toChar, Registers.initialize, rom)
}
