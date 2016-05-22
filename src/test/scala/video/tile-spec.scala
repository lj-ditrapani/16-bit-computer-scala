package info.ditrapani.ljdcomputer.video

class VideoTileSpec extends Spec {
  def LargeTestTileRam: Ram = {
    val inc = "00" + "01" + "10" + "11"  // 0 1 2 3
    val dec = "11" + "10" + "01" + "00"  // 3 2 1 0
    val incThenDec = Integer.parseInt(inc + dec, 2).toChar
    val decThenInc = Integer.parseInt(dec + inc, 2).toChar
    Vector(incThenDec, decThenInc) ++
      Vector.fill(28)(0xF0F0.toChar) ++
      Vector(decThenInc, incThenDec)
  }

  def SmallTestTileRam: Ram = {
    val inc = "00" + "01" + "10" + "11"  // 0 1 2 3
    val dec = "11" + "10" + "01" + "00"  // 3 2 1 0
    val incThenDec = Integer.parseInt(inc + dec, 2).toChar
    val decThenInc = Integer.parseInt(dec + inc, 2).toChar
    Vector(incThenDec, decThenInc) ++
      Vector.fill(4)(0xF0F0.toChar) ++
      Vector(decThenInc, incThenDec)
  }

  def TextCharTestTileRam: Ram = {
    val r1 = Integer.parseInt("0110011001100110", 2).toChar
    val r2 = Integer.parseInt("0000000011111111", 2).toChar
    Vector(r1) ++
      Vector.fill(2)(0xF0F0.toChar) ++
      Vector(r2)
  }

  def checkLargeTile(tile: LargeTile): Unit = {
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

  def checkSmallTile(tile: SmallTile): Unit = {
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

  def checkTextCharTile(tile: TextCharTile): Unit = {
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

  describe("makeLargeTileSet") {
    it("fails if ram < 2,048") {
      an [AssertionError] should be thrownBy {
        LargeTileSet(Vector.fill(2047)(0.toChar))
      }
    }

    it("fails if ram > 2,048") {
      an [AssertionError] should be thrownBy {
        LargeTileSet(Vector.fill(2049)(0.toChar))
      }
    }

    it("creates a large tile set") {
      val ram = 0.until(64).flatMap((i) => LargeTestTileRam).to[Vector]
      val tile_set = LargeTileSet(ram)
      tile_set.size should === (64)
      checkLargeTile(tile_set.head)
      checkLargeTile(tile_set.last)
    }
  }

  describe("makeSmallTileSet") {
    it("fails if ram < 512") {
      an [AssertionError] should be thrownBy {
        SmallTileSet(Vector.fill(511)(0.toChar))
      }
    }

    it("fails if ram > 512") {
      an [AssertionError] should be thrownBy {
        SmallTileSet(Vector.fill(513)(0.toChar))
      }
    }

    it("creates a small tile set") {
      val ram = 0.until(64).flatMap((i) => SmallTestTileRam).to[Vector]
      val tile_set = SmallTileSet(ram)
      tile_set.size should === (64)
      checkSmallTile(tile_set.head)
      checkSmallTile(tile_set.last)
    }
  }

  describe("makeTextCharTileSet") {
    it("fails if ram < 512") {
      an [AssertionError] should be thrownBy {
        TextCharTileSet(Vector.fill(511)(0.toChar))
      }
    }

    it("fails if ram > 512") {
      an [AssertionError] should be thrownBy {
        TextCharTileSet(Vector.fill(513)(0.toChar))
      }
    }

    it("creates a text char tile set") {
      val ram = 0.until(128).flatMap((i) => TextCharTestTileRam).to[Vector]
      val tile_set = TextCharTileSet(ram)
      tile_set.size should === (128)
      checkTextCharTile(tile_set.head)
      checkTextCharTile(tile_set.last)
    }
  }

  describe("makeLargeTile") {
    it("fails if tile_ram < 32") {
      a [AssertionError] should be thrownBy {
        LargeTile(Vector.fill(31)(0.toChar))
      }
    }

    it("fails if tile_ram > 32") {
      a [AssertionError] should be thrownBy {
        LargeTile(Vector.fill(33)(0.toChar))
      }
    }

    it("returns a 16 x 16 tile of 2-bit per pixel values") {
      val tile = LargeTile(LargeTestTileRam)
      checkLargeTile(tile)
    }
  }

  describe("makeSmallTile") {
    it("fails if tile_ram < 8") {
      a [AssertionError] should be thrownBy {
        SmallTile(Vector.fill(7)(0.toChar))
      }
    }

    it("fails if tile_ram > 8") {
      a [AssertionError] should be thrownBy {
        SmallTile(Vector.fill(9)(0.toChar))
      }
    }

    it("returns an 8 x 8 tile of 2-bit per pixel values") {
      val tile = SmallTile(SmallTestTileRam)
      checkSmallTile(tile)
    }
  }

  describe("makeTextCharTile") {
    it("fails if tile_ram < 4") {
      a [AssertionError] should be thrownBy {
        TextCharTile(Vector.fill(3)(0.toChar))
      }
    }

    it("fails if tile_ram > 4") {
      a [AssertionError] should be thrownBy {
        TextCharTile(Vector.fill(5)(0.toChar))
      }
    }

    it("returns an 8 x 8 tile of 1-bit per pixel values") {
      val tile = TextCharTile(TextCharTestTileRam)
      checkTextCharTile(tile)
    }
  }
}
