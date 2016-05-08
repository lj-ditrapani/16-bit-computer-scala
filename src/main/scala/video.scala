package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

case class Video(video_ram: Vector[Char]) {
  type VideoBuffer = Vector[Vector[Color]]

  def buffer: VideoBuffer = Vector(Vector())
}
