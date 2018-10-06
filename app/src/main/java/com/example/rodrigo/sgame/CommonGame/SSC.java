package com.example.rodrigo.sgame.CommonGame;
/**
 * @author Rodrigo Vidal Villaseñor
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SSC {


    int id = -1;//For online and propietaries songs
    public File path;
    public String pathSSC;
    public boolean isPropitarySongs ;
    private ArrayList<Float[]> SCROLLS, BPMS, FAKES, TICKCOUNTS, STOPS, DELAYS, SPEEDS, WARPS;
    ArrayList<String[]> ATTACKS = null;
    public Map<String, String> songInfo = new HashMap<>();//datos generales para el screen select music
    public Map[] chartsInfo = new Map[30];//datos de cada chart
    public ArrayList[] charts = new ArrayList[30];//stepps como arraylist de arraylist de arraylist
    private short wasLong[] = new short[40];

    @TargetApi(Build.VERSION_CODES.KITKAT)


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SSC(String raw,boolean isPropitary) {
        this.ParseSSCLines(StripComments(raw), false);
        this.isPropitarySongs=isPropitary;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SSC(String raw, boolean option,boolean isPropitary) {
        this.isPropitarySongs=isPropitary;
        this.ParseSSCLines(StripComments(raw), option);


    }

    private static String StripComments(String data) {//Ya quedo uwu
        return data.replaceAll("(\\s+//-([^;]+)\\s)|(//[\\s+]measure\\s[0-9]+\\s)", "");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public void ParseSSCLines(String data, boolean miniVer) {
        for (int hh = 0; hh < 12; hh++) {
            wasLong[hh] = 0;
        }
        Matcher m = Pattern.compile("#([^;]+);").matcher(data);
        int chart = -1;
        while (m.find()) {
            String auxstring = m.group();
            String key = auxstring.substring(1, auxstring.indexOf(":"));
            String valor = auxstring.substring(auxstring.indexOf(":") + 1, auxstring.indexOf(";"));
            if (key.equals("NOTEDATA")) {
                chart += 1;

            } else {
                if (chart > -1) {//es cuando ya se entró al chart
                    switch (key) {
                        case "NOTES":
                            if (!miniVer) {
                                valor = valor.replaceAll(" ", "").replaceAll("&",",");
                                this.charts[chart] = this.step2Array(valor);
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


    private ArrayList step2Array(String data) {
        ArrayList<ArrayList<String>> steps = new ArrayList<>();
        ArrayList<String> auxBlock = new ArrayList<>();
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


   /* private ArrayList step2Array2(String data) {
        ArrayList<ArrayList<String>> steps = new ArrayList<>();
        ArrayList<String> auxBlock = new ArrayList<>();
        String[] arrayString = data.split("\n");
        for (String x : arrayString) {
            if (x.contains(",")) {
                steps.add(auxBlock);
                auxBlock = new ArrayList<>();
                if (x.contains("0") || x.contains("1") || x.contains("2") || x.contains("3")) {
                    auxBlock.add(x.replace(",", ""));
                }
            } else {
                auxBlock.add(x);
            }
        }
        //steps.add(auxBlock);
        return steps;
    }*/


  /*  public ArrayList createBuffer(int nchar) {
        if (this.chartsInfo[nchar].get("SCROLLS") != null && !this.chartsInfo[nchar].get("SCROLLS").equals("")) {
            SCROLLS = this.arrayListTag(this.chartsInfo[nchar].get("SCROLLS").toString());
        }
        double currentBeat = 0;//offset/(60/BPM);
        ArrayList<String[]> buffer = new ArrayList<>();
        ArrayList<ArrayList<String>> aux = this.charts[nchar];
        int numberBlock = 0;
        for (int i = 0; i < aux.size(); i++) {
            for (int j = 0; j < 192; j++) {
                String auxString[] = new String[4];
                if (aux.get(i).size() == 192) {
                    auxString[0] = checkLong(aux.get(i).get(j));
                } else {
                    int div = 192 / aux.get(i).size();
                    if (j % div == 0) {
                        auxString[0] = checkLong(aux.get(i).get(j / div));
                    } else {
                        auxString[0] = checkLong("0000000000");
                    }
                }
                auxString[1] = 192 + "";
                auxString[2] = currentBeat + "";
                auxString[3] = "1";
                buffer.add(auxString);
                currentBeat = (double) numberBlock / 48;
                numberBlock++;
                //currentBeat += (double) 4 / 192;
            }
        }
        for (int i = 0; i < buffer.size() && SCROLLS != null; i++) {
            buffer.get(i)[3] = foundScroll(Float.parseFloat(buffer.get(i)[2]), SCROLLS) + "";
        }

        return buffer;
    }*/


    public ArrayList<Object[]> createBuffer2(int nchar) {
        if (this.chartsInfo[nchar].get("SCROLLS") != null && !this.chartsInfo[nchar].get("SCROLLS").equals("")) {
            SCROLLS = this.arrayListTag(this.chartsInfo[nchar].get("SCROLLS").toString());
        }//DO not remove is for scrolling

        double currentBeat = 0;//offset/(60/BPM);
        ArrayList<Object[]> buffer = new ArrayList<>();
        ArrayList<ArrayList<String>> aux = this.charts[nchar];
        int numberBlock = 0;


        for (int i = 0; i < aux.size(); i++) {
            for (int j = 0; j < 192; j++) {
                Object auxObject[] = new Object[4];
                if (aux.get(i).size() == 192) {
                    auxObject[0] = stringStep2ByteArary(checkLong(aux.get(i).get(j)));
                } else {
                    int div = 192 / aux.get(i).size();
                    if (j % div == 0) {
                        if (i>= aux.size() || (j/div )>=aux.get(i).size()){
                            String x="sdsdsd";
                        }

                        auxObject[0] = stringStep2ByteArary(checkLong(aux.get(i).get(j / div)));
                    } else {
                        auxObject[0] = stringStep2ByteArary(checkLong("0000000000"));
                    }
                }

                auxObject[1] = currentBeat;
                auxObject[2] = 1f;
                buffer.add(auxObject);
                currentBeat = (double) numberBlock / 48;
                numberBlock++;
                //currentBeat += (double) 4 / 192;
            }
        }
        for (int i = 0; i < buffer.size() && SCROLLS != null; i++) {
            buffer.get(i)[2] = foundScroll((Double) buffer.get(i)[1], SCROLLS);
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

    private String checkLong(String charts) {
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
    }

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


    public ArrayList<Float[]> arrayListSpeed(String data) {
        ArrayList<Float[]> buffer=null;

        if (!   data.equals("")) {
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
        if (SCROLLS != null || SCROLLS.size() == 1) {
            for (int x = 0; x < SCROLLS.size() && SCROLLS.get(x)[0] <= Beat; x++) {
                f = SCROLLS.get(x)[1];
            }
        } else {

            return 1f;
        }

        return f;
    }

    public static byte[] stringStep2ByteArary(String stepData) {
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
        byte[] data = new byte[stepData.length()];
        for (int x = 0; x < data.length; x++) {
            char aux = stepData.charAt(x);
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


}
