package com.example.comovaibitcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comovaibitcoin.api.BtcService;
import com.example.comovaibitcoin.model.TickerItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.primitives.Ints;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button botaoBtc;
    private Button botaoBtcBrl;
    private Button botaoDolar;
    private TextView textoReal;
    private TextView textoDolar;
    private TextView textoResultado;
    private TextView textoResultadoBtcBrl;
    private Retrofit retrofit;
    private EditText textoInserido;
    private EditText textoInseridoBtc;

    //recupera o valor do BTC
    Double numeroBtc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaControles();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.mercadobitcoin.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarBtcRetrofit();

        botaoBtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calcularQtdBtc();

            }
        });
        botaoBtcBrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calcularValorBtcBrl();

            }
        });
    }

    private void recuperarBtcRetrofit(){

        BtcService btcService = retrofit.create(BtcService.class);
        Call<TickerItem> call = btcService.recuperarBtc();

        call.enqueue(new Callback<TickerItem>() {
            @Override
            public void onResponse(Call<TickerItem> call, Response<TickerItem> response) {

                if (response.isSuccessful()) {

                    TickerItem btc = response.body();
                    String valorBtc = btc.getTicker().getLast();

                    numeroBtc = Double.parseDouble(valorBtc);
                    Double valorDolar = numeroBtc/5.25;

                    Locale ptBr = new Locale("pt", "BR");
                    Locale usa = new Locale("en", "US");
                    String valorString = NumberFormat.getCurrencyInstance(ptBr).format(numeroBtc);
                    String valorStringUsa = NumberFormat.getCurrencyInstance(usa).format(valorDolar);

                    textoDolar.setText(valorStringUsa);

                    textoReal.setText(valorString);
                }

            }

            @Override
            public void onFailure(Call<TickerItem> call, Throwable t) {

            }
        });
    }
    private void calcularQtdBtc(){

        if (textoInserido!=null && !textoInserido.equals(textoInserido.getText())){

            //calcula valor inserido / btc
            String txtQuantida = textoInserido.getText().toString();
            Double valorInserido = Double.parseDouble(txtQuantida);

            //variavel de quanto R$ "xx,xx" valem em bitcoin
            Double resultadoBtcBrl = valorInserido / numeroBtc;

            //formata double pra string com x casas após virgula
            String resultado = String.format("%.6f", resultadoBtcBrl);

            Locale ptBr = new Locale("pt", "BR");
            String valorStringResultado = NumberFormat.getCurrencyInstance(ptBr).format(valorInserido);

            textoResultado.setText(valorStringResultado+". \nvalem "+resultado+" em Bitcoin");

        }else {

            Toast.makeText(this,
                    "Insira numero no campo em branco",
                    Toast.LENGTH_LONG).show();

        }
    }
    private void calcularValorBtcBrl(){

        //recupera valor inserido
        String valorInseridoBtc = textoInseridoBtc.getText().toString();

        //transforma valorInserido em double
        Double valorInseridoCalculo = Double.parseDouble(valorInseridoBtc);

        //calculo valorInserido x BTC
        Double conta = valorInseridoCalculo*numeroBtc;

        //transforma o calculo para ser exibido como texto
        Locale ptBr = new Locale("pt", "BR");
        String exibirResultado = NumberFormat.getCurrencyInstance(ptBr).format(conta);

        textoResultadoBtcBrl.setText(exibirResultado);

    }
    public void iniciaControles(){


        botaoBtc = findViewById(R.id.botaoBitcoin);
        textoDolar = findViewById(R.id.textoValorDolar);
        textoReal = findViewById(R.id.textoValorReais);
        textoInserido = findViewById(R.id.editTextInserir);
        textoResultado = findViewById(R.id.textResultadoBtc);

        botaoBtcBrl = findViewById(R.id.btnCalcularBtcEmBrl);
        textoInseridoBtc = findViewById(R.id.edtQtdBtc);
        textoResultadoBtcBrl = findViewById(R.id.textResultadoBtcEmBrl);

    }
}