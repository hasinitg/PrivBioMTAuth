package lib.zkp4.identity.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hasini on 4/6/16.
 */
public class Utilz {
    /*format for parsing the timestamp*/
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd hh:mm:ss.SSS";

    public static Timestamp reformatTimestamp(Timestamp ts) throws ParseException {
        String timestampInStr = ts.toString();
        SimpleDateFormat df = new SimpleDateFormat(TIME_STAMP_FORMAT);
        Date parsedDate = df.parse(timestampInStr);
        return new Timestamp(parsedDate.getTime());
    }

    public static String getAttributeFromJSONMessage(JSONObject jsonObject, String attributeName) throws JSONException {
        if (!jsonObject.isNull(attributeName)) {
            return (String) jsonObject.get(attributeName);
        } else {
            return null;
        }
    }
}
