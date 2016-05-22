package info.ditrapani.ljdcomputer.video

class VideoCellSpec extends Spec {
  describe("BgCell.apply") {
    it("creates a BgCell") {
      def runTest(
        s: String, cp1: Int, cp2: Int, x: Boolean, y: Boolean, index: Int
      ): Unit = {
        val c = Integer.parseInt(s, 2).toChar
        BgCell(c) should === (
          BgCell(cp1.toByte, cp2.toByte, x, y, index.toByte)
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

  describe("BgCellGrid.apply") {
    it("fails if ram size < 240") {
      an [AssertionError] should be thrownBy {
        BgCellGrid(Vector.fill(239)(0.toChar))
      }
    }

    it("fails if ram size > 240") {
      an [AssertionError] should be thrownBy {
        BgCellGrid(Vector.fill(241)(0.toChar))
      }
    }

    it("creates a BgCell grid") {
      //         cp1=8    cp2=4     x     y    index=11
      val bits = "1000" + "0100" + "1" + "0" + "001011"
      val cell_word = Integer.parseInt(bits, 2).toChar
      val ram = Vector.fill(240)(0.toChar).updated(2, cell_word).updated(18, cell_word)
      val grid = BgCellGrid(ram)
      grid.size should ===(15)
      grid(0).size should ===(16)
      grid.last.size should ===(16)
      val expected_cell = BgCell(8, 4, true, false, 11)
      grid(0)(2) should ===(expected_cell)
      grid(1)(2) should ===(expected_cell)
    }
  }

  describe("TextCharCell.apply") {
    it("fails if byte is negative") {
      an [AssertionError] should be thrownBy {
        TextCharCell(-1)
      }
    }

    it("fails if byte is > 255") {
      an [AssertionError] should be thrownBy {
        TextCharCell(256)
      }
    }

    it("creates a TextCharCell") {
      //                  on = true   index = 120 = 0x78
      val c1 = Integer.parseInt("1" + "1111000", 2).toChar
      TextCharCell(c1) should === (
        TextCharCell(true, 120.toByte)
      )
      //                  on = false  index =  89 = 0x59
      val c2 = Integer.parseInt("0" + "1011001", 2).toChar
      TextCharCell(c2) should === (
        TextCharCell(false, 89.toByte)
      )
    }
  }

  describe("TextCharCellGrid.apply") {
    it("fails if ram size < 480") {
      an [AssertionError] should be thrownBy {
        TextCharCellGrid(Vector.fill(479)(0.toChar))
      }
    }

    it("fails if ram size > 480") {
      an [AssertionError] should be thrownBy {
        TextCharCellGrid(Vector.fill(481)(0.toChar))
      }
    }

    it("creates a TextCharCell grid") {
      //         off       126      on        63
      val bits = "0" + "1111110" + "1" + "0111111"
      val cell_word = Integer.parseInt(bits, 2).toChar
      // 17 is 3rd & 4th cell in 2nd row; 17 = 32/2 + 1
      val ram = Vector.fill(480)(0.toChar).updated(17, cell_word)
      val grid = TextCharCellGrid(ram)
      grid.size should ===(30)
      grid(0).size should ===(32)
      grid.last.size should ===(32)
      grid(1)(2) should ===(TextCharCell(false, 126))
      grid(1)(3) should ===(TextCharCell(true, 63))
    }
  }
}
