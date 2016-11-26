package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

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
