package com.azgfd.jsonparsingdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.azgfd.jsonparsingdemo.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public TextView tvData;


    ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMovies = (ListView) findViewById(R.id.lvMovies);
        //new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoList.txt");
        //new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt");
        //new JSONTask().execute("http://demonwebsoftware.com/jsonColors.txt");

    }

    public class JSONTask extends AsyncTask<String, String, List<MovieModel>> {
        @Override
        protected List<MovieModel> doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("movies");

                List<MovieModel> movieModelList = new ArrayList<>();
                for (int i = 0; i < parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);

                    MovieModel movieModel = new MovieModel();
                    movieModel.setMovie(finalObject.getString("movie"));
                    movieModel.setYear(finalObject.getInt("year"));
                    movieModel.setRating((float) finalObject.getDouble("rating"));
                    movieModel.setDirector(finalObject.getString("director"));
                    movieModel.setDuration(finalObject.getString("duration"));
                    movieModel.setTagline(finalObject.getString("tagline"));
                    movieModel.setImage(finalObject.getString("image"));
                    movieModel.setStory(finalObject.getString("story"));


                    List<MovieModel.Cast> castList = new ArrayList<>();
                    for (int j = 0; j < finalObject.getJSONArray("cast").length(); j++) {
                        MovieModel.Cast cast = new MovieModel.Cast();
                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
                        castList.add(cast);
                    }
                    movieModel.setCastList(castList);
                    movieModelList.add(movieModel);
                }

                return movieModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieModel> result) {

            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row, result);
            lvMovies.setAdapter(adapter);

            super.onPostExecute(result);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MovieAdapter extends ArrayAdapter<MovieModel> {

        private List<MovieModel> movieModelList;
        private int resource; //this is the R.Id.row id number
        private LayoutInflater inflater;

        public MovieAdapter(Context context, int resource, List<MovieModel> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }

            ImageView ivIcon;
            TextView tvMovie;
            TextView tvTagline;
            TextView tvYear;
            TextView tvDuration;
            TextView tvDirector;
            RatingBar rbMovie;
            TextView tvCast;
            TextView tvStory;

            ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            tvMovie = (TextView) convertView.findViewById(R.id.tvMovie);
            tvTagline = (TextView) convertView.findViewById(R.id.tvTagline);
            tvYear = (TextView) convertView.findViewById(R.id.tvYear);
            tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
            tvDirector = (TextView) convertView.findViewById(R.id.tvDirector);
            rbMovie = (RatingBar) convertView.findViewById(R.id.rbMovie);
            tvCast = (TextView) convertView.findViewById(R.id.tvCast);
            tvStory = (TextView) convertView.findViewById(R.id.tvStory);




            tvMovie.setText(movieModelList.get(position).getMovie());
            tvTagline.setText(movieModelList.get(position).getTagline());
            tvYear.setText("Year: " + movieModelList.get(position).getYear());
            tvDuration.setText("Duration: " + movieModelList.get(position).getDuration());
            tvDirector.setText("Director: " + movieModelList.get(position).getDirector());
            tvStory.setText("Time: " + movieModelList.get(position).getStory());

            //divide by 2 because website returns values 1-10 and stars go 1-5
            rbMovie.setRating(movieModelList.get(position).getRating() / 2);

            StringBuffer stringBuffer = new StringBuffer();
            //for each loop
            for (MovieModel.Cast cast : movieModelList.get(position).getCastList()) {
                stringBuffer.append(cast.getName() + ", ");
            }
            tvCast.setText("Cast: " + stringBuffer);

            return convertView;
        }
    }



}

