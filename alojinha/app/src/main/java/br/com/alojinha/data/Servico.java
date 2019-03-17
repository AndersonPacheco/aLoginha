package br.com.alojinha.data;

import android.content.Context;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import br.com.alojinha.model.Retorno;
import br.com.alojinha.util.ConnectionDetector;
import br.com.alojinha.util.Constantes;

public class Servico {

    public boolean verificarConexao(Context context){
        ConnectionDetector cd = new ConnectionDetector(context);
        return cd.isConnectingToInternet();
    }

    public Retorno request(String urlString, String method) throws SocketTimeoutException, IOException, Exception {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Retorno retorno = new Retorno();

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(Constantes.READ_TIMEOUT);
            connection.setConnectTimeout(Constantes.CONNECT_TIMEOUT);
            connection.setRequestMethod(method);
            connection.setRequestProperty(Constantes.WS_ACCEPT, Constantes.WS_APPLICATION_JSON);
            connection.setRequestProperty(Constantes.WS_CONTENT_TYPE, Constantes.WS_APPLICATION_JSON);
            connection.setDoInput(true);

            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_CREATED
                    || connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED
                    || connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = connection.getInputStream();
                JSONObject message = (JSONObject) new JSONTokener(convertStreamToStringRequest(inputStream)).nextValue();
                retorno.setStatusCode(connection.getResponseCode());
                retorno.setStrRetorno(message.toString());//convertStreamToStringRequest(inputStream));
                retorno.setMensagem(message.isNull("message") ? "" : message.get("message").toString());
            } else if(connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                inputStream = connection.getErrorStream();
                JSONObject message = (JSONObject) new JSONTokener(convertStreamToStringRequest(inputStream)).nextValue();
                retorno.setStatusCode(connection.getResponseCode());
                retorno.setMensagem(message.isNull("message") ? "" : message.get("message").toString());
            } else{
                inputStream = connection.getErrorStream();
                JSONObject message = (JSONObject) new JSONTokener(convertStreamToStringRequest(inputStream)).nextValue();
                retorno.setStatusCode(connection.getResponseCode());
                retorno.setMensagem(message.isNull("message") ? "" : message.get("message").toString());
            }
            return retorno;
        } finally {
            inputStream.close();
            connection.disconnect();
        }
    }

    public String convertStreamToStringRequest(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
