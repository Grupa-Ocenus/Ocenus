package com.example.ocenus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private Uzytkownik uzytkownik;
    private ListView coursesListView;
    private ListView gradesListView;
    public HomeFragment(){

    }
    public HomeFragment(Uzytkownik uzytkownik){
        this.uzytkownik = uzytkownik;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StringBuilder courses = new StringBuilder();
        for (Kierunek course : uzytkownik.getCourses()) {
            courses.append(course.getCourseName()).append("\n");
        }
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Integer size = 0;
        for(Kierunek kierunek: uzytkownik.getCourses()){
           size += kierunek.getSubjects().size();
        }
        String[] s = new String[size];

        int counter = 0;
        for (Kierunek kierunek : uzytkownik.getCourses()) {
            for(Przedmiot przedmiot : kierunek.getSubjects()){
                s[counter] = przedmiot.getSubjectName();
                counter++;
            }

        }


        final ArrayAdapter<String> adp = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, s);

        coursesListView = (ListView) view.findViewById(R.id.simpleListView);
        coursesListView.setAdapter(adp);
//        gradesListView.setVisibility(View.INVISIBLE);

        coursesListView.setOnItemClickListener(
                (parent, view1, position, id) -> {
                    String selectedFromList = (String) coursesListView.getItemAtPosition(position);
                    List<Ocena> grades = new ArrayList<>();
                    for(Ocena ocena: uzytkownik.getGrades()){
                        if (Objects.equals(ocena.getSubjectName(), selectedFromList)){
                            grades.add(ocena);
                        }
                    }


                    String[] gradesNames = new String[grades.size()];

                    int gradesCounter = 0;
                    for (Ocena ocena : grades) {
                        gradesNames[gradesCounter] = ocena.getName() +": "+ocena.getGrade();
                        gradesCounter++;
                    }


                    final ArrayAdapter<String> gradesAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_list_item_1, gradesNames);

                    gradesListView = (ListView) view.findViewById(R.id.gradesListView);
                    gradesListView.setAdapter(gradesAdapter);
                    gradesListView.setVisibility(View.VISIBLE);
                    coursesListView.setVisibility(View.INVISIBLE);
                }
        );

        return view;
    }
}