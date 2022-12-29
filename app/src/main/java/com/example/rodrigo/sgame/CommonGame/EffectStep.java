package com.example.rodrigo.sgame.CommonGame;

import com.example.rodrigo.sgame.Player.GamePlay;

public class EffectStep {
    public String type = null;
    public Float[] values;

    public EffectStep(String type, Float[] values) {
        this.type = type;
        this.values = values;
    }


    public void execute(GamePlay game) {
        if (values != null) {
            switch (this.type) {
                case "BPMS":
                    game.BPM = values[1];
                    break;
                case "STOPS":
                    game.posStop++;
                    double dif = game.currentBeat - values[0];
                    game.currentBeat = values[0];
                    game.curentempobeat = System.nanoTime();
                    game.currentDelay = values[1] + game.currentSecond - Common.beat2Second(dif * 1.018, game.BPM);
                    game.currentDelay = this.values[1] + game.currentSecond;//- Common.beat2Second(dif * 1.018, game.BPM);
                    break;
                case "DELAYS":
                    game.posDelay++;
                     dif = game.currentBeat - values[0];
                    game.currentBeat = values[0];
                    game.curentempobeat = System.nanoTime();
                    game.currentDelay = values[1] + game.currentSecond - Common.beat2Second(dif * 1.018, game.BPM);
                    game.currentDelay = this.values[1] + game.currentSecond;//- Common.beat2Second(dif * 1.018, game.BPM);

                    break;
                case "WARPS":
                    if (game.currentBeat < values[1] + values[0]) {
                        game.lostBeatbyWarp = game.currentBeat - values[0];
                        game.currentBeat = values[1] + values[0];//perdidad
                    }
                    break;
                case "TIMESIGNATURES":
                    break;
                case "TICKCOUNTS":
                    game.currentTickCount = values[1].intValue();
                    break;
                case "COMBOS":
                    break;
                case "SPEEDS":
                    break;
                case "LABELS":
                    break;
                case "SCROLLS":
                    break;
                case "FAKES":
                    game.currentDurationFake = (float) (game.currentBeat + values[0]);
                    break;
                default:

            }
        }


    }
}
