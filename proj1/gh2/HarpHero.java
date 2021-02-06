package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class HarpHero {
    private static final double CONCERT_A = 440.0;

    public static void main(String[] args) {
        /* create guitar strings*/
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        HarpString[] string37keys = new HarpString[keyboard.length()];
        for (int i = 0; i < keyboard.length(); i++) {
            string37keys[i] =  new HarpString(CONCERT_A * Math.pow(2, (i - 24.0) / 12.0));
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyboard.contains(Character.toString(key))) {
                    string37keys[keyboard.indexOf(key)].pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (HarpString stringTone:string37keys) {
                sample += stringTone.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (HarpString stringTone:string37keys) {
                stringTone.tic();
            }
        }
    }
}
