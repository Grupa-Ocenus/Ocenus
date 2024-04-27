package com.example.ocenus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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



    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
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
        navigationView.setItemBackgroundResource(R.drawable.checked_item);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        replaceFragment(new HomeFragment());


        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.white));
        } else {
            changeMenuItemColor(navigationView.getMenu(), R.id.nav_home, Color.BLACK);
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.motyw_noc));
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            resetMenuItemColors(navigationView);
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment());
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_home, Color.WHITE);
                    break;
                case R.id.nav_profil:
                    Toast.makeText(this, "Kliknięto mój profil!", Toast.LENGTH_SHORT).show();
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_profil, Color.WHITE);
                    break;
                case R.id.nav_wydarzenia:
                    replaceFragment(new WydarzeniaFragment());
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_wydarzenia, Color.WHITE);
                    break;
                case R.id.nav_statystyki:
                    replaceFragment(new StatystykiFragment());
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_statystyki, Color.WHITE);
                    break;
                case R.id.nav_ustawienia:
                    replaceFragment(new SettingsFragment());
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_ustawienia, Color.WHITE);
                    break;
                case R.id.nav_blad:
                    Toast.makeText(this, "Kliknięto zgłoś błąd!", Toast.LENGTH_SHORT).show();
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_blad, Color.WHITE);
                    break;
                case R.id.nav_informacje:
                    Toast.makeText(this, "Kliknięto informacje o aplikacji!", Toast.LENGTH_SHORT).show();
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_informacje, Color.WHITE);
                    break;
                case R.id.nav_wyloguj:
                    changeMenuItemColor(navigationView.getMenu(), R.id.nav_wyloguj, Color.WHITE);
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
            navigationView.setCheckedItem(item.getItemId());
            return true;
        });

        bottomNavigationView.setBackground(null);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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

    private void changeMenuItemColor(Menu menu, int menuItemId, int color) {
        MenuItem menuItem = menu.findItem(menuItemId);
        SpannableString spannable = new SpannableString(menuItem.getTitle());
        spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(), 0);
        menuItem.setTitle(spannable);
    }


    private void resetMenuItemColors(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        // Iterujemy po każdym elemencie menu
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.hasSubMenu()) {
                SubMenu subMenu = menuItem.getSubMenu();
                // Iterujemy po elementach submenu
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    // Ustawiamy kolor tekstu na czarny
                    SpannableString spannable = new SpannableString(subMenuItem.getTitle());
                    spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannable.length(), 0);
                    subMenuItem.setTitle(spannable);
                }
            } else {
                // Ustawiamy kolor tekstu na czarny dla pojedynczych elementów menu
                SpannableString spannable = new SpannableString(menuItem.getTitle());
                spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannable.length(), 0);
                menuItem.setTitle(spannable);
            }
        }
    }


    private void showBottomDialog() {

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout ocenaLayout = dialog.findViewById(R.id.layoutOcena);
        LinearLayout przedmiotLayout = dialog.findViewById(R.id.layoutPrzedmiot);
        LinearLayout wydarzenieLayout = dialog.findViewById(R.id.layoutWydarzenie);
        LinearLayout kierunekLayout = dialog.findViewById(R.id.layoutKierunek);
        LinearLayout dialogLayout = dialog.findViewById(R.id.dialog_layout);
        ImageView cancelButton = dialog.findViewById(R.id.cofnij_dodaj);

        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            dialogLayout.setBackgroundResource(R.drawable.dialogbkg); // Dla motywu jasnego
        } else {
            dialogLayout.setBackgroundResource(R.drawable.dialogbkg_night); // Dla motywu ciemnego
        }

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


                        Ocena grade = new Ocena(subjectFound.getCourseName(), (String.valueOf(addGrade.getText())),Integer.valueOf(String.valueOf(addGrade2.getText())));
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

        });

        przedmiotLayout.setOnClickListener(v -> {

            login = intent.getStringExtra("login");
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
            DatabaseReference reference = database.getReference("users").child(login).child("courses");
            String[] s = new String[uzytkownik.getCourses().size()];
            int counter = 0;
            for (Kierunek kierunek : uzytkownik.getCourses()) {
                s[counter] = kierunek.getCourseName();
                counter++;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(dialog.getContext());
            final ArrayAdapter<String> adp = new ArrayAdapter<>(StronaGlowna.this,
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
//                        values.put("name",String.valueOf(addCourse.getText()));
                        values.put("ects",String.valueOf(addCourse2.getText()));
                        reference.child(course).child("subjects").child(String.valueOf(addCourse.getText())).updateChildren(values);
                        Toast.makeText(StronaGlowna.this,"Dodano przedmiot!",Toast.LENGTH_SHORT).show();
                        Przedmiot subject = new Przedmiot(course, (String.valueOf(addCourse.getText())),Integer.valueOf(String.valueOf(addCourse2.getText())));
                        Kierunek courseFound = uzytkownik.getCourses()
                                .stream()
                                .filter(courseParam -> course.equals(courseParam.getCourseName()))
                                .collect(Collectors.toList()).stream().findFirst().get();
                        int courseIndex = uzytkownik.getCourses().indexOf(courseFound);
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
                        String courseName = addCourse.getText().toString().trim();
                        if (!courseName.isEmpty()) {
                            Map<String,Object> values = new HashMap<>();
                            values.put(courseName, courseName);
                            reference.updateChildren(values);
                            Toast.makeText(StronaGlowna.this,"Dodano kierunek!",Toast.LENGTH_SHORT).show();
                            uzytkownik.getCourses().add(new Kierunek(courseName));
                        } else {
                            Toast.makeText(StronaGlowna.this, "Wprowadź nazwę kierunku!", Toast.LENGTH_SHORT).show();
                        }
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

