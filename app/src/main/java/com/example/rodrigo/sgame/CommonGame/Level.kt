package com.example.rodrigo.sgame.CommonGame

/**
 * Creator : Rodrigo Vidal Villaseñor
 */
class Level {
    var numberLevel: Int = 0
        internal set
    lateinit var gameType: String
        internal set
    lateinit var tag: String
        internal set

    /**
     * Void Constructor
     */
    constructor() {}

    /**
     * Create a Level object for the adapter
     * @param numberLevel Number of level
     * @param gameType Type of step
     * @param tag Some additional information
     */
    constructor(numberLevel: Int, gameType: String, tag: String) {
        this.numberLevel = numberLevel
        this.gameType = gameType
        this.tag = tag
    }
}
