package co.a3tecnology.enghaw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalheActivity extends AppCompatActivity {

    //ira adiar animacao da tela até que tudo esteja pronto
    Target mPicassoTarget;

    public static final String EXTRA_DISCO = "disco";

    @BindView(R.id.fabFavorito)
    FloatingActionButton mFabFavorito;

    @BindView(R.id.imgCapa)
    ImageView mImgCapa;

    @BindView(R.id.txtTitulo)
    TextView mTxtTitulo;

    @BindView(R.id.txtAno)
    TextView mTxtAno;

    @BindView(R.id.txtGravadora)
    TextView mTxtGravadora;

    @BindView(R.id.txtFormacao)
    TextView mTxtFormacao;

    @BindView(R.id.txtMusicas)
    TextView mTxtMusicas;

    @Nullable
    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinator;

    @Nullable
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;

    @Nullable
    @BindView(R.id.collapseToolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    DiscoDb mDiscoDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        ButterKnife.bind(this);

        Disco disco = (Disco) getIntent().getSerializableExtra(EXTRA_DISCO);
        preencherCampos(disco);

        confirgurarBarraDeTitulo(disco.titulo);

        carregarCapa(disco);
        configuracaoAnimacaoEntrada();

        mDiscoDb = new DiscoDb(this);
        configurarFab(disco);
    }

    //configuração do floatingButton
    private void configurarFab(final Disco disco) {
        boolean favorito = mDiscoDb.favorito(disco);
        mFabFavorito.setImageDrawable(getFabIcone(favorito));
        mFabFavorito.setBackgroundTintList(getFabBackground(favorito));

        mFabFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean favorito = mDiscoDb.favorito(disco);
                if (favorito) {
                    mDiscoDb.excluir(disco);
                } else {
                    mDiscoDb.inserir(disco);
                }
                mFabFavorito.setImageDrawable(getFabIcone(!favorito));
                mFabFavorito.setBackgroundTintList(getFabBackground(!favorito));

                //animacao do botao FAB
                animar(!favorito);

                //evento de click do OTTO que será disparado ao chamar a class DiscoEVENTO
                ((DiscoApp) getApplication()).getBus().post(new DiscoEvento(disco));
            }
        });
    }

    //icone do button
    private Drawable getFabIcone(boolean favorito) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ResourcesCompat.getDrawable(
                    getResources(),
                    favorito ? R.drawable.ic_cancel_anim : R.drawable.ic_check_anim,
                    getTheme());
        } else {
            return getResources().getDrawable(
                    favorito ? R.drawable.ic_baseline_cancel_24 :
                    R.drawable.ic_baseline_check_24);

        }
    }

    //animacao do btn FAB
    private void animar(boolean favorito) {
        mFabFavorito.setBackgroundTintList(getFabBackground(favorito));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable)
                    getFabIcone(!favorito);
            mFabFavorito.setImageDrawable(avd);
            avd.start();
        } else {
            mFabFavorito.setImageDrawable(getFabIcone(favorito));
        }
    }

    //cores do icones
    private ColorStateList getFabBackground(boolean favorito) {
        return getResources().getColorStateList(favorito
                ? R.color.bg_fab_cancel :
                R.color.bg_fab_favorito);
    }

    @Override
    public void onBackPressed() {
        mFabFavorito.animate().scaleX(0).scaleY(0).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        DetalheActivity.super.onBackPressed();
                    }
                }).start();
    }

    //carrega a capa do Disco de cada cd através do ID
    private void carregarCapa(Disco disco) {
        if (mPicassoTarget == null) {
            mPicassoTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mImgCapa.setImageBitmap(bitmap);
                    iniciarAnimacaoDeEntrada(mCoordinator);

                    //ira mudar a cor da tela conforme a cor da capa
                    definirCores(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    iniciarAnimacaoDeEntrada(mCoordinator);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
        }
        Picasso.with(this)
                .load(DiscoHttp.BASE_URL + disco.capaGrande)
                .into(mPicassoTarget);
    }

    private void definirCores(Bitmap bitmap) {

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(Color.BLACK);
                int darkVibrantColor = palette.getDarkVibrantColor(Color.BLACK);
                int darkMutedColor = palette.getDarkMutedColor(Color.BLACK);
                int ligthMuteColor = palette.getLightMutedColor(Color.WHITE);

                mTxtTitulo.setTextColor(vibrantColor);
                if (mAppBar != null) {
                    mAppBar.setBackgroundColor(vibrantColor);
                } else {
                    mToolbar.setBackgroundColor(Color.TRANSPARENT);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(darkMutedColor);
                }

                if (mCollapsingToolbarLayout != null) {
                    mCollapsingToolbarLayout.setContentScrimColor(darkVibrantColor);
                }

                mCoordinator.setBackgroundColor(ligthMuteColor);
                iniciarAnimacaoDeEntrada(mCoordinator);
            }
        });
    }


    private void iniciarAnimacaoDeEntrada(final View shareElement) {
        shareElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        shareElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        ActivityCompat.startPostponedEnterTransition(DetalheActivity.this);
                        return true;
                    }
                });

    }

    private void preencherCampos(Disco disco) {

        mTxtTitulo.setText(disco.titulo);
        mTxtAno.setText(String.valueOf(disco.ano));
        mTxtGravadora.setText(disco.gravadora);

        StringBuilder sb = new StringBuilder();

        for (String integrante : disco.formacao) {
            if (sb.length() != 0) sb.append("\n");
            sb.append(integrante);
        }

        mTxtFormacao.setText(sb.toString());
        sb = new StringBuilder();
        for (int i = 0; i < disco.faixas.length; i++) {
            if (sb.length() != 0) sb.append('\n');
            sb.append(i + 1).append(". ").append(disco.faixas[i]);
        }

        mTxtMusicas.setText(sb.toString());
    }

    private void confirgurarBarraDeTitulo(String titulo) {
        setSupportActionBar(mToolbar);

        if (mAppBar != null) {
            if (mAppBar.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)
                        mAppBar.getLayoutParams();
                lp.height = getResources().getDisplayMetrics().widthPixels;
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mCollapsingToolbarLayout != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            mCollapsingToolbarLayout.setTitle(titulo);
        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
            return super.onOptionsItemSelected(item);
    }

    //animacao entrada
    private void configuracaoAnimacaoEntrada() {
        //transicao das activitys(animacao)
        ViewCompat.setTransitionName(mImgCapa, "capa");
        ViewCompat.setTransitionName(mTxtTitulo, "titulo");
        ViewCompat.setTransitionName(mTxtAno, "ano");

        //ira impedir que a animacao de entrada seja realizada
        // ate que o metodo startPostponedEnterTransition seja chamado
        ActivityCompat.postponeEnterTransition(this);
    }
}



















