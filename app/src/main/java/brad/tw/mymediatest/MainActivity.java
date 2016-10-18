package brad.tw.mymediatest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private SoundPool sp;
    private int sound1,sound2;
    private boolean isRecording;
    private MediaRecorder mRecorder;
    private File sdroot;
    private Button recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO},
                        123);

        }

        sdroot = Environment.getExternalStorageDirectory();
        recorder = (Button)findViewById(R.id.recorder);
        recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test3();
            }
        });

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


    public void test3(){
        isRecording = !isRecording;
        if (isRecording){
            recorder.setText("STOP");
            startRecording();
        }else{
            recorder.setText("START");
            stopRecording();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(sdroot.getAbsolutePath() + "/brad.3gp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            Log.e("brad", "prepare() failed");
        }

    }


    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


}
