package com.example.rodrigo.sgame.Player;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.EffectStep;
import com.example.rodrigo.sgame.CommonGame.Note;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;

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
                        } else if (div >= 1) {
                            game.speedMod = game.SPEEDS.get(game.posSpeed - 1)[1];
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

                if (Common.EVALUATE_ON_SECUNDARY_THREAD) {
                    evaluate();
                }
                sleep(1);
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


    public void evaluate() {
        if (game.doEvaluate) {
            double[] currentJudge = Common.JUDMENT[ParamsSong.judgment];
            if (ParamsSong.autoplay) {
                game.ObjectCombo.posjudge = 0;
                if (containTapNote(game.bufferSteps.get(game.posBuffer).rowStep)) {
                    game.playTick();
                    combopp();
                    game.currentLife += 0.5 * game.currentCombo;
                    game.ObjectCombo.show();
                    Note[] auxrow = game.bufferSteps.get(game.posBuffer).rowStep;
                    for (int w = 0; w < auxrow.length; w++) {//animations
                        int aux = auxrow[w].noteType;
                        if (aux == 1) {
                            game.steps.noteSkins[0].explotions[w].play();
                        } else if (aux == 2) {
                            game.steps.noteSkins[0].explotions[w].play();
                            game.steps.noteSkins[0].explotionTails[w].play();
                        } else if (aux == 0) {
                            game.steps.noteSkins[0].explotionTails[w].stop();
                        }
                    }
                    game.bufferSteps.get(game.posBuffer).rowStep = game.preseedRow;


                } else if (containLongs(game.bufferSteps.get(game.posBuffer).rowStep)) {


                    game.residuoTick += ((double) game.currentTickCount / 48);
                    if (game.residuoTick >= 1) {
                        game.residuoTick -= 1;
                        game.ObjectCombo.show();
                        combopp();
                        game.currentLife += 0.5 * game.currentCombo;
                    }


                    game.bufferSteps.get(game.posBuffer).rowStep = game.preseedRow;

                }

            } else {//juicio normal
                int posBack;
                int posNext;
                int backSteps;
                int rGreat = mil2BackSpaces((float) currentJudge[3]);
                int rGood = rGreat + mil2BackSpaces((float) currentJudge[2]);
                int rBad = rGood + mil2BackSpaces((float) currentJudge[1]);
                backSteps = rBad + 1;

                posBack = -backSteps;
                if (backSteps >= game.posBuffer) {
                    posBack = -game.posBuffer;
                }
                posNext = backSteps;

             /*   if (containTaps(game.bufferSteps.get(game.posBuffer + posBack).rowStep)) {//evaluate miss
                    comboLess();
                    game.bufferSteps.get(game.posBuffer + posBack).rowStep = game.preseedRow;
                    posBack++;
                }
                if (containsMine(game.bufferSteps.get(game.posBuffer + posBack).rowStep)) {//evaluate miss
                    game.bufferSteps.get(game.posBuffer + posBack).rowStep = game.preseedRow;
                    posBack++;
                }
                if (containLongs(game.bufferSteps.get(game.posBuffer + posBack).rowStep)) {
                    game.residuoTick += ((double) game.currentTickCount / 48);
                    if (game.residuoTick >= 1) {
                        game.residuoTick -= 1;
                        game.ObjectCombo.show();
                        comboLess();
                        game.currentLife += 0.5 * game.currentCombo;
                        game.miss += game.currentCombo;
                        //bufferSteps.get(posBuffer + posBack).rowStep = preseedRow;
                    }
                }*/
                int posEvaluate = -1;
                while ((game.posBuffer + posBack < game.bufferSteps.size()) && posBack <= posNext && game.fingersOnScreen > 0) {
                    if (containSteps((Note[]) game.bufferSteps.get(game.posBuffer + posBack).rowStep)) {
                        boolean checkLong = true;
                        //byte[] auxRow = (byte[]) bufferSteps.get(posBuffer + posBack)[0];
                        for (int w = 0; w < game.bufferSteps.get(game.posBuffer + posBack).rowStep.length; w++) {
                            Note currentChar = game.bufferSteps.get(game.posBuffer + posBack).rowStep[w];


                            if (posBack < 1 && (game.inputs[w] != 0) && (containLongs(currentChar))) {

                                if (containStartLong(game.bufferSteps.get(game.posBuffer).rowStep)) {
                                    game.ObjectCombo.show();
                                    combopp();
                                    game.currentLife += 0.5 * game.currentCombo;
                                }
                                game.steps.noteSkins[0].explotionTails[w].play();


                                if (checkLong) {
                                    game.residuoTick += ((double) game.currentTickCount / 48);
                                    checkLong = false;// se hace que no se sume de nuevo
                                }
                                (game.bufferSteps.get(game.posBuffer + posBack).rowStep)[w].noteType = 100;//Se vacia el array

                                if (game.residuoTick >= 1) {
                                    game.residuoTick -= 1;
                                    game.ObjectCombo.posjudge = 0;
                                    game.ObjectCombo.show();
                                    game.currentLife += 0.5 * game.currentCombo;
                                    combopp();

                                    game.perfect += game.currentCombo;
                                }
                                game.inputs[w] = 2;

                            }

                            if (game.inputs[w] == 1 && containTaps(currentChar)) {// tap1
                                game.steps.noteSkins[0].explotions[w].play();
                                game.bufferSteps.get(game.posBuffer + posBack).rowStep[w].noteType = 0;
                                game.inputs[w] = 2;
                                posEvaluate = game.posBuffer + posBack;

                            }

                            if (game.inputs[w] == 1 && containsMine(currentChar)) {//tap mine
                                // steps.explotions[w].play();
                                game.bufferSteps.get(game.posBuffer + posBack).rowStep[w].noteType = 0;
                                game.inputs[w] = 2;
                                posEvaluate = game.posBuffer + posBack;
                                game.soundPool.play(game.soundPullMine, 0.8f, 0.8f, 1, 0, 1f);
                                game.mineHideValue = 255;
                                game.currentLife -= 10;
                            }


                            if (game.inputs[w] == 0) {
                                if (w < Steps.noteSkins[0].explotionTails.length) {
                                    Steps.noteSkins[0].explotionTails[w].stop();
                                }
                            }

                            //bufferSteps.get(posBuffer + posBack)[0] = auxRow;

                        }
                    }
                    if (posEvaluate != -1) {
                        if (!containTaps(game.bufferSteps.get(posEvaluate).rowStep)) {

                            int auxRetro = Math.abs(posBack);

                            if (auxRetro < rGreat) {//perfetc
                                game.ObjectCombo.posjudge = 0;
                                combopp();
                                game.currentLife += 0.5 * game.currentCombo;
                                game.perfect += game.currentCombo;
                            } else if (auxRetro < rGood) {//great
                                game.ObjectCombo.posjudge = 1;
                                combopp();
                                game.great += game.currentCombo;
                            } else if (auxRetro < rBad) {//good
                                game.ObjectCombo.posjudge = 2;
                                game.good += game.currentCombo;
                            } else {//bad
                                game.ObjectCombo.posjudge = 3;
                                game.Combo = 0;
                                game.currentLife -= 0.5;
                                game.bad += game.currentCombo;
                            }
                            // AQUI SE VERA SI ES GREAT O QUE ONDA
                            game.ObjectCombo.show();
                        }
                        posEvaluate = -1;

                    }
                    posBack++;
                }
            }
        }
    }

    private boolean containStartLong(Note[] row) {
        for (Note x : row) {
            if (x.noteType % 10 == 2) {
                return true;
            }
        }
        return false;

    }

    private void combopp() {
        if (game.Combo < 0) {
            game.Combo = 0;
        }
        game.Combo += game.currentCombo;
        if (game.Combo > game.maxCombo) {
            game.maxCombo = game.Combo;
        }
    }

    private void comboLess() {
        game.miss += game.currentCombo;
        game.ObjectCombo.posjudge = 4;
        if (game.Combo > 0) {
            game.Combo = 0;
        } else {
            game.Combo -= game.currentCombo;
        }
        game.ObjectCombo.show();
        game.currentLife -= 1 - game.Combo;
    }


    private void addContadorTick() {
        game.contadorTick++;
        if (game.contadorTick > 48) {
            game.contadorTick = 0;
        }
    }

    private int mil2BackSpaces(float judgeTime) {
        int backs = 0;
        float auxJudge = 0;
        while ((game.posBuffer - backs) >= 0) {
            auxJudge += Common.beat2Second((double) 4 / 192, game.BPM) * 1000;
            backs++;
            if (auxJudge >= judgeTime + 23) {
                break;
            }
        }
        return backs;
    }

    private boolean containTaps(Note... row) {
        for (Note x : row) {
            if (!x.fake && (x.noteType % 10 == 1)) {
                return true;
            }
        }
        return false;
    }


    private boolean containsMine(Note... row) {
        for (Note x : row) {
            if (!x.fake && (x.noteType % 10 == 7)) {
                return true;
            }
        }
        return false;
    }

    private boolean containLongs(Note... row) {
        for (Note x : row) {

            if (!x.fake && (x.noteType > 0) && (x.noteType % 10 == 2 || x.noteType % 10 == 3 || x.noteType % 10 == 4)) {
                return true;
            }
        }
        return false;
    }


    private boolean containTapNote(Note... row) {
        for (Note x : row) {

            if (!x.fake && (x.noteType % 10 == 1 || x.noteType % 10 == 2)) {
                return true;
            }
        }
        return false;
    }


    private boolean containSteps(Note... row) {
        for (Note x : row) {
            if ((x.noteType != 0 && x.noteType != 127)) {
                return true;
            }
        }
        return false;
    }


}
