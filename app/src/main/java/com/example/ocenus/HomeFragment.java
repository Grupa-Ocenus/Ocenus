package com.example.ocenus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private Uzytkownik uzytkownik;
    private GridView coursesGridView; // Changed from ListView
    private ListView gradesListView;

    public HomeFragment() {
        // Default constructor
    }

    public HomeFragment(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Create an array of subject names
        List<String> subjectNames = new ArrayList<>();
        for (Kierunek kierunek : uzytkownik.getCourses()) {
            for (Przedmiot przedmiot : kierunek.getSubjects()) {
                subjectNames.add(przedmiot.getSubjectName());
            }
        }

        // Set up the GridView with subject names
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.grid_item_layout, R.id.textView, subjectNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.textView);
                String subjectName = subjectNames.get(position);
                if (subjectName.length() > 13) {
                    subjectName = subjectName.substring(0, 10) + "...";
                }
                textView.setText(subjectName);
                return view;
            }

        };
        coursesGridView = view.findViewById(R.id.gridView); // Use your grid view ID
        coursesGridView.setAdapter(adapter);

        // Handle item click on the GridView
        coursesGridView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedSubject = subjectNames.get(position);
            List<Ocena> grades = new ArrayList<>();
            for (Ocena ocena : uzytkownik.getGrades()) {
                if (Objects.equals(ocena.getSubjectName(), selectedSubject)) {
                    grades.add(ocena);
                }
            }

            String[] gradeNames = new String[grades.size()];
            for (int i = 0; i < grades.size(); i++) {
                Ocena ocena = grades.get(i);
                gradeNames[i] = ocena.getName() + ": " + ocena.getGrade();
            }

            final ArrayAdapter<String> gradesAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, gradeNames);

            gradesListView = view.findViewById(R.id.gradesListView); // Use your list view ID
            gradesListView.setAdapter(gradesAdapter);
            gradesListView.setVisibility(View.VISIBLE);
            coursesGridView.setVisibility(View.INVISIBLE);
        });

        return view;
    }
}