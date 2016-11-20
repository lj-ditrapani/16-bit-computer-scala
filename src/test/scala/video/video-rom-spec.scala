package info.ditrapani.ljdcomputer.video

class VideoRomsSpec extends Spec {
  describe("VideoRoms object") {
    describe("make") {
      it("fails if custom rom size is less than 1,552 words") {
        an [AssertionError] should be thrownBy {
          VideoRoms.make(Vector.fill(1551)(0.toChar))
        }
      }

      it("fails if custom rom size is greater than 1,552 words") {
        an [AssertionError] should be thrownBy {
          VideoRoms.make(Vector.fill(1553)(0.toChar))
        }
      }

      it("creates a VideoRoms instance") {
        val video_rom = VideoRoms.make(
          Vector.fill(16)(7.toChar) ++
          Vector.fill(1024 + 512)(3.toChar)
        )
        val built_in_colors = video_rom.built_in_colors
        built_in_colors.size shouldBe 16
        built_in_colors(0) shouldBe 0
        built_in_colors(15) shouldBe 0x49
        val built_in_tiles = video_rom.built_in_tiles
        built_in_tiles.size shouldBe 1536
        built_in_tiles(0) shouldBe 32
        built_in_tiles(1535) shouldBe 0
        val custom_colors = video_rom.custom_colors
        custom_colors.size shouldBe 16
        custom_colors(0) shouldBe 7
        custom_colors(15) shouldBe 7
        val custom_tiles = video_rom.custom_tiles
        custom_tiles.size shouldBe 1536
        custom_tiles(0) shouldBe 3
        custom_tiles(1535) shouldBe 3
      }
    }
  }
}
