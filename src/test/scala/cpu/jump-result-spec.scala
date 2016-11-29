package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class JumpResultSpec extends Spec {
  describe("fromBool") {
    val address = 0x1234.toChar

    it("returns DontJump when false") {
      JumpResult.fromBool(false, address) shouldBe DontJump
    }

    it("returns TakeJump(address) when true") {
      JumpResult.fromBool(true, address) shouldBe TakeJump(address)
    }
  }
}
