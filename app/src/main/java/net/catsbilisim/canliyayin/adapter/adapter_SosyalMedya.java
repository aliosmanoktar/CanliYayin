package net.catsbilisim.canliyayin.adapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.UserRequestMessage;
import net.catsbilisim.canliyayin.Preferences.SosyalMedya;
import net.catsbilisim.canliyayin.Preferences.SosyayalMedyaCallBack;
import net.catsbilisim.canliyayin.R;

import java.util.List;

public class adapter_SosyalMedya extends RecyclerView.Adapter<adapter_SosyalMedya.ViewHolder> {
    private RecyclerView recyclerView;
    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;
    private List<SosyalMedya> medyalar;
    SosyayalMedyaCallBack callBack;
    UserRequestMessage requestMessage;
    public adapter_SosyalMedya(List<SosyalMedya> medyas, RecyclerView recyclerView,SosyayalMedyaCallBack callBack){
        this.medyalar=medyas;
        this.recyclerView=recyclerView;
        this.callBack=callBack;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_sosyalmedya,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final SosyalMedya item = medyalar.get(i);
        viewHolder.txtName.setText(item.getName());
        viewHolder.txtLink.setText(item.getLink());
        viewHolder.sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.Sil(item.getId(),i);
            }
        });
        viewHolder.Duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.Duzenle(item.getId(),i,item.getName(),item.getLink());
            }
        });
    }
    @Override
    public int getItemCount() {
        return medyalar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout expandButton;
        private ImageView imageView;
        private TextView txtName;
        private TextView txtLink;
        private Button sil;
        private Button Duzenle;
        public ViewHolder(View itemView) {
            super(itemView);
            expandButton = itemView.findViewById(R.id.expand_button);
            txtName = itemView.findViewById(R.id.item_sosyalMedya_Name);
            txtLink = itemView.findViewById(R.id.item_sosyalMedya_Link);
            imageView = itemView.findViewById(R.id.img_arrow);
            expandButton.setOnClickListener(this);
            sil=itemView.findViewById(R.id.btn_sosyal_item_sil);
            Duzenle = itemView.findViewById(R.id.btn_sosyal_item_duzenle);
        }
        @Override
        public void onClick(View view) {
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.imageView.setImageResource(R.drawable.arrow_down);
                holder.expandButton.setSelected(false);
            }

            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                imageView.setImageResource(R.drawable.arrow_up);
                expandButton.setSelected(true);
                selectedItem = position;
            }
        }
    }
}
