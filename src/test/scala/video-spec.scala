package info.ditrapani.ljdcomputer

import org.scalatest.{FunSpec, Matchers}
import scalafx.scene.paint.Color

class VideoSpec extends FunSpec with Matchers {
  describe("Video class") {
    describe("buffer") {
      it("returns all disabledBuffer if video is disabled") {
        val ram = Vector.fill(0x10000)(0.toChar)
        val video = Video.make(ram)
        val buffer = video.buffer
        buffer should === (Video.disabledBuffer)
        buffer.size should === (240)
        buffer(0).size should === (256)
        buffer.last.size should === (256)
        buffer(0)(0) should === (Color.rgb(0, 0, 0))
        buffer.last.last should === (Color.rgb(0, 0, 0))
      }
    }
  }

  describe("Video object") {
    describe("make") {
      val video_ram_size = 4096

      it("sets video ram correctly") {
        val ram = Vector.fill(0x10000)(7.toChar)
        val video: Video = Video.make(ram)
        val video_ram = video.video_ram
        video_ram.size should === (video_ram_size)
        video_ram(0) should === (7)
        video_ram(video_ram_size - 1) should === (7)
      }

      describe("sets the enable & custom_tiles flags correctly") {
        //               enable   custom_tiles
        type FlagTest = (Boolean, Boolean)

        val tests: List[FlagTest] = List(
          (false, false),
          (true, false),
          (false, true),
          (true, true)
        )

        def bool2int(b: Boolean): Int = b match {
          case true => 1
          case false => 0
        }

        def runTest(test: FlagTest): Unit = {
          val (enable, custom_tiles) = test
          val enable_bits: Char =
            ((bool2int(custom_tiles) << 2) +  (bool2int(enable) << 1)).toChar
          val ram = Vector.fill(0xDDF3)(7.toChar) ++ Vector(enable_bits)
          val video: Video = Video.make(ram)
          it(s"enable $enable custom_tiles $custom_tiles are set correctly") {
            video.enable should === (enable)
            video.custom_tiles should === (custom_tiles)
          }
        }

        tests.map { runTest(_) }
      }
    }

    describe("disabledBuffer") {
      it("returns all black buffer") {
        val buffer = Video.disabledBuffer
        buffer.size should === (240)
        buffer(0).size should === (256)
        buffer.last.size should === (256)
        buffer(0)(0) should === (Color.rgb(0, 0, 0))
        buffer.last.last should === (Color.rgb(0, 0, 0))
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
            Video.Color8(r1.toByte, g1.toByte, b1.toByte).toColor should ===(
              Color.rgb(r2, g2, b2)
            )
          }
        }

        tests.map(runTest)
      }
    }

    describe("Color8 object") {
      describe("make") {
        it("creates a Color8") {
          val char1 = Integer.parseInt("101" + "100" + "11", 2).toChar
          Video.Color8.make(char1) should ===(Video.Color8(5, 4, 3))
          val char2 = Integer.parseInt("110" + "111" + "00", 2).toChar
          Video.Color8.make(char2) should ===(Video.Color8(6, 7, 0))
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
            Video.Color8.convert2bitTo8bit(bit2.toByte) should ===(bit8)
          }
        }

        tests.map(runTest)
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
            Video.Color8.convert3bitTo8bit(bit3.toByte) should ===(bit8)
          }
        }

        tests.map(runTest)
      }
    }
  }
}
