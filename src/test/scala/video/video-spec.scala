package info.ditrapani.ljdcomputer.video

import org.scalatest.OptionValues._

import scalafx.scene.paint.Color

class VideoSpec extends Spec {
  val rom = Vector.fill(512 * 3 + 16)(0.toChar)
  val video_roms = VideoRoms.make(rom)

  describe("Video class") {
    describe("buffer") {
      it("returns all disabledBuffer if video is disabled") {
        val ram = Vector.fill(0x10000)(0.toChar)
        val video = Video.make(ram, video_roms)
        val buffer = video.buffer
        buffer shouldBe Video.disabledBuffer
        buffer.size shouldBe 240
        buffer(0).size shouldBe 256
        buffer.lastOption.value.size shouldBe 256
        buffer(0)(0) shouldBe Color.rgb(0, 0, 0)
        buffer.lastOption.value.lastOption.value shouldBe Color.rgb(0, 0, 0)
      }
    }
  }

  describe("Video object") {
    describe("make") {
      val cells_size = 640

      it("sets video ram correctly") {
        val ram = Vector.fill(0x10000)(7.toChar)
        val video = Video.make(ram, video_roms)
        val cells = video.cells
        cells.size shouldBe cells_size
        cells(0) shouldBe 7
        cells(cells_size - 1) shouldBe 7
      }

      describe("sets the enable & custom_video_rom flags correctly") {
        //               enable   custom_video_rom
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
          val (enable, custom_video_rom) = test
          val enable_bits: Char =
            ((bool2int(custom_video_rom) << 1) +  bool2int(enable)).toChar
          val ram =
            Vector.fill(0xFE80)(7.toChar) ++
            Vector(enable_bits) ++
            Vector.fill(383)(8.toChar)
          val video = Video.make(ram, video_roms)
          it(s"enable $enable custom_video_rom $custom_video_rom are set correctly") {
            video.enable shouldBe enable
            video.enable_custom_video_rom shouldBe custom_video_rom
          }
        }

        for (test <- tests) runTest(test)
      }
    }

    describe("disabledBuffer") {
      it("returns all black buffer") {
        val buffer = Video.disabledBuffer
        buffer.size shouldBe 240
        buffer(0).size shouldBe 256
        buffer.lastOption.value.size shouldBe 256
        buffer(0)(0) shouldBe Color.rgb(0, 0, 0)
        buffer.lastOption.value.lastOption.value shouldBe Color.rgb(0, 0, 0)
      }
    }
  }
}
