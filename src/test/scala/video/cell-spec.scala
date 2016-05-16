package info.ditrapani.ljdcomputer.video

import org.scalatest.{FunSpec, Matchers}

class VideoCellSpec extends FunSpec with Matchers {
  describe("makeBgCell") {
    it("creates a BgCell") {
      def runTest(
        s: String, cp1: Int, cp2: Int, x: Boolean, y: Boolean, index: Int
      ): Unit = {
        val c = Integer.parseInt(s, 2).toChar
        Cell.makeBgCell(c) should === (
          Cell.BgCell(cp1.toByte, cp2.toByte, x, y, index.toByte)
        )
      }
      //        15       14    true  false      60
      val s1 = "1111" + "1110" + "1" + "0" + "111100"
      runTest(s1, 15, 14, true, false, 60)
      //        14       13    false  true      6
      val s2 = "1110" + "1101" + "0" + "1" + "000110"
      runTest(s2, 14, 13, false, true, 6)
    }
  }

  describe("makeTextCharCell") {
    it("fails if byte is negative") {
      a [AssertionError] should be thrownBy {
        Cell.makeTextCharCell(-1)
      }
    }

    it("fails if byte is > 255") {
      a [AssertionError] should be thrownBy {
        Cell.makeTextCharCell(256)
      }
    }

    it("creates a TextCharCell") {
      //                  on = true   index = 120 = 0x78
      val c1 = Integer.parseInt("1" + "1111000", 2).toChar
      Cell.makeTextCharCell(c1) should === (
        Cell.TextCharCell(true, 120.toByte)
      )
      //                  on = false  index =  89 = 0x59
      val c2 = Integer.parseInt("0" + "1011001", 2).toChar
      Cell.makeTextCharCell(c2) should === (
        Cell.TextCharCell(false, 89.toByte)
      )
    }
  }
}
