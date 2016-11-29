package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

class VideoColorSpec extends Spec {
  describe("Color8 class") {
    describe("toColor") {
      type SixInts = (Int, Int, Int, Int, Int, Int)

      val tests: List[SixInts] = List(
        (0, 2, 3, 0, 73, 255),
        (6, 5, 1, 219, 182, 85),
        (3, 7, 2, 109, 255, 170)
      )

      def runTest(test: SixInts): Unit = {
        val (r1, g1, b1, r2, g2, b2) = test
        it(s"Color8($r1, $g1, $b1) => Color.rgb($r2, $g2, $b2)") {
          val color = Color8(r1.toByte, g1.toByte, b1.toByte).toColor
          color shouldBe Color.rgb(r2, g2, b2)
        }
      }

      for (test <- tests) runTest(test)
    }
  }

  describe("Color8 object") {
    describe("apply") {
      it("creates a Color8") {
        val char1 = Integer.parseInt("101" + "100" + "11", 2)
        Color8(char1) shouldBe Color8(5, 4, 3)
        val char2 = Integer.parseInt("110" + "111" + "00", 2)
        Color8(char2) shouldBe Color8(6, 7, 0)
      }
    }

    describe("convert2bitTo8bit") {
      val tests: List[(Int, Int)] = List(
        (0, 0),
        (1, 85),
        (2, 170),
        (3, 255)
      )

      def runTest(test: (Int, Int)): Unit = {
        val (bit2, bit8) = test
        it(s"converts 2-bit value $bit2 to 8-bit value $bit8") {
          Color8.convert2bitTo8bit(bit2.toByte) shouldBe bit8
        }
      }

      for (test <- tests) runTest(test)
    }

    describe("convert3bitTo8bit") {
      val tests: List[(Int, Int)] = List(
        (0, 0),
        (1, 36),
        (2, 73),
        (3, 109),
        (4, 146),
        (5, 182),
        (6, 219),
        (7, 255)
      )

      def runTest(test: (Int, Int)): Unit = {
        val (bit3, bit8) = test
        it(s"converts 3-bit value $bit3 to 8-bit value $bit8") {
          Color8.convert3bitTo8bit(bit3.toByte) shouldBe bit8
        }
      }

      for (test <- tests) runTest(test)
    }
  }

  describe("new Colors") {
    it("fails if ram size < 16") {
      an[AssertionError] should be thrownBy {
        new Colors(Vector.fill(15)(0.toChar))
      }
    }

    it("fails if ram size > 16") {
      an[AssertionError] should be thrownBy {
        new Colors(Vector.fill(17)(0.toChar))
      }
    }

    it("returns a vector of 16 Color8") {
      //         [ R       G       B ]
      val bits1 = "111" + "110" + "11"
      val bits2 = "011" + "010" + "01"
      val char1 = Integer.parseInt(bits1, 2).toChar
      val char2 = Integer.parseInt(bits2, 2).toChar
      val ram = Vector.fill(16)(0.toChar).updated(1, char1).updated(14, char2)
      val colors = new Colors(ram).vector
      colors.size shouldBe 16
      colors(1) shouldBe Color8(7, 6, 3)
      colors(14) shouldBe Color8(3, 2, 1)
    }
  }
}
