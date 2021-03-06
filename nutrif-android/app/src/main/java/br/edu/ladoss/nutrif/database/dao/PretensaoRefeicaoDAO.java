package br.edu.ladoss.nutrif.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import br.edu.ladoss.nutrif.model.PretensaoRefeicao;

/**
 * Created by juan on 10/04/16.
 */
public class PretensaoRefeicaoDAO extends GenericDAO<br.edu.ladoss.nutrif.model.PretensaoRefeicao> {
    public static final String DIAREFEICAO_TABLE = "tb_pretensao";
    public static final String CREATE_TABLE = "create table IF NOT EXISTS " + DIAREFEICAO_TABLE + "(" +
            "_id integer primary key unique, " +
            "dia text not null, " +
            "hora_init text not null, " +
            "hora_final text not null, " +
            "data long not null," +
            "tipo text not null, " +
            "keyAccess text );";
    public static final String DROP_TABLE = "drop table " + DIAREFEICAO_TABLE + ";";

    public static PretensaoRefeicaoDAO getInstance(Context context) {
        return new PretensaoRefeicaoDAO(context);
    }

    public PretensaoRefeicaoDAO(Context context) {
        super(context);
    }

    public void insert(PretensaoRefeicao pretensaoRefeicao) {
        delete(pretensaoRefeicao);
        ContentValues values = new ContentValues();

        values.put("_id", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getId());
        values.put("dia", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getDia().getNome());
        values.put("hora_init", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getHoraInicio());
        values.put("hora_final", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getHoraFinal());
        values.put("data", pretensaoRefeicao.getConfirmaPretensaoDia().getDataPretensao());
        values.put("tipo", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getTipo());
        values.put("keyAccess", pretensaoRefeicao.getKeyAccess());

        insert(DIAREFEICAO_TABLE, values);
    }

    public void delete(PretensaoRefeicao pretensaoRefeicao) {
        db.delete(DIAREFEICAO_TABLE, "_id = " + pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getId(), null);
    }

    public void update(PretensaoRefeicao pretensaoRefeicao) {
        ContentValues values = new ContentValues();

        values.put("_id", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getId());
        values.put("dia", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getDia().getNome());
        values.put("hora_init", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getHoraInicio());
        values.put("hora_final", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getHoraFinal());
        values.put("data", pretensaoRefeicao.getConfirmaPretensaoDia().getDataPretensao());
        values.put("tipo", pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getTipo());
        values.put("keyAccess", pretensaoRefeicao.getKeyAccess());

        db.update(DIAREFEICAO_TABLE, values, "_id = " + pretensaoRefeicao.getId(), null);

    }

    public PretensaoRefeicao find(int id) {
        PretensaoRefeicao pretensaoRefeicao = null;
        String[] colums = new String[]{"_id", "dia", "hora_init", "hora_final", "data", "tipo", "keyAccess"};
        Cursor cursor = db.query(DIAREFEICAO_TABLE, colums, "_id =" + id, null, null, null, "_id");

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            pretensaoRefeicao = new PretensaoRefeicao();
            pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao()
                    .setId(cursor.getInt(0));
            pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getDia()
                    .setNome(cursor.getString(1));
            pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao()
                    .setHoraInicio(cursor.getString(2));
            pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao()
                    .setHoraFinal(cursor.getString(3));

            pretensaoRefeicao.getConfirmaPretensaoDia().setDataPretensao(cursor.getLong(4));

            pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao()
                    .setTipo(cursor.getString(5));
            pretensaoRefeicao.setKeyAccess(cursor.getString(6));
            cursor.close();
            return pretensaoRefeicao;
        }
        cursor.close();
        return null;
    }

    public void insertOrUpdate(PretensaoRefeicao pretensaoRefeicao) {
        //TODO: otimizar
        if (find(pretensaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getId()) == null) {
            insert(pretensaoRefeicao);
        } else
            update(pretensaoRefeicao);
    }

}
