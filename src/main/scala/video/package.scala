package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

package object video {
  type VideoBuffer = Vector[Vector[Color]]
  type Ram = Vector[Char]
  type LargeTileSet = Vector[LargeTile]
  type SmallTileSet = Vector[SmallTile]
  type TextCharTileSet = Vector[TextCharTile]
  type LargeTile = Vector[Vector[(Boolean, Boolean)]]
  type SmallTile = Vector[Vector[(Boolean, Boolean)]]
  type TextCharTile = Vector[Vector[Boolean]]
  type BgCellGrid = Vector[Vector[BgCell]]
  type TextCharCellGrid = Vector[Vector[TextCharCell]]
}
