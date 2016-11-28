package info.ditrapani.ljdcomputer.cpu

sealed abstract class JumpResult {
  override def toString: String = this match {
    case DontJump => {
      "DontJump"
    }
    case TakeJump(address) => {
      val s = Integer.toHexString(address.toInt).toUpperCase
      "TakeJump($" + s"$s)"
    }
  }
}
final case class TakeJump(address: Char) extends JumpResult
final object DontJump extends JumpResult

final class Executor(
    registers: Array[Char],
    initial_carry: Boolean,
    initial_overflow: Boolean,
    ram: Array[Char]) {

  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var carry = initial_carry
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var overflow = initial_overflow

  private def basicAdd(a: Char, b: Char, c: Int, rd: Int): Unit = {
    val int_sum = a + b + c
    carry = int_sum >= 65536
    val char_sum = int_sum.toChar
    overflow = BitUtils.hasOverflowedOnAdd(a, b, char_sum)
    registers(rd) = char_sum
  }

  def getRegisters: Registers = new Registers(registers.toVector, carry, overflow)

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

  def add(rs1: Int, rs2: Int, rd: Int): Unit =
    basicAdd(registers(rs1), registers(rs2), 0, rd)

  def sub(rs1: Int, rs2: Int, rd: Int): Unit = {
    val not_b = (~ registers(rs2)).toChar
    basicAdd(registers(rs1), not_b, 1, rd)
  }

  def adi(rs1: Int, immd: Int, rd: Int): Unit =
    basicAdd(registers(rs1), immd.toChar, 0, rd)

  def sbi(rs1: Int, immd: Int, rd: Int): Unit = {
    val not_b = (~ immd).toChar
    basicAdd(registers(rs1), not_b, 1, rd)
  }

  def and(rs1: Int, rs2: Int, rd: Int): Unit = {
    registers(rd) = (registers(rs1) & registers(rs2)).toChar
  }

  def orr(rs1: Int, rs2: Int, rd: Int): Unit = {
    registers(rd) = (registers(rs1) | registers(rs2)).toChar
  }

  def xor(rs1: Int, rs2: Int, rd: Int): Unit = {
    registers(rd) = (registers(rs1) ^ registers(rs2)).toChar
  }

  def not(rs1: Int, rd: Int): Unit = {
    registers(rd) = (~ registers(rs1)).toChar
  }

  def shf(rs1: Int, da: Int, rd: Int): Unit = {
    val value = registers(rs1)
    val direction = Direction.fromNibble(da)
    val amount = (da & 7) + 1
    carry = BitUtils.getShiftCarry(value, direction, amount)
    registers(rd) = (direction match {
      case Right => value >> amount
      case Left => (value << amount) & 0xFFFF
    }).toChar
  }

  def brv(rs1: Int, rs2: Int, cond_v: Int): JumpResult = {
    val (value, jump_addr) = (registers(rs1), registers(rs2))
    BitUtils.matchValue(value, cond_v & 7) match {
      case false => DontJump
      case true => TakeJump(jump_addr)
    }
  }

  def brf(rs2: Int, cond_f: Int): JumpResult = {
    val jump_addr = registers(rs2)
    BitUtils.matchFlags(overflow, carry, cond_f & 7) match {
      case false => DontJump
      case true => TakeJump(jump_addr)
    }
  }
}

object Executor {
  def make(registers_obj: Registers, ram: Array[Char]): Executor = {
    new Executor(
      registers_obj.vector.toArray, registers_obj.carry, registers_obj.overflow, ram
    )
  }
}
