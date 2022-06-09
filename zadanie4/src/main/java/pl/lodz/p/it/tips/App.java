package pl.lodz.p.it.tips;

public class App {

    public static void main(String[] args) {
        Recorder recorder = new Recorder();
        System.out.println("Nacisnij aby rozpoczac nagrywanie");
        recorder.click();
        recorder.captureAudio();

        System.out.println("Nacisnij aby zakonczyc nagrywanie");
        recorder.click();
        recorder.stopAudio();
        System.out.println("Nagrywanie zakonczone, nacisnij aby odtworzyc nagrywanie");
        recorder.click();

        recorder.playRecord();
    }
}
