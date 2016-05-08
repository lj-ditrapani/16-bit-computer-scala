package info.ditrapani.ljdcomputer

import scalafx.scene.paint.Color

case class Computer(cpu: Cpu, ram: Vector[Char], video: Video) {
  type VideoBuffer = Vector[Vector[Color]]

  def runFrame(key_press: Byte): (VideoBuffer, Computer) = {
    val ram2 = addKeyPress(key_press)
    val (ram3, new_cpu) = cpu.step(400000, ram2)
    val (new_video, ram4) = swapVideoRam(ram3)
    val new_computer = Computer(new_cpu, ram4, new_video)
    (new_video.buffer, new_computer)
  }

  def addKeyPress(key_press: Byte): Vector[Char] = ram

  def swapVideoRam(new_ram: Vector[Char]): (Video, Vector[Char]) = (video, new_ram)
}
