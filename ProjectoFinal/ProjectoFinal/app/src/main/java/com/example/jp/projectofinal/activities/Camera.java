package com.example.jp.projectofinal.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.asyncTasks.LoadYTVideoAsyncTask;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

/**
 * This is a very bare sample app to demonstrate the usage of the CameraDetector object from Affectiva.
 * It displays statistics on frames per second, percentage of time a face was detected, and the user's smile score.
 *
 * The app shows off the maneuverability of the SDK by allowing the user to start and stop the SDK and also hide the camera SurfaceView.
 *
 * For use with SDK 2.02
 */
public class Camera extends YouTubeBaseActivity implements Detector.ImageListener, CameraDetector.CameraEventListener,
        YouTubePlayer.OnInitializedListener {
    private boolean firstRun = false;
    private String DEVELOPER_KEY = "AIzaSyC3GrHmHq_a7MTpXJ2wzO9H5qryoYn7dJw";

    final String LOG_TAG = "CameraDetectorDemo";
    private MyPlaybackEventListener playbackEventListener;

    SurfaceView cameraPreview;

    boolean isSDKStarted = false;
    private boolean portrait = true;
    private boolean enableRec = true;

    RelativeLayout mainLayout;

    CameraDetector detector;
    int previewWidth = 0;
    int previewHeight = 0;
    static final Integer CAMERA = 0x5;
    private static final int RECORD_REQUEST_CODE = 101;


    private YouTubePlayerView playerView;
    private ImageView imageViewPlayControl;
    private Button skipButton;
    private static final int RECOVERY_REQUEST = 1;

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Camera.this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Camera.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Camera.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(Camera.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                RECORD_REQUEST_CODE);
        //askForPermission(Manifest.permission.CAMERA, CAMERA);

    }


    public void getOrientation()
    {
        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels)
            this.portrait = false;
        else
            this.portrait = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
    }

    private void defaultSetup(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean facialRec = sharedPreferences.getBoolean("show_bass", true);
        if (facialRec)
            this.enableRec = true;
        else
            this.enableRec = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defaultSetup();
        if(this.enableRec)
            setContentView(R.layout.activity_youtube_player);
        else
            setContentView(R.layout.activity_youtube_player_without_rec);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }

        getOrientation();

        MainActivity.sv.setFile("values.txt");

        imageViewPlayControl = (ImageView) findViewById(R.id.imageViewPlayControl);
        skipButton = (Button) findViewById(R.id.skip);

        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                stopDetector();
                MainActivity.sv.saveList();

            }
        });

        playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        playerView.initialize(DEVELOPER_KEY, this);

        playbackEventListener = new MyPlaybackEventListener(this.enableRec);

        //We create a custom SurfaceView that resizes itself to match the aspect ratio of the incoming camera frames
        mainLayout = (RelativeLayout) findViewById(R.id.main_relative_layout);

        cameraPreview = new SurfaceView(this) {
            @Override
            public void onMeasure(int widthSpec, int heightSpec) {
                int measureWidth;
                int measureHeight;
                if(enableRec){
                    measureWidth = 200;
                    measureHeight = 350;
                }else{
                    measureWidth = 0;
                    measureHeight = 0;
                }
                int width;
                int height;
                if (previewHeight == 0 || previewWidth == 0) {
                    width = measureWidth;
                    height = measureHeight;
                } else {
                    float viewAspectRatio = (float)measureWidth/measureHeight;
                    float cameraPreviewAspectRatio = (float) previewWidth/previewHeight;

                    if (cameraPreviewAspectRatio > viewAspectRatio) {
                        width = measureWidth;
                        height =(int) (measureWidth / cameraPreviewAspectRatio);
                    } else {
                        width = (int) (measureHeight * cameraPreviewAspectRatio);
                        height = measureHeight;
                    }
                }
                setMeasuredDimension(width,height);
            }
        };
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_TOP, RelativeLayout.TRUE);
        if(portrait)
            params.addRule(RelativeLayout.ALIGN_LEFT);
        else
            params.setMargins(60, 0,0, 0);
        cameraPreview.setLayoutParams(params);
        mainLayout.addView(cameraPreview, 0);
        //mainLayout.bringChildToFront(cameraPreview); // To put facial rec on the front - it pause the video

        detector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraPreview);
        /* Features to detect */
        detector.setDetectSmile(true);
        detector.setDetectAnger(true);
        detector.setDetectJoy(true);
        detector.setDetectAttention(true);
        detector.setDetectFear(true);
        detector.setDetectSurprise(true);
        detector.setImageListener(this);
        detector.setOnCameraEventListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setPlaybackEventListener(playbackEventListener);
        if(this.enableRec)
            player.setShowFullscreenButton(true);
        else
            player.setShowFullscreenButton(false);
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        if (!wasRestored) {
            new LoadYTVideoAsyncTask().execute(player);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSDKStarted) {
            startDetector();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDetector();
    }

    void startDetector() {
        if (!detector.isRunning()) {
            detector.start();
        }
    }

    void stopDetector() {
        if (detector.isRunning()) {
            detector.stop();

        }
    }



    @Override
    public void onImageResults(List<Face> list, Frame frame, float v) {
        if (list == null)
            return;
        if (list.size() == 0) {
            //smileTextView.setText("NO FACE");
            //joyTextView.setText("");
            //angerTextView.setText("");
            //Toast toast = Toast.makeText(getApplicationContext(), "Not recognizing a face", Toast.LENGTH_SHORT);
            //toast.show();
            imageViewPlayControl.setImageResource(R.drawable.of_small);

        } else {
            Face face = list.get(0);
            //smileTextView.setText(String.format("SMILE\n%.2f",face.expressions.getSmile()));
            //joyTextView.setText(String.format("JOY\n%.2f",face.emotions.getJoy()));
            //angerTextView.setText(String.format("ANGER\n%.2f",face.emotions.getAnger()));

            //Log.d("SMILE", String.format("SMILE\n%.2f",face.expressions.getSmile()));

            imageViewPlayControl.setImageResource(R.drawable.on_small);

            try{
                MainActivity.sv.addValuesExpressions("smile",face.expressions.getSmile(), MainActivity.mv.getLastWatchedTrailer());
                MainActivity.sv.addValuesExpressions("anger", face.emotions.getAnger(), MainActivity.mv.getLastWatchedTrailer());
                MainActivity.sv.addValuesExpressions("joy", face.emotions.getJoy(), MainActivity.mv.getLastWatchedTrailer());
                MainActivity.sv.addValuesExpressions("fear", face.emotions.getFear(), MainActivity.mv.getLastWatchedTrailer());
                MainActivity.sv.addValuesExpressions("attention", face.expressions.getAttention(), MainActivity.mv.getLastWatchedTrailer());
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            //Log.d("JOY", String.format("JOY\n%.2f",face.emotions.getJoy()));
            //Log.d("ANGER",String.format("ANGER\n%.2f",face..getAnger()));

            //Face feature points coordinates
            //PointF[] points = face.getFacePoints();
        }
    }


    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onCameraSizeSelected(int width, int height, Frame.ROTATE rotate) {
        if (rotate == Frame.ROTATE.BY_90_CCW || rotate == Frame.ROTATE.BY_90_CW) {
            previewWidth = height;
            previewHeight = width;
        } else {
            previewHeight = height;
            previewWidth = width;
        }
        cameraPreview.requestLayout();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener, YouTubePlayer.PlayerStateChangeListener {

        private boolean enableRec;

        private MyPlaybackEventListener(boolean enableRec){
            this.enableRec = enableRec;
        }
        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing Facial Recognition");
            //imageViewPlayControl.setImageResource(R.drawable.on_small);
            if(this.enableRec)
                imageViewPlayControl.setImageResource(R.drawable.on_small);
            startDetector();
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused Facial Recognition");
            if(this.enableRec)
                imageViewPlayControl.setImageResource(R.drawable.of_small);
            stopDetector();
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped Facial Recognition");
            stopDetector();

            Log.e("ola","Stopped Facial Recognition");

        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {
            showMessage("Next Video");

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    }
}