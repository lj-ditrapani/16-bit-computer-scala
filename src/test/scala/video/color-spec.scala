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
          Color8(r1.toByte, g1.toByte, b1.toByte).toColor.should(
            === (Color.rgb(r2, g2, b2))
          )
        }
      }

      for (test <- tests) runTest(test)
    }
  }

  describe("Color8 object") {
    describe("apply") {
      it("creates a Color8") {
        val char1 = Integer.parseInt("101" + "100" + "11", 2).toChar
        Color8(char1) should ===(Color8(5, 4, 3))
        val char2 = Integer.parseInt("110" + "111" + "00", 2).toChar
        Color8(char2) should ===(Color8(6, 7, 0))
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
          Color8.convert2bitTo8bit(bit2.toByte) should ===(bit8)
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
          Color8.convert3bitTo8bit(bit3.toByte) should ===(bit8)
        }
      }

      for (test <- tests) runTest(test)
    }
  }
}
