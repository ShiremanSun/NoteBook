package com.example.droodsunny.memorybook;

/**
 * Created by DroodSunny on 2017/9/24.
 */

public class NumberFormatUtil {
    static char[] numArray = {'零','一','二','三','四','五','六','七','八','九'};
    static String[] units = {"","十","百","千","万","十万","百万","千万","亿","十亿","百亿","千亿","万亿" };
    /**
     * 将整数转换成汉字数字
     * @param num 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len;i++){
            String m=val[i]+"";
            int n=Integer.valueOf(m);
            switch (n){
                case 0:
                    sb.append(numArray[0]);
                    break;
                case 1:
                    sb.append(numArray[1]);
                    break;
                case 2:
                    sb.append(numArray[2]);
                    break;
                case 3:
                    sb.append(numArray[3]);
                    break;
                case 4:
                    sb.append(numArray[4]);
                    break;
                case 5:
                    sb.append(numArray[5]);
                    break;
                case 6:
                    sb.append(numArray[6]);
                    break;
                case 7:
                    sb.append(numArray[7]);
                    break;
                case 8:
                    sb.append(numArray[8]);
                    break;
                case 9:
                    sb.append(numArray[9]);
                    break;
            }
        }
        return sb.toString();
    }
    public static String mdformatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                if(n==1&&(len-i)==2){
                    sb.append(unit);
                }else {
                    sb.append(numArray[n]);
                    sb.append(unit);
                }
            }
        }
        return sb.toString();
    }
}
