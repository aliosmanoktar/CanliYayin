package net.catsbilisim.canliyayin.adapter.Helper;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import net.catsbilisim.canliyayin.adapter.adapter_canliyayinlar;

public class ReyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ReyclerItemTouchHelperListener listener;

    public ReyclerItemTouchHelper(int dragDirs,int swipeDir,ReyclerItemTouchHelperListener listener) {
        super(dragDirs,swipeDir);
        this.listener=listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        if (listener!=null)
            listener.onSwiped(viewHolder,i,viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View frg=((adapter_canliyayinlar.ViewHolder)viewHolder).frg_view;
        getDefaultUIUtil().clearView(frg);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View frg=((adapter_canliyayinlar.ViewHolder)viewHolder).frg_view;
        getDefaultUIUtil().onDraw(c,recyclerView,frg,dX/4,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View frg=((adapter_canliyayinlar.ViewHolder)viewHolder).frg_view;
        getDefaultUIUtil().onDrawOver(c,recyclerView,frg,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder!=null)
        {
            View frg=((adapter_canliyayinlar.ViewHolder)viewHolder).frg_view;
            getDefaultUIUtil().onSelected(frg);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

}