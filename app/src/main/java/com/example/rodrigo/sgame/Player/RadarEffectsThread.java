package com.example.rodrigo.sgame.Player;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.EffectStep;

import java.util.ArrayList;

public class RadarEffectsThread extends Thread {
    public boolean running = true;
    GamePlay game;


    public RadarEffectsThread(GamePlay game) {
        this.game = game;
    }


    @Override
    public void run() {
        while (running) {
            try {

                if (checkTime(game.BPMS, game.posBPM + 1) && !Common.testingRadars) {
                    game.BPM = game.BPMS.get(game.posBPM + 1)[1];
                    double difBettwenBeats = game.currentBeat - game.BPMS.get(game.posBPM + 1)[0];
                    game.currentBeat = game.BPMS.get(game.posBPM + 1)[0] + (game.BPMS.get(game.posBPM)[0] / game.BPMS.get(game.posBPM + 1)[0]) * difBettwenBeats;
                    game.posBPM++;

                }

                if (checkTime(game.DELAYS, game.posDelay) && !Common.testingRadars) {
                    double dif = game.currentBeat - game.DELAYS.get(game.posDelay)[0];
                    game.currentBeat = game.DELAYS.get(game.posDelay)[0] + 0.00000;
                    game.curentempobeat = System.nanoTime();
                    game.currentDelay = game.DELAYS.get(game.posDelay)[1] + game.currentSecond - Common.beat2Second(dif * 1.0018, game.BPM);//0.946f
                    game.posDelay++;
                }
                //stops
                if (checkTime(game.STOPS, game.posStop) && !Common.testingRadars) {
                    //                                                                                                                                                                                 delayTime= (long) (STOPS.get(posStop)[1]*1000000000.0);
                    double dif = game.currentBeat - game.STOPS.get(game.posStop)[0];
                    game.currentBeat = game.STOPS.get(game.posStop)[0];
                    game.curentempobeat = System.nanoTime();
                    game.currentDelay = game.STOPS.get(game.posStop)[1] + game.currentSecond - Common.beat2Second(dif * 1.018, game.BPM);
                    // STOPS.remove(posStop);
                    game.posStop++;
                }

                if (checkTime(game.FAKES, game.posFake)) {
                    game.currentDurationFake = game.FAKES.get(game.posFake)[1] + game.FAKES.get(game.posFake)[0];
                    game.posFake++;
                }
                //spedmods
                if (checkTime(game.SPEEDS, game.posSpeed)) {
                    game.metaBeatSpeed = game.currentBeat + game.SPEEDS.get(game.posSpeed)[2];
                    // game.lastSpeed = game.speedMod;
                    game.beatsofSpeedMod = game.SPEEDS.get(game.posSpeed)[2];
                    game.speedMod = game.SPEEDS.get(game.posSpeed)[1];
                    if (game.speedMod < 0) {
                        game.speedMod = 0;
                    }
                    game.posSpeed++;
                }

                //tickcount
                if (checkTime(game.TICKCOUNTS, game.posTickCount)) {
                    game.currentTickCount = game.TICKCOUNTS.get(game.posTickCount)[1].intValue();
                    game.posTickCount++;
                }

                if (checkTime(game.COMBOS, game.posCombo)) {
                    game.currentCombo = game.COMBOS.get(game.posCombo)[1].intValue();
                    game.posCombo++;
                }

                //scrolls
                //
                if (checkTime(game.SCROLLS, game.posScroll)) {
                    game.posScroll++;
                }

                //se interpola el speedMod
                if (game.metaBeatSpeed >= game.currentBeat) {


                    double div = (game.metaBeatSpeed - game.currentBeat) / game.SPEEDS.get(game.posSpeed - 1)[2];//%
                    if (div >= 0) {
                        if ((game.posSpeed > 1) && (div < 1 || game.SPEEDS.get(game.posSpeed - 1)[2] == 0)) {
                            double difvel = game.SPEEDS.get(game.posSpeed - 2)[1] - game.SPEEDS.get(game.posSpeed - 1)[1];
                            game.speedMod = (game.lastSpeed + (div * difvel));
                        } else if (div >=1 ) {
                            game.speedMod=game.SPEEDS.get(game.posSpeed-1)[1];
                           game.lastSpeed = game.speedMod;
                        }

                    } else {
                        game.speedMod = game.SPEEDS.get(game.posSpeed - 1)[1];
                        game.lastSpeed = game.speedMod;

                    }
                } else if (game.posSpeed > 1) {
                    game.speedMod = game.SPEEDS.get(game.posSpeed - 1)[1];
                    game.lastSpeed = game.speedMod;
                }
                game.doEvaluate = !(game.currentDurationFake >= game.currentBeat);

                //WARP

                if (game.currentDelay <= 0 && checkTime(game.WARPS, game.posWarp) && !Common.testingRadars) {
                    if (game.currentBeat >= game.WARPS.get(game.posWarp)[1] + game.WARPS.get(game.posWarp)[0]) {
                        game.posWarp++;
                    } else {
                        //  game.lostBeatbyWarp = game.currentBeat - game.WARPS.get(game.posWarp)[0];
                        game.currentBeat += game.WARPS.get(game.posWarp)[1]; //perdidad
                        game.posWarp++;

                        while (game.posBuffer + 1 < game.bufferSteps.size() && (double) game.bufferSteps.get(game.posBuffer + 1).beat <= game.currentBeat) {
                            game.posBuffer++;
                            if (game.bufferSteps.get(game.posBuffer).effect != null && Common.testingRadars) {
                                for (EffectStep effect : game.bufferSteps.get(game.posBuffer).effect) {
                                    effect.execute(game);
                                }
                            }

                        }
                        game.curentempobeat = System.nanoTime();
                    }

                    //
                    //  event = lostBeatbyWarp + "";

                    //rrentbeat = WARPS.get(posWarp)[1] + WARPS.get(posWarp)[0]+lostBeatbyWarp;//perdidad
                }

                // event = lostBeatbyWarp + "";


                sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public boolean checkTime(ArrayList<Float[]> array, int pos) {
        return array != null && (array.size() > 0 && array.size() > pos && game.currentBeat >= array.get(pos)[0]);
    }





}
