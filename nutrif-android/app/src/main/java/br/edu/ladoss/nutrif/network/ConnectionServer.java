package br.edu.ladoss.nutrif.network;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by juan on 14/03/16.
 */
public class ConnectionServer {

    private static final String URL_BASE = "http://10.1.134.46:8080/NutrIF_Service/";
    private static APIService service;
    private static ConnectionServer ourInstance = new ConnectionServer();

    public static ConnectionServer getInstance() {
        return ourInstance;
    }

    public APIService getService() {
        return service;
    }

    private ConnectionServer() {
        updateServiceAdress();
    }

    public void updateServiceAdress() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIService.class);
    }
}
