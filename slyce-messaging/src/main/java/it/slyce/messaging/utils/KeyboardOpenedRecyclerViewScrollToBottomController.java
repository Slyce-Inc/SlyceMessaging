package it.slyce.messaging.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

/**
 * Created by pr on 31.01.2017.
 */

public class KeyboardOpenedRecyclerViewScrollToBottomController implements ViewTreeObserver.OnGlobalLayoutListener {
    private RecyclerView recyclerView;
    boolean isScrollingToBottom;

    public KeyboardOpenedRecyclerViewScrollToBottomController(RecyclerView recyclerView){

        this.recyclerView = recyclerView;
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        recyclerView.getWindowVisibleDisplayFrame(r);
        int screenHeight = recyclerView.getRootView().getHeight();

        // r.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        int keypadHeight = screenHeight - r.bottom;


        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
            // keyboard is opened
            if (!isScrollingToBottom) {
                isScrollingToBottom = true;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                int adapterItemCount = adapter.getItemCount();
                if (adapter != null && adapterItemCount > 0) {
                    recyclerView.scrollToPosition(adapterItemCount - 1);
                }
            }
        }
        else {
            isScrollingToBottom=false;
        }
    }
}
