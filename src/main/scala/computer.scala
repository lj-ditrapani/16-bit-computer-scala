package info.ditrapani.ljdcomputer

import info.ditrapani.ljdcomputer.video.Video
import scalafx.scene.paint.Color

case class Computer(cpu: Cpu, ram: Vector[Char], video_obj: Video) {
  type VideoBuffer = video.VideoBuffer

  def runFrame(key_press: Byte): (VideoBuffer, Computer) = {
    val ram2 = addKeyPress(key_press)
    val (ram3, new_cpu) = cpu.step(400000, ram2)
    val (new_video, ram4) = swapVideoRam(ram3)
    val new_computer = Computer(new_cpu, ram4, new_video)
    (new_video.buffer, new_computer)
  }

  def addKeyPress(key_press: Byte): Vector[Char] = ram

  def swapVideoRam(new_ram: Vector[Char]): (Video, Vector[Char]) =
    (video_obj, new_ram)
}

object Computer {
  def load(binary: Vector[Char]): Computer = {
    val end = 64 * 1024
    val rom = binary.slice(0, end).padTo(end, 0.toChar)
    val ram = binary.slice(end, binary.length).padTo(end, 0.toChar)
    Computer(Cpu(rom), ram, Video.make(ram))
  }
}
