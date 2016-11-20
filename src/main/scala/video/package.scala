package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

package object video {
  type VideoBuffer = Seq[Seq[Color]]
  type Ram = Vector[Char]
  type Rom = Vector[Char]
  type TileSet = Vector[Tile]
  type Tile = Vector[Vector[Boolean]]
  type CellGrid = Vector[Vector[Cell]]
}
