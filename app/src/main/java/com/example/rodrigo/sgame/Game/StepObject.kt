package game

import java.util.*

class StepObject {
    /**Game Data*/
    lateinit var steps: ArrayList<GameRow>
    var songMetada: Map<String, String> = HashMap()
    var levelMetada: Map<String, String> = HashMap()
    var name: String = ""
    var offset: Double = 0.0
    var stepType: String = ""


    /**Media Data*/
    var path: String=""
    var songFileName: String = ""
    var bgImageFileName: String = ""





    //Functions

    fun getMusicPath():String{
        return path+"/"+ songMetada.get("MUSIC")
    }

    ///TEST AREA
//ATTACKS= setMetadata(stepData.chartsInfo[nchar].get("ATTACKS"),stepData.songInfo.get("ATTACKS"));

    fun getSongOffset():Float{

    if (levelMetada["OFFSET"] != null)
    {
        val xof: String = levelMetada["OFFSET"].toString()
        offset = xof.toFloat().toDouble()
    } else
    {
        val xof: String = songMetada["OFFSET"].toString()
        offset += xof.toFloat()
    }
//    offset += (com.example.rodrigo.sgame.CommonGame.Common.OFFSET as kotlin.Float / 1000)
        return offset.toFloat()
    }


//    fun getBPM():Doubl{
//
//        return steps[0].modifiers!!["BPMS"]).get(1)
//    }

}


