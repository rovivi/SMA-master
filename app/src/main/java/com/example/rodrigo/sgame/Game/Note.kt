package game


class Note {
    var type: Short = 0
    var player: Byte = 0
    var skin: Byte = 0
    var sudden: Boolean = false
    var fake: Boolean = false
    var hidden: Boolean = false
    var vanish: Boolean = false
    // var effects :SDEffect[] //Will be implemented in the future
    var longOrigin:Note?=null

    companion object {
        fun CloneNote(baseNote: Note): Note {
            val newNote = Note()
            newNote.fake = baseNote.fake
            newNote.vanish = baseNote.vanish
            newNote.hidden = baseNote.hidden
            newNote.skin = baseNote.skin
            newNote.player = baseNote.player
            newNote.sudden = baseNote.sudden
            newNote.type = baseNote.type
            return newNote
        }
    }

}