package com.example.ocenus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private Uzytkownik uzytkownik;
    private GridView coursesGridView; // Changed from ListView
    private ListView gradesListView;

    private Spinner course_spinner;

    private OnBackPressedCallback callback;

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

        if (uzytkownik == null) {
            
            return view;
        }
        course_spinner = view.findViewById(R.id.course_spinner);

        String[] s = new String[uzytkownik.getCourses().size()];

        int counter = 0;
        for (Kierunek kierunek : uzytkownik.getCourses()) {
            s[counter] = kierunek.getCourseName();
            counter++;
        }


        final ArrayAdapter<String> adp = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, s);


        course_spinner.setAdapter(adp);

        List<String> subjectNames = new ArrayList<>();
        for (Kierunek kierunek : uzytkownik.getCourses()) {
            if(Objects.equals(kierunek.getCourseName(), course_spinner.getItemAtPosition(0).toString())){
                for (Przedmiot przedmiot : kierunek.getSubjects()) {
                    subjectNames.add(przedmiot.getSubjectName());
                }
            }
        }

        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectNames.removeAll(subjectNames);
                for (Kierunek kierunek : uzytkownik.getCourses()) {
                    if(Objects.equals(kierunek.getCourseName(), course_spinner.getItemAtPosition(i).toString())){
                        for (Przedmiot przedmiot : kierunek.getSubjects()) {
                            subjectNames.add(przedmiot.getSubjectName());
                        }
                    }
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.grid_item_layout, R.id.textView, subjectNames) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = view.findViewById(R.id.textView);
                        TextView textViewOcena = view.findViewById(R.id.textViewOcena);

                        String subjectName = subjectNames.get(position);
                        if (subjectName.length() > 13) {
                            subjectName = subjectName.substring(0, 10) + "...";
                        }
                        textView.setText(subjectName);

                        // Find the corresponding subject and calculate its weighted average
                        Przedmiot subject = null;
                        outerLoop:
                        for (Kierunek kierunek : uzytkownik.getCourses()) {
                            for (Przedmiot przedmiot : kierunek.getSubjects()) {
                                if (przedmiot.getSubjectName().equals(subjectNames.get(position))) {
                                    subject = przedmiot;
                                    break outerLoop;
                                }
                            }
                        }

                        if (subject != null) {
                            double average = calculateWeightedAverage(subject);
                            textViewOcena.setText(String.format("%.2f", average));
                        }

                        return view;
                    }

                };

                if (Objects.nonNull(coursesGridView)){
                    coursesGridView.setAdapter(adapter);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.grid_item_layout, R.id.textView, subjectNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.textView);
                TextView textViewOcena = view.findViewById(R.id.textViewOcena);

                String subjectName = subjectNames.get(position);
                if (subjectName.length() > 13) {
                    subjectName = subjectName.substring(0, 10) + "...";
                }
                textView.setText(subjectName);

                // Find the corresponding subject and calculate its weighted average
                Przedmiot subject = null;
                outerLoop:
                for (Kierunek kierunek : uzytkownik.getCourses()) {
                    for (Przedmiot przedmiot : kierunek.getSubjects()) {
                        if (przedmiot.getSubjectName().equals(subjectNames.get(position))) {
                            subject = przedmiot;
                            break outerLoop;
                        }
                    }
                }

                if (subject != null) {
                    double average = calculateWeightedAverage(subject);
                    textViewOcena.setText(String.format("%.2f", average));
                }

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
            course_spinner.setVisibility(View.INVISIBLE);
        });

        return view;
    }

    private double calculateWeightedAverage(Przedmiot przedmiot) {
        double totalWeight = 0;
        double weightedSum = 0;
        for (Ocena ocena : przedmiot.getGrades()) {
            double grade = ocena.getGrade();
            double weight = przedmiot.getECTS(); // Assuming each subject has an ECTS weight
            weightedSum += grade * weight;
            totalWeight += weight;
        }
        return totalWeight == 0 ? 0 : weightedSum / totalWeight;
    }

    @Override
    public void onResume() {
        super.onResume();
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (gradesListView != null && gradesListView.getVisibility() == View.VISIBLE) {
                    gradesListView.setVisibility(View.INVISIBLE);
                    coursesGridView.setVisibility(View.VISIBLE);
                    course_spinner.setVisibility(View.VISIBLE);
                } else {
                    requireActivity().onBackPressed();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callback != null) {
            callback.remove();
        }
}}