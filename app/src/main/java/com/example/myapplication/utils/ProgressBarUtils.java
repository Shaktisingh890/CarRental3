package com.example.myapplication.utils;
import android.view.View;
import android.widget.ImageView;

public class ProgressBarUtils {

    /**
     * Shows or hides a progress bar with an overlay.
     *
     * @param progressOverlay The overlay view to be shown/hidden.
     * @param progressBar     The progress bar to be shown/hidden.
     * @param show            True to show, false to hide.
     */
    public static void showProgress(View progressOverlay, View progressBar, boolean show, ImageView image_logo) {
        if (show) {
            progressOverlay.setVisibility(View.VISIBLE); // Show overlay
            progressBar.setVisibility(View.VISIBLE);     // Show progress bar
            image_logo.setVisibility(View.VISIBLE);

        } else {
            progressOverlay.setVisibility(View.GONE);    // Hide overlay
            progressBar.setVisibility(View.GONE);
            image_logo.setVisibility(View.GONE);
        }
    }
}






