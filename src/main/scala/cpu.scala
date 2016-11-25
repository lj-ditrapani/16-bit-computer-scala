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
      val keep_going = step()
      if (keep_going) {
        run(n - 1)
      }
    }
  }

  def step(): Boolean = {
    val (op_code, a, b, c) = BitUtils.getNibbles(ram(instruction_counter.toInt))
    val immd8 = a << 8 | b
    op_code match {
      case 0x0 => false
      case 0x1 => hby(immd8, c)
      case 0x2 => lby(immd8, c)
      case 0x3 => lod(a, c)
      case 0x4 => str(a, b)
      case 0x5 => add(a, b, c)
      case 0x6 => sub(a, b, c)
      case 0x7 => adi(a, b, c)
      case 0x8 => sbi(a, b, c)
      case 0x9 => and(a, b, c)
      case 0xA => orr(a, b, c)
      case 0xB => xor(a, b, c)
      case 0xC => not(a, c)
      case 0xD => shf(a, b, c)
      case 0xE => brv(a, b, c)
      case 0xF => brf(b, c)
      case _ => true
    }
  }

  def ramAsVector: Vector[Char] = ram.toVector

  def getCpu: Cpu = Cpu(instruction_counter, Registers(registers.toVector), cpu.rom)

  def hby(immd8: Int, c: Int): Boolean = true

  def lby(immd8: Int, c: Int): Boolean = true

  def lod(rs1: Int, rd: Int): Boolean = true

  def str(rs1: Int, rs2: Int): Boolean = true

  def add(rs1: Int, rs2: Int, rd: Int): Boolean = true

  def sub(rs1: Int, rs2: Int, rd: Int): Boolean = true

  def adi(rs1: Int, rs2: Int, rd: Int): Boolean = true

  def sbi(rs1: Int, rs2: Int, rd: Int): Boolean = true

  def and(rs1: Int, rs2: Int, rd: Int): Boolean = true

  def orr(rs1: Int, rs2: Int, rd: Int): Boolean = true

  def xor(rs1: Int, rs2: Int, rd: Int): Boolean = true

  def not(rs1: Int, rd: Int): Boolean = true

  def shf(rs1: Int, da: Int, rd: Int): Boolean = true

  def brv(rs1: Int, rs2: Int, cond_v: Int): Boolean = true

  def brf(rs2: Int, cond_f: Int): Boolean = true
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
