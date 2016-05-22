package info.ditrapani.ljdcomputer.video

class VideoSpriteSpec extends Spec {
  describe("makeSprite") {
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
