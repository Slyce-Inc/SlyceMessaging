package it.slyce.messaging;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Bundle extras = getIntent().getExtras();
        final String url = extras.getString("URL", null);
        final ImageView imageView = (ImageView) findViewById(R.id.image_view_large);
        Picasso picasso = Picasso.with(getApplicationContext());
        if (picasso != null && url != null) {
            picasso.load(url).into(imageView);
        }
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
                sendIntent.setType("image/jpeg");
                startActivity(sendIntent);
                return false;
            }
        });
    }
}
