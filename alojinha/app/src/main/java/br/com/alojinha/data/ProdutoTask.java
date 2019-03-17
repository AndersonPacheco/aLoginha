package br.com.alojinha.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.alojinha.model.Categoria;
import br.com.alojinha.model.Produto;
import br.com.alojinha.model.Retorno;
import br.com.alojinha.util.Constantes;

public class ProdutoTask extends AsyncTask {

    private ProgressDialog dialog;

    private ProdutoDelegate delegate;
    private Context context;
    private Servico helper;

    public ProdutoTask(ProdutoDelegate delegate, Context context){
        this.delegate = delegate;
        this.context = context;
        this.helper = new Servico();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.dialog = new ProgressDialog(context);
        this.dialog.setMessage("Carregando");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    @Override
    protected Object doInBackground(Object... params) {
        ProdutoRes produtoRes = new ProdutoRes();
        String param = (String) params[0];
        String metodo = (String) params[1];

        try {
            Retorno retorno = helper.request(Constantes.URL + param + "/" , metodo);
            if(retorno != null) {
                if (retorno.getStatusCode() == HttpURLConnection.HTTP_OK) {

                    JSONObject json = (JSONObject) new JSONTokener(retorno.getStrRetorno()).nextValue();
                    if (metodo.equals(Constantes.REQUEST_METHOD_POST)){
                        produtoRes.setStatusCode(retorno.getStatusCode());
                        produtoRes.setMensagem(retorno.getMensagem());
                    }else {
                        JSONArray jsonArray = json.getJSONArray("data");

                        List<Produto> produtoList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Produto produto = new Produto();
                            produto.setId(jsonArray.getJSONObject(i).getInt("id"));
                            produto.setDescricao(jsonArray.getJSONObject(i).getString("descricao"));
                            produto.setNome(jsonArray.getJSONObject(i).getString("nome"));
                            Categoria categoria = new Categoria();
                            categoria.setId(jsonArray.getJSONObject(i).getJSONObject("categoria").getInt("id"));
                            categoria.setDescricao(jsonArray.getJSONObject(i).getJSONObject("categoria").getString("descricao"));
                            categoria.setUrlImagem(jsonArray.getJSONObject(i).getJSONObject("categoria").getString("urlImagem"));
                            produto.setCategoria(categoria);
                            produto.setPrecoDe(jsonArray.getJSONObject(i).getDouble("precoDe"));
                            produto.setPrecoPor(jsonArray.getJSONObject(i).getDouble("precoPor"));
                            produto.setUrlImagem(jsonArray.getJSONObject(i).getString("urlImagem"));
                            produtoList.add(produto);
                        }

                        produtoRes.setProdutoList(produtoList);
                        produtoRes.setStatusCode(retorno.getStatusCode());
                        produtoRes.setMensagem(retorno.getMensagem());
                    }
                } else {
                    produtoRes.setStatusCode(retorno.getStatusCode());
                    produtoRes.setMensagem(retorno.getMensagem());
                }
            }else {
                produtoRes.setStatusCode(400);
                produtoRes.setMensagem(Constantes.MSG_PROBLEMA_PESQUISA_IO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return produtoRes;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.onProdutoResult((ProdutoRes) o);
        this.dialog.dismiss();
    }
}
