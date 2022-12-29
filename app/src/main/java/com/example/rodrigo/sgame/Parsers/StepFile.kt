package parsers

import game.StepObject
import java.io.File
import java.io.FileNotFoundException


interface StepFile {
    var pathFile:String
    var indexStep:Int
    fun parseData (): StepObject
    fun writeFile (path :String)

    class UtilsSteps {
        companion object {
            fun pathToString (path : String ):String {
                var file = File (path)
                return  if (file.exists() && file.isFile) file.bufferedReader().use { i-> i.readText() } else throw  FileNotFoundException()
            }
            fun pathToByteArray (path : String ):ByteArray {
                var file = File (path)
                return  if (file.exists() && file.isFile) file.readBytes() else throw  FileNotFoundException()
            }
        }

    }
}
