package moremounts.utils;

import necesse.gfx.GameResources;

public class NightVision {

    public static void turnOnNightVision() {

        if(GameResources.debugColorShader != null) {

            GameResources.debugColorShader.use();
            GameResources.debugColorShader.pass1f("green", 1.6f);
            GameResources.debugColorShader.pass1f("contrast", 0.6f);
            GameResources.debugColorShader.stop();
        }
    }

    public static void turnOffNightVision() {

        if(GameResources.debugColorShader != null) {

            GameResources.debugColorShader.use();
            GameResources.debugColorShader.pass1f("green", 1);
            GameResources.debugColorShader.pass1f("contrast", 1);
            GameResources.debugColorShader.stop();
        }
    }
}
