package info.ditrapani.ljdcomputer.cpu

import scala.annotation.tailrec

final class Controller(cpu: Cpu, ramSnapshot: Vector[Char]) {
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var instruction_counter = cpu.instruction_counter
  private val executor = new Executor(cpu.registers.vector.toArray, ramSnapshot.toArray)

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
      cpu.rom(instruction_counter.toInt)
    )
    val immd8 = a << 8 | b
    op_code match {
      case 0x0 => false
      case _ => {
        op_code match {
          case x if x < 0xE => {
            op_code match {
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
            }
            instruction_counter = (instruction_counter + 1).toChar
          }
          case _ => {
            instruction_counter = op_code match {
              case 0xE => executor.brv(a, b, c)
              case 0xF => executor.brf(b, c)
            }
          }
        }
        true
      }
    }
  }

  def ramAsVector: Vector[Char] = executor.getRam.toVector

  def getCpu: Cpu = Cpu(
    instruction_counter, Registers(executor.getRegisters.toVector), cpu.rom
  )
}
