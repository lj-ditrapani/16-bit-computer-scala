package info.ditrapani.ljdcomputer.cpu

import scala.annotation.tailrec

final case class Registers(vector: Vector[Char]) {
  assert(vector.size == 16)
}

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

final class Controller(cpu: Cpu, ramSnapshot: Vector[Char]) {
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private val executor = new Executor(
    cpu.instruction_counter, cpu.registers.vector.toArray, ramSnapshot.toArray
  )

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
    val (op_code, a, b, c) = BitUtils.getNibbles(
      cpu.rom(executor.getInstructionCounter.toInt)
    )
    val immd8 = a << 8 | b
    op_code match {
      case 0x0 => false
      case 0x1 => executor.hby(immd8, c)
      case 0x2 => executor.lby(immd8, c)
      case 0x3 => executor.lod(a, c)
      case 0x4 => executor.str(a, b)
      case 0x5 => executor.add(a, b, c)
      case 0x6 => executor.sub(a, b, c)
      case 0x7 => executor.adi(a, b, c)
      case 0x8 => executor.sbi(a, b, c)
      case 0x9 => executor.and(a, b, c)
      case 0xA => executor.orr(a, b, c)
      case 0xB => executor.xor(a, b, c)
      case 0xC => executor.not(a, c)
      case 0xD => executor.shf(a, b, c)
      case 0xE => executor.brv(a, b, c)
      case 0xF => executor.brf(b, c)
      case _ => true
    }
  }

  def ramAsVector: Vector[Char] = executor.getRam.toVector

  def getCpu: Cpu = Cpu(
    executor.getInstructionCounter, Registers(executor.getRegisters.toVector), cpu.rom
  )
}

final class Executor(instruction_counter: Char, registers: Array[Char], ram: Array[Char]) {
  def getInstructionCounter: Char = instruction_counter

  def getRegisters: Array[Char] = registers

  def getRam: Array[Char] = ram

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
