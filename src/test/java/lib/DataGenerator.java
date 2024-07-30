package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return String.format("learnqa%s@example.com",timestamp);
    }
    public static Map<String,String> getRegistrationData() {
        Map<String,String> data = new HashMap<>();
        data.put("email",getRandomEmail());
        data.put("password","123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");

        return data;
    }
    public static Map<String,String> getRegistrationData(Map<String,String> userData) {
        Map<String,String> data = getRegistrationData();
        String[] keys = new String[] {"email","username","firstName","lastName","password"};
        for (String key:keys) {
            if (userData.containsKey(key)) {
                data.put(key,userData.get(key));
            }
        }
        return data;
    }

}
