package com.example.ocenus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class StronaGlowna extends AppCompatActivity {

    Intent intent;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    String login;
    FirebaseDatabase database;
    DatabaseReference reference;

    Uzytkownik uzytkownik;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strona_glowna);

        drawerLayout = findViewById(R.id.drawer_layout);


        intent = getIntent();
        login = intent.getStringExtra("login");

        reference = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/").getReference("users").child(login).child("courses");
        uzytkownik = new Uzytkownik(login, null, null);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getChildrenCount() !=0){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Kierunek course = new Kierunek(dataSnapshot.getKey(),5);
                        for(DataSnapshot dataSnapshotLevel2: dataSnapshot.child("subjects").getChildren()){
                            Przedmiot subject = new Przedmiot(course.getCourseName(), dataSnapshotLevel2.getKey(),Integer.valueOf(dataSnapshotLevel2.child("ects").getValue(String.class)),5);
                            for(DataSnapshot dataSnapshotLevel3: dataSnapshotLevel2.child("grades").getChildren()){
                                Ocena grade = new Ocena(course.getCourseName(), "uwu", dataSnapshotLevel3.getKey(),Integer.valueOf(dataSnapshotLevel3.child("grade").getValue(String.class)), RodzajOceny.WYKLAD);
                                subject.getGrades().add(grade);
                                uzytkownik.getGrades().add(grade);
                            }
                            course.getSubjects().add(subject);
                        }
                        uzytkownik.getCourses().add(course);

                    }
                }
                replaceFragment(new HomeFragment(uzytkownik));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }
        );


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(uzytkownik)).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        replaceFragment(new HomeFragment(uzytkownik));


        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment(uzytkownik));
                    break;
                case R.id.nav_ustawienia:
                    replaceFragment(new SettingsFragment());
                    break;
                case R.id.nav_wyloguj:
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent = new Intent(StronaGlowna.this, Logowanie.class); // Replace YourCurrentActivity with the appropriate activity name
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
                    startActivity(intent); // Navigate back to the login page
                    break;

            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        bottomNavigationView.setBackground(null);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.StronaGlowna:
                    replaceFragment(new HomeFragment(uzytkownik));
                    break;
                case R.id.Statystyki:
                    replaceFragment(new StatystykiFragment());
                    break;
                case R.id.Wydarzenia:
                    replaceFragment(new WydarzeniaFragment());
                    break;
                case R.id.Edycja:
                    replaceFragment(new EdycjaFragment());
                    break;
            }

            return true;
        });

        fab.setOnClickListener(view -> showBottomDialog());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout ocenaLayout = dialog.findViewById(R.id.layoutOcena);
        LinearLayout przedmiotLayout = dialog.findViewById(R.id.layoutPrzedmiot);
        LinearLayout wydarzenieLayout = dialog.findViewById(R.id.layoutWydarzenie);
        LinearLayout kierunekLayout = dialog.findViewById(R.id.layoutKierunek);

        ImageView cancelButton = dialog.findViewById(R.id.cofnij_dodaj);

        ocenaLayout.setOnClickListener(v -> {
            database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
            reference = database.getReference("users").child(login).child("courses");

            int subjectSize = 0;
            for(Kierunek course: uzytkownik.getCourses()){
                subjectSize+=course.getSubjects().size();
            }

            String[] s = new String[subjectSize];
            int counter = 0;
            for (Kierunek kierunek : uzytkownik.getCourses()) {
                for(Przedmiot subject: kierunek.getSubjects()){
                    s[counter] = subject.getSubjectName();
                    counter++;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(dialog.getContext());
            final ArrayAdapter<String> adp = new ArrayAdapter<>(StronaGlowna.this,
                    android.R.layout.simple_spinner_item, s);

            final Spinner sp = new Spinner(StronaGlowna.this);
            sp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            sp.setAdapter(adp);

            EditText addGrade = new EditText(StronaGlowna.this);
            EditText addGrade2 = new EditText(StronaGlowna.this);

            LinearLayout layout = new LinearLayout(builder.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(addGrade);
            addGrade.setHint("Czynność");
            layout.addView(addGrade2);
            addGrade2.setHint("Ocena");
            addGrade2.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(sp);

            builder.setView(layout);
            builder
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog1, id) -> {
                        String subject = sp.getSelectedItem().toString();
                        Map<String,Object> values = new HashMap<>();
//                        values.put("name",String.valueOf(addGrade.getText()));
                        values.put("grade",String.valueOf(addGrade2.getText()));
                        List<Przedmiot> subjects= new ArrayList<>();
                        for(Kierunek course: uzytkownik.getCourses()){
                            subjects.addAll(course.getSubjects());
                        }
                        Przedmiot subjectFound = subjects.stream().filter(subjectParam ->
                                Objects.equals(subject, subjectParam.getSubjectName())).findFirst().get();
                        reference.child(subjectFound.getCourseName()).child("subjects").child(subject).child("grades").child(String.valueOf(addGrade.getText())).updateChildren(values);
                        Toast.makeText(StronaGlowna.this,"Dodano ocenę!",Toast.LENGTH_SHORT).show();


                        Ocena grade = new Ocena(subjectFound.getCourseName(), subjectFound.getSubjectName(), (String.valueOf(addGrade.getText())),Integer.valueOf(String.valueOf(addGrade2.getText())),RodzajOceny.WYKLAD);
                        Kierunek courseFound = uzytkownik.getCourses()
                                .stream()
                                .filter(courseParam -> Objects.equals(subjectFound.getCourseName(), courseParam.getCourseName()))
                                .collect(Collectors.toList()).stream().findFirst().get();
                        int courseIndex = uzytkownik.getCourses().indexOf(courseFound);
                        Przedmiot subjectToAppend = courseFound.getSubjects()
                                .stream()
                                .filter(subjectParam -> Objects.equals(subjectFound.getSubjectName(),subjectParam.getSubjectName()))
                                .collect(Collectors.toList()).stream().findFirst().get();
                        int subjectIndex = uzytkownik.getCourses().get(courseIndex).getSubjects().indexOf(subjectToAppend);

                        uzytkownik.getCourses().get(courseIndex).getSubjects().get(subjectIndex).getGrades().add(grade);

                    });
            AlertDialog alert = builder.create();
            alert.show();
            replaceFragment(new HomeFragment(uzytkownik));
        });

        przedmiotLayout.setOnClickListener(v -> {
            addSubjectShowDialog(dialog);
            replaceFragment(new HomeFragment(uzytkownik));
        });
        wydarzenieLayout.setOnClickListener(v -> {

            dialog.dismiss();
            Toast.makeText(StronaGlowna.this,"Kliknięto by dodać wydarzenie",Toast.LENGTH_SHORT).show();

        });
        kierunekLayout.setOnClickListener(v -> {
            addCourseShowDialog(dialog);
            replaceFragment(new HomeFragment(uzytkownik));
        });



        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void addCourseShowDialog(Dialog dialog){
        login = intent.getStringExtra("login");
        database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
        reference = database.getReference("users").child(login).child("courses");


        AlertDialog.Builder builder = new AlertDialog.Builder(dialog.getContext());

        EditText addCourse = new EditText(StronaGlowna.this);
        EditText addSemester = new EditText(StronaGlowna.this);

        LinearLayout layout = new LinearLayout(builder.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(addCourse);
        addCourse.setHint("Nazwa");
        layout.addView(addSemester);
        addSemester.setHint("Aktualny semestr");
        addSemester.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(layout);
        builder
                .setMessage("Nazwa kierunku")
                .setCancelable(true)
                .setPositiveButton("OK", (dialog1, id) -> {
                    Map<String,Object> values = new HashMap<>();
                    values.put("Current semester",String.valueOf(addSemester.getText()));
                    reference.child(String.valueOf(addCourse.getText())).updateChildren(values);
                    Toast.makeText(StronaGlowna.this,"Dodano kierunek!",Toast.LENGTH_SHORT).show();
                    uzytkownik.getCourses().add(new Kierunek(String.valueOf(addCourse.getText()), Integer.valueOf(String.valueOf(addSemester.getText()))));
                });

        AlertDialog alert = builder.create();
        alert.show();


    }

    private Spinner createCoursesSpinner(Dialog dialog){
        String[] s = new String[uzytkownik.getCourses().size()];

        int counter = 0;
        for (Kierunek kierunek : uzytkownik.getCourses()) {
            s[counter] = kierunek.getCourseName();
            counter++;
        }


        final ArrayAdapter<String> adp = new ArrayAdapter<>(StronaGlowna.this,
                android.R.layout.simple_spinner_item, s);

        final Spinner sp = new Spinner(StronaGlowna.this);
        sp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);
        return sp;
    }
    private void addSubjectShowDialog(Dialog dialog){

        login = intent.getStringExtra("login");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
        DatabaseReference reference = database.getReference("users").child(login).child("courses");
        AlertDialog.Builder builder = new AlertDialog.Builder(dialog.getContext());

        Spinner sp = createCoursesSpinner(dialog);
        TextView coursesListLabel = new TextView(StronaGlowna.this);
        TextView lectureLabel = new TextView(StronaGlowna.this);
        TextView classesLabel =  new TextView(StronaGlowna.this);
        EditText addCourseName = new EditText(StronaGlowna.this);
        EditText addCourseEcts = new EditText(StronaGlowna.this);
        EditText addCourseSemester = new EditText(StronaGlowna.this);
        CheckBox lectureCheck = new CheckBox(StronaGlowna.this);
        CheckBox classesCheck = new CheckBox(StronaGlowna.this);
        EditText lectureWeight = new EditText(StronaGlowna.this);
        EditText classesWeight = new EditText(StronaGlowna.this);

        LinearLayout layout = new LinearLayout(builder.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(addCourseName);
        addCourseName.setHint("Nazwa przedmiotu");
        layout.addView(addCourseEcts);
        addCourseEcts.setHint("ECTS");
        addCourseEcts.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(addCourseSemester);
        addCourseSemester.setHint("Semestr");
        addCourseSemester.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(coursesListLabel);
        coursesListLabel.setText("\n Lista przedmiotów\n");
        layout.addView(sp);
        classesLabel.setText("\n Zajęcia\n");
        layout.addView(classesLabel);
        layout.addView(classesCheck);
        classesCheck.setChecked(true);
        layout.addView(classesWeight);
        classesWeight.setEnabled(false);
        classesWeight.setHint("Waga (%)");
        classesWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(lectureLabel);
        lectureLabel.setText("\n Wykład\n");
        layout.addView(lectureCheck);
        layout.addView(lectureWeight);
        lectureWeight.setEnabled(false);
        lectureWeight.setHint("Waga (%)");
        lectureWeight.setInputType(InputType.TYPE_CLASS_NUMBER);

        lectureCheck.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if(isChecked){
                lectureWeight.setEnabled(true);
                lectureWeight.setHintTextColor(Color.WHITE);
            }
            else{
                lectureWeight.setEnabled(false);
                lectureWeight.setHintTextColor(Color.GRAY);
            }
        }
        );

        classesCheck.setOnCheckedChangeListener((buttonView, isChecked) ->
                {
                    if(isChecked){
                        classesWeight.setEnabled(true);
                        classesWeight.setHintTextColor(Color.WHITE);
                    }
                    else{
                        classesWeight.setEnabled(false);
                        classesWeight.setHintTextColor(Color.GRAY);
                    }
                }
        );


        builder.setView(layout);
        builder
                .setCancelable(true)
                .setPositiveButton("OK", (dialog1, id) -> {

                    String course = sp.getSelectedItem().toString();

                    Map<String,Object> values = new HashMap<>();
                    values.put("ects",String.valueOf(addCourseEcts.getText()));
                    values.put("semester", String.valueOf(addCourseSemester.getText()));
                    reference.child(course).child("subjects").child(String.valueOf(addCourseName.getText())).updateChildren(values);
                    values = new HashMap<>();
                    if(lectureCheck.isChecked() && classesCheck.isChecked()){
                        values.put("lecture",String.valueOf(lectureWeight.getText()));
                        values.put("classes", String.valueOf(classesWeight.getText()));
                    }
                    else if (lectureCheck.isChecked()){
                        values.put("lecture",String.valueOf(100));
                    }
                    else {
                        values.put("classes",String.valueOf(100));
                    }
                    reference.child(course).child("subjects").child(String.valueOf(addCourseName.getText())).child("weights").updateChildren(values);

                    Toast.makeText(StronaGlowna.this,"Dodano przedmiot!",Toast.LENGTH_SHORT).show();
                    Przedmiot subject = new Przedmiot(course, (String.valueOf(addCourseName.getText())),Integer.valueOf(String.valueOf(addCourseEcts.getText())),Integer.valueOf(String.valueOf(addCourseSemester.getText())));
                    Kierunek courseFound = uzytkownik.getCourses()
                            .stream()
                            .filter(courseParam -> course.equals(courseParam.getCourseName()))
                            .collect(Collectors.toList()).stream().findFirst().get();
                    int courseIndex = uzytkownik.getCourses().indexOf(courseFound);
                    uzytkownik.getCourses().get(courseIndex).getSubjects().add(subject);

                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

