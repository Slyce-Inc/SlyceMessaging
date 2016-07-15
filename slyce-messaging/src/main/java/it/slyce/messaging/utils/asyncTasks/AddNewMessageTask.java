package it.slyce.messaging.utils.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import it.slyce.messaging.SlyceMessagingFragment;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageRecyclerAdapter;
import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.utils.MessageUtils;
import it.slyce.messaging.utils.ScrollUtils;

/**
 * @Author Matthew Page
 * @Date 7/13/16
 */
public class AddNewMessageTask extends AsyncTask {
    private List<Message> messages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private Context context;
    private CustomSettings customSettings;

    public AddNewMessageTask(
            List<Message> messages,
            List<MessageItem> mMessageItems,
            MessageRecyclerAdapter mRecyclerAdapter,
            RecyclerView mRecyclerView,
            Context context,
            CustomSettings customSettings) {
        this.messages = messages;
        this.mMessageItems = mMessageItems;
        this.mRecyclerAdapter = mRecyclerAdapter;
        this.mRecyclerView = mRecyclerView;
        this.context = context;
        this.customSettings = customSettings;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        int i = mMessageItems.size() - 1;
        for (Message message : messages) {
            if (context == null)
                return new Object();
            mMessageItems.add(message.toMessageItem(context)); // this call is why we need the AsyncTask
        }
        for (; i < mMessageItems.size(); i++)
            MessageUtils.setFirstOrLast(i, mMessageItems);
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
                ScrollUtils.scrollToTopWithDelay(mRecyclerView, mRecyclerAdapter);
            }
            Snackbar snackbar = Snackbar.make(mRecyclerView, "New message!", Snackbar.LENGTH_SHORT)
                    .setAction("VIEW", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRecyclerView.smoothScrollToPosition(mRecyclerAdapter.getItemCount() - 1);
                        }
                    }).setActionTextColor(customSettings.snackbarButtonColor);
            //TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            //textView.setTextColor(customSettings.snackbarTitleColor);
            //snackbar.getView().setBackgroundColor(customSettings.snackbarBackground);
            snackbar.show();
        }
    }
}
