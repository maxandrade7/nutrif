package br.edu.ladoss.nutrif.view.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import br.edu.ladoss.nutrif.R;
import br.edu.ladoss.nutrif.controller.DiaRefeicaoController;
import br.edu.ladoss.nutrif.controller.PessoaController;
import br.edu.ladoss.nutrif.controller.Replyable;
import br.edu.ladoss.nutrif.database.dao.AlunoDAO;
import br.edu.ladoss.nutrif.entitys.Aluno;
import br.edu.ladoss.nutrif.entitys.DiaRefeicao;
import br.edu.ladoss.nutrif.entitys.output.Erro;
import br.edu.ladoss.nutrif.util.AndroidUtil;
import br.edu.ladoss.nutrif.view.adapters.HorarioAdapter;
import br.edu.ladoss.nutrif.view.callback.RecycleButtonClicked;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RefeitorioActivity extends AppCompatActivity implements RecycleButtonClicked, Replyable<List<DiaRefeicao>> {
    @Bind(R.id.carregando_layout)
    LinearLayout carregarLayout;

    @Bind(R.id.content)
    LinearLayout content;

    @Bind(R.id.recycle)
    RecyclerView recycle;

    private Drawer drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeitorio);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_name);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        buildSlideBar(toolbar,savedInstanceState);
        change(false);
        DiaRefeicaoController.gerarHorario(this, this);
    }

    public void buildSlideBar(Toolbar toolbar, Bundle savedInstanceState){
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withRootView(R.id.content)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSelectedItem(0)
                .withSavedInstance(savedInstanceState)
                .build();
        Aluno aluno = AlunoDAO.getInstance(this).find();
        ProfileDrawerItem profile = new ProfileDrawerItem()
                .withEmail(aluno.getEmail());
        drawer.addItem(profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sair) {
            sair();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
        }
    }

    public void montaTabela(List<DiaRefeicao> refeicoes) {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(gridLayoutManager);
        recycle.setAdapter(new HorarioAdapter(this, refeicoes, this));
        if (recycle.getAdapter().getItemCount() == 0) {
            recycle.setVisibility(View.GONE);
            TextView textView = new TextView(this);
            textView.setText(R.string.nodays);
            textView.setGravity(Gravity.CENTER);
            content.addView(textView);
        }else
            recycle.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClickCallback(View view, int position) {
        Intent intent = new Intent(this, RefeicaoActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }


    @Override
    public void onSuccess(List<DiaRefeicao> diaRefeicaos) {
        montaTabela(diaRefeicaos);
        change(true);
    }

    @Override
    public void onFailure(Erro erro) {
        AndroidUtil.showSnackbar(RefeitorioActivity.this, erro.getMensagem());
        change(true);
    }

    @Override
    public void failCommunication(Throwable throwable) {
        AndroidUtil.showSnackbar(RefeitorioActivity.this, R.string.impossivelcarregar);
        montaTabela(DiaRefeicaoController.getRefeicoes());
        change(true);
    }

    public void change(final boolean ativo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                carregarLayout.setVisibility(ativo ? View.GONE : View.VISIBLE);
                content.setVisibility(!ativo ? View.GONE : View.VISIBLE);
            }
        });
    }

    public void sair() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.sair);
        alertDialog.setMessage(getString(R.string.sairconfirmation));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.sair),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PessoaController.logoff(RefeitorioActivity.this);
                        startActivity(new Intent(RefeitorioActivity.this, LoginActivity.class));
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.nao),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}