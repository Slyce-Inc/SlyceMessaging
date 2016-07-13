package it.slyce.messaging.utils.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageRecyclerAdapter;
import it.slyce.messaging.utils.MessageUtils;
import it.slyce.messaging.utils.Refresher;

/**
 * @Auther Matthew Page
 * @Date 7/13/16
 */
public class ReplaceMessagesTask extends AsyncTask {
    private List<Message> mMessages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private Context context;
    private Refresher mRefresher;
    private int upTo;

    public ReplaceMessagesTask(List<Message> messages, MessageRecyclerAdapter mRecyclerAdapter, int upTo, Refresher refresher, List<MessageItem> messageitems, Context context) {
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
        Log.d("debug", "mIsRefreshing = true");
        mRefresher.setIsRefreshing(true);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        for (int i = 0; i < mMessageItems.size(); i++)
            mMessageItems.remove(i);
        for (Message message : mMessages) {
            if (context == null)
                return new Object();
            mMessageItems.add(message.toMessageItem(context)); // this call is why we need the AsyncTask
        }
        for (int i = 0; i < mMessageItems.size(); i++)
            MessageUtils.setFirstOrLast(i, mMessageItems);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o != null)
            return;
        mRecyclerAdapter.updateMessageItemDataList(mMessageItems);
        if (upTo >= 0)
            mRecyclerAdapter.notifyItemRangeInserted(0, upTo);
        else
            mRecyclerAdapter.notifyDataSetChanged();
        Log.d("debug", "mIsRefreshing = false");
        Log.d("debug", mMessages.size() + "," + mMessageItems.size() + "," + mRecyclerAdapter.getItemCount());
        mRefresher.setIsRefreshing(false);
    }
}