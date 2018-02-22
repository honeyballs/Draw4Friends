package de.thm.draw4friends.Login;

import java.util.UUID;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

public class TokenGenerator {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
