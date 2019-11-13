package com.ceshi.helloworld.bean;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.Serializable;

public class ReturnXMLParser implements Serializable {

    public static String parseGetAuthInfoXML(InputStream is) throws  Exception{
        String result=null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is,"UTF-8");

        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("authinfo")) {
                        eventType = parser.next();
                        result=parser.getText();
                        return result;

                    }

                    /*if (parser.getName().equals("return_code")){
                        eventType = parser.next();
                        if (null!=parser.getText()&&parser.getText().equals("SUCCESS")) {
                            eventType = parser.next();
                            if (parser.getName().equals("authinfo")) {
                                eventType = parser.next();
                                result = parser.getText();
                                break;
                            }
                        }
                        eventType = parser.next();
                    }

                    if (parser.getName().equals("return_msg")){

                        eventType = parser.next();
                        result = parser.getText();
                    }*/

            }
            eventType = parser.next();
        }

        return result;
    }
}
