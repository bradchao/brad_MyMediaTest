package brad.tw.mymediatest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SoundPool sp;
    private int sound1,sound2;
    private boolean isRecording;
    private MediaRecorder mRecorder;
    private File sdroot;
    private Button recorder;
    private String strFile;
    private Uri uriMusic, uriPhoto;
    private ImageView img;

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
        img = (ImageView)findViewById(R.id.img);

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


    public void test3(View v){
        isRecording = !isRecording;
        if (isRecording){
            recorder.setText("STOP");
            new Thread(){
                @Override
                public void run() {
                    startRecording();
                }
            }.start();
        }else{
            recorder.setText("START");
            stopRecording();
        }
    }

    private void startRecording() {

        strFile = sdroot.getAbsolutePath() + "/brad.mp3";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(strFile);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

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

    public void test4(View v){
        Intent it = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(it, 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12){
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();
                Log.v("brad",uri.getPath());
                Log.v("brad",getFilePathFromUri(uri));
                uriMusic = uri;
            }else{
                uriMusic = null;
            }
        }else if (requestCode == 34){
            //after1(data);
            after2();
        }
    }

    // 縮圖
    private void after1(Intent it){
        Bitmap bmp = (Bitmap)it.getExtras().get("data");
        img.setImageBitmap(bmp);

    }
    // 縮圖
    private void after2(){
        img.setImageURI(uriPhoto);

    }

    private String getFilePathFromUri(Uri uri){
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor c = getContentResolver().query(uri,proj,null,null,null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(proj[0]));
    }

    public void test5(View v){
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(strFile);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            Log.v("brad", "e1:" + e.toString());
        }
    }
    public void test6(View v){
        if (uriMusic == null) return;

        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(this,uriMusic);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            Log.v("brad", "e1:" + e.toString());
        }
    }

    public void test7(View v){
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        uriPhoto = Uri.fromFile(new File(sdroot, "brad.jpg"));
        it.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);


        startActivityForResult(it, 34);

    }


}
