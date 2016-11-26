package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class RegistersSpec extends Spec {
  def makeRegisters(size: Int): Registers =
    Registers(Vector.fill(size)(0.toChar), false, false)

  describe("apply") {
    it("fails if vector size < 16") {
      an [AssertionError] should be thrownBy {
        makeRegisters(15)
      }
    }

    it("fails if vector size > 16") {
      an [AssertionError] should be thrownBy {
        makeRegisters(17)
      }
    }

    it("creates registers") {
      makeRegisters(16).vector.size shouldBe 16
    }
  }
}
