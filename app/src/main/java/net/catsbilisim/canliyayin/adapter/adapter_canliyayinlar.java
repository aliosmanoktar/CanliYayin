package net.catsbilisim.canliyayin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.catsbilisim.canliyayin.DataBase.InstagramUser;
import net.catsbilisim.canliyayin.DataBase.PeriscopeUser;
import net.catsbilisim.canliyayin.DataBase.YoutubeUser;
import net.catsbilisim.canliyayin.R;
import java.util.List;

public class adapter_canliyayinlar extends RecyclerView.Adapter<adapter_canliyayinlar.ViewHolder>   {
    private RecyclerView recyclerView;
    List<Object> sosyalMedya;

    public adapter_canliyayinlar(RecyclerView recyclerView, List<Object> sosyalMedya) {
        this.recyclerView = recyclerView;
        this.sosyalMedya = sosyalMedya;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sosyalmedya,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Object item=sosyalMedya.get(i);
        if (item instanceof YoutubeUser){
            YoutubeUser user =(YoutubeUser)item;
            viewHolder.txtName.setText(user.getName());
            viewHolder.txtLink.setText("Youtube");

        }else if (item instanceof InstagramUser){
            InstagramUser user =(InstagramUser)item;
            viewHolder.txtName.setText(user.getName());
            viewHolder.txtLink.setText("Instagram");

        }else if (item instanceof PeriscopeUser){
            PeriscopeUser user = (PeriscopeUser)item;
            viewHolder.txtName.setText(user.getUserName());
            viewHolder.txtLink.setText("Periscope");

        }
    }

    public Object getItem(int index){
        return sosyalMedya.get(index);
    }

    public void removeItem(int index){
        sosyalMedya.remove(index);
        notifyItemRemoved(index);
    }

    public void restoreItem(Object data,int index){
        sosyalMedya.add(index,data);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return sosyalMedya.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtName,txtLink;
        public RelativeLayout bck_edit,bck_delete,frg_view;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.item_sosyalMedya_Name);
            txtLink = itemView.findViewById(R.id.item_sosyalMedya_Link);
            bck_edit=itemView.findViewById(R.id.bck_edit);
            bck_delete=itemView.findViewById(R.id.bck_delete);
            frg_view=itemView.findViewById(R.id.frg_view);
        }
        @Override
        public void onClick(View view) {

        }

    }
}
