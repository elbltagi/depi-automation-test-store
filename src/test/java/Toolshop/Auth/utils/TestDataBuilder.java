package Toolshop.Auth.utils;
import java.util.UUID;
public class TestDataBuilder {
    public static String generateUniqueEmail() {
        String randomPart = UUID.randomUUID().toString().substring(0, 8);
        return "mostafa_" + randomPart + "@gmail.com";
    }
}
