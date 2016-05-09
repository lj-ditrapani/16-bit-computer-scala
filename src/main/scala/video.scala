package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

case class Video(video_ram: Vector[Char], enable: Boolean, custom_tiles: Boolean) {
  type VideoBuffer = Vector[Vector[Color]]

  def buffer: VideoBuffer =
    if (enable)
      // if custom_tiles use ram tile-set, else use built-in tile-set
      (for (i <- 0 until 240) yield {
        (for (j <- 0 until 256) yield Color.rgb(200, 200, 255)).to[Vector]
      }).to[Vector]
    else
      Video.disabledBuffer
}

object Video {
  type VideoBuffer = Vector[Vector[Color]]

  def make(ram: Vector[Char]): Video = {
    val enable_bits = ram(0xDDF3)
    val enable: Boolean = (enable_bits & 2) > 0
    val custom_tiles: Boolean = (enable_bits & 4) > 0
    val video_ram = ram.slice(0xF000, 0x10000)
    Video(video_ram, enable, custom_tiles)
  }

  val disabledBuffer: VideoBuffer =
    (for (i <- 0 until 240) yield {
      (for (j <- 0 until 256) yield Color.rgb(0, 0, 0)).to[Vector]
    }).to[Vector]
}
