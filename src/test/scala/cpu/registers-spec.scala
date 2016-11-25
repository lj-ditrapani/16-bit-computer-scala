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
