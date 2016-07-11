# Slyce Messaging API

![](https://github.com/snipsnap/SlyceMessaging/blob/master/Screenshot_20160708-162931.png?raw=true | width=100)

Basic features of the API:

 * Full custimization of colors
 * Dynamic timestamps
 * Support for (optional) photo messages
 * Avatar photos (with onclick listeners)
 * Copy text with long press
 * Allows for more mesages to be loaded when user scrolls close to the top
 * Upon recieval of a message, a snackbar is shown which the user can use to scroll to the next message

## Installation

Download the [arr file](https://github.com/snipsnap/SlyceMessaging/releases/download/1.0.0/slyce-messaging-release.aar). In your project do File -> New -> New Module. Select "Import .JAR/.AAR Package". Select the file you downloaded and give the subproject any name you want, and click "finish".

Now do File -> Project Structure. On the left hand side, at the bottom, select your app's module. Under the "dependencies" tab, add a module dependency to the module you created above.

If you get compilation or runtime errors, try adding the following to your app's gradle file:

```ruby
dependencies {
    compile 'com.android.support:cardview-v7:23.2.1'
    compile 'com.android.support:design:23.2.1'
    compile 'com.makeramen:roundedimageview:2.2.1'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'de.hdodenhof:circleimageview:2.0.0'
}
```

## The API

### SlyceMessagingFragment

```java
public void setUserSendsMessageListener(UserSendsMessageListener listener); // gets called when the user sends a message
public void setUserClicksAvatarPhotoListener(UserClicksAvatarPhotoListener listener); // gets called when a user clicks an avatar photo. Optional.
public void setDefaultAvatarUrl(String url); // The avatar for the current user.
public void setDefaultUserId(String id); // A unique identifier for the current user.
public void setPictureButtonVisible(boolean bool); // Used to toggle whether the user can send picture messages. Default is true.
public void setStyle(int style); // See section "Custimize colors"
public void setMoreMessagesExist(boolean bool); // Sets whether more messages can be loaded from the top
public void setShouldLoadMoreMessagesListener(ShouldLoadMoreMessagesListener listener); // Gets called when the user scrolls close to the top, if relevent

public void addMessage(Message message);
public void addMessages(List<Message> messages);
public void replaceMessages(List<MessageItems> messages);
```

### Message System
```java
public abstract class Message {
    public void setDate(String date);
	public void setOrigin(MessageOrigin origin);
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

public enum MessageOrigin {
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

public interface ShouldLoadMoreMessagesListener {
    public List<Message> shouldLoadMoreMessages();
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
public class ExampleActivity extends AppCompatActivity {

    SlyceMessagingFragment slyceMessagingFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        slyceMessagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.fragment_for_scout);
        slyceMessagingFragment.setDefaultAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
        slyceMessagingFragment.setDefaultDisplayName("Matthew Page");
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo");
        slyceMessagingFragment.setMoreMessagesExist(true);
        slyceMessagingFragment.setShouldLoadMoreMessagesListener(new ShouldLoadMoreMessagesListener() {
            @Override
            public List<Message> shouldLoadMoreMessages() {
                Log.i("info", "shrug.txt");
                ArrayList<Message> messages = new ArrayList<Message>();
                for (int i = 0; i < 50; i++) {
                    TextMessage textMessage = new TextMessage();
                    textMessage.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
                    textMessage.setText("MessageData # " + i);
                    textMessage.setDate(System.currentTimeMillis());
                    textMessage.setOrigin(MessageSource.EXTERNAL_USER);
                    textMessage.setDisplayName("Larry Page");
                    messages.add(textMessage);
                }
                return messages;
            }
        });

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
