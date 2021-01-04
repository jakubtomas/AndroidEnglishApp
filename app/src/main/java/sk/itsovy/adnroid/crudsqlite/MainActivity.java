package sk.itsovy.adnroid.crudsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData, btnSearch;
    private EditText editText;
    private TextView textViewResult;
    private NestedScrollView scrollView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        textViewResult = findViewById(R.id.text_view_result);
        scrollView = findViewById(R.id.nestedScrollView);

        mDatabaseHelper = new DatabaseHelper(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.dictionaryapi.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputWord = editText.getText().toString();
                System.out.println("input paramter " + editText.getText().toString());
                System.out.println("input paramter " + inputWord);

                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<ResponseBody> call = jsonPlaceHolderApi.getStringResponse2(inputWord);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            textViewResult.setText("Code: " + response.code());
                            return;
                        }

                        Object obj = null;
                        obj = response.body(); // try to string


                        try {
                            //      JSONArray jsonArray1 = new JSONArray(response.body().toString());
                            JSONArray jsonArray2 = new JSONArray(response.body().string());

                            Object phonetics = jsonArray2.getJSONObject(0).get("phonetics");
                            Object meanings = jsonArray2.getJSONObject(0).get("meanings");

                            JSONArray jsonArray = (JSONArray) jsonArray2.getJSONObject(0).get("phonetics");
                            JSONArray jsonArrMeanings = (JSONArray) jsonArray2.getJSONObject(0).get("meanings");

                            System.out.println("Phonetics ---> text " + jsonArray.getJSONObject(0).get("text"));
                            System.out.println("Meanings ---> partOfSpeech " + jsonArrMeanings.getJSONObject(0).get("partOfSpeech"));
                            System.out.println("Meanings ---> definitions " + jsonArrMeanings.getJSONObject(0).get("definitions"));
                            // System.out.println("" + jsonArray.getJSONObject(0).get("text"));
//
                            JSONArray jsonArrMeaningsDefinitions = (JSONArray) jsonArrMeanings.getJSONObject(0).get("definitions");

                            System.out.println("length of jsonArraymeinanig definitions is " + jsonArrMeaningsDefinitions.length());
                            int lengthOfDefinitions = jsonArrMeaningsDefinitions.length();



                            // todo potrebne osetrit  ci mame string alebo je to prazdne ,, osetrenie chyby
                            for (int i = 0; i < lengthOfDefinitions; i++) {
                                System.out.println("-------------------------------------");
                                System.out.println("-- Definition  [" + i +"]" +jsonArrMeaningsDefinitions.getJSONObject(i).get("definition"));
                                System.out.println("-- Example  [" + i +"]" +jsonArrMeaningsDefinitions.getJSONObject(i).get("example"));
                                System.out.println("-------------------------------------");
                            }


/*
                            System.out.println(" json Arrmeaing Definitions one " + jsonArrMeaningsDefinitions.getJSONObject(1));
                            System.out.println(" json Arrmeaing Definitions one " + jsonArrMeaningsDefinitions.getJSONObject(1).toString());
                            System.out.println(" is null one " + jsonArrMeaningsDefinitions.getJSONObject(1).isNull("definition"));
                            System.out.println(" definition one  " + jsonArrMeaningsDefinitions.getJSONObject(1).get("definition"));
*/


                            /*System.out.println("=====================================================");
                            System.out.println(" Definitions zero " + jsonArrMeaningsDefinitions.getJSONObject(0).get("definition"));
                            System.out.println("Example zero" + jsonArrMeaningsDefinitions.getJSONObject(0).get("example"));
                            System.out.println("=====================================================");

                            System.out.println("  Definitions one " + jsonArrMeaningsDefinitions.getJSONObject(1).get("definition"));
                            System.out.println("  example one " + jsonArrMeaningsDefinitions.getJSONObject(1).get("example"));
                            */
                            textViewResult.setText(jsonArray2.toString());

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        textViewResult.setText(t.getMessage());

                    }

                });

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    AddData(newEntry);
                    editText.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}