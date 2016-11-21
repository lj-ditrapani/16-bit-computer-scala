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
        val built_in_colors = video_rom.built_in_colors.vector
        built_in_colors.size shouldBe 16
        built_in_colors(0) shouldBe Color8(0, 0, 0)
        built_in_colors(15) shouldBe Color8(2, 2, 1)
        val built_in_tiles = video_rom.built_in_tiles.vector
        built_in_tiles.size shouldBe 256
        built_in_tiles(1).rows(1) shouldBe Vector(
          false, true, true, false, false, true, true, false
        )
        built_in_tiles(255).rows(1) shouldBe Vector(
          true, false, false, false, false, false, false, false
        )
        val custom_colors = video_rom.custom_colors.vector
        custom_colors.size shouldBe 16
        custom_colors(0) shouldBe Color8(0,1,3)
        custom_colors(15) shouldBe Color8(0,1,3)
        val custom_tiles = video_rom.custom_tiles.vector
        custom_tiles.size shouldBe 256
        custom_tiles(0).rows(0) shouldBe Vector(
          false, false, false, false, false, false, false, false
        )
        custom_tiles(0).rows(1) shouldBe Vector(
          false, false, false, false, false, false, true, true
        )
        custom_tiles(255).rows(0) shouldBe Vector(
          false, false, false, false, false, false, false, false
        )
        custom_tiles(255).rows(1) shouldBe Vector(
          false, false, false, false, false, false, true, true
        )
      }
    }
  }
}
