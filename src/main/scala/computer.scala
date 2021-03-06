package info.ditrapani.ljdcomputer

import info.ditrapani.ljdcomputer.video.{Video, VideoRoms, Ram}
import info.ditrapani.ljdcomputer.cpu.Cpu

final case class Computer(cpu: Cpu, video_roms: VideoRoms, ram: Ram, video_obj: Video) {
  type VideoBuffer = video.VideoBuffer

  def runFrame(): (Computer) = {
    val (ram2, new_cpu) = cpu.run(ram)
    Computer(new_cpu, video_roms, ram2, video_obj)
  }

  def swapRam(key_presses: Char): Computer = {
    val ram2 = addKeyPresses(key_presses)
    val (new_video, ram3) = swapVideoRam(ram2)
    Computer(cpu, video_roms, ram3, new_video)
  }

  def setInstructionCounterIfInterruptEnable(): Computer = {
    val (interrupt_enable, interrupt_vector) = getInterruptValues(ram)
    interrupt_enable match {
      case false =>
        this
      case true =>
        val new_cpu = cpu.copy(instruction_counter = interrupt_vector)
        Computer(new_cpu, video_roms, ram, video_obj)
    }
  }

  def renderVideoBuffer(): VideoBuffer = {
    video_obj.buffer
  }

  private def addKeyPresses(key_presses: Char): Vector[Char] =
    ram.slice(0, 0xFFFD) ++ Vector(key_presses) ++ ram.slice(0xFFFE, 0x10000)

  private def swapVideoRam(old_ram: Vector[Char]): (Video, Vector[Char]) = {
    val new_ram =
      old_ram.slice(0, 0xFC00) ++
      video_obj.getVideoRam ++
      old_ram.slice(0xFFFD, old_ram.size)
    (Video.make(old_ram, video_roms), new_ram)
  }

  private def getInterruptValues(ram: Ram): (Boolean, Char) = {
    val enable = (ram(0xFFFE) & 1) == 1
    (enable, ram(0xFFFF))
  }
}

object Computer {
  def load(binary: Vector[Char]): Computer = {
    val size64k = 64 * 1024
    val video_rom_size = 1024 + 512 + 16
    val video_rom_start = size64k
    val ram_start = video_rom_start + video_rom_size
    val end = 132624
    assert(binary.size <= end)
    val rom = binary
      .slice(0, video_rom_start)
      .padTo(size64k, 0.toChar)
    val custom_video_rom = binary
      .slice(video_rom_start, ram_start)
      .padTo(video_rom_size, 0.toChar)
    val ram = binary
      .slice(ram_start, end)
      .padTo(size64k, 0.toChar)
    val video_roms = VideoRoms.make(custom_video_rom)
    Computer(Cpu.initialize(rom), video_roms, ram, Video.init(video_roms))
  }
}
