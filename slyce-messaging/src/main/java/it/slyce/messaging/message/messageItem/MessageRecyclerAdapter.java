package it.slyce.messaging.message.messageItem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import it.slyce.messaging.BuildConfig;
import it.slyce.messaging.R;
import it.slyce.messaging.message.messageItem.user.media.MessageUserMediaViewHolder;
import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.message.messageItem.scout.media.MessageScoutMediaViewHolder;
import it.slyce.messaging.message.messageItem.scout.text.MessageScoutTextViewHolder;
import it.slyce.messaging.message.messageItem.spinner.SpinnerViewHolder;
import it.slyce.messaging.message.messageItem.user.text.MessageUserTextViewHolder;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> implements OnClickListener {

    private static final String TAG = MessageRecyclerAdapter.class.getName();

    private List<MessageItem> mMessageItems;
    private WeakReference<Picasso> mPicassoRef;

    private CustomSettings customSettings;

    public MessageRecyclerAdapter(List<MessageItem> messageItems, Picasso picasso, CustomSettings customSettings) {
        mMessageItems = messageItems;
        mPicassoRef = new WeakReference<Picasso>(picasso);
        this.customSettings = customSettings;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MessageViewHolder viewHolder = null;

        MessageItemType messageItemType = MessageItemType.values()[viewType];
        switch (messageItemType) {


            case SCOUT_MEDIA:
                View scoutMediaView = inflater.inflate(R.layout.item_message_external_media, parent, false);
                viewHolder = new MessageScoutMediaViewHolder(scoutMediaView, customSettings);
                break;

            case SCOUT_TEXT:
                View scoutTextView = inflater.inflate(R.layout.item_message_external_text, parent, false);
                viewHolder = new MessageScoutTextViewHolder(scoutTextView, customSettings);
                break;

            case USER_MEDIA:
                View userMediaView = inflater.inflate(R.layout.item_message_user_media, parent, false);
                viewHolder = new MessageUserMediaViewHolder(userMediaView, customSettings);
                break;

            case USER_TEXT:
                View userTextView = inflater.inflate(R.layout.item_message_user_text, parent, false);
                viewHolder = new MessageUserTextViewHolder(userTextView, customSettings);
                break;

            case SPINNER:
                View spinnerView = inflater.inflate(R.layout.item_spinner, parent, false);
                viewHolder = new SpinnerViewHolder(spinnerView, customSettings);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int position) {
        if (messageViewHolder == null) {
            return;
        }

        // Build the item
        Picasso picasso = mPicassoRef.get();
        MessageItem messageItem = getMessageItemByPosition(position);
        if (messageItem != null) {
            messageItem.buildMessageItem(
                    messageViewHolder,
                    picasso,
                    this
            );
        }
    }

    @Override
    public int getItemCount() {
        return mMessageItems != null ? mMessageItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        // Get the item type
        Integer itemType = getMessageItemType(position);
        if (itemType != null) {
            return itemType;
        }

        return super.getItemViewType(position);
    }

    @Override
    public void onClick(View v) {

        MessageItem messageItem = null;
        MessageItemClickType messageItemClickType = null;

        // Get the item
        Object item = v.getTag(R.id.key_message_item);
        if (item != null && item instanceof MessageItem) {
            messageItem = (MessageItem) item;
        }

        // Get the click type
        Object clickType = v.getTag(R.id.key_message_item_click_type);
        if (clickType != null && clickType instanceof MessageItemClickType) {
            messageItemClickType = (MessageItemClickType) clickType;
        }

        // Forward the item and click type to the activity
        if (messageItem != null && messageItemClickType != null) {
        } else {
        }
    }

    private MessageItem getMessageItemByPosition(int position) {
        if (mMessageItems != null && !mMessageItems.isEmpty()) {
            if (position >= 0 && position < mMessageItems.size()) {
                MessageItem messageItem = mMessageItems.get(position);
                if (messageItem != null) {
                    return messageItem;
                }
            }
        }

        return null;
    }

    private Integer getMessageItemType(int position) {
        MessageItem messageItem = getMessageItemByPosition(position);
        if (messageItem != null) {
            return messageItem.getMessageItemTypeOrdinal();
        }

        return null;
    }

    public void updateMessageItemDataList(List<MessageItem> messageItems) {
        mMessageItems = messageItems;
        notifyDataSetChanged();
    }
}
