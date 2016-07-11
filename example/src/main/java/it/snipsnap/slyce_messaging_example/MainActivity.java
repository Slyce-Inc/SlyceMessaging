package it.snipsnap.slyce_messaging_example;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.slyce.slyce_messaging.messenger.SlyceMessagingFragment;
import it.slyce.slyce_messaging.messenger.listeners.ShouldLoadMoreMessagesListener;
import it.slyce.slyce_messaging.messenger.listeners.UserSendsMessageListener;
import it.slyce.slyce_messaging.messenger.message.Message;
import it.slyce.slyce_messaging.messenger.message.MessageSource;
import it.slyce.slyce_messaging.messenger.message.TextMessage;

public class MainActivity extends AppCompatActivity {

    SlyceMessagingFragment slyceMessagingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.snipsnap.slyce_messaging_example.R.layout.activity_main);

        slyceMessagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.fragment_for_scout);
        slyceMessagingFragment.setDefaultAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
        slyceMessagingFragment.setDefaultDisplayName("Matthew Page");
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo");
        slyceMessagingFragment.setMoreMessagesExist(false);

        ArrayList<Message> messages = new ArrayList<Message>();
        TextMessage textMessage = new TextMessage();
        textMessage.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque mollis sed lacus eget hendrerit. Etiam in.");
        textMessage.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
        textMessage.setDate(new Date().getTime());
        textMessage.setDisplayName("Sam");
        textMessage.setUserId("Sam");
        textMessage.setOrigin(MessageSource.EXTERNAL_USER);
        messages.add(textMessage);

        textMessage = new TextMessage();
        textMessage.setText("Nullam lorem metus, dignissim blandit orci a, malesuada semper nunc. Pellentesque viverra risus.");
        textMessage.setAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
        textMessage.setDate(new Date().getTime());
        textMessage.setDisplayName("Me");
        textMessage.setUserId("Me");
        textMessage.setOrigin(MessageSource.LOCAL_USER);
        messages.add(textMessage);

        textMessage = new TextMessage();
        textMessage.setText("Donec scelerisque dolor nec lectus ultrices imperdiet. Donec ligula ante, commodo in finibus sed, imperdiet ac magna. Integer ultricies, libero accumsan egestas semper, lectus tortor vulputate ante, at vestibulum elit turpis.");
        textMessage.setAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
        textMessage.setDate(new Date().getTime());
        textMessage.setDisplayName("Me");
        textMessage.setUserId("Me");
        textMessage.setOrigin(MessageSource.LOCAL_USER);
        messages.add(textMessage);

        textMessage = new TextMessage();
        textMessage.setText("Sed vehicula eget ante sit.");
        textMessage.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
        textMessage.setDate(new Date().getTime());
        textMessage.setDisplayName("Sam");
        textMessage.setUserId("Sam");
        textMessage.setOrigin(MessageSource.EXTERNAL_USER);
        messages.add(textMessage);

        textMessage = new TextMessage();
        textMessage.setText("In ultrices in nisl tincidunt laoreet. Cras sodales libero id turpis feugiat, sit amet suscipit ex aliquet. Phasellus suscipit nisi sed.");
        textMessage.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
        textMessage.setDate(new Date().getTime());
        textMessage.setDisplayName("Sam");
        textMessage.setUserId("Sam");
        textMessage.setOrigin(MessageSource.EXTERNAL_USER);
        messages.add(textMessage);

        slyceMessagingFragment.addNewMessages(messages);

        slyceMessagingFragment.setOnSendMessageListener(new UserSendsMessageListener() {
            @Override
            public void onUserSendsTextMessage(String text) {
                System.out.println("******************************** " + text);
            }

            @Override
            public void onUserSendsMediaMessage(Uri imageUri) {
                System.out.println("******************************** " + imageUri);
            }
        });


        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; true; i++) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TextMessage textMessage = new TextMessage();
                    textMessage.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
                    textMessage.setText("MessageData # " + i);
                    textMessage.setDate(System.currentTimeMillis());
                    textMessage.setOrigin(MessageSource.EXTERNAL_USER);
                    textMessage.setDisplayName("Larry Page");
                    slyceMessagingFragment.addNewMessage(textMessage);
                }
            }
        }).start();
        */

    }


}
