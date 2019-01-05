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
                    //   game.posBPM++;

                  /*  if (game.BPMS.get(game.posBPM)[1] >= 1000) {
                        game.posBPM++;
                        if (game.currentBeat < game.BPMS.get(1 + game.posBPM)[0]) { //BPM warp


                            game.lostBeatbyWarp = game.currentBeat - game.BPMS.get(game.posBPM + 1)[0];

                            game.BPM = game.BPMS.get(game.posBPM)[1];
                            game.currentDurationFake -= (game.BPMS.get(game.posBPM)[0] - game.currentBeat);
                            game.currentBeat = game.BPMS.get(game.posBPM)[0];

                            while ((double) game.bufferSteps.get(game.posBuffer + 1).beat <= game.currentBeat) {
                                game.posBuffer++;
                            }

                        }

                    }*/


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

                  /*  double dif2 = game.currentBeat - values[0];
                    game.currentBeat = values[0];
                    game.curentempobeat = System.nanoTime();
                    game.currentDelay = this.values[1] + game.currentSecond;//   - Common.beat2Second(dif2 * 1.018, game.BPM);

                   // game.currentDelay = game.currentBeat+ this.values[1]+ game.currentSecond - Common.beat2Second(dif2 * 1.00001, game.BPM);//0.946f
                    break;*/
                case "WARPS":

                    if (game.currentBeat < values[1] + values[0]) {

                        game.lostBeatbyWarp = game.currentBeat - values[0];
                        game.currentBeat = values[1] + values[0];//perdidad

                      /*  while (game.posBuffer + 1 < game.bufferSteps.size() && (double) game.bufferSteps.get(game.posBuffer + 1).beat <= game.currentBeat) {
                            game.posBuffer++;
                        }*/
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
                    //game.currentDurationFake = game.FAKES.get(game.posFake)[1] + game.FAKES.get(game.posFake)[0];
                    game.currentDurationFake = (float) (game.currentBeat + values[0]);


                    break;
                default:

            }
        }


    }
}
