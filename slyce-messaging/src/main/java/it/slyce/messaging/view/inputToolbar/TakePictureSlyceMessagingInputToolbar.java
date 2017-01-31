package it.slyce.messaging.view.inputToolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.ZoomStyle;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import it.slyce.messaging.R;
import it.slyce.messaging.SlyceMessagingFragment;

/**
 * Created by pr on 30.01.2017.
 */

public class TakePictureSlyceMessagingInputToolbar implements SlyceMessagingInputToolbar {
    private View.OnClickListener clickListener;

    public TakePictureSlyceMessagingInputToolbar(View.OnClickListener clickListener){

        this.clickListener = clickListener;
    }

    private File file;
    private Uri outputFileUri;

    public File getFile(){
        return file;
    }

    public Uri getOutputFile(){
        return outputFileUri;
    }

    @Override
    public void inflateInputToolbarViews(Context context, ViewGroup viewGroup) {
        LayoutInflater.from(context).inflate(R.layout.input_toolbar_takephoto, viewGroup, true);

        View takePhotoView = viewGroup.findViewById(R.id.slyce_messaging_image_view_snap);
        takePhotoView.setOnClickListener(this.clickListener);
    }

    @Override
    public void onInputToolbarViewClicked(SlyceMessagingFragment fragment, View clickedView) {
        if (clickedView.getId() == R.id.slyce_messaging_image_view_snap) {
            fragment.getInputEditText().setText("");
            final File mediaStorageDir = fragment.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File root = new File(mediaStorageDir, "SlyceMessaging");
            root.mkdirs();
            final String fname = "img_" + System.currentTimeMillis() + ".jpg";
            file = new File(root, fname);
            outputFileUri = Uri.fromFile(file);
            Intent takePhotoIntent = new CameraActivity.IntentBuilder(fragment.getActivity().getApplicationContext())
                    .skipConfirm()
                    .to(this.file)
                    .zoomStyle(ZoomStyle.SEEKBAR)
                    .updateMediaStore()
                    .build();
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhotoIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Take a photo or select one from your device");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePhotoIntent});
            try {
                fragment.startActivityForResult(chooserIntent, 1);
            } catch (RuntimeException exception) {
                Log.d("debug", exception.getMessage());
                exception.printStackTrace();
            }
        }
    }


}
