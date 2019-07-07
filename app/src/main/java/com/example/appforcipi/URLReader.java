package com.example.appforcipi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class URLReader  {
    public static void main(String[] args) throws Exception {



       /*try {
            URL oracle = new URL("http://134.0.113.128/index.php?r=api%2Fproviders");
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

            String inputLine;
            String out = "";
            while ((inputLine = in.readLine()) != null){
                out = out + inputLine;
                //System.out.println(inputLine);
            } //Можно   накапливать в StringBuilder а потом присвоить перемной String результат накопления


            String inStr = "{\"providers\":[{\"id\":\"2\",\"provider_name\":\"Intercom\"},{\"id\":\"4\",\"provider_name\":\"Telecomm Technology\"},{\"id\":\"5\",\"provider_name\":\"Eastera\"}],\"sites\":[{\"id\":\"1\",\"site\":\"https://www.facebook.com\",\"alias\":\"https://www.facebook.com\"},{\"id\":\"2\",\"site\":\"https://www.instagram.com\",\"alias\":\"https://www.instagram.com\"},{\"id\":\"3\",\"site\":\"https://www.google.com\",\"alias\":\"https://www.google.com\"},{\"id\":\"4\",\"site\":\"https://www.youtube.com\",\"alias\":\"https://www.youtube.com\"},{\"id\":\"5\",\"site\":\"https://news.tj\",\"alias\":\"https://www.news.tj\"},{\"id\":\"6\",\"site\":\"https://akhbor.com\",\"alias\":\"https://www.akhbor.com\"},{\"id\":\"7\",\"site\":\"https://www.drive.google.com\",\"alias\":\"https://www.drive.google.com\"},{\"id\":\"8\",\"site\":\"https://www.translate.google.com\",\"alias\":\"https://www.translate.google.com\"},{\"id\":\"9\",\"site\":\"https://www.ozodi.org\",\"alias\":\"https://www.ozodi.org\"},{\"id\":\"10\",\"site\":\"https://ok.ru\",\"alias\":\"https://ok.ru\"},{\"id\":\"11\",\"site\":\"https://vk.com\",\"alias\":\"https://vk.com\"},{\"id\":\"12\",\"site\":\"https://yandex.ru/\",\"alias\":\"https://yandex.ru/\"},{\"id\":\"13\",\"site\":\"https://www.maps.google.com/\",\"alias\":\"https://www.maps.google.com/\"},{\"id\":\"14\",\"site\":\"https://www.twitter.com/\",\"alias\":\"https://www.twitter.com/\"},{\"id\":\"15\",\"site\":\"https://zen.yandex.ru/newstj\",\"alias\":\"https://zen.yandex.ru/newstj\"},{\"id\":\"16\",\"site\":\"https://www.play.google.com/\",\"alias\":\"https://www.play.google.com/\"},{\"id\":\"17\",\"site\":\"https://eurasianet.org/\",\"alias\":\"https://eurasianet.org/\"},{\"id\":\"18\",\"site\":\"https://telegra.ph/\",\"alias\":\"https://telegra.ph/\"},{\"id\":\"19\",\"site\":\"https://zen.yandex.ru\",\"alias\":\"https://zen.yandex.ru\"}]}";

            String[] inArray = out.split(",\"sites\"");
            String inJsonProviders =  inArray[0].substring(inArray[0].indexOf(":")+1);
            String inJsonSites =  inArray[1].substring(inArray[1].indexOf(":")+1,inArray[1].length()-1);

            Gson gson = new Gson();
            //Providers
            Type type1 = new TypeToken<ArrayList<ProvidersBean>>(){}.getType();
            ArrayList<ProvidersBean> readProviders = gson.fromJson(inJsonProviders,type1);
            //Sites
            Type type2 = new TypeToken<ArrayList<SitesBean>>(){}.getType();
            ArrayList<SitesBean> readSites = gson.fromJson(inJsonSites,type2);


            in.close();

        }catch (MalformedURLException ex) {
            ex.printStackTrace();
        }*/


       for( int i =0; i < 10; i++){
           Calendar dating = Calendar.getInstance();
           SimpleDateFormat formating = new SimpleDateFormat("dd:MM:YYYY:HH:mm");
           Date date = formating.parse(formating.format(dating.getTime()));
           long time = date.getTime();
           System.out.println( formating.format(dating.getTime()) );
           System.out.println( time );


           /*Date dte = new Date();
           long milliSeconds = dte.getTime();
           String strLong = Long.toString(milliSeconds);
           System.out.println(milliSeconds);

           Date date = new Date(milliSeconds);
           System.out.println(date.toString());*/
           Thread.sleep(5000);
       }





        String out = "{\"username\':\'root\',\"password\":\"pass\"}";
        byte[] outt = out.getBytes();

        for (int i = 0; i < outt.length; i++){
          //  System.out.println(outt[i]);
        }

    }
}
