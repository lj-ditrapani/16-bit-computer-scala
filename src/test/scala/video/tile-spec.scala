package info.ditrapani.ljdcomputer.video

import org.scalatest.OptionValues._

class VideoTileSpec extends Spec {
  def TestTileRam: Ram = {
    val inc = "00" + "01" + "10" + "11"  // 0 1 2 3
    val dec = "11" + "10" + "01" + "00"  // 3 2 1 0
    val incThenDec = Integer.parseInt(inc + dec, 2).toChar
    val decThenInc = Integer.parseInt(dec + inc, 2).toChar
    Vector(incThenDec, decThenInc) ++
      Vector.fill(2)(0xF0F0.toChar) ++
      Vector(decThenInc, incThenDec)
  }

  def expectInc(tile_row: Vector[Boolean]): Unit =
    tile_row shouldBe Vector(
      false, false, false, true, true, false, true, true
    )

  def expectDec(tile_row: Vector[Boolean]): Unit =
    tile_row shouldBe Vector(
      true, true, true, false, false, true, false, false
    )

  def expectF0(tile_row: Vector[Boolean]): Unit =
    tile_row shouldBe Vector(
      true, true, true, true, false, false, false, false
    )

  def checkTile(tile: Tile): Unit = {
    tile.size shouldBe 12
    tile(0).size shouldBe 8
    tile(11).size shouldBe 8
    expectInc(tile(0))
    expectDec(tile(1))
    expectDec(tile(2))
    expectInc(tile(3))
    expectF0(tile(4))
    expectF0(tile(5))
    expectF0(tile(6))
    expectF0(tile(7))
    expectDec(tile(8))
    expectInc(tile(9))
    expectInc(tile(10))
    expectDec(tile(11))
  }

  describe("TileSet.apply") {
    it("fails if ram < 1,536") {
      an [AssertionError] should be thrownBy {
        TileSet(Vector.fill(1535)(0.toChar))
      }
    }

    it("fails if ram > 1,536") {
      an [AssertionError] should be thrownBy {
        TileSet(Vector.fill(1537)(0.toChar))
      }
    }

    it("creates a tile set") {
      val ram = 0.until(256).flatMap((i) => TestTileRam).to[Vector]
      val tile_set = TileSet(ram)
      tile_set.size shouldBe 256
      checkTile(tile_set.headOption.value)
      checkTile(tile_set.lastOption.value)
    }
  }

  describe("Tile.apply") {
    it("fails if tile_ram < 6") {
      a [AssertionError] should be thrownBy {
        Tile(Vector.fill(5)(0.toChar))
      }
    }

    it("fails if tile_ram > 6") {
      a [AssertionError] should be thrownBy {
        Tile(Vector.fill(7)(0.toChar))
      }
    }

    it("returns an 8 x 12 tile of 1-bit per pixel values") {
      val tile = Tile(TestTileRam)
      checkTile(tile)
    }
  }
}
