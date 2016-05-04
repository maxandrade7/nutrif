package br.edu.ladoss.nutrif.controller;

import android.content.Context;

import br.edu.ladoss.nutrif.database.dao.PretensaoRefeicaoDAO;
import br.edu.ladoss.nutrif.entitys.DiaRefeicao;
import br.edu.ladoss.nutrif.entitys.PretensaoRefeicao;
import br.edu.ladoss.nutrif.network.ConnectionServer;
import br.edu.ladoss.nutrif.util.ErrorUtils;
import br.edu.ladoss.nutrif.util.PreferencesUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by juan on 21/03/16.
 */
public class PretensaoRefeicaoController {

    public static void pedirRefeicao(final Context context, int position, final Replyable<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao> ui) {

        final DiaRefeicao refeicao = DiaRefeicaoController.getRefeicoes().get(position);

        final br.edu.ladoss.nutrif.entitys.PretensaoRefeicao pretencao = new br.edu.ladoss.nutrif.entitys.PretensaoRefeicao();

        pretencao.getConfirmaPretensaoDia().getDiaRefeicao().setId(refeicao.getId());

        Call<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao> call = ConnectionServer
                .getInstance()
                .getService()
                .pedirRefeicao(PreferencesUtils.getAccessKeyOnSharedPreferences(context), pretencao);
        call.enqueue(new Callback<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao>() {
            @Override
            public void onResponse(Response<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    PretensaoRefeicaoDAO.getInstance(context).insertOrUpdate(response.body());
                    ui.onSuccess(response.body());
                } else {
                    ui.onFailure(ErrorUtils.parseError(response, retrofit, context));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ui.failCommunication(t);
            }
        });

    }

    public static void retornarRefeicao(final Context context, int position, final Replyable<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao> ui){
        br.edu.ladoss.nutrif.entitys.PretensaoRefeicao pretensaoRefeicao = new br.edu.ladoss.nutrif.entitys.PretensaoRefeicao();
        final int id = DiaRefeicaoController.getRefeicoes().get(position).getId();
        pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().setId(
                id
        );

        Call<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao> call = ConnectionServer.getInstance().getService().infoRefeicao(
                PreferencesUtils.getAccessKeyOnSharedPreferences(context),
                pretensaoRefeicao
        );
        call.enqueue(new Callback<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao>() {
            @Override
            public void onResponse(Response<br.edu.ladoss.nutrif.entitys.PretensaoRefeicao> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    PretensaoRefeicao refeicao = PretensaoRefeicaoDAO.getInstance(context).find(id);

                    if(refeicao != null && refeicao.getConfirmaPretensaoDia().getDataPretensao()
                            .equals(response.body().getConfirmaPretensaoDia().getDataPretensao())){
                        response.body().setKeyAccess(refeicao.getKeyAccess());
                    }
                    ui.onSuccess(response.body());
                }else {
                    ui.onFailure(ErrorUtils.parseError(response,retrofit,context));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ui.failCommunication(t);
            }
        });
    }

}