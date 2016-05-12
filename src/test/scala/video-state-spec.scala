package info.ditrapani.ljdcomputer

import org.scalatest.{FunSpec, Matchers}
import scalafx.scene.paint.Color

class VideoStateSpec extends FunSpec with Matchers {
  describe("VideoState object") {
    describe("makeLargeTile") {
      it("fails if tile_ram < 32") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeLargeTile(Vector.fill(31)(0.toChar))
        }
      }

      it("fails if tile_ram > 32") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeLargeTile(Vector.fill(33)(0.toChar))
        }
      }

      it("returns a 16 x 16 tile of 2-bit per pixel values") {
        val inc = "00" + "01" + "10" + "11"  // 0 1 2 3
        val dec = "11" + "10" + "01" + "00"  // 3 2 1 0
        val incThenDec = Integer.parseInt(inc + dec, 2).toChar
        val decThenInc = Integer.parseInt(dec + inc, 2).toChar
        val tile = VideoState.makeLargeTile(
          Vector(incThenDec, decThenInc) ++
          Vector.fill(28)(0xF0F0.toChar) ++
          Vector(decThenInc, incThenDec)
        )
        tile.size should === (16)
        tile(0).size should === (16)
        tile(15).size should === (16)
        tile(0) should === (Vector(
          (false, false), (false, true), (true, false), (true, true),
          (true, true), (true, false), (false, true), (false, false),
          (true, true), (true, false), (false, true), (false, false),
          (false, false), (false, true), (true, false), (true, true)
        ))
        tile(1) should === (Vector(
          (true, true), (true, true), (false, false), (false, false),
          (true, true), (true, true), (false, false), (false, false),
          (true, true), (true, true), (false, false), (false, false),
          (true, true), (true, true), (false, false), (false, false)
        ))
        tile(14) should === (Vector(
          (true, true), (true, true), (false, false), (false, false),
          (true, true), (true, true), (false, false), (false, false),
          (true, true), (true, true), (false, false), (false, false),
          (true, true), (true, true), (false, false), (false, false)
        ))
        tile(15) should === (Vector(
          (true, true), (true, false), (false, true), (false, false),
          (false, false), (false, true), (true, false), (true, true),
          (false, false), (false, true), (true, false), (true, true),
          (true, true), (true, false), (false, true), (false, false)
        ))
      }
    }

    describe("makeSmallTile") {
      it("fails if tile_ram < 8") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeSmallTile(Vector.fill(7)(0.toChar))
        }
      }

      it("fails if tile_ram > 8") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeSmallTile(Vector.fill(9)(0.toChar))
        }
      }

      it("returns an 8 x 8 tile of 2-bit per pixel values") {
        val inc = "00" + "01" + "10" + "11"  // 0 1 2 3
        val dec = "11" + "10" + "01" + "00"  // 3 2 1 0
        val incThenDec = Integer.parseInt(inc + dec, 2).toChar
        val decThenInc = Integer.parseInt(dec + inc, 2).toChar
        val tile = VideoState.makeSmallTile(
          Vector(incThenDec, decThenInc) ++
          Vector.fill(4)(0xF0F0.toChar) ++
          Vector(decThenInc, incThenDec)
        )
        tile.size should === (8)
        tile(0).size should === (8)
        tile(7).size should === (8)
        tile(0) should === (Vector(
          (false, false), (false, true), (true, false), (true, true),
          (true, true), (true, false), (false, true), (false, false)
        ))
        tile(1) should === (Vector(
          (true, true), (true, false), (false, true), (false, false),
          (false, false), (false, true), (true, false), (true, true)
        ))
        tile(2) should === (Vector(
          (true, true), (true, true), (false, false), (false, false),
          (true, true), (true, true), (false, false), (false, false)
        ))
        tile(6) should === (Vector(
          (true, true), (true, false), (false, true), (false, false),
          (false, false), (false, true), (true, false), (true, true)
        ))
        tile(7) should === (Vector(
          (false, false), (false, true), (true, false), (true, true),
          (true, true), (true, false), (false, true), (false, false)
        ))
      }
    }

    describe("makeTextCharTile") {
      it("fails if tile_ram < 4") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeTextCharTile(Vector.fill(3)(0.toChar))
        }
      }

      it("fails if tile_ram > 4") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeTextCharTile(Vector.fill(5)(0.toChar))
        }
      }

      it("returns an 8 x 8 tile of 1-bit per pixel values") {
        val r1 = Integer.parseInt("0110011001100110", 2).toChar
        val r2 = Integer.parseInt("0000000011111111", 2).toChar
        val tile = VideoState.makeTextCharTile(
          Vector(r1) ++
          Vector.fill(2)(0xF0F0.toChar) ++
          Vector(r2)
        )
        tile.size should === (8)
        tile(0).size should === (8)
        tile(7).size should === (8)
        tile(0) should === (Vector(
          false, true, true, false, false, true, true, false
        ))
        tile(1) should === (Vector(
          false, true, true, false, false, true, true, false
        ))
        tile(2) should === (Vector(
          true, true, true, true, false, false, false, false
        ))
        tile(6) should === (Vector(
          false, false, false, false, false, false, false, false
        ))
        tile(7) should === (Vector(
          true, true, true, true, true, true, true, true
        ))
      }
    }

    describe("makeBgCell") {
      it("creates a BgCell") {
        //        15       14    true  false      60
        val s = "1111" + "1110" + "1" + "0" + "111100"
        val c = Integer.parseInt(s, 2).toChar
        VideoState.makeBgCell(c) should === (
          VideoState.BgCell(15, 14, true, false, 60)
        )
      }
    }

    describe("makeTextCharCell") {
      it("creates a TextCharCell") {
        //                  on = true  index = 120 = 0x78
        val c = Integer.parseInt("1" + "1111000", 2).toChar
        VideoState.makeTextCharCell(c) should === (
          VideoState.TextCharCell(true, 120.toByte)
        )
      }
    }

    describe("makeSprite") {
      it("fails if word_pair < 2") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeSprite(Vector.fill(1)(0.toChar), false)
        }
      }

      it("fails if word_pair > 2") {
        a [java.lang.AssertionError] should be thrownBy {
          VideoState.makeSprite(Vector.fill(3)(0.toChar), false)
        }
      }

      it("creates a sprite") {
        //       cp1 7    cp2 8   xflip  yflip index 19
        val w1 = "0111" + "1000" + "0" + "1" + "010011"
        //         XX    xpos 31   true   XX     ypos 27
        val w2 = "000" + "11111" + "1" + "00" +  "11011"
        val word_pair = Vector(
          Integer.parseInt(w1, 2).toChar,
          Integer.parseInt(w2, 2).toChar
        )
        VideoState.makeSprite(word_pair, false) should === (
          VideoState.Sprite(7.toByte, 8.toByte, false, true, 19, 31, 27, true)
        )
      }
    }

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
            VideoState.Color8(r1.toByte, g1.toByte, b1.toByte).toColor.should(
              === (Color.rgb(r2, g2, b2))
            )
          }
        }

        for (test <- tests) runTest(test)
      }
    }

    describe("Color8 object") {
      describe("make") {
        it("creates a Color8") {
          val char1 = Integer.parseInt("101" + "100" + "11", 2).toChar
          VideoState.Color8.make(char1) should ===(VideoState.Color8(5, 4, 3))
          val char2 = Integer.parseInt("110" + "111" + "00", 2).toChar
          VideoState.Color8.make(char2) should ===(VideoState.Color8(6, 7, 0))
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
            VideoState.Color8.convert2bitTo8bit(bit2.toByte) should ===(bit8)
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
            VideoState.Color8.convert3bitTo8bit(bit3.toByte) should ===(bit8)
          }
        }

        for (test <- tests) runTest(test)
      }
    }
  }
}
