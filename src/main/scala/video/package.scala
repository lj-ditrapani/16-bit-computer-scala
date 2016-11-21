package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

package object video {
  type VideoBuffer = Seq[Seq[Color]]
  type Ram = Vector[Char]
  type Rom = Vector[Char]
  type CellGrid = Vector[Vector[Cell]]
}
