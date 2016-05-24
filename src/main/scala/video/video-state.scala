package info.ditrapani.ljdcomputer.video

import scala.collection.mutable.ArraySeq
import scalafx.scene.paint.Color

case class VideoState(
  large_tiles: Vector[LargeTile],
  small_tiles: Vector[SmallTile],
  text_char_tiles: Vector[TextCharTile],
  bg_cells: Vector[Vector[BgCell]],
  text_cells: Vector[Vector[TextCharCell]],
  bg_colors: Vector[(Color8, Color8)],
  fg_colors: Vector[(Color8, Color8)],
  large_sprites: Vector[Sprite],
  small_sprites: Vector[Sprite]
) {
  type Buff = ArraySeq[ArraySeq[Color]]

  def buffer: VideoBuffer = {
    val b = ArraySeq.fill(240, 256)(Color.rgb(0, 0, 0))
    draw_bg_cells(b)
    draw_text_cells(b)
    draw_large_sprites(b)
    draw_small_splrites(b)
    b
  }

  def draw_bg_cells(b: Buff): Unit = {
    for ((row: Seq[BgCell], j) <- bg_cells.zipWithIndex) {
      for ((cell: BgCell, i) <- row.zipWithIndex) {
        draw_bg_cell(b, cell, j * 16, i * 16)
      }
    }
  }

  def draw_bg_cell(b: Buff, cell: BgCell, j: Int, i: Int): Unit = {
    val tile = large_tiles(cell.large_tile_index)
    val cp1 = bg_colors(cell.color_pair_1)
    val cp2 = bg_colors(cell.color_pair_2)
    for ((row, y) <- tile.zip(j.to(j + 16))) {
      for ((pixel, x) <- row.zip(i.to(i + 16))) {
        val (pair, lr) = pixel
        val cp =
          if (pair) {
            cp2
          } else {
            cp1
          }
        val color =
          if (lr) {
            cp._2
          } else {
            cp._1
          }
        b(y).update(x, color.toColor)
      }
    }
  }

  def draw_text_cells(b: Buff): Unit = {
  }

  def draw_large_sprites(b: Buff): Unit = {
  }

  def draw_small_splrites(b: Buff): Unit = {
  }
}

object VideoState {
  def apply(tiles: Ram, cells: Ram, colors: Ram, sprites: Ram): VideoState = {
    assert(tiles.size == 3072)          // 2048 + 512 + 512
    assert(cells.size == 736)           // 240 + 16 + 480
    assert(colors.size == 32)           // 16 + 16
    assert(sprites.size == 256)         // 128 + 128
    val (large_tiles, other_tiles) = tiles.splitAt(2048)
    val (small_tiles, text_char_tiles) = other_tiles.splitAt(512)
    val (bg_cells, text_cells) = cells.splitAt(256)
    val (bg_colors, sprite_colors) = colors.splitAt(16)
    val (large_sprites, small_sprites) = sprites.splitAt(128)
    VideoState(
      LargeTileSet(large_tiles),
      SmallTileSet(small_tiles),
      TextCharTileSet(text_char_tiles),
      BgCellGrid(bg_cells.take(240)),   // 16 unused cells in 256 block
      TextCharCellGrid(text_cells),
      ColorPairs(bg_colors),
      ColorPairs(sprite_colors),
      LargeSpriteArray(large_sprites),
      SmallSpriteArray(small_sprites)
    )
  }

  def getColorXYandIndex(word: Char): (Byte, Byte, Boolean, Boolean, Byte) = {
    val color_pair_1 = (word >> 12).toByte
    val color_pair_2 = ((word >> 8) & 0xF).toByte
    val x_flip = ((word >> 7) & 1) > 0
    val y_flip = ((word >> 6) & 1) > 0
    val tile_index = (word & 0x3F).toByte
    (color_pair_1, color_pair_2, x_flip, y_flip, tile_index)
  }
}
