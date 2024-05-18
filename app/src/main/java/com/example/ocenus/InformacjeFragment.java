package com.example.ocenus;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformacjeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformacjeFragment extends Fragment {

    public static void showAboutDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_o_aplikacji, null);

        TextView description1 = view.findViewById(R.id.description1);

        description1.setText("Ocenus to aplikacja do liczenia średniej ocen i monitorowania postępów w nauce. Stworzona przez grupę trzech studentów, aplikacja ma na celu ułatwienie organizacji i poprawę wyników edukacyjnych. Dzięki Ocenus możesz śledzić swoje oceny, analizować postępy oraz motywować się do dalszej nauki. Nasz zespół dokłada wszelkich starań, aby aplikacja była intuicyjna i pomocna dla każdego studenta.");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setIcon(R.drawable.baseline_info_24);
        builder.setTitle(" O aplikacji");

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Additional logic if needed after clicking OK
        });

        builder.create().show();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InformacjeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformacjeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformacjeFragment newInstance(String param1, String param2) {
        InformacjeFragment fragment = new InformacjeFragment();
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
        return inflater.inflate(R.layout.fragment_o_aplikacji, container, false);
    }
}