package com.example.ocenus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;


public class StatystykiFragment extends Fragment {

    private Uzytkownik uzytkownik;

    public StatystykiFragment() {
        // Default constructor
    }

    public StatystykiFragment(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statystyki, container, false);

        if (uzytkownik == null) {
            return view;
        }

        List<Kierunek> courses = uzytkownik.getCourses();
        LinearLayout layout = view.findViewById(R.id.layout_course_averages);

        // Iterate through each course
        for (int i = 0; i < courses.size(); i++) {
            Kierunek course = courses.get(i);
            double[] courseAverage = calculateCourseWeightedAverages(uzytkownik);

            // Inflate the custom layout for the list item
            View listItemView = inflater.inflate(R.layout.list_item_course_average, null); // Change to your list item layout

            // Find TextViews inside the list item layout
            TextView textViewCourseName = listItemView.findViewById(R.id.text_view_course_name);
            TextView textViewCourseAverage = listItemView.findViewById(R.id.text_view_course_average);

            // Set course name and average to TextViews
            textViewCourseName.setText(course.getCourseName());
            textViewCourseAverage.setText(String.format("%.2f", courseAverage[i])); // Use the course average from the array

            // Add the list item to the layout
            layout.addView(listItemView);
        }

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

    public double[] calculateCourseWeightedAverages(Uzytkownik uzytkownik) {
        List<Kierunek> courses = uzytkownik.getCourses();
        double[] courseAverages = new double[courses.size()];

        // Iterate through each course
        for (int i = 0; i < courses.size(); i++) {
            Kierunek course = courses.get(i);
            double totalWeightedSum = 0;
            double totalWeight = 0;

            // Iterate through each subject in the current course
            for (Przedmiot subject : course.getSubjects()) {
                double weightedAvg = calculateWeightedAverage(subject); // Calculate weighted average for the subject
                double weight = subject.getECTS(); // Get ECTS weight for the subject
                totalWeightedSum += weightedAvg * weight; // Add to total weighted sum
                totalWeight += weight; // Add to total weight
            }

            // Calculate the weighted average for the current course
            double courseWeightedAvg = totalWeight == 0 ? 0 : totalWeightedSum / totalWeight;
            courseAverages[i] = courseWeightedAvg;
        }

        return courseAverages;
    }

}
