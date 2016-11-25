package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class ExecutorSpec extends Spec {
  trait Fixture {
    val registers = Array.fill(16)(0.toChar)
    val ram = Array.fill(16)(0.toChar)
    val executor = new Executor(registers, ram)
  }

  describe("set byte operations") {
    trait SetByteFixture {
      def operation(executor: Executor, immediate: Int, register: Int): Unit

      def testSetByteOperation(tests: List[(Int, Int, Int, Int)]): Unit = {
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
    }

    trait HbyFixture extends SetByteFixture {
      def operation(executor: Executor, immediate: Int, register: Int): Unit = {
        executor.hby(immediate, register)
      }
    }

    trait LbyFixture extends SetByteFixture {
      def operation(executor: Executor, immediate: Int, register: Int): Unit = {
        executor.lby(immediate, register)
      }
    }

    describe("hby") {
      new HbyFixture {
        val tests = List(
          (0x05, 0x0, 0x0000, 0x0500),
          (0x00, 0x3, 0xFFFF, 0x00FF),
          (0xEA, 0xF, 0x1234, 0xEA34)
        )

        testSetByteOperation(tests)
      }
    }

    describe("lby") {
      new LbyFixture {
        val tests = List(
          (0x05, 0x0, 0x0000, 0x0005),
          (0x00, 0x3, 0xFFFF, 0xFF00),
          (0xEA, 0xF, 0x1234, 0x12EA)
        )

        testSetByteOperation(tests)
      }
    }
  }
}
