package shinni.badapple
import kotlinx.coroutines.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import java.io.IOException

fun executeFFmpegCommand(command: String) {
    val process = ProcessBuilder(*command.split(" ").toTypedArray())
        .redirectErrorStream(true)
        .redirectOutput(ProcessBuilder.Redirect.DISCARD) // Redirect output to /dev/null or NUL
        .start()
    process.waitFor()
}

fun clearTerminal() {
    print("\u001b[2J") // Clear the terminal
    print("\u001b[H") // Move cursor to the top left corner
}

fun main() {
    val framesDir = "frames"
    val videoFilePath = "src/main/kotlin/bad_apple.mp4"
    val audioFile = "src/main/kotlin/bad_apple.wav" // WAV audio file
    val frameRate = 24  // FPS

    // Check if frames directory exists, if not, create it
    val framesDirectory = File(framesDir)
    if (!framesDirectory.exists()) {
        framesDirectory.mkdir()
    }

    clearTerminal() // Clear the terminal before rendering frames

    // Execute FFmpeg command to extract frames from the video
    println("Rendering frames...")
    executeFFmpegCommand("ffmpeg -i $videoFilePath -vf fps=$frameRate $framesDir/frame_%04d.bmp")
    println("Frames rendered.")

    // Check if frames were extracted successfully
    val frames = framesDirectory.listFiles { _, name -> name.endsWith(".bmp") }?.sorted() ?: emptyList()
    if (frames.isEmpty()) {
        println("No frames were extracted. Please check the FFmpeg command and try again.")
        return
    }

    // Load WAV audio file
    val audioStream = AudioSystem.getAudioInputStream(File(audioFile))

    val clip = AudioSystem.getLine(DataLine.Info(Clip::class.java, audioStream.format)) as Clip
    clip.open(audioStream)

    // Prepare ASCII conversion
    var terminalWidth: Int
    var terminalHeight: Int

    try {
        terminalWidth = Runtime.getRuntime().exec("tput cols").inputStream.bufferedReader().readLine()?.toInt() ?: 80
        terminalHeight = Runtime.getRuntime().exec("tput lines").inputStream.bufferedReader().readLine()?.toInt() ?: 24
    } catch (e: IOException) {
        // Handle the case where getting terminal dimensions fails
        terminalWidth = 80
        terminalHeight = 24
    }

    val asciiChars = arrayOf(' ', '.', ':', '-', '=', '+', '*', '#', '%', '@')

    // Play audio
    GlobalScope.launch {
        clip.framePosition = 0
        clip.start()
        clip.loop(Clip.LOOP_CONTINUOUSLY)
        delay(clip.microsecondLength / 1000) // Sleep until the audio finishes
    }

    // Display frames
    println("Rendering frames...")
    runBlocking {
        frames.forEachIndexed { _, frame ->
            val image = ImageIO.read(frame)
            val asciiArt = convertToAscii(image, terminalWidth, terminalHeight, asciiChars)

            // Clear the terminal before rendering each frame
            clearTerminal()

            // Print ASCII art
            print(asciiArt)

            // Flush output to make sure everything is printed
            System.out.flush()

            // Delay for the frame rate
            delay((1000 / frameRate).toLong())

        }
    }
    println("Rendering complete.")

    clip.close()
}

fun convertToAscii(image: BufferedImage, width: Int, height: Int, asciiChars: Array<Char>): String {
    val sb = StringBuilder()

    val scaledImage = image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH)
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = bufferedImage.createGraphics()
    graphics.drawImage(scaledImage, 0, 0, null)
    graphics.dispose()

    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = bufferedImage.getRGB(x, y)
            val r = (color shr 16) and 0xff
            val g = (color shr 8) and 0xff
            val b = color and 0xff
            val luminance = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
            val charIndex = (luminance * (asciiChars.size - 1)) / 255
            sb.append(asciiChars[charIndex])
        }
        sb.append("\n")
    }
    return sb.toString()
}
