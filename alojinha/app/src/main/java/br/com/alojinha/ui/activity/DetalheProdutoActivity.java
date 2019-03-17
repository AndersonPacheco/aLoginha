package br.com.alojinha.ui.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;

import java.net.HttpURLConnection;
import java.text.NumberFormat;

import br.com.alojinha.R;
import br.com.alojinha.data.ProdutoDelegate;
import br.com.alojinha.data.ProdutoRes;
import br.com.alojinha.data.ProdutoTask;
import br.com.alojinha.model.Produto;
import br.com.alojinha.databinding.ActivityDetalheProdutoBinding;
import br.com.alojinha.util.Alert;
import br.com.alojinha.util.Constantes;
import br.com.alojinha.util.Utils;

public class DetalheProdutoActivity extends AppCompatActivity implements ProdutoDelegate {

    private ActivityDetalheProdutoBinding binding;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detalhe_produto);

        setToolbar();
        binding.fab.setEnabled(true);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fab.setEnabled(false);
                doProduto();
            }
        });

        addObservable();
        carregarProduto();
        produto = new Produto();
        produto = getProdutoSelecionado();
        setTitle(produto.getCategoria().getDescricao());
    }

    public void doProduto(){
        new ProdutoTask(this, this).execute("produto/"+produto.getId(), Constantes.REQUEST_METHOD_POST);
    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
                finish();
        }
    };

    private void setToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addObservable() {

    }

    private Produto getProdutoSelecionado() {
        Bundle bundle = getIntent().getExtras();
        return (Produto) bundle.getSerializable(getString(R.string.param_produto));
    }

    private void carregarProduto(){
        Produto produto = getProdutoSelecionado();
        if(produto != null){
            Glide
                    .with(this)
                    .load(produto.getUrlImagem())
                    .placeholder(R.drawable.loading)
                    .error(R.mipmap.ic_nao_disponivel)
                    .into(binding.contentDetalheProduto.ivProduto);

            NumberFormat  defaultFormat = NumberFormat.getInstance(Utils.getCurrentLocale(DetalheProdutoActivity.this));

            binding.contentDetalheProduto.tvDescricao.setText(produto.getNome());
            binding.contentDetalheProduto.tvPrecoDe.setText("De " + defaultFormat.format(produto.getPrecoDe()));
            binding.contentDetalheProduto.tvPrecoDe.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            binding.contentDetalheProduto.tvPrecoPor.setText("Por " + defaultFormat.format(produto.getPrecoPor()));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.contentDetalheProduto.tvDescricaoCompleta.setText(
                        Html.fromHtml(produto.getDescricao(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                binding.contentDetalheProduto.tvDescricaoCompleta.setText(Html.fromHtml(produto.getDescricao()));
            }
        }
    }

    @Override
    public void onProdutoResult(ProdutoRes produtoRes) {
        if(produtoRes != null) {
            if (produtoRes.getStatusCode() == HttpURLConnection.HTTP_OK) {
                if (produtoRes.getMensagem().isEmpty()) {
                    Alert.showSimpleDialog(getString(R.string.msg_sucesso_reserva_produto), DetalheProdutoActivity.this, null, listener);
                }else {
                    Alert.showSimpleDialog(produtoRes.getMensagem(), DetalheProdutoActivity.this, null, listener);
                }
            }else {
                Alert.showSimpleDialog(getString(R.string.msg_erro_produto_por_categoria), DetalheProdutoActivity.this, null, listener);
            }
        }
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
}
