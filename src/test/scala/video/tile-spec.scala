package info.ditrapani.ljdcomputer.video

import org.scalatest.{FunSpec, Matchers}

class VideoTileSpec extends FunSpec with Matchers {
  describe("makeLargeTile") {
    it("fails if tile_ram < 32") {
      a [AssertionError] should be thrownBy {
        Tile.makeLargeTile(Vector.fill(31)(0.toChar))
      }
    }

    it("fails if tile_ram > 32") {
      a [AssertionError] should be thrownBy {
        Tile.makeLargeTile(Vector.fill(33)(0.toChar))
      }
    }

    it("returns a 16 x 16 tile of 2-bit per pixel values") {
      val inc = "00" + "01" + "10" + "11"  // 0 1 2 3
      val dec = "11" + "10" + "01" + "00"  // 3 2 1 0
      val incThenDec = Integer.parseInt(inc + dec, 2).toChar
      val decThenInc = Integer.parseInt(dec + inc, 2).toChar
      val tile = Tile.makeLargeTile(
        Vector(incThenDec, decThenInc) ++
        Vector.fill(28)(0xF0F0.toChar) ++
        Vector(decThenInc, incThenDec)
      )
      tile.size should === (16)
      tile(0).size should === (16)
      tile(15).size should === (16)
      tile(0) should === (Vector(
        (false, false), (false, true), (true, false), (true, true),
        (true, true), (true, false), (false, true), (false, false),
        (true, true), (true, false), (false, true), (false, false),
        (false, false), (false, true), (true, false), (true, true)
      ))
      tile(1) should === (Vector(
        (true, true), (true, true), (false, false), (false, false),
        (true, true), (true, true), (false, false), (false, false),
        (true, true), (true, true), (false, false), (false, false),
        (true, true), (true, true), (false, false), (false, false)
      ))
      tile(14) should === (Vector(
        (true, true), (true, true), (false, false), (false, false),
        (true, true), (true, true), (false, false), (false, false),
        (true, true), (true, true), (false, false), (false, false),
        (true, true), (true, true), (false, false), (false, false)
      ))
      tile(15) should === (Vector(
        (true, true), (true, false), (false, true), (false, false),
        (false, false), (false, true), (true, false), (true, true),
        (false, false), (false, true), (true, false), (true, true),
        (true, true), (true, false), (false, true), (false, false)
      ))
    }
  }

  describe("makeSmallTile") {
    it("fails if tile_ram < 8") {
      a [AssertionError] should be thrownBy {
        Tile.makeSmallTile(Vector.fill(7)(0.toChar))
      }
    }

    it("fails if tile_ram > 8") {
      a [AssertionError] should be thrownBy {
        Tile.makeSmallTile(Vector.fill(9)(0.toChar))
      }
    }

    it("returns an 8 x 8 tile of 2-bit per pixel values") {
      val inc = "00" + "01" + "10" + "11"  // 0 1 2 3
      val dec = "11" + "10" + "01" + "00"  // 3 2 1 0
      val incThenDec = Integer.parseInt(inc + dec, 2).toChar
      val decThenInc = Integer.parseInt(dec + inc, 2).toChar
      val tile = Tile.makeSmallTile(
        Vector(incThenDec, decThenInc) ++
        Vector.fill(4)(0xF0F0.toChar) ++
        Vector(decThenInc, incThenDec)
      )
      tile.size should === (8)
      tile(0).size should === (8)
      tile(7).size should === (8)
      tile(0) should === (Vector(
        (false, false), (false, true), (true, false), (true, true),
        (true, true), (true, false), (false, true), (false, false)
      ))
      tile(1) should === (Vector(
        (true, true), (true, false), (false, true), (false, false),
        (false, false), (false, true), (true, false), (true, true)
      ))
      tile(2) should === (Vector(
        (true, true), (true, true), (false, false), (false, false),
        (true, true), (true, true), (false, false), (false, false)
      ))
      tile(6) should === (Vector(
        (true, true), (true, false), (false, true), (false, false),
        (false, false), (false, true), (true, false), (true, true)
      ))
      tile(7) should === (Vector(
        (false, false), (false, true), (true, false), (true, true),
        (true, true), (true, false), (false, true), (false, false)
      ))
    }
  }

  describe("makeTextCharTile") {
    it("fails if tile_ram < 4") {
      a [AssertionError] should be thrownBy {
        Tile.makeTextCharTile(Vector.fill(3)(0.toChar))
      }
    }

    it("fails if tile_ram > 4") {
      a [AssertionError] should be thrownBy {
        Tile.makeTextCharTile(Vector.fill(5)(0.toChar))
      }
    }

    it("returns an 8 x 8 tile of 1-bit per pixel values") {
      val r1 = Integer.parseInt("0110011001100110", 2).toChar
      val r2 = Integer.parseInt("0000000011111111", 2).toChar
      val tile = Tile.makeTextCharTile(
        Vector(r1) ++
        Vector.fill(2)(0xF0F0.toChar) ++
        Vector(r2)
      )
      tile.size should === (8)
      tile(0).size should === (8)
      tile(7).size should === (8)
      tile(0) should === (Vector(
        false, true, true, false, false, true, true, false
      ))
      tile(1) should === (Vector(
        false, true, true, false, false, true, true, false
      ))
      tile(2) should === (Vector(
        true, true, true, true, false, false, false, false
      ))
      tile(6) should === (Vector(
        false, false, false, false, false, false, false, false
      ))
      tile(7) should === (Vector(
        true, true, true, true, true, true, true, true
      ))
    }
  }
}
