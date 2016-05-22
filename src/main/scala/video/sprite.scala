package info.ditrapani.ljdcomputer.video

case class Sprite(
  color_pair_1: Byte,
  color_pair_2: Byte,
  x_flip: Boolean,
  y_flip: Boolean,
  tile_index: Byte,
  x_position: Byte,
  y_position: Byte,
  on: Boolean
)

object LargeSpriteArray {
  val makeLargeSprite = LargeSprite.apply _

  def apply(ram: Ram): Vector[Sprite] = {
    SpriteArray(ram, makeLargeSprite)
  }
}

object SmallSpriteArray {
  val makeSmallSprite = SmallSprite.apply _

  def apply(ram: Ram): Vector[Sprite] = {
    SpriteArray(ram, makeSmallSprite)
  }
}

object SpriteArray {
  def apply(ram: Ram, f: (Ram) => Sprite): Vector[Sprite] = {
    assert(ram.size == 128)
    ram.grouped(2).map(f(_)).to[Vector]
  }
}

object LargeSprite {
  def apply(word_pair: Vector[Char]): Sprite = {
    Sprite(word_pair, true)
  }
}

object SmallSprite {
  def apply(word_pair: Vector[Char]): Sprite = {
    Sprite(word_pair, false)
  }
}

object Sprite {
  def getXYpos(word: Char, large_sprite: Boolean): (Byte, Byte) = {
    val mask =
      if (large_sprite) {
        0x0F
      } else {
        0x1F
      }
    (((word >> 8) & mask).toByte, (word & mask).toByte)
  }

  def apply(word_pair: Vector[Char], large_sprite: Boolean): Sprite = {
    assert(word_pair.size == 2)
    val (w1, w2) = (word_pair(0), word_pair(1))
    val (color_pair_1, color_pair_2, x_flip, y_flip, tile_index) =
      VideoState.getColorXYandIndex(w1)
    val (x_position, y_position) = getXYpos(w2, large_sprite)
    val on: Boolean = ((w2 >> 7) & 1) > 0
    Sprite(
      color_pair_1, color_pair_2, x_flip, y_flip,
      tile_index, x_position, y_position, on
    )
  }
}
