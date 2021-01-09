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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private TextView textViewResult, searchingWord;
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
        searchingWord = findViewById(R.id.searchingParameter);
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

                searchingWord.setText(inputWord);

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

                            System.out.println("size meanings");
                            System.out.println(jsonArrMeanings.length());
                            System.out.println("size meanings");

                            System.out.println("Phonetics ---> text " + jsonArray.getJSONObject(0).get("text"));
                            System.out.println("Meanings ---> partOfSpeech " + jsonArrMeanings.getJSONObject(0).get("partOfSpeech"));
                            System.out.println("Meanings ---> definitions " + jsonArrMeanings.getJSONObject(0).get("definitions"));
                            // System.out.println("" + jsonArray.getJSONObject(0).get("text"));
                            //
                            JSONArray jsonArrMeaningsDefinitions = (JSONArray) jsonArrMeanings.getJSONObject(0).get("definitions");

                            System.out.println("length of jsonArraymeinanig definitions is " + jsonArrMeaningsDefinitions.length());
                            int lengthOfDefinitions = jsonArrMeaningsDefinitions.length();


                            JSONObject jsonObject2 = new JSONObject();
                            JSONArray jsonArray1 = new JSONArray();
                            List<JSONObject> list = new ArrayList<>();
                            Object[] ArrayObject = new Object[lengthOfDefinitions];



                            //
                            for (int i = 0; i < lengthOfDefinitions; i++) {
                                JSONObject jsonObject = new JSONObject();
                                System.out.println("-------------------------------------");
                                System.out.println("dlzka " + jsonArrMeaningsDefinitions.getJSONObject(i).get("definition").toString().length());
                                //  int number = ;
                                if (!jsonArrMeaningsDefinitions.getJSONObject(i).isNull("definition")) {

                                    jsonObject.put("Definition", jsonArrMeaningsDefinitions.getJSONObject(i).get("definition"));
                                    System.out.println("-- Definition  [" + i + "]" + jsonArrMeaningsDefinitions.getJSONObject(i).get("definition"));
                                    System.out.println("number is bigger than 10");
                                }

                                if (!jsonArrMeaningsDefinitions.getJSONObject(i).isNull("example")) {
                                    jsonObject.put("Example", jsonArrMeaningsDefinitions.getJSONObject(i).get("example"));
                                    System.out.println("dlzka " + jsonArrMeaningsDefinitions.getJSONObject(i).get("example").toString().length());
                                    System.out.println("-- Example  [" + i + "]" + jsonArrMeaningsDefinitions.getJSONObject(i).get("example"));
                                }
                                if (!jsonArrMeaningsDefinitions.getJSONObject(i).isNull("synonyms")) {
                                    //JSONArray jsonArrSynonyms= (JSONArray) jsonArrMeanings.getJSONObject(0).get("synonyms");
                                    System.out.println("synonyms");
                                    System.out.println(jsonArrMeaningsDefinitions.getJSONObject(i).get("synonyms"));

                                }
                                System.out.println("-------------------------------------");
                                System.out.println("vklad poziciiu " +i);
                                System.out.println(jsonObject);
                                list.add(jsonObject);
                                ArrayObject[i] = jsonObject;

                                System.out.println(jsonObject);
                                jsonArray1.put(jsonObject);
                                System.out.println("=========================================");
                                System.out.println(jsonArray1);
                                System.out.println(jsonArray1.getJSONObject(0));

                                System.out.println("=========================================");
                            }


                            System.out.println("result " + jsonArray1);
                            System.out.println("list " + list);


                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");

                            for (int i = 0; i < ArrayObject.length; i++) {
                                System.out.println(ArrayObject[i]);
                            }

                            //textViewResult.setText(jsonArray2.toString());

                            textViewResult.setText(list.toString());

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