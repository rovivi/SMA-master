package parsers


import com.example.rodrigo.sgame.CommonSteps
import game.GameRow
import game.Note
import game.StepObject
import java.lang.Exception
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FileSSC(override var pathFile: String, override var indexStep: Int) : StepFile {

    override fun writeFile(path: String) {
    }

    override fun parseData(): StepObject {
        //var stringData = StepFile.UtilsSteps.pathToString(pathFile)
        var stringData = pathFile
        val songMetaData: HashMap<String, String> = HashMap()
        val levelMetaData: HashMap<String, String> = HashMap()
        val modifiers: HashMap<String, ArrayList<ArrayList<Double>>> = HashMap()
        val stepObject = StepObject()
        stepObject.levelMetada = HashMap()
        var steps: ArrayList<GameRow> = arrayListOf()

        //Se limpian los comentarios
        stringData = stringData.replace(Regex("(\\s+//-([^;]+)\\s)|(//[\\s+]measure\\s[0-9]+\\s)"), "")

        //Se crea el matcher Para Seccionar el Regex
        val matcher = Pattern.compile("#([^;]+);").matcher(stringData)
        var indexLevel = -1//

        //Parse for the win
        while (matcher.find()) {
            val currentTag = matcher.group().split(":")
            val key = currentTag[0].replace("#", "")
            val value = currentTag[1].replace(";", "")
            if (key == "NOTEDATA") {
                indexLevel++
            }//next step
            when (indexLevel) {
                indexStep -> when (key) {
                    "NOTES" -> steps = processNotes(value)
                    "STEPSTYPE" -> stepObject.stepType = value
                    "BPMS", "STOPS", "DELAYS", "WARPS", "TIMESIGNATURES", "TICKCOUNTS", "COMBOS", "SPEEDS", "SCROLLS" -> {
                        if (value != "") modifiers[key] = fillModifiers(value)
                    }
                    else -> {
                        levelMetaData[key] = value
                    }
                }
                -1 -> {
                    when (key) {//Si se tienen effectos globales
                        "BPMS", "STOPS", "DELAYS", "WARPS", "TIMESIGNATURES", "TICKCOUNTS", "COMBOS", "SPEEDS", "SCROLLS" ->
                            if (value != "") modifiers[key] = fillModifiers(value)
                        else -> songMetaData[key] = value
                    }
                }
            }
        }
        /**End Parsing */
        /**Start Setting effects*/
        modifiers.forEach { modifier ->
            when (modifier.key) {
                "BPMS", "WARPS", "TICKCOUNTS", "SPEEDS", "SCROLLS", "STOPS", "DELAYS", "COMBOS" -> {
                    modifier.value.forEach { values ->
                        //effect List
                        val beat = values[0]
                        val element =
                                steps.firstOrNull { row -> CommonSteps.almostEqual(row.currentBeat, beat) }
                        val index = (steps.indexOf(element))
                        if (index != -1) {
                            if (steps[index].modifiers == null) steps[index].modifiers = HashMap()
                            steps[index].modifiers?.put(modifier.key, values)
                        } else {
                            val newRow = GameRow()
                            newRow.currentBeat = values[0]
                            newRow.modifiers = HashMap()
                            newRow.modifiers?.put(modifier.key, values)
                            steps.add(newRow)
                        }
                    }
                }
            }
        }

        CommonSteps.orderByBeat(steps)//se ordernan
        CommonSteps.applyLongNotes(steps, CommonSteps.lengthByStepType(stepObject.stepType))//Se aplican los longs

        CommonSteps.stopsToScroll(steps)//Se aplican los longs
        CommonSteps.orderByBeat(steps)
        steps.filter { x -> x.notes != null }.forEach { x -> println(x.toString()) }
        stepObject.steps = steps
        /**End Apply effects*/
        stepObject.songMetada = songMetaData
        stepObject.levelMetada=levelMetaData
        //stepObject.steps.forEach { x -> println(x) }
        return stepObject
    }

    private fun fillModifiers(data: String): ArrayList<ArrayList<Double>> {
        val list: ArrayList<ArrayList<Double>> = ArrayList()
        val elements = data.replace("\r", "").replace("\n", "").split(",")
        elements.forEach { e ->
            val currentItem: ArrayList<Double> = ArrayList()
            val params = e.split("=")
            params.forEach { x ->
                currentItem.add(x.toDouble())
            }
            list.add(currentItem)
        }
        return list
    }

    private fun processNotes(notes: String): ArrayList<GameRow> {
        val data = notes.replace(" ", "").replace("\n\n", "\n")
        val listGameRow = ArrayList<GameRow>()
        val blocks = data.split(",")
        var currentBeat = 0.0
        blocks.forEach { block ->
            val rowsStep = block.split("\n").filter { x -> x != "" }
            val blockSize = rowsStep.size
            rowsStep.forEach { row ->
                if (!checkEmptyRow(row)) {
                    val gameRow = stringToGameRow(row)
                    gameRow.currentBeat = currentBeat
                    listGameRow.add(gameRow)
                }
                currentBeat += 4.0 / blockSize
            }
        }
        return listGameRow
    }

    private fun stringToGameRow(data: String): GameRow {
        val gameRow = GameRow()
        var row = data.replace("{x}", "f")
        val re = Regex("\\{([^\\}]+)\\}")
        val matcher = Pattern.compile(re.toString()).matcher(row)
        val arrayNotes = ArrayList<Note>()
        val arrayEspecialNote = ArrayList<String>()
        while (matcher.find()) {
            arrayEspecialNote.add(matcher.group())
        }
        row = row.replace(re, "E")
        var indexEspecialNote = 0
        row.forEach { l ->
            if (l == 'E') {
                arrayNotes.add(specialToNote(arrayEspecialNote[indexEspecialNote]))
                indexEspecialNote++
            } else {
                arrayNotes.add(charToNote(l))
            }
        }
        gameRow.notes = arrayNotes
        return gameRow
    }

    private fun checkEmptyRow(row: String): Boolean {
        return Regex("(0+)").matches(row)
    }

    private fun charToNote(caracter: Char): Note {
        val note = Note()
        var charCode: Short = CommonSteps.NOTE_EMPTY
        when (caracter) {
            '1' -> charCode = CommonSteps.NOTE_TAP
            '2', '6' -> charCode = CommonSteps.NOTE_LONG_START
            '3' -> charCode = CommonSteps.NOTE_LONG_END
            'M' -> charCode = CommonSteps.NOTE_MINE
            'F', 'f' -> charCode = CommonSteps.NOTE_FAKE
            'V' -> {
                charCode = CommonSteps.NOTE_TAP
                note.vanish = true
            }
            'h' -> {
                charCode = CommonSteps.NOTE_TAP
                note.hidden = true
            }
            'x' -> {
                charCode = CommonSteps.NOTE_LONG_START
                note.player = CommonSteps.PLAYER_1
            }
            'X' -> {
                charCode = CommonSteps.NOTE_TAP
                note.player = CommonSteps.PLAYER_1
            }
            'y' -> {
                charCode = CommonSteps.NOTE_LONG_START
                note.player = CommonSteps.PLAYER_2
            }
            'Y' -> {
                charCode = CommonSteps.NOTE_TAP
                note.player = CommonSteps.PLAYER_2
            }
            'z' -> {
                charCode = CommonSteps.NOTE_LONG_START
                note.player = CommonSteps.PLAYER_3
            }
            'Z' -> {
                charCode = CommonSteps.NOTE_TAP
                note.player = CommonSteps.PLAYER_3
            }
        }
        note.type = charCode
        return note
    }

    private fun specialToNote(re: String): Note {
        val noteString = re.replace("{", "").replace("}", "").replace("|", "")
        return try {
            val note = charToNote(noteString[0])
            when (noteString[1]) {
                'v', 'V' -> note.vanish = true
                'h', 'H' -> note.hidden = true
                's', 'S' -> note.sudden = true
            }
            if (noteString[2] == '1') note.fake = true
            note
        } catch (ex: Exception) {
            ex.printStackTrace()
            Note()
        }
    }


}