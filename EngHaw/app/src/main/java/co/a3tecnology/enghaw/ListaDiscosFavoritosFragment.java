package co.a3tecnology.enghaw;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//essa classe não realizar consulta na web
//mas do banco do smartphone
public class ListaDiscosFavoritosFragment extends Fragment
        implements DiscoAdapter.AoClicarNoDiscoListener{

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipe;

    @BindView(R.id.recyclerView)
    RecyclerView mRecycleView;

    DiscoDb mDiscoDb;
    List<Disco> mDiscos;

    Bus mBus;

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mBus = ((DiscoApp)getActivity().getApplication()).getBus();
        mBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.lista_discos, container, false);
        unbinder = ButterKnife.bind(this, v);

        mSwipe.setEnabled(false); // desabilitou o refresh,pois, está in loco
        mRecycleView.setTag("fav");
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        //configuracao de layout
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {

            //orientacao vertical
            mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {

            //orientacao horizontal
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        return v;
    }

    //destroi a view(activity)
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mBus.unregister(this);
        mBus = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanteState) {
        super.onActivityCreated(savedInstanteState);
        mDiscoDb = new DiscoDb(getActivity());

        if (mDiscos == null) {
            mDiscos = mDiscoDb.getDiscos();
        }

        atualizarLista();
    }

    //atualiza a lista
    private void atualizarLista() {
        Disco[] array = new Disco[mDiscos.size()];
        mDiscos.toArray(array);
        DiscoAdapter adapter = new DiscoAdapter(getActivity(), array);
        adapter.setAoClicarNoDiscoListener(this);
        mRecycleView.setAdapter(adapter);
    }

    @Subscribe
    public void atualizarLista(DiscoEvento event){
        mDiscos = mDiscoDb.getDiscos();
        atualizarLista();
    }

    @Override
    public void aoClicarNoDisco(View v, int position, Disco disco) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                Pair.create(v.findViewById(R.id.imgCapa), "capa"),
                Pair.create(v.findViewById(R.id.txtTitulo), "titulo"),
                Pair.create(v.findViewById(R.id.txtAno), "ano")
        );
        Intent it = new Intent(getActivity(), DetalheActivity.class);
        it.putExtra(DetalheActivity.EXTRA_DISCO, disco);
        ActivityCompat.startActivity(getActivity(), it, options.toBundle());
    }
}



























































