package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

case class Video(video_ram: Vector[Char], enable: Boolean, custom_tiles: Boolean) {
  type VideoBuffer = Vector[Vector[Color]]

  def buffer: VideoBuffer = Vector(Vector())
  // if not enbled -> disabledBuffer
  // else
  //    if custom_tiles use ram tile-set, else use built-in tile-set

  // def disabledBuffer: VideoBuffer =
  // all black 0,0,0
}

object Video {
  def make(ram: Vector[Char]): Video = {
    val enable_bits = ram(0xDDF3)
    val enable: Boolean = (enable_bits & 2) > 0
    val custom_tiles: Boolean = (enable_bits & 4) > 0
    val video_ram = ram.slice(0xF000, 0x10000)
    Video(video_ram, enable, custom_tiles)
  }
}
