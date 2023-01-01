package com.kyadevs.stepdroid.CommonGame

class Note {
    var noteType: Byte = 0
    internal var sudden = false
    internal var hidden = false
    var fake = false


    constructor(note: Byte) {
        noteType = note

    }

    constructor() {

    }
}
