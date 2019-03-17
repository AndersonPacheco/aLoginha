package br.com.alojinha.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.alojinha.R;
import br.com.alojinha.model.Banner;
import br.com.alojinha.model.Retorno;
import br.com.alojinha.util.Constantes;

public class BannerTask extends AsyncTask {

    private ProgressDialog dialog;

    private BannerDelegate delegate;
    private Context context;
    private Servico helper;

    public BannerTask(BannerDelegate delegate, Context context){
        this.delegate = delegate;
        this.context = context;
        this.helper = new Servico();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.dialog = new ProgressDialog(context);
        this.dialog.setMessage("Carregando o Banner");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    @Override
    protected Object doInBackground(Object... params) {
        BannerRes bannerRes = new BannerRes();
        String param = (String) params[0];

        try {
            Retorno retorno = helper.request(Constantes.URL + param + "/" , Constantes.REQUEST_METHOD_GET);
            if(retorno != null) {
                if (retorno.getStatusCode() == HttpURLConnection.HTTP_OK) {

                    JSONObject json = (JSONObject) new JSONTokener(retorno.getStrRetorno()).nextValue();
                    JSONArray jsonArray = json.getJSONArray("data");

                    List<Banner> bannerList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        Banner banner = new Banner();
                        banner.setId(jsonArray.getJSONObject(i).getInt("id"));
                        banner.setLinkUrl(jsonArray.getJSONObject(i).getString("linkUrl"));
                        banner.setUrlImagem(jsonArray.getJSONObject(i).getString("urlImagem"));
                        bannerList.add(banner);
                    }

                    bannerRes.setBanner(bannerList);
                    bannerRes.setStatusCode(retorno.getStatusCode());
                    bannerRes.setMensagem(retorno.getMensagem());

                } else {
                    bannerRes.setStatusCode(retorno.getStatusCode());
                    bannerRes.setMensagem(retorno.getMensagem());
                }
            }else {
                bannerRes.setStatusCode(400);
                bannerRes.setMensagem(Constantes.MSG_PROBLEMA_PESQUISA_IO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bannerRes;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.onBannerResult((BannerRes) o);
        this.dialog.dismiss();
    }

}
