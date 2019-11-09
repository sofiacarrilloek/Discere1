package com.jhpat.discere;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StreamingAudio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StreamingAudio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StreamingAudio extends Fragment {
    Spinner spinner;
    AsyncHttpClient client;
    View vista;
    ImageButton play,stop;
    MediaPlayer mediaPlayer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StreamingAudio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StreamingAudio.
     */
    // TODO: Rename and change types and number of parameters
    public static StreamingAudio newInstance(String param1, String param2) {
        StreamingAudio fragment = new StreamingAudio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.streaming_audio, container, false);
        spinner = (Spinner) vista.findViewById(R.id.SpinnerAudios);
        play = (ImageButton) vista.findViewById(R.id.playA);
        stop = (ImageButton) vista.findViewById(R.id.stopA);
        client = new AsyncHttpClient();



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playA();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopA();
            }
        });
        llenarSpinner();
        return vista;
    }



    public void llenarSpinner(){
        String url="http://34.226.77.86/discere/obtener_audios.php";
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    cargarSpiner(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void cargarSpiner(String s) {
        ArrayList<Audios> lista = new ArrayList<Audios>();
        try{
            JSONArray jsonArray = new JSONArray(s);
            for (int i=0; i<jsonArray.length();i++){
                Audios n = new Audios();
                n.setLink(jsonArray.getJSONObject(i).getString("link"));
                lista.add(n);
            }
            ArrayAdapter<Audios> adapter = new ArrayAdapter<Audios>(this.getActivity(), android.R.layout.simple_spinner_item,lista);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private class Audios{
        private int id_;
        private String id_lesson_result;
        private String link;

        public Audios(){

        }

        public Audios(int id_, String id_lesson_result, String last_name){
            this.id_ = id_;
            this.id_lesson_result = id_lesson_result;
            this.link = last_name;
        }

        public void setId_(int id_){
            this.id_=id_;
        }

        public void setId_lesson_result(String id_lesson_result){
            this.id_lesson_result=id_lesson_result;
        }

        public void setLink(String link){
            this.link=link;
        }

        @Override
        public String toString(){
            return link;
        }


    }

    public void playA(){
        String nameAudio=spinner.getSelectedItem().toString();
        mediaPlayer =  new MediaPlayer();
        play.setBackgroundResource(R.drawable.playoscuro);
        stop.setBackgroundResource(R.drawable.stop);
            try {
                mediaPlayer.setDataSource(nameAudio);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public void stopA(){
        play.setBackgroundResource(R.drawable.play);
        stop.setBackgroundResource(R.drawable.stoposcuro);
        mediaPlayer.stop();
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
