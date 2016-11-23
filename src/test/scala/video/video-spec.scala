package info.ditrapani.ljdcomputer.video

import org.scalatest.OptionValues._

import scalafx.scene.paint.Color

class VideoSpec extends Spec {
  val rom = Vector.fill(512 * 3 + 16)(0.toChar)
  val video_roms = VideoRoms.make(rom)

  describe("Video class") {
    describe("buffer") {
      it("returns disabledBuffer if video is disabled") {
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

    describe("getVideoRam") {
      type TestCase = (Int, Boolean, Boolean)

      val tests = List(
        (0, false, false),
        (0xFF, true, true),
        (0xA9, false, true),
        (0x5D, true, false)
      )

      def bool2int(b: Boolean): Int = b match {
        case false => 0
        case true => 1
      }

      def runTest(test: TestCase): Unit = {
        val (cell, enable, enable_custom_video_rom) = test
        val test_name =
          s"returns correct ram when the cells have value ${cell}," +
          s" enbale is ${enable} and" +
          s" enable_custom_video_rom is ${enable_custom_video_rom}"
        it(test_name) {
          val cells = Vector.fill(640)(cell.toChar)
          val video = Video(cells, video_roms, enable, enable_custom_video_rom)
          val video_ram = video.getVideoRam
          val enable_bits = (bool2int(enable_custom_video_rom) << 1) + bool2int(enable)
          video_ram.size shouldBe 1021
          video_ram(0) shouldBe cell
          video_ram(639) shouldBe cell
          video_ram(640) shouldBe enable_bits
          video_ram(641) shouldBe 0
          video_ram(1020) shouldBe 0
        }
      }

      for (test <- tests) runTest(test)
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
