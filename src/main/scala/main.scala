package info.ditrapani.ljdcomputer

import scalafx.application.JFXApp
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.animation.AnimationTimer
import scala.util.{Try, Success, Failure}

object Main extends JFXApp {
  type VideoBuffer = Vector[Vector[Color]]

  Config.load(parameters.unnamed, Map(parameters.named.toSeq: _*)) match {
    case Left(s) => printErrorHelpAndExit(s)
    case Right(config) => loadAndRun(config)
  }

  def printErrorHelpAndExit(message: String): Unit = {
    if (message != "Printing help text...") {
      println(s"\n[ERROR] $message\n")
    }
    val input_stream = getClass.getResourceAsStream("/help.txt")
    val help_text = scala.io.Source.fromInputStream(input_stream).mkString
    println(help_text)
    System.exit(0)
  }

  def loadAndRun(config: Config): Unit = {
    startGfx(config)
  }

  def startGfx(config: Config): Unit = {
    val time_delta: Long = 100 * 1000000L
    val gc = makeGfxContext(config)
    val drawScene = makeSceneDrawer(config, gc)

    var computer: Computer = Computer.load(config.binary)
    var last_time = System.nanoTime()

    AnimationTimer(curr_time => {
      if (curr_time - last_time > time_delta) {
        last_time = curr_time
        // val key_press: Byte = get_key_press()
        val key_press: Byte = 0.toByte
        val (video_buffer, new_computer) = computer.runFrame(key_press)
        drawScene(video_buffer)
        computer = new_computer
      }
    }).start()
  }

  def makeGfxContext(config: Config): GraphicsContext = {
    val width = 256 * config.pixel_multiplier
    val height = 240 * config.pixel_multiplier
    val canvas = new Canvas(width, height)
    val gc = canvas.graphicsContext2D
    canvas.translateX = 0
    canvas.translateY = 0

    gc.setFill(Color.rgb(100, 200, 255))
    gc.fillRect(0, 0, width, height)

    stage = new JFXApp.PrimaryStage {
      title = "ljd 16-bit computer by L. J. Di Trapani"
      scene = new Scene(width.toDouble, height.toDouble) {
        content = canvas
      }
    }

    gc
  }

  def makeSceneDrawer(config: Config, gc: GraphicsContext): VideoBuffer => Unit = {
    val m = config.pixel_multiplier

    (VideoBuffer) => {
      for ((row: Vector[Color], j: Int) <- VideoBuffer.zipWithIndex) {
        for ((color: Color, i: Int) <- row.zipWithIndex) {
          gc.setFill(color)
          gc.fillRect(i * m,  j * m, m, m)
        }
      }
    }
  }
}
