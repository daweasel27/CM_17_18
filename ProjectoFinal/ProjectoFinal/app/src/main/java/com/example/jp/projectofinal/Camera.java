package com.example.jp.projectofinal;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;
import java.util.Random;

import static android.R.attr.x;

/**
     * This is a very bare sample app to demonstrate the usage of the CameraDetector object from Affectiva.
     * It displays statistics on frames per second, percentage of time a face was detected, and the user's smile score.
     *
     * The app shows off the maneuverability of the SDK by allowing the user to start and stop the SDK and also hide the camera SurfaceView.
     *
     * For use with SDK 2.02
     */
    public class Camera extends YouTubeBaseActivity implements Detector.ImageListener, CameraDetector.CameraEventListener, YouTubePlayer.OnInitializedListener {

        final String[] trailers = {"hAUTdjf9rko", "DblEwHkde8U", "ue80QwXMRHg"};

        final String LOG_TAG = "CameraDetectorDemo";

        Button startSDKButton;
        Button surfaceViewVisibilityButton;
        TextView smileTextView;
        TextView ageTextView;
        TextView ethnicityTextView;
        ToggleButton toggleButton;

        SurfaceView cameraPreview;

        boolean isCameraBack = false;
        boolean isSDKStarted = false;

        RelativeLayout mainLayout;

        CameraDetector detector;

        int previewWidth = 0;
        int previewHeight = 0;
        static final Integer CAMERA = 0x5;

        private YouTubePlayerView youtube;
        YouTubePlayerFragment myYouTubePlayerFragment;
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


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.trailer_watch);

            askForPermission(Manifest.permission.CAMERA,CAMERA);

            myYouTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager()
                    .findFragmentById(R.id.youtube);
            myYouTubePlayerFragment.initialize("AIzaSyC3GrHmHq_a7MTpXJ2wzO9H5qryoYn7dJw", this);
            //youtube = (YouTubePlayerView) findViewById(R.id.youtube);
            //youtube.setVisibility(View.VISIBLE);
            //youtube.initialize("AIzaSyC3GrHmHq_a7MTpXJ2wzO9H5qryoYn7dJw",this);

            smileTextView = (TextView) findViewById(R.id.smile_textview);
            ageTextView = (TextView) findViewById(R.id.age_textview);
            ethnicityTextView = (TextView) findViewById(R.id.ethnicity_textview);

            toggleButton = (ToggleButton) findViewById(R.id.front_back_toggle_button);
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isCameraBack = isChecked;
                    switchCamera(isCameraBack? CameraDetector.CameraType.CAMERA_BACK : CameraDetector.CameraType.CAMERA_FRONT);
                }
            });

            startSDKButton = (Button) findViewById(R.id.sdk_start_button);
            startSDKButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSDKStarted) {
                        isSDKStarted = false;
                        stopDetector();

                        startSDKButton.setText("Start Camera");
                    } else {
                        isSDKStarted = true;
                        startDetector();
                        startSDKButton.setText("Stop Camera");
                    }
                }
            });
            startSDKButton.setText("Start Camera");

            //We create a custom SurfaceView that resizes itself to match the aspect ratio of the incoming camera frames
            mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
            cameraPreview = new SurfaceView(this) {
                @Override
                public void onMeasure(int widthSpec, int heightSpec) {
                    int measureWidth = 300;
                    int measureHeight = 450;
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
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_TOP,RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            cameraPreview.setLayoutParams(params);
            mainLayout.addView(cameraPreview,0);

            surfaceViewVisibilityButton = (Button) findViewById(R.id.surfaceview_visibility_button);
            surfaceViewVisibilityButton.setText("HIDE SURFACE");
            surfaceViewVisibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cameraPreview.getVisibility() == View.VISIBLE) {
                        cameraPreview.setVisibility(View.INVISIBLE);
                        surfaceViewVisibilityButton.setText("SHOW SURFACE");
                        //youtube.setVisibility(View.VISIBLE);
                    } else {
                        cameraPreview.setVisibility(View.VISIBLE);
                        surfaceViewVisibilityButton.setText("HIDE SURFACE");
                        //youtube.setVisibility(View.INVISIBLE);
                    }
                }
            });

            detector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraPreview);
            detector.setDetectSmile(true);
            detector.setDetectAge(true);
            detector.setDetectEthnicity(true);
            detector.setImageListener(this);
            detector.setOnCameraEventListener(this);
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
            if (!wasRestored) {
                player.cueVideo(trailers[new Random().nextInt(trailers.length)]); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
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

        void switchCamera(CameraDetector.CameraType type) {
            detector.setCameraType(type);
        }

        @Override
        public void onImageResults(List<Face> list, Frame frame, float v) {
            if (list == null)
                return;
            if (list.size() == 0) {
                smileTextView.setText("NO FACE");
                ageTextView.setText("");
                ethnicityTextView.setText("");
            } else {
                Face face = list.get(0);
                smileTextView.setText(String.format("SMILE\n%.2f",face.expressions.getSmile()));
                switch (face.appearance.getAge()) {
                    case AGE_UNKNOWN:
                        ageTextView.setText("");
                        break;
                    case AGE_UNDER_18:
                        ageTextView.setText(R.string.age_under_18);
                        break;
                    case AGE_18_24:
                        ageTextView.setText(R.string.age_18_24);
                        break;
                    case AGE_25_34:
                        ageTextView.setText(R.string.age_25_34);
                        break;
                    case AGE_35_44:
                        ageTextView.setText(R.string.age_35_44);
                        break;
                    case AGE_45_54:
                        ageTextView.setText(R.string.age_45_54);
                        break;
                    case AGE_55_64:
                        ageTextView.setText(R.string.age_55_64);
                        break;
                    case AGE_65_PLUS:
                        ageTextView.setText(R.string.age_over_64);
                        break;
                }

                switch (face.appearance.getEthnicity()) {
                    case UNKNOWN:
                        ethnicityTextView.setText("");
                        break;
                    case CAUCASIAN:
                        ethnicityTextView.setText(R.string.ethnicity_caucasian);
                        break;
                    case BLACK_AFRICAN:
                        ethnicityTextView.setText(R.string.ethnicity_black_african);
                        break;
                    case EAST_ASIAN:
                        ethnicityTextView.setText(R.string.ethnicity_east_asian);
                        break;
                    case SOUTH_ASIAN:
                        ethnicityTextView.setText(R.string.ethnicity_south_asian);
                        break;
                    case HISPANIC:
                        ethnicityTextView.setText(R.string.ethnicity_hispanic);
                        break;
                }

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
    }
