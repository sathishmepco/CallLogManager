package com.codificador.calllogmanager;

public class Utils {
    public static String formatSeconds(long timeInSeconds)
    {
        int hours = (int) (timeInSeconds / 3600);
        int secondsLeft = (int) (timeInSeconds - hours * 3600);
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
            /*if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";*/

        if(hours > 0)
            formattedTime += hours+"h ";

        formattedTime += minutes+"m ";
        formattedTime += seconds+"s";

            /*if (minutes < 10)
                formattedTime += "0";
            formattedTime += minutes + ":";

            if (seconds < 10)
                formattedTime += "0";
            formattedTime += seconds ;*/

        return formattedTime;
    }
}
