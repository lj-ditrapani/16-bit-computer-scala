package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

class VideoStateSpec extends Spec {
  private val colors = new Colors(VideoRoms.built_in_colors)
  private val tiles = new TileSet(VideoRoms.built_in_tiles)

  describe("VideoState object") {
    describe("make") {
      it("fails if cells size is less than 640 words") {
        an[AssertionError] should be thrownBy {
          VideoState.make(Vector.fill(639)(0.toChar), colors, tiles)
        }
      }

      it("fails if cells size is greater than 640 words") {
        an[AssertionError] should be thrownBy {
          VideoState.make(Vector.fill(641)(0.toChar), colors, tiles)
        }
      }

      it("makes a VideoState") {
        val video_state = VideoState.make(Vector.fill(640)(0.toChar), colors, tiles)
        video_state.cells.rows.size shouldBe 20
      }
    }
  }

  describe("VideoState class") {
    describe("buffer") {
      it("returns a buffer with pixel colors set correctly") {
        val cells =
          Vector(0x01FF.toChar) ++            // black/white 256th tile
          Vector.fill(638)(0xC901.toChar) ++  // dark green/light green 2nd tile
          Vector(0x42FE.toChar)               // blue/red 255th tile
        val buffer = VideoState.make(cells, colors, tiles).buffer
        buffer(0)(0) shouldBe Color.rgb(0, 0, 0)
        buffer(1)(0) shouldBe Color.rgb(255, 255, 255)
        buffer(2)(8) shouldBe Color.rgb(0, 0x49, 0)
        buffer(2)(9) shouldBe Color.rgb(0x92, 255, 0xAA)
        buffer(235)(248) shouldBe Color.rgb(0, 0, 255)
        buffer(235)(249) shouldBe Color.rgb(255, 0, 0)
      }
    }
  }
}
