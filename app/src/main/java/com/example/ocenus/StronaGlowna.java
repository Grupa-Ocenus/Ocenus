package com.example.ocenus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Naciśnij dwukrotnie, aby zamknąć aplikację", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strona_glowna);
        intent = getIntent();
        login = intent.getStringExtra("login");

        reference = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/").getReference("users").child(login).child("courses");
        uzytkownik = new Uzytkownik(login, null, null);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getChildrenCount() !=0){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Kierunek course = new Kierunek(dataSnapshot.getKey());
                        for(DataSnapshot dataSnapshotLevel2: dataSnapshot.child("subjects").getChildren()){

                            course.getSubjects().add(new Przedmiot(course.getCourseName(), dataSnapshotLevel2.getKey(),Integer.valueOf(dataSnapshotLevel2.child("ects").getValue(String.class))));
                        }
                        uzytkownik.getCourses().add(course);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_studia);
        }

        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.StronaGlowna:
                    replaceFragment(new HomeFragment());
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

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
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

            Integer subjectSize = 0;
            for(Kierunek course: uzytkownik.getCourses()){
                subjectSize+=course.getSubjects().size();
            }

            String[] s = new String[subjectSize];
            Integer counter = 0;
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
                        values.put("name",String.valueOf(addGrade.getText()));
                        values.put("grade",String.valueOf(addGrade2.getText()));
                        List<Przedmiot> subjects= new ArrayList<>();
                        for(Kierunek course: uzytkownik.getCourses()){
                            subjects.addAll(course.getSubjects());
                        }
                        Przedmiot subjectFound = subjects.stream().filter(subjectParam ->
                                Objects.equals(subject, subjectParam.getSubjectName())).findFirst().get();
                        reference.child(subjectFound.getCourseName()).child("subjects").child(subject).child("grades").child(String.valueOf(addGrade.getText())).updateChildren(values);
                        Toast.makeText(StronaGlowna.this,"Dodano ocenę!",Toast.LENGTH_SHORT).show();


                        Ocena grade = new Ocena(subjectFound.getCourseName(), (String.valueOf(addGrade.getText())),Integer.valueOf(String.valueOf(addGrade2.getText())));
                        Kierunek courseFound = uzytkownik.getCourses()
                                .stream()
                                .filter(courseParam -> Objects.equals(subjectFound.getCourseName(), courseParam.getCourseName()))
                                .collect(Collectors.toList()).stream().findFirst().get();
                        Integer courseIndex = uzytkownik.getCourses().indexOf(courseFound);
                        Przedmiot subjectToAppend = courseFound.getSubjects()
                                .stream()
                                .filter(subjectParam -> Objects.equals(subjectFound.getSubjectName(),subjectParam.getSubjectName()))
                                .collect(Collectors.toList()).stream().findFirst().get();
                        Integer subjectIndex = uzytkownik.getCourses().get(courseIndex).getSubjects().indexOf(subjectToAppend);

                        uzytkownik.getCourses().get(courseIndex).getSubjects().get(subjectIndex).getGrades().add(grade);

                    });
            AlertDialog alert = builder.create();
            alert.show();

        });

        przedmiotLayout.setOnClickListener(v -> {

            login = intent.getStringExtra("login");
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
            DatabaseReference reference = database.getReference("users").child(login).child("courses");
            String[] s = new String[uzytkownik.getCourses().size()];
            Integer counter = 0;
            for (Kierunek kierunek : uzytkownik.getCourses()) {
                s[counter] = kierunek.getCourseName();
                counter++;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(dialog.getContext());
            final ArrayAdapter<String> adp = new ArrayAdapter<String>(StronaGlowna.this,
                    android.R.layout.simple_spinner_item, s);

            final Spinner sp = new Spinner(StronaGlowna.this);
            sp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            sp.setAdapter(adp);

            EditText addCourse = new EditText(StronaGlowna.this);
            EditText addCourse2 = new EditText(StronaGlowna.this);
            LinearLayout layout = new LinearLayout(builder.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(addCourse);
            addCourse.setHint("Nazwa przedmiotu");
            layout.addView(addCourse2);
            addCourse2.setHint("ECTS");
            addCourse2.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(sp);

            builder.setView(layout);
            builder
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog1, id) -> {
                        String course = sp.getSelectedItem().toString();
                        Map<String,Object> values = new HashMap<>();
                        values.put("name",String.valueOf(addCourse.getText()));
                        values.put("ects",String.valueOf(addCourse2.getText()));
                        reference.child(course).child("subjects").child(String.valueOf(addCourse.getText())).updateChildren(values);
                        Toast.makeText(StronaGlowna.this,"Dodano przedmiot!",Toast.LENGTH_SHORT).show();
                        Przedmiot subject = new Przedmiot(course, (String.valueOf(addCourse.getText())),Integer.valueOf(String.valueOf(addCourse2.getText())));
                        Kierunek courseFound = uzytkownik.getCourses()
                                .stream()
                                .filter(courseParam -> course.equals(courseParam.getCourseName()))
                                .collect(Collectors.toList()).stream().findFirst().get();
                        Integer courseIndex = uzytkownik.getCourses().indexOf(courseFound);
                        uzytkownik.getCourses().get(courseIndex).getSubjects().add(subject);

                    });
            AlertDialog alert = builder.create();
            alert.show();

        });
        wydarzenieLayout.setOnClickListener(v -> {

            dialog.dismiss();
            Toast.makeText(StronaGlowna.this,"Kliknięto by dodać wydarzenie",Toast.LENGTH_SHORT).show();

        });
        kierunekLayout.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(dialog.getContext());
            EditText addCourse = new EditText(StronaGlowna.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addCourse.setLayoutParams(lp);
            login = intent.getStringExtra("login");
            database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
            reference = database.getReference("users").child(login).child("courses");



            builder.setView(addCourse);
            builder
                    .setMessage("Nazwa kierunku")
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog1, id) -> {
                        Map<String,Object> values = new HashMap<>();
                        values.put(String.valueOf(addCourse.getText()),String.valueOf(addCourse.getText()));
                        reference.updateChildren(values);
                        Toast.makeText(StronaGlowna.this,"Dodano kierunek!",Toast.LENGTH_SHORT).show();
                        uzytkownik.getCourses().add(new Kierunek(String.valueOf(addCourse.getText())));
                    });

            AlertDialog alert = builder.create();
            alert.show();

        });

       

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}

