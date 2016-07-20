# Slyce Messaging API

![](https://circleci.com/gh/snipsnap/SlyceMessaging.svg?style=shield&circle-token=46075f470208f71a4836c234126bb773c51219d8)

![](sample-photos/example.png?raw=true) ![](sample-photos/chat-with-image.png?raw=true)

Basic features of the API:

 * Full custimization of colors
 * Dynamic timestamps
 * Support for (optional) photo messages
 * Avatar photos (with onclick listeners)
 * Copy text with long press
 * Allows for more mesages to be loaded when user scrolls close to the top
 * Upon recieval of a message, a snackbar is shown which the user can use to scroll to the next message

## Installation

Download the [arr file](https://github.com/snipsnap/SlyceMessaging/releases/download/1.0.2/slyce-messaging.aar). In your project do File -> New -> New Module. Select "Import .JAR/.AAR Package". Select the file you downloaded and give the subproject any name you want, and click "finish".

Now do File -> Project Structure. On the left hand side, at the bottom, select your app's module. Under the "dependencies" tab, add a module dependency to the module you created above.

If you get compilation or runtime errors, try adding the following to your app's gradle file:

```ruby
repositories {
    maven {
        url "https://s3.amazonaws.com/repo.commonsware.com"
    }
}

dependencies {
    compile 'com.android.support:design:23.2.1'
    compile 'com.makeramen:roundedimageview:2.2.1'
    compile 'de.hdodenhof:circleimageview:2.0.0'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.commonsware.cwac:cam2:0.6.2'
}
```

## The API

You must initialize the fragment by declaring an XML tag like the following:

```xml
<fragment
            android:name="it.slyce.messaging.SlyceMessagingFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/messaging_fragment"/>
```

### SlyceMessagingFragment

```java
public void setUserSendsMessageListener(UserSendsMessageListener listener); // gets called when the user sends a message
public void setUserClicksAvatarPhotoListener(UserClicksAvatarPhotoListener listener); // gets called when a user clicks an avatar photo. Optional.
public void setDefaultAvatarUrl(String url); // The avatar for the current user.
public void setDefaultUserId(String id); // A unique identifier for the current user.
public void setPictureButtonVisible(boolean bool); // Used to toggle whether the user can send picture messages. Default is true.
public void setStyle(int style); // See section "Custimize colors"
public void setMoreMessagesExist(boolean bool); // Sets whether more messages can be loaded from the top
public void setLoadMoreMessagesListener(ShouldLoadMoreMessagesListener listener); // Gets called when the user scrolls close to the top, if relevent

public void addMessage(Message message);
public void addMessages(List<Message> messages);
public void replaceMessages(List<MessageItems> messages);
```

### Message System
```java
public abstract class Message {
    public void setDate(String date);
	public void setSource(MessageSource source);
	public void setAvatarUrl(String url);
	public void setDisplayName(String name);
	public void setUserId(String id);
}

public class TextMessage extends Message {
	public void setText(String text);
}

public class MediaMessage extends Message{
	public void setPhotoUrl(String url);
}

public enum MessageSource {
	LOCAL_USER,
	EXTERNAL_USER
}
```

### Listeners
```java
public interface UserSendsMessageListener {
	public void onUserSendsTextMessage(String text);
	public void onUserSendsMediaMessage(Uri imageUri);
}

public interface UserClicksAvatarPictureListener {
	public void userClicksAvatarPhoto(MessageOrigin messageOrigin, String userId);
}

public interface LoadMoreMessagesListener {
    public List<Message> loadMoreMessages();
}
```

### Custimize colors

You can custimize the colors of the fragment by providing a style with the following attributes in a method call to SlyceMessagingFragment. All attributes are colors.

* backgroundColor
* timestampTextColor
* localBubbleBackground
* localBubbleTextColor
* externalBubbleBackground
* externalBubbleTextColor
* snackbarBackground
* snackbarTitleColor
* snackbarButtonColor

## Example

```java
public class MainActivity extends AppCompatActivity {
    private static int n = 0;
    private static String[] latin = {
            "Vestibulum dignissim enim a mauris malesuada fermentum. Vivamus tristique consequat turpis, pellentesque.",
            "Quisque nulla leo, venenatis ut augue nec, dictum gravida nibh. Donec augue nisi, volutpat nec libero.",
            "Cras varius risus a magna egestas.",
            "Mauris tristique est eget massa mattis iaculis. Aenean sed purus tempus, vestibulum ante eget, vulputate mi. Pellentesque hendrerit luctus tempus. Cras feugiat orci.",
            "Morbi ullamcorper, sapien mattis viverra facilisis, nisi urna sagittis nisi, at luctus lectus elit.",
            "Phasellus porttitor fermentum neque. In semper, libero id mollis.",
            "Praesent fermentum hendrerit leo, ac rutrum ipsum vestibulum at. Curabitur pellentesque augue.",
            "Mauris finibus mi commodo molestie placerat. Curabitur aliquam metus vitae erat vehicula ultricies. Sed non quam nunc.",
            "Praesent vel velit at turpis vestibulum eleifend ac vehicula leo. Nunc lacinia tellus eget ipsum consequat fermentum. Nam purus erat, mollis sed ullamcorper nec, efficitur.",
            "Suspendisse volutpat enim eros, et."
    };

    private static Message getRandomMessage() {
        n++;
        TextMessage textMessage = new TextMessage();
        textMessage.setText(n + ""); // +  ": " + latin[(int) (Math.random() * 10)]);
        textMessage.setDate(new Date().getTime());
        if (Math.random() > 0.5) {
            textMessage.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
            textMessage.setUserId("LP");
            textMessage.setSource(MessageSource.EXTERNAL_USER);
        } else {
            textMessage.setAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
            textMessage.setUserId("MP");
            textMessage.setSource(MessageSource.LOCAL_USER);
        }
        return textMessage;
    }

    SlyceMessagingFragment slyceMessagingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slyceMessagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.fragment_for_slyce);
        slyceMessagingFragment.setDefaultAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
        slyceMessagingFragment.setDefaultDisplayName("Matthew Page");
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo");

        slyceMessagingFragment.setOnSendMessageListener(new UserSendsMessageListener() {
            @Override
            public void onUserSendsTextMessage(String text) {
                Log.d("inf", "******************************** " + text);
            }

            @Override
            public void onUserSendsMediaMessage(Uri imageUri) {
                Log.d("inf", "******************************** " + imageUri);
            }
        });

        slyceMessagingFragment.setLoadMoreMessagesListener(new LoadMoreMessagesListener() {
            @Override
            public List<Message> loadMoreMessages() {
                ArrayList<Message> messages = new ArrayList<>();
                for (int i = 0; i < 50; i++)
                    messages.add(getRandomMessage());
                return messages;
            }
        });

        slyceMessagingFragment.setMoreMessagesExist(true);
    }
}
```

```xml
<style name="MyTheme">
    <item name="backgroundColor">@color/background_white</item>
    <item name="timestampTextColor">@color/text_black</item>
    <item name="localBubbleBackground">@color/background_blue</item>
    <item name="localBubbleTextColor">@color/text_white</item>
    <item name="externalBubbleBackground">@color/background_gray</item>
    <item name="externalBubbleTextColor">@color/text_black</item>
    <item name="snackbarBackground">@color/background_gray_darkest</item>
    <item name="snackbarTitleColor">@color/text_black</item>
    <item name="snackbarButtonColor">@color/text_blue</item>
</style>
```

## Developers

We have a local.properties file checked in for CI builds. Please do not check in changes to local.properties. Run this command to prevent that file from showing up as changed:

```git update-index --assume-unchanged local.properties```
