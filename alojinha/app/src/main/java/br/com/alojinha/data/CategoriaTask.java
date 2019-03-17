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
import br.com.alojinha.model.Retorno;
import br.com.alojinha.util.Constantes;

public class CategoriaTask extends AsyncTask {

    private ProgressDialog dialog;

    private CategoriaDelegate delegate;
    private Context context;
    private Servico helper;

    public CategoriaTask(CategoriaDelegate delegate, Context context){
        this.delegate = delegate;
        this.context = context;
        this.helper = new Servico();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.dialog = new ProgressDialog(context);
        this.dialog.setMessage("Carregando o Categoria");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    @Override
    protected Object doInBackground(Object... params) {
        CategoriaRes categoriaRes = new CategoriaRes();
        String param = (String) params[0];

        try {
            Retorno retorno = helper.request(Constantes.URL + param + "/" , Constantes.REQUEST_METHOD_GET);
            if(retorno != null) {
                if (retorno.getStatusCode() == HttpURLConnection.HTTP_OK) {

                    JSONObject json = (JSONObject) new JSONTokener(retorno.getStrRetorno()).nextValue();
                    JSONArray jsonArray = json.getJSONArray("data");

                    List<Categoria> categoriaList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        Categoria categoria = new Categoria();
                        categoria.setId(jsonArray.getJSONObject(i).getInt("id"));
                        categoria.setDescricao(jsonArray.getJSONObject(i).getString("descricao"));
                        categoria.setUrlImagem(jsonArray.getJSONObject(i).getString("urlImagem"));
                        categoriaList.add(categoria);
                    }

                    categoriaRes.setCategoriaList(categoriaList);
                    categoriaRes.setStatusCode(retorno.getStatusCode());
                    categoriaRes.setMensagem(retorno.getMensagem());

                } else {
                    categoriaRes.setStatusCode(retorno.getStatusCode());
                    categoriaRes.setMensagem(retorno.getMensagem());
                }
            }else {
                categoriaRes.setStatusCode(400);
                categoriaRes.setMensagem(Constantes.MSG_PROBLEMA_PESQUISA_IO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoriaRes;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.onCategoriaResult((CategoriaRes) o);
        this.dialog.dismiss();
    }
}
