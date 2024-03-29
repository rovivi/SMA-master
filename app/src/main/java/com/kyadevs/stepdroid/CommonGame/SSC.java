package com.kyadevs.stepdroid.CommonGame;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rodrigo Vidal Villaseñor
 */

public class SSC implements Serializable {


    ArrayList<RowStep> bufferTest = new ArrayList<>();


    int id = -1;//For online and propietaries songs
    public File path;
    public String pathSSC;
    public boolean isPropitarySongs;
    public Map<String, ArrayList<Float[]>> effectMap = new HashMap<>();
    private ArrayList<Float[]> SCROLLS, BPMS, FAKES, TICKCOUNTS, STOPS, DELAYS, SPEEDS, WARPS;
    ArrayList<String[]> ATTACKS = null;
    public Map<String, String> songInfo = new HashMap<>();//datos generales para el screen select music
    public Map[] chartsInfo = new Map[30];//datos de cada chart
    public ArrayList[] charts = new ArrayList[30];//stepps como arraylist de arraylist de arraylist
    private byte wasLong[] = new byte[12];
    public Map<Integer, String> perfAux = new HashMap<>();

    @TargetApi(Build.VERSION_CODES.KITKAT)

    public SSC(String raw, boolean isPropitary) {
        this.ParseSSCLines(StripComments(raw), false);
        this.isPropitarySongs = isPropitary;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SSC(String raw, boolean option, boolean isPropitary) {
        this.isPropitarySongs = isPropitary;
        this.ParseSSCLines(StripComments(raw), option);
    }


    /**
     * Clean comments in the SM string
     *
     * @param data
     * @return String whit out comments
     */
    private static String StripComments(String data) {//Ya quedo uwu
        return data.replaceAll("(\\s+//-([^;]+)\\s)|(//[\\s+]measure\\s[0-9]+\\s)", "");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public void ParseSSCLines(String data, boolean miniVer) {
        for (int hh = 0; hh < 12; hh++) {
            wasLong[hh] = 0;
        }// reset long checker
        Matcher matcher = Pattern.compile("#([^;]+);").matcher(data);// this matcher read  each tag in the sm
        int chart = -1;
        while (matcher.find()) {  //read any tag in the sm string
            String auxString = matcher.group();//Separate tagc
            String key = auxString.substring(1, auxString.indexOf(":"));// separate key and value
            String valor = auxString.substring(auxString.indexOf(":") + 1, auxString.indexOf(";"));
            if (key.equals("NOTEDATA")) {//when you read note data index++
                chart += 1;

            } else {
                if (chart > -1) {//es cuando ya se entró al chart
                    switch (key) {
                        case "NOTES":
                            if (!miniVer) {
                                if (valor.contains("&")) {
                                    String[] steps = valor.split("&");
                                    this.charts[chart] = this.step2Array(steps[0]);
                                    this.perfAux.put(chart, steps[1]);
                                } else {
                                    this.charts[chart] = this.step2Array(valor);
                                }
                            }
                            break;
                        case "BPMS":
                        case "STOPS":
                        case "DELAYS":
                        case "WARPS":
                        case "TIMESIGNATURES":
                        case "TICKCOUNTS":
                        case "COMBOS":
                        case "SPEEDS":
                        case "LABELS":
                        case "SCROLLS":
                            if (miniVer) {
                                break;
                            }
                        case "STEPSTYPE":
                        default:
                            if (this.chartsInfo[chart] == null) {
                                Map<String, String> currentChartInfo = new HashMap<>();
                                this.chartsInfo[chart] = currentChartInfo;
                            }
                            this.chartsInfo[chart].put(key, valor);
                    }
                } else {
                    this.songInfo.put(key, valor);
                }
            }
        }
    }

    /**
     * Converted string into a steps array,
     *
     * @param data String whit steps
     * @return array whit steps structure
     */
    private ArrayList step2Array(String data) {


        //code for


        ArrayList<ArrayList<String>> steps = new ArrayList<>(); // initialize the step array
        ArrayList<String> auxBlock = new ArrayList<>(); //initialize the aux block (used to set new block of steps)
        String[] arrayString = data.split("\n");
        for (int x = 1; x < arrayString.length; x++) {
            if (arrayString[x].contains(",")) {
                steps.add(auxBlock);
                auxBlock = new ArrayList<>();
                if (arrayString[x].contains("0") || arrayString[x].contains("1") || arrayString[x].contains("2") || arrayString[x].contains("3")) {
                    auxBlock.add(arrayString[x].replace(",", ""));
                }

            } else {
                auxBlock.add(arrayString[x]);
            }
        }
        steps.add(auxBlock);
        return steps;
    }


    /**
     * Create the "thing" will be used by the player, contains steps, scroll size , and current beat info
     *
     * @param nchar Contains the index to be used in the player
     * @return the "thing"
     */


    public ArrayList<RowStep> createBuffer(int nchar) {

        String rowEmpaty = "";

        switch (chartsInfo[nchar].get("STEPSTYPE").toString()) {
            case "pump-double":
            case "pump-routine":
                rowEmpaty = "0000000000";
                break;
            case "pump-single":
                rowEmpaty = "00000";
                break;
            case "pump-halfdouble":
                rowEmpaty = "00000";
                break;
        }
        if (this.chartsInfo[nchar].get("SCROLLS") != null && !this.chartsInfo[nchar].get("SCROLLS").equals("")) {
            SCROLLS = this.arrayListTag(this.chartsInfo[nchar].get("SCROLLS").toString());
        }//DO not remove is for scrolling

        if (!Common.testingRadars) {
            TICKCOUNTS = setMetadata(chartsInfo[nchar].get("TICKCOUNTS"), songInfo.get("TICKCOUNTS"));
            //WARPS = setMetadata(chartsInfo[nchar].get("WARPS"), songInfo.get("WARPS"));
            STOPS = setMetadata(chartsInfo[nchar].get("STOPS"), songInfo.get("STOPS"));
            DELAYS = setMetadata(chartsInfo[nchar].get("DELAYS"), songInfo.get("DELAYS"));
        }


        //BPMS = setMetadata(chartsInfo[nchar].get("BPMS"), songInfo.get("BPMS"));


        ArrayList<Float[]> auxBPM2WARP = new ArrayList<>();
        //  effectMap.put("BPMS", BPMS);
        if (BPMS != null) {
            for (int x = 0; x < BPMS.size(); x++) {
                if (BPMS.get(x)[1] > 1000) {
                    Float[] auxF = BPMS.get(x).clone();
                    float warpValue = BPMS.get(1)[0] - BPMS.get(0)[0];
                    warpValue *= 0.98;
                    auxF[1] = warpValue;
                    auxBPM2WARP.add(auxF);
                    BPMS.remove(x);
                    x--;
                }
            }


            effectMap.put("BPMS", BPMS);
        }


        if (STOPS != null) {
            effectMap.put("STOPS", STOPS);
        }
        if (DELAYS != null) {
            effectMap.put("DELAYS", DELAYS);
        }

        if (WARPS != null) {
            effectMap.put("WARPS", WARPS);

        } else {
        }


        if (TICKCOUNTS != null) {
            effectMap.put("TICKCOUNTS", TICKCOUNTS);
        }


        double currentBeat = 0;//offset/(60/BPM);
        ArrayList<RowStep> buffer = new ArrayList<>();
        ArrayList<ArrayList<String>> aux = this.charts[nchar];
        int numberBlock = 0;
        for (int i = 0; i < aux.size(); i++) {
            for (int j = 0; j < 192; j++) {
                RowStep row = new RowStep();
                if (aux.get(i).size() == 192) {
                    row.rowStep = stringStep2NoteArary(aux.get(i).get(j));
                } else {
                    int div = 192 / aux.get(i).size();
                    if (j % div == 0) {
                        if (i >= aux.size() || (j / div) >= aux.get(i).size()) {
                            String x = "sdsdsd";
                        }

                        if ((j / div) < aux.get(i).size()) {

                            row.rowStep = stringStep2NoteArary(aux.get(i).get(j / div));

                        }

                    } else {
                        row.rowStep = stringStep2NoteArary(rowEmpaty);
                    }
                }


                row.beat = currentBeat;

                row.scroll = foundScroll(currentBeat, SCROLLS);
                row.effect = foundEffectOnBeat(currentBeat);
                buffer.add(row);
                if (row.effect != null) {
                    bufferTest.add(row);
                }
                currentBeat = (double) numberBlock++ / 48;
            }
        }


        if (perfAux.get(nchar) != null) {


            ArrayList<RowStep> buffer2 = new ArrayList<>();
            ArrayList<ArrayList<String>> aux2 = this.step2Array(perfAux.get(nchar));
            for (int i = 0; i < aux2.size(); i++) {
                for (int j = 0; j < 192; j++) {
                    RowStep row = new RowStep();
                    if (aux2.get(i).size() == 192) {
                        row.rowStep = stringStep2NoteArary(aux2.get(i).get(j));
                    } else {
                        int div = 192 / aux2.get(i).size();
                        if (j % div == 0) {
                            if (i >= aux2.size() || (j / div) >= aux2.get(i).size()) {
                                String x = "sdsdsd";
                            }
                            if ((j / div) < aux2.get(i).size()) {
                                row.rowStep = stringStep2NoteArary(aux2.get(i).get(j / div));
                            }
                        } else {
                            row.rowStep = stringStep2NoteArary(rowEmpaty);
                        }
                    }
                    buffer2.add(row);
                    if (row.effect != null) {
                        bufferTest.add(row);
                    }
                }
            }


            for (int j = 0; j < buffer.size(); j++) {// se suma el valor de perf para cada nota
                if (buffer.get(j) != null || buffer.get(j).rowStep != null) {
                    for (int i = 0; (i < buffer.get(j).rowStep.length); i++) {
                        if (buffer.get(j).rowStep[i].getNoteType() != 0) {

                            buffer.get(j).rowStep[i].setNoteType((byte) (buffer.get(j).rowStep[i].getNoteType()+50));
                        }
                    }
                }
            }

            for (int j = 0; j < buffer.size(); j++) {
                if (buffer2.get(j) != null || buffer2.get(j).rowStep != null) {
                    for (int i = 0; (i < buffer2.get(j).rowStep.length); i++) {
                        if (buffer2.get(j).rowStep[i].getNoteType() != 0) {
                            buffer.get(j).rowStep[i] = buffer2.get(j).rowStep[i];
                            buffer.get(j).rowStep[i].setNoteType((byte) (buffer.get(j).rowStep[i].getNoteType()+60));

                        }
                    }
                }
            }
        }







        /*
        for (int i = 0; i < buffer.size() && SCROLLS != null; i++) {
            buffer.get(i)[2] = foundScroll((Double) buffer.get(i)[1], SCROLLS);
        }


        BPMS = setMetadata(chartsInfo[nchar].get("BPMS"), songInfo.get("BPMS"));
        FAKES = setMetadata(chartsInfo[nchar].get("FAKES"), songInfo.get("FAKES"));
        TICKCOUNTS = setMetadata(chartsInfo[nchar].get("TICKCOUNTS"), songInfo.get("TICKCOUNTS"));
        STOPS = setMetadata(chartsInfo[nchar].get("STOPS"), songInfo.get("STOPS"));
        DELAYS = setMetadata(chartsInfo[nchar].get("DELAYS"), songInfo.get("DELAYS"));
        SPEEDS = setMetadata(chartsInfo[nchar].get("SPEEDS"), songInfo.get("SPEEDS"));
        WARPS = setMetadata(chartsInfo[nchar].get("WARPS"), songInfo.get("WARPS"));
*/


        return buffer;
    }


    private EffectStep[] foundEffectOnBeat(double beat) {
        Iterator<Map.Entry<String, ArrayList<Float[]>>> it = effectMap.entrySet().iterator();
        ArrayList<EffectStep> auxArray = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<Float[]>> pair = it.next();
            String key = pair.getKey();//Nombre del doMagic
            ArrayList<Float[]> value = pair.getValue();// se declara la lista de efectos


            if (value.size() > 0 && beat == value.get(0)[0]) {
                if (key == "BPMS") {//se confirma para transformar bpm alto en warp
                    if (value.get(0)[1] > 999) {//found bpm warp
                        float warpValue = value.get(1)[0] - value.get(0)[0];
                        warpValue *= 0.98;
                        Float[] auxFloatArray = value.get(0).clone();
                        auxFloatArray[1] = warpValue;
                        auxArray.add(new EffectStep("WARPS", auxFloatArray));
                    } else {
                        auxArray.add(new EffectStep(key, value.get(0)));
                    }

                } else {
                    auxArray.add(new EffectStep(key, value.get(0)));
                }
                value.remove(0);
            }

        }


        EffectStep[] effi = new EffectStep[auxArray.size()];
        for (int z = 0; z < auxArray.size(); z++) {
            effi[z] = auxArray.get(z);
        }
        return (auxArray.size() == 0) ? null : effi;
    }


    /*private String checkLong(String charts) {
        if (charts.contains("{")) {
            charts = charts.replace(" ", "").
                    replace("{3|v|0|0}", "E").
                    replace("{2|v|0|0}", "S").
                    replace("{1|v|0|0}", "V").
                    replace("{3|v|1|0}", "E").
                    replace("{2|v|1|0}", "S").
                    replace("{1|v|1|0}", "V").
                    replace("{3|h|0|0}", "e").
                    replace("{2|h|0|0}", "s").
                    replace("{1|h|0|0}", "h").
                    replace("{3|h|1|0}", "e").
                    replace("{2|h|1|0}", "s").
                    replace("{1|h|1|0}", "h").
                    replace("{3|n|1|0}", "f").
                    replace("{2|n|1|0}", "f").
                    replace("{x}", "f").
                    replace("{1|n|1|0}", "f")


            ;
        }
        if (charts.length() > 10) {
            charts = charts.substring(0, 10);
        }


        for (int x = 0; x < charts.length(); x++) {
            switch (charts.charAt(x)) {
                case '3':
                case '1':
                case 'E':
                case 'e':
                case 'v':
                    wasLong[x] = 0;
                    break;
                case '2':
                    wasLong[x] = 1;
                    break;
                case 'x':
                    wasLong[x] = 2;
                    break;
                case 'y':
                    wasLong[x] = 3;
                    break;
                case 'z':
                    wasLong[x] = 4;
                    break;
                case 'S':
                    wasLong[x] = 5;
                    break;
                case 's':
                    wasLong[x] = 6;
                    break;
                case '0':
                    switch (wasLong[x]) {
                        case 1:
                            charts = changeCharInPosition(x, 'L', charts);
                            break;
                        case 2:
                            charts = changeCharInPosition(x, 'O', charts);
                            break;
                        case 3:
                            charts = changeCharInPosition(x, 'N', charts);
                            break;
                        case 4:
                            charts = changeCharInPosition(x, 'G', charts);
                            break;
                        case 5:
                            charts = changeCharInPosition(x, 'l', charts);
                            break;
                        case 6:
                            charts = changeCharInPosition(x, 'o', charts);
                            break;
                    }
                    break;
            }

        }

        return charts;
    }*/

    public String changeCharInPosition(int position, char ch, String str) {
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }


    public ArrayList<Float[]> arrayListTag(String data) {
        ArrayList<Float[]> buffer = new ArrayList<>();
        String[] array = data.replace("\n", "").split(",");
        for (String x : array) {
            Float auxString[] = new Float[2];
            auxString[0] = Float.parseFloat(x.substring(0, x.indexOf("=")));
            auxString[1] = Float.parseFloat(x.substring(x.indexOf("=") + 1, x.length() - 1));
            buffer.add(auxString);
        }

        return buffer;
    }

    /***
     * Read list of effects (BPM,SPEED,SCROLLSIZE)
     * @param data
     * @return
     */
    public ArrayList<Float[]> arrayListSpeed(String data) {
        ArrayList<Float[]> buffer = null;
        if (!data.equals("")) {
            buffer = new ArrayList<>();
            String[] array = data.replace("\n", "").split(",");
            for (String x : array) {
                Float auxString[] = new Float[6];
                String[] array24 = x.split("=");
                for (int j = 0; j < array24.length; j++) {
                    auxString[j] = Float.parseFloat(array24[j]);
                }
                buffer.add(auxString);
            }
        }
        return buffer;
    }


    public ArrayList<String[]> listAttack(String data) {
        ArrayList<String[]> buffer = new ArrayList<>();
        String[] array = data.replace("\n", "").split(":");
        int x = 0;
        /*
        while( x < array.length-1) {
            String auxString[] = new String[3];

            auxString[0]=array[x++];
            auxString[1]=array[x++];
            auxString[2]=array[x++];

            buffer.add(auxString);
        }*/

        return buffer;
    }


    private float foundScroll(double Beat, ArrayList<Float[]> SCROLLS) {
        float f = 1f;
        if (SCROLLS != null) {
            for (int x = 0; x < SCROLLS.size() && SCROLLS.get(x)[0] <= Beat; x++) {
                f = SCROLLS.get(x)[1];
            }
        } else {

            return 1f;
        }

        return f;
    }

    public Note[] stringStep2NoteArary(String row) {
      /*  0 null char
            1 normal step
            2 start long
            3 end long
            4 body long
            5 fake
            6 hidden
            7 mine
            8 poisson
            9 pressed long

            +40 fit
            51 --PERFORMANCE TAP
            52 --PERFORMANCE START LONG
            53 --PERFORMANCE END LONG
            54 --PERFORMANCE BODY
            THE SAME P2+10 +P3+20



            127 presed

        */

        Matcher matcher = Pattern.compile("\\{([^\\}]+)\\}").matcher(row);
        StringBuffer bufStr = new StringBuffer();

        while (matcher.find()) {
            String s1 = matcher.group();
            int charType;
            try {
                charType = Integer.parseInt(s1.charAt(1) + "");
            } catch (Exception e) {
                e.printStackTrace();
                charType = letterToByte(s1.charAt(1), 0);
            }
            int suma = 0;
            switch (s1.charAt(3)) {
                case 'v':
                case 'V':
                    suma = 210;
                    break;
                case 'H':
                case 'h':
                    suma = 220;
                    break;
                case 'S':
                case 's':
                    suma = 230;
                    break;
                case 'n':
                    suma = 0;
                    break;

            }
            suma += charType;
            if (s1.charAt(5) == '1') {
                suma = suma + 1000;
            }
            matcher.appendReplacement(bufStr, (char) suma + "");

        }
        matcher.appendTail(bufStr);
        row = bufStr.toString().replace(" ", "");
        Note[] data = new Note[row.length()];

        for (int x = 0; x < data.length; x++) {
            data[x] = new Note();
            char aux = row.charAt(x);
            if (aux < 200) {
                switch (aux) {
                    case '0':
                        if (wasLong[x] != 0) {
                            data[x].setNoteType( wasLong[x]);
                        }
                        break;
                    case '2':
                        data[x].setNoteType(letterToByte(aux, x));
                        wasLong[x] = 4;
                        break;
                    case '3':
                        data[x].setNoteType( letterToByte(aux, x));
                        if (wasLong[x] != 0) {
                            data[x].setNoteType((byte) (wasLong[x] - 1));
                        }
                        wasLong[x] = 0;
                        break;
                    default:
                        data[x].setNoteType( letterToByte(aux, x));


                }

            } else {


                data[x].setNoteType((byte) (aux - 200)) ;

                if (aux > 1000) {// la nota se considera fake
                    data[x].setFake( true);
                    data[x].setNoteType( (byte) (aux - 1200));
                }

                if ((data[x].getNoteType() - 3) % 10 == 0) {
                    data[x].setNoteType( (byte) (wasLong[x] - 1));

                    wasLong[x] = 0;
                } else if ((data[x].getNoteType() - 2) % 10 == 0) {
                    wasLong[x] = (byte) (data[x].getNoteType() + 2);
                }

            }


        }
        return data;


    }

    public static byte[] stringStep2ByteArary2Old(String row) {
      /*  0 null char
            1 normal step
            2 start long
            3 end long
            4 body long
            5 fake
            6 hidden
            7 mine
            8 poisson
            +10 vanish
            +20hidden
            +30 sundden
            +40 fit
            51 --PERFORMANCE TAP
            52 --PERFORMANCE START LONG
            53 --PERFORMANCE END LONG
            54 --PERFORMANCE BODY
            THE SAME P2+10 +P3+20



            127 presed

        */
        byte[] data = new byte[row.length()];
        for (int x = 0; x < data.length; x++) {
            char aux = row.charAt(x);
            switch (aux) {
                case '1':
                    data[x] = 1;
                    break;
                case '2':
                    data[x] = 2;
                    break;
                case '3':
                    data[x] = 3;
                    break;
                case 'L':
                    data[x] = 4;
                    break;
                case 'M':
                    data[x] = 7;
                    break;
                case 'F':
                case 'f':
                    data[x] = 5;
                    break;
                case 'V':
                    data[x] = 11;
                    break;
                case 'h':
                    data[x] = 21;
                    break;
                case 'O':
                    data[x] = 54;
                    break;
                case 'N':
                    data[x] = 64;
                    break;
                case 'G':
                    data[x] = 74;
                    break;
                case 'l':
                    data[x] = 24;
                    break;
                case 'o':
                    data[x] = 34;
                    break;
                case 'x':
                    data[x] = 52;
                    break;
                case 'X':
                    data[x] = 51;
                    break;
                case 'y':
                    data[x] = 62;
                    break;
                case 'Y':
                    data[x] = 61;
                    break;
                case 'z':
                    data[x] = 72;
                    break;
                case 'Z':
                    data[x] = 71;
                    break;
                case 'P':
                    data[x] = 127;
                    break;
                default:
                    data[x] = 0;
                    break;

            }

        }

        return data;


    }


    private ArrayList setMetadata(Object data, String s) {
        if (data != null && !data.equals("")) {
            return arrayListSpeed(data.toString());
        } else if (s != null && !s.equals("")) {
            return setMetadata(s, null);
        } else {
            return null;
        }
    }


    private byte letterToByte(char letter, int pos) {
        byte x = 0;
        switch (letter) {
            case '1':
                x = 1;
                break;
            case '2':
                x = 2;
                break;
            case '3':
                if (wasLong[pos] != 0) {

                    x = (byte) (wasLong[pos] - 1);
                    wasLong[pos] = 0;

                }


            case '6':
                x = 2;
                break;
            case 'L':
                x = 4;

                break;
            case 'M':
                x = 7;
                break;
            case 'F':
            case 'f':
                x = 5;
                break;
            case 'V':
                x = 11;
                break;
            case 'h':
                x = 21;
                break;
            case 'O':
                x = 54;

                break;
            case 'N':
                x = 64;
                break;
            case 'G':
                x = 74;
                break;
            case 'l':
                x = 24;
                break;
            case 'o':
                x = 34;
                break;
            case 'x':
                x = 52;
                break;
            case 'X':
                x = 51;
                break;
            case 'y':
                x = 62;
                break;
            case 'Y':
                x = 61;
                break;
            case 'z':
                x = 72;
                break;
            case 'Z':
                x = 71;
                break;
            case 'P':
                x = 127;
                break;
            default:
                x = 0;
                if (wasLong[pos] != 0) {
                    x = wasLong[pos];
                }
                break;
        }
        if (x % 100 % 10 == 3) {
            if (wasLong[pos] != 0) {

                x = (byte) (wasLong[pos] - 1);
                wasLong[pos] = 0;

            }

        } else if (x % 100 % 10 == 2) {
            wasLong[pos] = (byte) (x + 2);
        }

        if (x == 3) {
            String uwu = "l";

        }

        return x;
    }


}
