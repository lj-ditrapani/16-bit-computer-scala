package info.ditrapani.ljdcomputer.video

class VideoSpriteSpec extends Spec {
  describe("SpriteArray.apply") {
    it("fails if ram size < 128") {
      an [AssertionError] should be thrownBy {
        SpriteArray(Vector.fill(127)(0.toChar), LargeSprite.apply _)
      }
    }

    it("fails if ram size > 128") {
      an [AssertionError] should be thrownBy {
        SpriteArray(Vector.fill(129)(0.toChar), LargeSprite.apply _)
      }
    }

    it("creates an array of sprites") {
      //          cp1 9    cp2 6    true  false  tile-index 53
      val bits1 = "1001" + "0110" + "1" + "0" + "110101"
      //           uuu    x-pos 17  true   uu    y-pos 26
      val bits2 = "000" + "10001" + "1" + "00" + "11010"
      val word1 = Integer.parseInt(bits1, 2).toChar
      val word2 = Integer.parseInt(bits2, 2).toChar
      val ram = Vector.fill(128)(0.toChar).updated(6, word1).updated(7, word2)
      val sprites = SmallSpriteArray(ram)
      sprites.size should ===(64)
      sprites(3) should ===(Sprite(9, 6, true, false, 53, 17, 26, true))
    }
  }

  describe("Sprite.apply") {
    it("fails if word_pair < 2") {
      a [AssertionError] should be thrownBy {
        Sprite(Vector.fill(1)(0.toChar), false)
      }
    }

    it("fails if word_pair > 2") {
      a [AssertionError] should be thrownBy {
        Sprite(Vector.fill(3)(0.toChar), false)
      }
    }

    it("creates a small sprite") {
      //       cp1 7    cp2 8   xflip  yflip index 19
      val w1 = "0111" + "1000" + "0" + "1" + "010011"
      //         XX    xpos 31   true   XX     ypos 27
      val w2 = "000" + "11111" + "1" + "00" +  "11011"
      val word_pair = Vector(
        Integer.parseInt(w1, 2).toChar,
        Integer.parseInt(w2, 2).toChar
      )
      SmallSprite(word_pair) should === (
        Sprite(7.toByte, 8.toByte, false, true, 19, 31, 27, true)
      )
    }

    it("creates a large sprite") {
      //       cp1 7    cp2 8   xflip  yflip index 19
      val w1 = "0111" + "1000" + "0" + "1" + "010011"
      //         XX    xpos 14   true   XX     ypos 11
      val w2 = "0000" + "1110" + "1" + "000" +  "1011"
      val word_pair = Vector(
        Integer.parseInt(w1, 2).toChar,
        Integer.parseInt(w2, 2).toChar
      )
      LargeSprite(word_pair) should === (
        Sprite(7.toByte, 8.toByte, false, true, 19, 14, 11, true)
      )
    }
  }
}
