package info.ditrapani.ljdcomputer

import org.scalatest.{FunSpec, Matchers}

class VideoSpec extends FunSpec with Matchers {
  describe("Video class") {
    describe("buffer") {
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
  }
}
