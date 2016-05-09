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
  }
}
