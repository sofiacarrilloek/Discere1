package com.jhpat.discere;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class profile_principal extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public String nombre_1_1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView nombre, apellido, email,nombre_1,apellido_1;
    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1;//CLASE
    JSONObject jsonObject;

    public  static String ID_USUARIO;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private ImageView profile;
    private String mParam2;
    RequestQueue requestQueue;
    View vista;
    FloatingActionMenu actionMenu;
    private OnFragmentInteractionListener mListener;


    public profile_principal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_principal.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_principal newInstance(String param1, String param2) {
        profile_principal fragment = new profile_principal();
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
        vista =inflater.inflate(R.layout.fragment_profile_principal, container, false);
        Button Imangenpas = (Button) vista.findViewById(R.id.button_Edit) ;
        Button btnpas = (Button) vista.findViewById(R.id.button_Password);
        profile=(ImageView)vista.findViewById(R.id.photoprin);
        nombre = (TextView)vista.findViewById(R.id.tv_name);
        nombre_1 = (TextView)vista.findViewById(R.id.t1);
        apellido = (TextView)vista.findViewById(R.id.tv_lastname);
        apellido_1 = (TextView)vista.findViewById(R.id.t2);
        email = (TextView)vista.findViewById(R.id.tv_email);
        requestQueue = Volley.newRequestQueue(getActivity());
        cargarP();
        Cargarfoto();


        Imangenpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), ProfileDos.class);
                startActivityForResult(intent, 0);

            }
        });

        btnpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Profile_Password.class);
                startActivityForResult(intent, 0);
            }
        });
        return vista;

    }

    private void Cargarfoto() {
        String url= "http://puntosingular.mx/cas/imagenes/"+nombre.getText()+".jpg";
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                profile.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
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


    private  void cargarP()
    {
        SharedPreferences preferencia = this.getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        ID1 = preferencia.getString("ID2", "NO EXISTE");
        String Name= preferencia.getString("NAME2", "NO EXISTE");
        String Last_Name= preferencia.getString("LAST_NAME2", "NO EXISTE");
        String emailV= preferencia.getString("EMAIL2", "NO EXISTE");

        nombre.setText(Name);
        nombre_1.setText(Name);
        apellido_1.setText(Last_Name);
        apellido.setText(Last_Name);
        email.setText(emailV);

    }//Fin cargar preferencias
}