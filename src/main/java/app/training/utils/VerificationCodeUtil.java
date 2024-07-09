package app.training.utils;

import java.util.Random;

public class VerificationCodeUtil {
    private static final String CODE
            = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CODE.length());
            sb.append(CODE.charAt(index));
        }
        return sb.toString();
    }
}
