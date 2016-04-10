package br.edu.nutrif.view.activitys;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;

import br.edu.nutrif.R;
import br.edu.nutrif.controller.DiaRefeicaoController;
import br.edu.nutrif.controller.PretensaoRefeicaoController;
import br.edu.nutrif.controller.Replyable;
import br.edu.nutrif.database.dao.PretensaoRefeicaoDAO;
import br.edu.nutrif.entitys.PretensaoRefeicao;
import br.edu.nutrif.entitys.output.Erro;
import br.edu.nutrif.util.qrcode.Contents;
import br.edu.nutrif.util.qrcode.QRCodeEncoder;
import br.edu.nutrif.util.AndroidUtil;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RefeicaoActivity extends AppCompatActivity {
    @Bind(R.id.loadinglayout)
    LinearLayout loadLayout;

    @Bind(R.id.content)
    LinearLayout content;

    @Bind(R.id.codelayout)
    LinearLayout codelayout;

    @Bind(R.id.dia)
    TextView dia;

    @Bind(R.id.hora_final)
    TextView hora_final;

    @Bind(R.id.hora_inicial)
    TextView hora_inicial;

    @Bind(R.id.tipo)
    TextView tipo;

    @Bind(R.id.data)
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        carregando();
        buildContent();

    }


    public void buildContent() {
        loadLayout.setVisibility(View.VISIBLE);
        final int position = this.getIntent().getIntExtra("position", -1);

        if (position != -1) {
            PretensaoRefeicaoController.retornarRefeicao(this, position, new Replyable<PretensaoRefeicao>() {
                @Override
                public void onSuccess(PretensaoRefeicao pretencaoRefeicao) {
                    RefeicaoActivity.this.organizarTela(pretencaoRefeicao);
                    carregando();
                }

                @Override
                public void onFailure(Erro erro) {
                    AndroidUtil.showToast(RefeicaoActivity.this, erro.getMensagem());
                    finish();
                }

                @Override
                public void failCommunication(Throwable throwable) {
                    PretensaoRefeicao pretensaoRefeicao = PretensaoRefeicaoDAO.
                            getInstance(RefeicaoActivity.this).find(
                            DiaRefeicaoController.refeicoes.get(position).getId());
                    if(pretensaoRefeicao != null){
                        AndroidUtil.showSnackbar(RefeicaoActivity.this,R.string.recuperaqrcode);
                        RefeicaoActivity.this.organizarTela(pretensaoRefeicao);
                        carregando();
                    }else{
                        AndroidUtil.showToast(RefeicaoActivity.this, R.string.erroconexao);
                        finish();
                    }
                }
            });
        }
    }

    public void organizarTela(final PretensaoRefeicao pretencaoRefeicao){
        dia.setText(pretencaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getDia().getNome());
        hora_final.setText(pretencaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getHoraFinal());
        hora_inicial.setText(pretencaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getHoraInicio());

        java.text.SimpleDateFormat dateformate = new SimpleDateFormat("dd/MM HH:mm:ss");
        String dataconvertida = dateformate.format(pretencaoRefeicao.getConfirmaPretensaoDia().getDataPretensao());

        data.setText(dataconvertida);
        tipo.setText(pretencaoRefeicao.getConfirmaPretensaoDia().getDiaRefeicao().getRefeicao().getTipo());
        LinearLayout body = (LinearLayout) findViewById(R.id.body);
        if(pretencaoRefeicao.getKeyAccess()== null){
            Button pretencaoButton = new Button(this);
            pretencaoButton.setBackgroundResource(R.drawable.layout_button);
            pretencaoButton.setTextAppearance(this, R.style.buttonCustom);
            pretencaoButton.setText(R.string.registrarPedido);
            pretencaoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            body.addView(pretencaoButton);

        }else {
            Button qrcodeButton = new Button(this);
            qrcodeButton.setBackgroundResource(R.drawable.layout_button);
            qrcodeButton.setTextAppearance(this, R.style.buttonCustom);
            qrcodeButton.setText(R.string.gerarcode);
            qrcodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gerandoQrcode(pretencaoRefeicao.getKeyAccess());
                }
            });
            body.addView(qrcodeButton);
        }
    }

    public void gerandoQrcode(String str) {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(str,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) findViewById(R.id.code);
            myImage.setImageBitmap(bitmap);
            alteraView(null);

        } catch (WriterException e) {
            AndroidUtil.showSnackbar(this, R.string.errocode);
        }
    }

    public void alteraView(View view) {
        codelayout.setVisibility(codelayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        content.setVisibility(content.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    public void carregando(){
        loadLayout.setVisibility(loadLayout.getVisibility() == View.GONE? View.VISIBLE : View.GONE);
        content.setVisibility(content.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}
