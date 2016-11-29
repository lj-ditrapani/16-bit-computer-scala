package info.ditrapani.ljdcomputer.video

import org.scalatest.OptionValues._

class VideoTileSpec extends Spec {
  def testTileRam: Ram = {
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
    val rows = tile.rows
    rows.size shouldBe 12
    rows(0).size shouldBe 8
    rows(11).size shouldBe 8
    expectInc(rows(0))
    expectDec(rows(1))
    expectDec(rows(2))
    expectInc(rows(3))
    expectF0(rows(4))
    expectF0(rows(5))
    expectF0(rows(6))
    expectF0(rows(7))
    expectDec(rows(8))
    expectInc(rows(9))
    expectInc(rows(10))
    expectDec(rows(11))
  }

  describe("new TileSet") {
    it("fails if ram < 1,536") {
      an[AssertionError] should be thrownBy {
        new TileSet(Vector.fill(1535)(0.toChar))
      }
    }

    it("fails if ram > 1,536") {
      an[AssertionError] should be thrownBy {
        new TileSet(Vector.fill(1537)(0.toChar))
      }
    }

    it("creates a tile set") {
      val ram = 0.until(256).flatMap((i) => testTileRam).to[Vector]
      val tile_set = new TileSet(ram).vector
      tile_set.size shouldBe 256
      checkTile(tile_set.headOption.value)
      checkTile(tile_set.lastOption.value)
    }
  }

  describe("new Tile") {
    it("fails if tile_ram < 6") {
      an[AssertionError] should be thrownBy {
        new Tile(Vector.fill(5)(0.toChar))
      }
    }

    it("fails if tile_ram > 6") {
      an[AssertionError] should be thrownBy {
        new Tile(Vector.fill(7)(0.toChar))
      }
    }

    it("returns an 8 x 12 tile of 1-bit per pixel values") {
      val tile = new Tile(testTileRam)
      checkTile(tile)
    }
  }
}
