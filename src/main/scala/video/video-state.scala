package info.ditrapani.ljdcomputer.video

import scala.collection.mutable.ArraySeq
import scalafx.scene.paint.Color

final case class VideoState(cells: CellGrid, colors: Colors, tiles: TileSet) {
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
    val tile = tiles.vector(cell.tile_index.toInt)
    val background = colors.vector(cell.background_color.toInt)
    val foreground = colors.vector(cell.foreground_color.toInt)
    for ((row, y) <- tile.rows.zip(j.to(j + 12))) {
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
  def make(cells: Ram, colors: Colors, tiles: TileSet): VideoState = {
    assert(cells.size == 640)           // 32 * 20 * 1
    VideoState(CellGrid(cells), colors, tiles)
  }
}
