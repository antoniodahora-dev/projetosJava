package co.a3tecnology.enghaw;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ListaDiscosWebFragment extends Fragment
        implements  DiscoAdapter.AoClicarNoDiscoListener {

    private Unbinder unbinder;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipe;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    Disco[] mDiscos;
    DiscosDownloadTask mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lista_discos, container, false);
        unbinder = ButterKnife.bind(this, v);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTask = new DiscosDownloadTask();
                mTask.execute();
            }
        });
        mRecyclerView.setTag("web");
        mRecyclerView.setHasFixedSize(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mDiscos == null) {
            if (mTask == null) {
                mTask = new DiscosDownloadTask();
                mTask.execute();

            } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
                exibirProgresso();
            }
        } else {
            atualizarLista();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTask != null) mTask.cancel(true);
    }

    @Override
    public void aoClicarNoDisco(View v, int position, Disco disco) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        Pair.create(v.findViewById(R.id.imgCapa), "capa"),
                        Pair.create(v.findViewById(R.id.txtTitulo), "titulo"),
                        Pair.create(v.findViewById(R.id.txtAno), "ano")
                        );

        Intent it = new Intent(getActivity(), DetalheActivity.class);
        it.putExtra(DetalheActivity.EXTRA_DISCO, disco);
        ActivityCompat.startActivity(getActivity(), it, options.toBundle());
    }

    private void atualizarLista() {
        DiscoAdapter adapter = new DiscoAdapter(getActivity(), mDiscos);
        adapter.setAoClicarNoDiscoListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    private void exibirProgresso() {
        mSwipe.post(new Runnable() {

            @Override
            public void run() {
                mSwipe.setRefreshing(true);
            }
        });
    }

    class DiscosDownloadTask extends AsyncTask<Void, Void, Disco[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgresso();
        }

        @Override
        protected Disco[] doInBackground(Void... params) {
            return DiscoHttp.obterDiscosDoServidor();
        }

        @Override
        protected void onPostExecute(Disco[] discos) {
            super.onPostExecute(discos);
            mSwipe.setRefreshing(false);

            if (discos != null) {
                mDiscos = discos;
                atualizarLista();
            }
        }
    }
}
