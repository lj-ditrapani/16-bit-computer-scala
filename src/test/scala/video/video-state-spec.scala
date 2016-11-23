package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

class VideoStateSpec extends Spec {
  val colors = new Colors(VideoRoms.built_in_colors)
  val tiles = new TileSet(VideoRoms.built_in_tiles)

  describe("VideoState object") {
    describe("make") {
      it("fails if cells size is less than 640 words") {
        an [AssertionError] should be thrownBy {
          VideoState.make(Vector.fill(639)(0.toChar), colors, tiles)
        }
      }

      it("fails if cells size is greater than 640 words") {
        an [AssertionError] should be thrownBy {
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
          Vector(0x0FFF.toChar) ++        // black/white 256th cell
          Vector.fill(638)(1.toChar) ++
          Vector(0x42FE.toChar)           // blue/red 255th cell
        val buffer = VideoState.make(cells, colors, tiles).buffer
        buffer(0)(0) shouldBe Color.rgb(0, 0, 0)
      }
    }
  }
}
