package brad.tw.mymediatest;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SoundPool sp;
    private int sound1,sound2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        sound1 = sp.load(this, R.raw.cars004, 1);
        sound2 = sp.load(this, R.raw.cars017, 1);
    }

    public void test1(View v){
        sp.play(sound1,0.5f,0.5f,1,0,1);
    }
    public void test2(View v){
        sp.play(sound2,0.5f,0.5f,1,0,1);
    }

}
