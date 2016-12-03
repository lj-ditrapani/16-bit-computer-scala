package info.ditrapani.ljdcomputer

import BitHelper.{b, bool2int, pprint}

class BitHelperSpec extends Spec {
  describe("b") {
    it("converts binary strings to Ints") {
      b("0111") shouldBe 7
      b("11100111") shouldBe 0xE7
    }
  }

  describe("bool2int") {
    it("converts false to 0") {
      bool2int(false) shouldBe 0
    }

    it("converts true to 1") {
      bool2int(true) shouldBe 1
    }
  }

  describe("pprint") {
    it("prints integers as $ hex string") {
      pprint(0xFACE) shouldBe "$FACE"
    }
  }
}
