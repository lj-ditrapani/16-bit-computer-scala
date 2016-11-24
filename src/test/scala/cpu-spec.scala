package info.ditrapani.ljdcomputer

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

class CpuAndRamSpec extends Spec {
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
