package pl.edu.ug.api;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String json = "";
            URL url;

            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1) {
                    char letter = (char) data;
                    json += letter;
                    data = reader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            try {
                    JSONObject jsonObject = new JSONObject(json);
                    String dataInfo = jsonObject.getString("data");

                    JSONArray array = new JSONArray(dataInfo);
                    JSONObject nazwa = array.getJSONObject(0);

                    Log.i("data", dataInfo);
                    Log.i("nazwa", nazwa.getString("name"));
                    Log.i("Data Założenia", nazwa.getString("founded"));
                    //nazwa
                    TextView testView = findViewById(R.id.testView);
                    testView.setText(nazwa.getString("name"));
                    //data założenia
                    TextView testView2 = findViewById(R.id.testView2);
                    testView2.setText(nazwa.getString("founded"));
                    //Logo
                    String imageURL = nazwa.getString("logo_path");
                    ImageView imageView;
                    imageView = findViewById(R.id.imageView);
                    Picasso.get().load(imageURL).into(imageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sprawdz(View view) {

        EditText nameText = findViewById(R.id.nameText);
        String teamName = nameText.getText().toString();

        DownloadTask task = new DownloadTask();
        task.execute("https://soccer.sportmonks.com/api/v2.0/teams/search/"+teamName+"?api_token=JAWk1l0LPb8VlALzQePEs17rbH8jeJ5ZPK8lMkwRbkylZLz4mRKmmdEq5zaI");

    }
}