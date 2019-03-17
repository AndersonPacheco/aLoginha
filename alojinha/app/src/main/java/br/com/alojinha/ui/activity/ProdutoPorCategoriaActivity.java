package br.com.alojinha.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import java.net.HttpURLConnection;

import br.com.alojinha.R;
import br.com.alojinha.data.ProdutoDelegate;
import br.com.alojinha.data.ProdutoRes;
import br.com.alojinha.data.ProdutoTask;
import br.com.alojinha.databinding.ActivityProdutoPorCategoriaBinding;
import br.com.alojinha.model.Categoria;
import br.com.alojinha.model.Produto;
import br.com.alojinha.ui.adapter.ProdutoAdapter;
import br.com.alojinha.util.Alert;
import br.com.alojinha.util.Constantes;

public class ProdutoPorCategoriaActivity extends AppCompatActivity implements ProdutoAdapter.OnItemClickListener, ProdutoDelegate {

    private ActivityProdutoPorCategoriaBinding binding;

    private ProdutoAdapter produtoAdapter;

    private LinearLayoutManager linearLayoutManager;

    private boolean isScrolling = false;
    private int currentItems = 0;
    private int totalItems = 0;
    private int scrollOutItems = 0;

    private Categoria categoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_produto_por_categoria);

        setToolbar();
        categoria = new Categoria();
        categoria = getCategoriaSelecionada();
        setTitle(categoria.getDescricao());

        binding.rvProdutos.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.rvProdutos.setLayoutManager(linearLayoutManager);

        addOnScrollListener();
        addObservable();

        doProduto();

    }

    private void setToolbar() {
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Categoria getCategoriaSelecionada() {
        Bundle bundle = getIntent().getExtras();
        return (Categoria) bundle.getSerializable(getString(R.string.param_categoria));
    }

    @Override
    public void onClickProduto(Produto item) {
        Intent intent = new Intent(this, DetalheProdutoActivity.class);
        intent.putExtra(getString(R.string.param_produto), item);
        startActivity(intent);
    }

    private void addOnScrollListener(){
        binding.rvProdutos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false;

                    if (dy > 0) {
                        // Scrolling up
                        Produto produto = produtoAdapter.getItemByPosition(totalItems - 1);

                        binding.pbLoading.setVisibility(View.VISIBLE);
                        doProdutoId(produto.getId());
                    } else {
                        // Scrolling down
                    }
                }
            }
        });
    }

    private void addObservable() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doProdutoId(int id){
        new ProdutoTask(this, this).execute("produto/"+id, Constantes.REQUEST_METHOD_GET);
    }

    public void doProduto(){
        new ProdutoTask(this, this).execute("produto/maisvendidos", Constantes.REQUEST_METHOD_GET);
    }

    @Override
    public void onProdutoResult(ProdutoRes produtoRes) {
        boolean lRet = true;
        if(produtoRes != null) {
            if (produtoRes.getStatusCode() == HttpURLConnection.HTTP_OK) {
                for (int i = 0; i < produtoRes.getProdutoList().size(); i++) {
                    if (produtoRes.getProdutoList().get(i).getCategoria().getId() == categoria.getId()) {
                        produtoAdapter = new ProdutoAdapter(this, this);
                        produtoAdapter.setData(produtoRes.getProdutoList());
                        binding.rvProdutos.setAdapter(produtoAdapter);
                        lRet = false;
                    }
                }
                if (lRet){
                    Alert.showSimpleDialog("Nenum item encontrado",this, null, null);
                }
            }
        }
    }
}
