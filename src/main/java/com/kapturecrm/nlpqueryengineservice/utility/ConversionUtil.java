package com.kapturecrm.nlpqueryengineservice.utility;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ConversionUtil {

    public static String getTimestampForSql(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        return String.valueOf(ts).substring(0, 19);
    }
}
