package info.ditrapani.ljdcomputer.video

import scala.collection.mutable.ArraySeq
import scalafx.scene.paint.Color

case class VideoState(cells: CellGrid, colors: Vector[Color8], tiles: TileSet) {
  type Buff = ArraySeq[ArraySeq[Color]]

  def buffer: VideoBuffer = {
    val b = ArraySeq.fill(240, 256)(Color.rgb(0, 0, 0))
    draw_cells(b)
    b
  }

  def draw_cells(b: Buff): Unit = {
    for ((row: Seq[Cell], j) <- cells.zipWithIndex) {
      for ((cell: Cell, i) <- row.zipWithIndex) {
        draw_cell(b, cell, j * 12, i * 8)
      }
    }
  }

  def draw_cell(b: Buff, cell: Cell, j: Int, i: Int): Unit = {
    val tile = tiles(cell.tile_index)
    val background = colors(cell.background_color)
    val foreground = colors(cell.foreground_color)
    for ((row, y) <- tile.zip(j.to(j + 12))) {
      for ((pixel, x) <- row.zip(i.to(i + 8))) {
        val color: Color8 = pixel match {
          case false => background
          case true => foreground
        }
        b(y).update(x, color.toColor)
      }
    }
  }
}

object VideoState {
  def apply(cells: Ram, colors: Ram, tiles: Ram): VideoState = {
    assert(cells.size == 640)           // 32 * 20 * 1
    assert(colors.size == 16)           // 16 * 1
    assert(tiles.size == 1536)          // 256 * 6
    VideoState(CellGrid(cells), Colors(colors), TileSet(tiles))
  }
}
