package net.catsbilisim.canliyayin.Api.InstagramApi.Class;

import java.util.UUID;

public class InstagramGenericUtil {
    public static String generateUuid(boolean dash) {
        String uuid = UUID.randomUUID().toString();

        if (dash) {
            return uuid;
        }
        return uuid.replaceAll("-", "");
    }
}
