package info.ditrapani.ljdcomputer

import scala.annotation.tailrec

final case class Registers(vector: Vector[Char]) {
  assert(vector.size == 16)
}

final case class Cpu(instruction_counter: Char, registers: Registers, rom: Vector[Char]) {
  def run(n: Int, ram: Vector[Char]): (Vector[Char], Cpu) = {
    val cpuAndRam = new CpuAndRam(this, ram)
    cpuAndRam.run(n)
    (cpuAndRam.ramAsVector, cpuAndRam.getCpu)
  }
}

object Cpu {
  def initialize(rom: Vector[Char]): Cpu =
    Cpu(0.toChar, new Registers(Vector.fill(16)(0.toChar)), rom)
}

final class CpuAndRam(cpu: Cpu, ramSnapshot: Vector[Char]) {
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  var instruction_counter: Char = cpu.instruction_counter
  val registers: Array[Char] = cpu.registers.vector.toArray
  val ram: Array[Char] = ramSnapshot.toArray

  @tailrec
  def run(n: Int): Unit = {
    if (n > 0) {
      step()
      run(n - 1)
    }
  }

  def step(): Boolean = true

  def ramAsVector: Vector[Char] = ram.toVector

  def getCpu: Cpu = Cpu(instruction_counter, Registers(registers.toVector), cpu.rom)
}

object BitUtils {
  def getNibbles(word: Char): (Int, Int, Int, Int) = {
    val op_code = word >> 12
    val a = word >> 8 & 0xF
    val b = word >> 4 & 0xF
    val c = word & 0xF
    (op_code, a, b, c)
  }
}
