package it.slyce.messaging.utils.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageRecyclerAdapter;
import it.slyce.messaging.utils.MessageUtils;
import it.slyce.messaging.utils.Refresher;

public class ReplaceMessagesTask extends AsyncTask {
    private List<Message> mMessages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private Context context;
    private Refresher mRefresher;
    private int upTo;

    public ReplaceMessagesTask(List<Message> messages, List<MessageItem> messageitems, MessageRecyclerAdapter mRecyclerAdapter, Context context, Refresher refresher, int upTo) {
        this.mMessages = messages;
        this.mRecyclerAdapter = mRecyclerAdapter;
        this.upTo = upTo;
        this.mRefresher = refresher;
        this.mMessageItems = messageitems;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRefresher.setIsRefreshing(true);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        for (int i = mMessageItems.size() - 1; i >= 0; i--) {
            mMessageItems.remove(i);
        }

        for (Message message : mMessages) {
            if (context == null)
                return null;
            mMessageItems.add(message.toMessageItem(context)); // this call is why we need the AsyncTask
        }

        for (int i = 0; i < mMessageItems.size(); i++) {
            MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(i, mMessageItems);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (o != null) {
            return;
        }


        if (upTo >= 0 && upTo < mMessageItems.size()) {
            mRecyclerAdapter.notifyItemRangeInserted(0, upTo);
            mRecyclerAdapter.notifyItemChanged(upTo);
        } else {
            mRecyclerAdapter.notifyDataSetChanged();
        }

        mRefresher.setIsRefreshing(false);
    }
}
