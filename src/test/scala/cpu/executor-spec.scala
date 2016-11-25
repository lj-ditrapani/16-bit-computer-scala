package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class ExecutorSpec extends Spec {
  def testSetByteOperation(tests: List[(Int, Int, Int, Int)]): Unit = {
    for (test <- tests) {
      val (immediate, register, current_value, final_value) = test
      it(s"sets R${register} to ${final_value}") {
        val registers = Array.fill(16)(0.toChar)
        registers(register) = current_value.toChar
        val executor = new Executor(registers, Array())
        executor.hby(immediate, register)
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
  }
}
