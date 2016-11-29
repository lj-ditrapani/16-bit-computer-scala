package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class DirectionSpec extends Spec {
  describe("fromNibble") {
    it("returns DLeft if high bit is 0") {
      Direction.fromNibble(7) shouldBe DLeft
    }

    it("returns DRight if high bit is 1") {
      Direction.fromNibble(8) shouldBe DRight
    }
  }

  describe("toString") {
    it("returns left for DLeft") {
      DLeft.toString shouldBe "left"
    }

    it("returns right for DRight") {
      DRight.toString shouldBe "right"
    }
  }
}
