package info.ditrapani.ljdcomputer.video

import org.scalatest.OptionValues._

class VideoCellSpec extends Spec {
  describe("Cell.apply") {
    it("creates a Cell") {
      def runTest(s: String, bg: Int, fg: Int, index: Int): Unit = {
        val c = Integer.parseInt(s, 2).toChar
        Cell(c) shouldBe Cell(bg.toByte, fg.toByte, index.toByte)
      }
      //        15       14        252
      val s1 = "1111" + "1110" + "11111100"
      runTest(s1, 15, 14, 252)
      //        14       13          6
      val s2 = "1110" + "1101" + "00000110"
      runTest(s2, 14, 13, 6)
    }
  }

  describe("CellGrid.apply") {
    it("fails if ram size < 640") {
      an [AssertionError] should be thrownBy {
        CellGrid(Vector.fill(639)(0.toChar))
      }
    }

    it("fails if ram size > 640") {
      an [AssertionError] should be thrownBy {
        CellGrid(Vector.fill(641)(0.toChar))
      }
    }

    it("creates a Cell grid") {
      //          bg=8     fg=4     index=11
      val bits = "1000" + "0100" + "00001011"
      val cell_word = Integer.parseInt(bits, 2).toChar
      val ram = Vector
        .fill(640)(0.toChar)
        .updated(2, cell_word)
        .updated(34, cell_word)
      val grid = CellGrid(ram)
      grid.size shouldBe 20
      grid(0).size shouldBe 32
      grid.lastOption.value.size shouldBe 32
      val expected_cell = Cell(8, 4, 11)
      grid(0)(2) shouldBe expected_cell
      grid(1)(2) shouldBe expected_cell
    }
  }
}
