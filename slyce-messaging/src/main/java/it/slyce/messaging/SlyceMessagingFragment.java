package it.slyce.messaging;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.ZoomStyle;

import it.slyce.messaging.listeners.ShouldLoadMoreMessagesListener;
import it.slyce.messaging.listeners.UserClicksAvatarPictureListener;
import it.slyce.messaging.listeners.UserSendsMessageListener;
import it.slyce.messaging.message.MediaMessage;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.SpinnerMessage;
import it.slyce.messaging.message.TextMessage;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageRecyclerAdapter;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.utils.Utils;
import it.slyce.messaging.view.ViewUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by John C. Hunchar on 1/12/16.
 */
public class SlyceMessagingFragment extends Fragment implements
        OnClickListener {

    private static final int START_RELOADING_DATA_AT_SCROLL_VALUE = 5000; // TODO: maybe change this? make it customizable?
    private static final int CONSIDER_AT_EDGE_IF_WITHIN_THIS_MUCH = 100;
    private static final int SMOOTH_SCROLL_IF_THIS_MUCH = 100000;

    private EditText mEntryField;
    private ImageView mSendButton;
    private ImageView mSnapButton;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Message> mMessages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private UserSendsMessageListener listener;

    private String defaultAvatarUrl;
    private String defaultDisplayName;
    private String defaultUserId;

    private View rootView;

    private boolean moreMessagesExist;
    private ShouldLoadMoreMessagesListener shouldLoadMoreMessagesListener;

    private int startHereWhenUpdate;

    private CustomSettings customSettings;

    public void setPictureButtonVisible(final boolean bool) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = (ImageView) rootView.findViewById(R.id.scout_image_view_snap);
                    imageView.setVisibility(bool ? View.VISIBLE : View.GONE);
                }
            });
    }

    private void addSpinner() {
        mMessages.add(0, new SpinnerMessage());
        replaceMessages(mMessages, -1);
    }

    private void removeSpinner() {
        mMessages.remove(0);
        replaceMessages(mMessages, -1);
    }

    public void setMoreMessagesExist(boolean moreMessagesExist) {
        if (this.moreMessagesExist == moreMessagesExist)
            return;
        this.moreMessagesExist = moreMessagesExist;
        if (moreMessagesExist)
            addSpinner();
        else
            removeSpinner();
    }

    public void setShouldLoadMoreMessagesListener(ShouldLoadMoreMessagesListener shouldLoadMoreMessagesListener) {
        this.shouldLoadMoreMessagesListener = shouldLoadMoreMessagesListener;
    }

    public void setUserClicksAvatarPictureListener(UserClicksAvatarPictureListener userClicksAvatarPictureListener) {
        this.customSettings.userClicksAvatarPictureListener = userClicksAvatarPictureListener;
    }

    public void setDefaultAvatarUrl(String defaultAvatarUrl) {
        this.defaultAvatarUrl = defaultAvatarUrl;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    public void setDefaultUserId(String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    // INTENT: create new request
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.fragment_slyce_messaging, null);
        this.rootView = v;

        // this.rootView = layoutInflater.inflate(R.layout.fragment_slyce_messaging, null);
        this.customSettings = new CustomSettings();
        // layoutInflater.inflate(R.layout.fragment_slyce_messaging, viewGroup);

        // Setup views
        mEntryField = (EditText) rootView.findViewById(R.id.scout_edit_text_entry_field);
        mSendButton = (ImageView) rootView.findViewById(R.id.scout_image_view_send);
        mSnapButton = (ImageView) rootView.findViewById(R.id.scout_image_view_snap);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.scout_recycler_view);

        // Add interfaces
        mSendButton.setOnClickListener(this);
        mSnapButton.setOnClickListener(this);

        // Init variables for recycler view
        mMessages = new ArrayList<>();
        mMessageItems = new ArrayList<MessageItem>();
        Picasso picasso = Picasso.with(this.getActivity().getApplicationContext());
        mRecyclerAdapter = new MessageRecyclerAdapter(mMessageItems, picasso, customSettings);
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        mLinearLayoutManager.setStackFromEnd(true);

        // Setup recycler view
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        startThreadToUpdateTimestamps();

        startHereWhenUpdate = 0;

        setStyle(R.style.MyTheme);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    if (shouldReloadData()) {
                        loadMoreMessages();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return rootView;
    }

    private class ReplaceMessagesTask extends AsyncTask {
        private List<Message> messages;
        private MessageRecyclerAdapter mRecyclerAdapter;
        private int upTo;

        public ReplaceMessagesTask(List<Message> messages, MessageRecyclerAdapter mRecyclerAdapter, int upTo) {
            this.messages = messages;
            this.mRecyclerAdapter = mRecyclerAdapter;
            this.upTo = upTo;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            mMessageItems = new ArrayList<MessageItem>();
            for (Message message : messages) {
                if (getActivity() == null || getActivity().getApplicationContext() == null)
                    return new Object();
                mMessageItems.add(message.toMessageItem(getActivity().getApplicationContext())); // this call is why we need the AsyncTask
            }
            for (int i = 0; i < mMessageItems.size(); i++)
                setFirstOrLast(i, mMessageItems);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o != null)
                return;
            // mRecyclerAdapter.updateMessageItemDataList(mMessageItems);
            if (upTo >= 0)
                mRecyclerAdapter.notifyItemRangeInserted(0, upTo);
            else
                mRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void loadMoreMessages() {
        if (moreMessagesExist)
            mMessages.remove(0);
        List<Message> messages = shouldLoadMoreMessagesListener.shouldLoadMoreMessages();
        int upTo = messages.size();
        if (moreMessagesExist)
            messages.add(0, new SpinnerMessage());
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            mMessages.add(0, message);
        }
        this.replaceMessages(mMessages, upTo);
    }

    private void replaceMessages(List<Message> messages, int upTo) {
        new ReplaceMessagesTask(messages, mRecyclerAdapter, upTo).execute();
    }

    private boolean shouldReloadData() {
        int scrollOffset = mRecyclerView.computeVerticalScrollOffset();
        if (shouldLoadMoreMessagesListener == null)
            return false;
        else if (moreMessagesExist == false)
            return false;
        else
            return scrollOffset < START_RELOADING_DATA_AT_SCROLL_VALUE;
    }

    public void setStyle(int style) {
        TypedArray ta = getActivity().obtainStyledAttributes(style, R.styleable.SlyceMessagingTheme);
        this.customSettings.backgroudColor = ta.getColor(R.styleable.SlyceMessagingTheme_backgroundColor, Color.GRAY);
        rootView.setBackgroundColor(this.customSettings.backgroudColor); // the background color
        this.customSettings.timestampColor = ta.getColor(R.styleable.SlyceMessagingTheme_timestampTextColor, Color.BLACK);
        this.customSettings.externalBubbleTextColor = ta.getColor(R.styleable.SlyceMessagingTheme_externalBubbleTextColor, Color.WHITE);
        this.customSettings.externalBubbleBackgroundColor = ta.getColor(R.styleable.SlyceMessagingTheme_externalBubbleBackground, Color.WHITE);
        this.customSettings.localBubbleBackgroundColor = ta.getColor(R.styleable.SlyceMessagingTheme_localBubbleBackground, Color.WHITE);
        this.customSettings.localBubbleTextColor = ta.getColor(R.styleable.SlyceMessagingTheme_localBubbleTextColor, Color.WHITE);
        this.customSettings.snackbarBackground = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarBackground, Color.WHITE);
        this.customSettings.snackbarButtonColor = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarButtonColor, Color.WHITE);
        this.customSettings.snackbarTitleColor = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarTitleColor, Color.WHITE);
    }

    private void updateAtValue(final int i) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerAdapter.notifyItemChanged(i);
            }
        });
    }

    private void startThreadToUpdateTimestamps() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = startHereWhenUpdate; i < mMessages.size() && i < mMessageItems.size(); i++) {
                        MessageItem messageItem = mMessageItems.get(i);
                        Message message = messageItem.getMessage();
                        if (Utils.dateNeedsUpdated(message.getDate(), messageItem.getDate())) {
                            messageItem.updateDate(message.getDate());
                            updateAtValue(i);
                        } else if (i == startHereWhenUpdate) {
                            i++;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, 0, 62, TimeUnit.SECONDS);
    }

    private boolean isFirst(int i, List<MessageItem> messageItems) {
        return i == 0 ||
                messageItems.get(i - 1).getMessageItemType() == MessageItemType.SPINNER ||
                messageItems.get(i - 1).getMessageSource() != messageItems.get(i).getMessageSource();
    }

    private boolean isLast(int i, List<MessageItem> messageItems) {
        if (messageItems.get(i).getMessageItemType() == MessageItemType.SPINNER)
            return false;
        return i == messageItems.size() - 1 ||
                messageItems.get(i).getMessageSource() != messageItems.get(i + 1).getMessageSource() ||
                messageItems.get(i + 1).getMessage().getDate() - messageItems.get(i).getMessage().getDate() > 1000 * 60 * 60; // one hour
    }

    private void setFirstOrLast(int i, List<MessageItem> messageItems) {
        if (i < 0 || i >= messageItems.size()) // avoid a NullPointerException
            return;
        MessageItem messageItem = messageItems.get(i);
        messageItem.setFirstConsecutiveMessageFromSource(false);
        messageItem.setLastConsecutiveMessageFromSource(false);
        if (isFirst(i, messageItems))
            messageItem.setFirstConsecutiveMessageFromSource(true);
        if (isLast(i, messageItems))
            messageItem.setLastConsecutiveMessageFromSource(true);
    }

    private class AddNewMessageTask extends AsyncTask {
        private List<Message> messages;
        private List<MessageItem> mMessageItems;
        private MessageRecyclerAdapter mRecyclerAdapter;
        private RecyclerView mRecyclerView;

        public AddNewMessageTask(List<Message> messages, List<MessageItem> mMessageItems, MessageRecyclerAdapter mRecyclerAdapter, RecyclerView mRecyclerView) {
            this.messages = messages;
            this.mMessageItems = mMessageItems;
            this.mRecyclerAdapter = mRecyclerAdapter;
            this.mRecyclerView = mRecyclerView;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            int i = mMessageItems.size() - 1;
            for (Message message : messages) {
                if (getActivity() == null || getActivity().getApplicationContext() == null)
                    return new Object();
                mMessageItems.add(message.toMessageItem(getActivity().getApplicationContext())); // this call is why we need the AsyncTask
            }
            for (; i < mMessageItems.size(); i++)
                setFirstOrLast(i, mMessageItems);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o != null)
                return;
            boolean isAtBottom = !mRecyclerView.canScrollVertically(1);
            boolean isAtTop = !mRecyclerView.canScrollVertically(-1);
            mRecyclerAdapter.updateMessageItemDataList(mMessageItems);
            if (isAtBottom)
                mRecyclerView.scrollToPosition(mRecyclerAdapter.getItemCount() - 1);
            else {
                if (isAtTop) {
                    scrollToTopWithDelay();
                }
                Snackbar snackbar = Snackbar.make(mRecyclerView, "New message!", Snackbar.LENGTH_SHORT)
                        .setAction("VIEW", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRecyclerView.smoothScrollToPosition(mRecyclerAdapter.getItemCount() - 1);
                            }
                        }).setActionTextColor(customSettings.snackbarButtonColor);
                TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(customSettings.snackbarTitleColor);
                snackbar.getView().setBackgroundColor(customSettings.snackbarBackground);
                snackbar.show();
            }
        }
    }

    public void addNewMessages(List<Message> messages) {
        mMessages.addAll(messages);
        new AddNewMessageTask(messages, mMessageItems, mRecyclerAdapter, mRecyclerView).execute();
    }

    public void addNewMessage(Message message) {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        addNewMessages(messages);
    }

    public void setOnSendMessageListener(UserSendsMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.scout_image_view_send) {
            sendUserTextMessage();
        } else if (v.getId() == R.id.scout_image_view_snap) {
            mEntryField.setText("");

            final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
            root.mkdirs();
            final String fname = "img_" + System.currentTimeMillis() + ".jpg";
            this.file = new File(root, fname);
            outputFileUri = Uri.fromFile(this.file);
            Intent takePhotoIntent = new CameraActivity.IntentBuilder(getActivity().getApplicationContext())
                    .skipConfirm()
                    // .facing(CameraActivity.Facing.FRONT)
                    .to(this.file)
                    .zoomStyle(ZoomStyle.SEEKBAR)
                    .updateMediaStore()
                    .build();
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhotoIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Take a photo or select one from your device");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePhotoIntent});
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 232);
            try {
                startActivityForResult(chooserIntent, 1);
            } catch (SecurityException e) {
            }
        }
    }

    private File file;
    private Uri outputFileUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 232 || (data == null && this.file.exists())) {
            /*
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
            System.out.println("we should be doing smth now...");
            startActivityForResult(takePhotoIntent, 1);
            */
            return;
        } else
            try {
                if (requestCode == 1) {
                    final boolean isCamera;
                    if (data == null) {
                        isCamera = true;
                    } else {
                        final String action = data.getAction();
                        if (action == null) {
                            isCamera = false;
                        } else {
                            isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }

                    Uri selectedImageUri;
                    if (isCamera && data != null) { // if there is no picture
                        return;
                    }
                    if (isCamera || data == null || data.getData() == null) {
                        selectedImageUri = outputFileUri;
                    } else {
                        selectedImageUri = data == null ? null : data.getData();
                    }
                    MediaMessage message = new MediaMessage();
                    message.setUrl(selectedImageUri.toString());
                    message.setDate(System.currentTimeMillis());
                    message.setDisplayName(this.defaultDisplayName);
                    message.setOrigin(MessageSource.LOCAL_USER);
                    message.setAvatarUrl(this.defaultAvatarUrl);
                    message.setUserId(this.defaultUserId);
                    addNewMessage(message);
                    scrollToBottomWithDelay();
                    if (listener != null)
                        listener.onUserSendsMediaMessage(selectedImageUri);
                }
            } catch (RuntimeException e) {
                System.out.println(e);
            }
    }

    private void sendUserTextMessage() {
        String text = ViewUtils.getStringFromEditText(mEntryField);
        if (TextUtils.isEmpty(text)) {
            return;
        }

        // Clear entry field
        mEntryField.setText("");

        // Build messageData object
        TextMessage message = new TextMessage();

        message.setDate(System.currentTimeMillis());
        message.setAvatarUrl(defaultAvatarUrl);
        message.setOrigin(MessageSource.LOCAL_USER);
        message.setDisplayName(defaultDisplayName);
        message.setText(text);
        message.setUserId(defaultUserId);
        addNewMessage(message);

        scrollToBottomWithDelay();
        if (listener != null)
            listener.onUserSendsTextMessage(message.getText());
    }

    private void scrollToTopWithDelay() {
        scrollToPositionAfterDelay(0);
    }

    private void scrollToBottomWithDelay() {
        scrollToPositionAfterDelay(mRecyclerAdapter.getItemCount() - 1);
    }

    private void scrollToPositionAfterDelay(final int position) {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        scheduleTaskExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                scrollToPosition(position);
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    private void scrollToPosition(int position) {
        int offsetOfScroll = getOffsetOfGoal(position);
        if (offsetOfScroll < SMOOTH_SCROLL_IF_THIS_MUCH)
            mRecyclerView.smoothScrollToPosition(position);
        else
            mRecyclerView.scrollToPosition(position);
    }

    private int getOffsetOfGoal(int position) {
        int scrollPosition = mRecyclerView.computeVerticalScrollOffset();
        int goalPosition;
        if (position == 0) // we are at the top
            goalPosition = 0;
        else if (position == mRecyclerAdapter.getItemCount() - 1)
            goalPosition = mRecyclerView.computeVerticalScrollRange();
        else
            goalPosition = 100000000; // this will cause scrollToPosition() to be called, which we want
        return Math.abs(scrollPosition - goalPosition);
    }
}
