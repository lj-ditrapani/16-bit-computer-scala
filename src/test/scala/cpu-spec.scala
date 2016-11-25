package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class RegistersSpec extends Spec {
  describe("apply") {
    it("fails if vector size < 16") {
      an [AssertionError] should be thrownBy {
        Registers(Vector.fill(15)(0.toChar))
      }
    }

    it("fails if vector size > 16") {
      an [AssertionError] should be thrownBy {
        Registers(Vector.fill(17)(0.toChar))
      }
    }

    it("creates registers") {
      Registers(Vector.fill(16)(0.toChar)).vector.size shouldBe 16
    }
  }
}

class CpuSpec extends Spec {
}

class ExecutorSpec extends Spec {
  def testSetByteOperation(tests: List[(Int, Int, Int, Int)]): Unit = {
    for (test <- tests) {
      val (immediate, register, current_value, final_value) = test
      it(s"sets R${register} to ${final_value}") {
        val registers = Array.fill(16)(0.toChar)
        registers(register) = current_value.toChar
        val executor = new Executor(0.toChar, registers, Array())
        executor.hby(immediate, register) shouldBe true
        registers(register) shouldBe final_value
      }
    }
  }

  describe("hby") {
    val tests = List(
      (0x05, 0x0, 0x0000, 0x0500),
      (0x00, 0x3, 0xFFFF, 0x00FF),
      (0xEA, 0xF, 0x1234, 0xEA34)
    )

    testSetByteOperation(tests)

    it("increments the instruction_counter") {
      val registers = Array.fill(16)(0.toChar)
      val executor = new Executor(0.toChar, registers, Array())
      executor.hby(0xFF, 0)
      executor.getInstructionCounter shouldBe 1.toChar
    }

    it("wraps the IC when it increments the instruction_counter") {
      val registers = Array.fill(16)(0.toChar)
      val executor = new Executor(0xFFFF.toChar, registers, Array())
      executor.hby(0xFF, 0)
      executor.getInstructionCounter shouldBe 0.toChar
    }
  }
}

class BitUtilsSpec extends Spec {
  describe("getNibbles") {
    it("splits a word into 4 4-bit nibbles") {
      BitUtils.getNibbles(0xABCD.toChar).shouldBe((0xA, 0xB, 0xC, 0xD))
    }

    it("splits another word into 4 4-bit nibbles") {
      BitUtils.getNibbles(0x7712.toChar).shouldBe((0x7, 0x7, 0x1, 0x2))
    }
  }
}
