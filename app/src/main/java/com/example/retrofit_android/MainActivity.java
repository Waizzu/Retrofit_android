package com.example.retrofit_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = findViewById(R.id.button);
        final TextView txt = (TextView)findViewById(R.id.textView);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Button pulsado", Toast.LENGTH_SHORT);
                Toast.makeText(getApplicationContext(),"Button pulsado", Toast.LENGTH_LONG);
                txt.setText("boton pulsado");
                HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor(); //depurar informacion en el logger
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client=new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)//le decimos que utilice el cliente creado arriba
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);
                Call<List<Repo>> repos = service.listRepos("Waizzu");
                //cuando hacemos una invocacion remota puede fallar
                /*
                try {
                    List<Repo> result = repos.execute().body();//recibidmos json
                    for (Repo r: result){
                        System.out.println(r) ;
                    }
                }catch(Exception e){
                    System.out.println("EXCEPTION: ");
                    System.out.println(e.toString());
                }*/

                repos.enqueue(new Callback<List<Repo>>() {
                    @Override
                    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                        List<Repo> repos = response.body();
                        String texto= "[";
                        for (Repo r : repos){
                            texto = texto + r.full_name+" ";
                        }
                        texto=texto+"]";

                        txt.setText(texto);
                    }

                    @Override
                    public void onFailure(Call<List<Repo>> call, Throwable t) {
                        txt.setText("ERROR");
                    }
                });
            }
        });
    }
}