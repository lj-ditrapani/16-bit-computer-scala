package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class ExecutorSpec extends Spec {
  trait WithRegisters {
    val registers = Array.fill(16)(0.toChar)
  }

  trait Fixture extends WithRegisters {
    val ram = Array.fill(16)(0.toChar)
    val executor = new Executor(registers, false, false, ram)
  }

  class WithFlags(initial_carry: Boolean, initial_overflow: Boolean)
    extends WithRegisters {
    val ram = Array.fill(16)(0.toChar)
    val executor = new Executor(registers, initial_carry, initial_overflow, ram)
  }

  trait FullFixture extends WithRegisters {
    val ram = Array.fill(64 * 1024)(0.toChar)
    val executor = new Executor(registers, false, false, ram)
  }

  val flag_values = List(
    (0, 0),
    (0, 1),
    (1, 0),
    (1, 1)
  )

  def int2bool(i: Int): Boolean = i match {
    case 0 => false
    case _ => true
  }

  describe("set byte operations") {
    def testSetByteOperation(
        tests: List[(Int, Int, Int, Int)],
        operation: (Executor, Int, Int) => Unit): Unit = {
      for (test <- tests) {
        val (immediate, register, current_value, final_value) = test
        it(s"sets R${register} to ${final_value}") {
          new Fixture {
            registers(register) = current_value.toChar
            operation(executor, immediate, register)
            registers(register) shouldBe final_value
          }
        }
      }
    }

    describe("HBY") {
      val tests = List(
        (0x05, 0x0, 0x0000, 0x0500),
        (0x00, 0x3, 0xFFFF, 0x00FF),
        (0xEA, 0xF, 0x1234, 0xEA34)
      )

      def operation(executor: Executor, immediate: Int, register: Int): Unit = {
        executor.hby(immediate, register)
      }

      testSetByteOperation(tests, operation)
    }

    describe("LBY") {
      val tests = List(
        (0x05, 0x0, 0x0000, 0x0005),
        (0x00, 0x3, 0xFFFF, 0xFF00),
        (0xEA, 0xF, 0x1234, 0x12EA)
      )

      def operation(executor: Executor, immediate: Int, register: Int): Unit = {
        executor.lby(immediate, register)
      }

      testSetByteOperation(tests, operation)
    }
  }

  describe("LOD") {
    val tests = List(
      (2, 13, 0x0100, 0xFEED),
      (3, 10, 0x1000, 0xFACE)
    )

    for (test <- tests) {
      val (address_register, destination_register, address, value) = test
      it(s"loads RAM[${address}] into R${destination_register}") {
        new FullFixture {
          registers(address_register) = address.toChar
          ram(address) = value.toChar
          executor.lod(address_register, destination_register)
          registers(destination_register) shouldBe value
        }
      }
    }
  }

  describe("STR") {
    val tests = List(
      (7, 15, 0x0100, 0xFEED),
      (12, 5, 0x1000, 0xFACE),
      (6, 6, 0x1000, 0x1000)
    )

    for (test <- tests) {
      val (address_register, value_register, address, value) = test
      it(s"stores R${value_register} into RAM[${address}]") {
        new FullFixture {
          registers(address_register) = address.toChar
          registers(value_register) = value.toChar
          executor.str(address_register, value_register)
          ram(address) shouldBe value.toChar
        }
      }
    }
  }

  describe("ADD/SUB/ADI/SBI instructions") {
    type AddSubTest = (Int, Int, Int, Int, Int)

    def testAddOrSub(name: String,symbol: String,tests: List[AddSubTest])
                    (setup_and_run: (Int, Int, Array[Char], Executor) => Int): Unit = {
      for (test <- tests; (initial_carry, initial_overflow) <- flag_values) {
        val (a, b, result, final_carry, final_overflow) = test
        val test_name = {
          val initial_cv = s"${initial_carry}${initial_overflow}"
          val final_cv = s"${final_carry}${final_overflow}"
          s"${name}: ${a} ${symbol} ${b} = ${result} (cv ${initial_cv} -> ${final_cv})"
        }
        it(test_name) {
          new WithFlags(int2bool(initial_carry), int2bool(initial_overflow)) {
            val rd = setup_and_run(a, b, registers, executor)
            registers(rd) shouldBe result
            val registers_obj = executor.getRegisters
            registers_obj.carry shouldBe int2bool(final_carry)
            registers_obj.overflow shouldBe int2bool(final_overflow)
          }
        }
      }
    }

    describe("ADD") {
      val tests = List(
        (0x0000, 0x0000, 0x0000, 0, 0),
        (0x00FF, 0xFF00, 0xFFFF, 0, 0),
        (0xFFFF, 0x0001, 0x0000, 1, 0),
        (0x0001, 0xFFFF, 0x0000, 1, 0),
        (0xFFFF, 0xFFFF, 0xFFFE, 1, 0),
        (0x8000, 0x8000, 0x0000, 1, 1),
        (0x1234, 0x9876, 0xAAAA, 0, 0),
        (0x1234, 0xDEAD, 0xF0E1, 0, 0),
        (0x7FFF, 0x0001, 0x8000, 0, 1),
        (0x0FFF, 0x7001, 0x8000, 0, 1),
        (0x7FFE, 0x0001, 0x7FFF, 0, 0)
      )

      testAddOrSub("ADD", "+", tests)((a, b, registers, executor) => {
        val (rs1, rs2, rd) = (0, 1, 2)
        registers(rs1) = a.toChar
        registers(rs2) = b.toChar
        executor.add(rs1, rs2, rd)
        rd
      })
    }

    describe("SUB") {
      val tests = List(
        (0x0000, 0x0000, 0x0000, 1, 0),
        (0x0000, 0x0001, 0xFFFF, 0, 0),
        (0x0005, 0x0007, 0xFFFE, 0, 0),
        (0x7FFE, 0x7FFF, 0xFFFF, 0, 0),
        (0xFFFF, 0xFFFF, 0x0000, 1, 0),
        (0xFFFF, 0x0001, 0xFFFE, 1, 0),
        (0x8000, 0x8000, 0x0000, 1, 0),
        (0x8000, 0x7FFF, 0x0001, 1, 1),
        (0xFFFF, 0x7FFF, 0x8000, 1, 0),
        (0x7FFF, 0xFFFF, 0x8000, 0, 1),
        (0x7FFF, 0x0001, 0x7FFE, 1, 0)
      )

      testAddOrSub("SUB", "-", tests)((a, b, registers, executor) => {
        val (rs1, rs2, rd) = (15, 11, 9)
        registers(rs1) = a.toChar
        registers(rs2) = b.toChar
        executor.sub(rs1, rs2, rd)
        rd
      })
    }

    describe("ADI") {
      val tests = List(
        (0x0000, 0x0000, 0x0000, 0, 0),
        (0xFFFF, 0x0001, 0x0000, 1, 0),
        (0x7FFF, 0x0001, 0x8000, 0, 1),
        (0x7FFE, 0x0001, 0x7FFF, 0, 0),
        (0xFFFE, 0x000F, 0x000D, 1, 0),
        (0x7FFE, 0x000F, 0x800D, 0, 1),
        (0xFEDF, 0x000E, 0xFEED, 0, 0)
      )

      testAddOrSub("ADI", "+", tests)((a, b, registers, executor) => {
        val (rs1, rd) = (7, 13)
        registers(rs1) = a.toChar
        executor.adi(rs1, b, rd)
        rd
      })
    }

    describe("SBI") {
      val tests = List(
        (0x0000, 0x0000, 0x0000, 1, 0),
        (0x0000, 0x0001, 0xFFFF, 0, 0),
        (0x8000, 0x0001, 0x7FFF, 1, 1),
        (0x7FFF, 0x0001, 0x7FFE, 1, 0),
        (0x000D, 0x000F, 0xFFFE, 0, 0),
        (0x800D, 0x000F, 0x7FFE, 1, 1),
        (0xFEED, 0x000E, 0xFEDF, 1, 0)
      )

      testAddOrSub("SBI", "-", tests)((a, b, registers, executor) => {
        val (rs1, rd) = (8, 5)
        registers(rs1) = a.toChar
        executor.sbi(rs1, b, rd)
        rd
      })
    }
  }

  describe("AND") {
    val tests = List(
      (0x0000, 0x0000, 0x0000),
      (0xFEED, 0xFFFF, 0xFEED),
      (0xFEED, 0x0F0F, 0x0E0D),
      (0x7BDC, 0xCCE3, 0x48C0)
    )

    for (test <- tests) {
      val (a, b, result) = test
      it(s"${a} AND ${b} = ${result}") {
        new Fixture {
          registers(0) = a.toChar
          registers(1) = b.toChar
          executor.and(0, 1, 2)
          registers(2) shouldBe result.toChar
        }
      }
    }
  }

  describe("ORR") {
    val tests = List(
      (0x0000, 0x0000, 0x0000),
      (0xFEED, 0xFFFF, 0xFFFF),
      (0xF000, 0x000F, 0xF00F),
      (0xC8C6, 0x3163, 0xF9E7)
    )

    for (test <- tests) {
      val (a, b, result) = test
      it(s"${a} ORR ${b} = ${result}") {
        new Fixture {
          registers(15) = a.toChar
          registers(14) = b.toChar
          executor.orr(15, 14, 13)
          registers(13) shouldBe result.toChar
        }
      }
    }
  }

  describe("XOR") {
    val tests = List(
      (0x0000, 0x0000, 0x0000),
      (0xFF00, 0x00FF, 0xFFFF),
      (0x4955, 0x835A, 0xCA0F)
    )

    for (test <- tests) {
      val (a, b, result) = test
      it(s"${a} XOR ${b} = ${result}") {
        new Fixture {
          registers(14) = a.toChar
          registers(7) = b.toChar
          executor.xor(14, 7, 0)
          registers(0) shouldBe result.toChar
        }
      }
    }
  }

  describe("NOT") {
    val tests = List(
      (0x0000, 0xFFFF),
      (0xFF00, 0x00FF),
      (0x4955, 0xB6AA)
    )

    for (test <- tests) {
      val (a, result) = test
      it(s"NOT ${a} = ${result}") {
        new Fixture {
          registers(9) = a.toChar
          executor.not(9, 5)
          registers(5) shouldBe result.toChar
        }
      }
    }
  }

  describe("SHF") {
    val tests = List(
      (0x0704, Left,  0x4, 0x7040, 0),
      (0x090F, Left,  0x1, 0x121E, 0),
      (0x090F, Left,  0x3, 0x4878, 0),
      (0x90F0, Right, 0x4, 0x090F, 0),
      (0x90F1, Right, 0x1, 0x4878, 1),
      (0x450A, Left,  0x7, 0x8500, 0),
      (0x450A, Left,  0x8, 0x0A00, 1),
      (0x450A, Right, 0x8, 0x0045, 0)
    )
    for ((value, direction, amount, result, carry) <- tests) {
      it(s"SHF ${value} ${direction} by ${amount} = ${result}") {
        new Fixture {
          val (rs1, rd) = (14, 7)
          registers(rs1) = value.toChar
          val int_direction = direction match {
            case Left => 0
            case Right => 1
          }
          val immd4 = int_direction * 8 + (amount - 1)
          executor.shf(rs1, immd4, rd)
          registers(rd) shouldBe result
          val registers_obj = executor.getRegisters
          registers_obj.carry shouldBe int2bool(carry)
          registers_obj.overflow shouldBe false
        }
      }
    }
  }
}
