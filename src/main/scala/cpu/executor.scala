package info.ditrapani.ljdcomputer.cpu

final class Executor(registers: Array[Char], ram: Array[Char]) {
  def getRegisters: Array[Char] = registers

  def getRam: Array[Char] = ram

  def hby(immd8: Int, rd: Int): Unit = {
    registers(rd) = (immd8 << 8 | registers(rd) & 0xFF).toChar
  }

  def lby(immd8: Int, rd: Int): Unit = {
    registers(rd) = (registers(rd) & 0xFF00 | immd8).toChar
  }

  def lod(rs1: Int, rd: Int): Unit = {
    registers(rd) = ram(registers(rs1).toInt)
  }

  def str(rs1: Int, rs2: Int): Unit = {
    ram(registers(rs1).toInt) = registers(rs2)
  }

  def add(rs1: Int, rs2: Int, rd: Int): Unit = {}

  def sub(rs1: Int, rs2: Int, rd: Int): Unit = {}

  def adi(rs1: Int, rs2: Int, rd: Int): Unit = {}

  def sbi(rs1: Int, rs2: Int, rd: Int): Unit = {}

  def and(rs1: Int, rs2: Int, rd: Int): Unit = {
    registers(rd) = (registers(rs1) & registers(rs2)).toChar
  }

  def orr(rs1: Int, rs2: Int, rd: Int): Unit = {
    registers(rd) = (registers(rs1) | registers(rs2)).toChar
  }

  def xor(rs1: Int, rs2: Int, rd: Int): Unit = {
    registers(rd) = (registers(rs1) ^ registers(rs2)).toChar
  }

  def not(rs1: Int, rd: Int): Unit = {}

  def shf(rs1: Int, da: Int, rd: Int): Unit = {}

  def brv(rs1: Int, rs2: Int, cond_v: Int): Char = 0.toChar

  def brf(rs2: Int, cond_f: Int): Char = 0.toChar
}
