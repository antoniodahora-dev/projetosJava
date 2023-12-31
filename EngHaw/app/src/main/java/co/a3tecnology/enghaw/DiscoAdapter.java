package co.a3tecnology.enghaw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoAdapter extends RecyclerView.Adapter<DiscoAdapter.DiscoViewHolder>{

    private Context mContext;
    private Disco[] mDiscos;
    private AoClicarNoDiscoListener mListener;


    public DiscoAdapter(Context ctx, Disco[] discos) {
        mContext = ctx;
        mDiscos = discos;
    }

    //evento de click
    public void setAoClicarNoDiscoListener(
            AoClicarNoDiscoListener l) {
        mListener = l;
    }

    @Override
    public int getItemCount() {
        return mDiscos != null ? mDiscos.length : 0;
    }

    @Override
    public DiscoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_disco, parent, false);
        DiscoViewHolder vh = new DiscoViewHolder(v);
        v.setTag(vh);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListener != null) {
                    DiscoViewHolder vh = (DiscoViewHolder)view.getTag();
                    int position = vh.getAdapterPosition();
                    mListener.aoClicarNoDisco(view, position, mDiscos[position]);
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(DiscoAdapter.DiscoViewHolder holder, int position) {
        Disco disco = mDiscos[position];
        Picasso.with(mContext).load(DiscoHttp.BASE_URL + disco.capa).into(holder.imgCapa);
        holder.txtTitulo.setText(disco.titulo);
        holder.txtAno.setText(String.valueOf(disco.ano));
    }

    public interface AoClicarNoDiscoListener {
        void aoClicarNoDisco(View v, int position, Disco disco);
    }




    public static class DiscoViewHolder extends  RecyclerView.ViewHolder {

        @BindView(R.id.imgCapa)
        ImageView imgCapa;

        @BindView(R.id.txtTitulo)
         TextView txtTitulo;

        @BindView(R.id.txtAno)
        TextView txtAno;

        public DiscoViewHolder(View parent) {
            super(parent);
            ButterKnife.bind(this, parent);

            //transicao das activitys(animacao)
            ViewCompat.setTransitionName(imgCapa, "capa");
            ViewCompat.setTransitionName(txtTitulo, "titulo");
            ViewCompat.setTransitionName(txtAno, "ano");
        }

    }
}

























