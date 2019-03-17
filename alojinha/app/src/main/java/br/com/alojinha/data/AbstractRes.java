package br.com.alojinha.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AbstractRes implements Parcelable {

    private int statusCode;
    private String mensagem;

    public AbstractRes(){
    }


    protected AbstractRes(Parcel in) {
        mensagem = in.readString();
        statusCode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mensagem);
        dest.writeInt(statusCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AbstractRes> CREATOR = new Creator<AbstractRes>() {
        @Override
        public AbstractRes createFromParcel(Parcel in) {
            return new AbstractRes(in);
        }

        @Override
        public AbstractRes[] newArray(int size) {
            return new AbstractRes[size];
        }
    };

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
