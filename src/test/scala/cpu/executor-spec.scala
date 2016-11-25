package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class ExecutorSpec extends Spec {
  trait Fixture {
    val registers = Array.fill(16)(0.toChar)
    val ram = Array.fill(16)(0.toChar)
    val executor = new Executor(registers, ram)
  }

  trait FullFixture {
    val registers = Array.fill(16)(0.toChar)
    val ram = Array.fill(64 * 1024)(0.toChar)
    val executor = new Executor(registers, ram)
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
}
