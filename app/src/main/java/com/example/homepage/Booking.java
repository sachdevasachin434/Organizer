package com.example.homepage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Booking extends Fragment {
    Button book1, book2;
    TextView tvline,tvconference1,tvconference2;

//    FirebaseFirestore mFirestore;
    int count=0;
    Bundle bundle;
    //used for date picker
    Calendar cal;
    int cur_year, cur_month, cur_day;

    //NEW
    String loggedInUser;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        book1 = view.findViewById(R.id.book_hall_1);
        book2 = view.findViewById(R.id.book_hall_2);
        tvconference1 = view.findViewById(R.id.tvconference1);
        tvconference2 = view.findViewById(R.id.tvconference2);

        //fetch the name of user who logged in
        loggedInUser = ((global_variables) getActivity().getApplication()).getName();
            Toast.makeText(getContext(), "Welcome back, "+loggedInUser+"!", Toast.LENGTH_SHORT).show();     //welcome the logged in user, by showing toast


        book1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("room", "Meeting Room");

                //Set a Date Picker Dialog on booking button, which allows user to pich the date on which meeting room is to be booked
                //First, fetch the current date; which is displayed as default for the date picker
                cal = Calendar.getInstance();
                cur_year = cal.get(Calendar.YEAR);
                cur_day = cal.get(Calendar.DAY_OF_MONTH);
                cur_month = cal.get(Calendar.MONTH);

                //Pick a date from date picker dialog
                DatePickerDialog dpd = new DatePickerDialog(getContext(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        //Fragment newFragment = new Calendar_Slots();  //made on 21june
                        Fragment newFragment = new NewCalendar_Slots();

                        bundle.putInt("picked_day",dayOfMonth);     //assuming the user always picks a date under a month (i.e month from today)
                        bundle.putInt("picked_month", month);
                        bundle.putInt("picked_year", year);
                        newFragment.setArguments(bundle);           //passing the picked date (from date picked dialog) to the next fragment (for booking)
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, newFragment);
                        ft.addToBackStack(null);
                        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                        ft.commit();
                    }
                }, cur_year, cur_month, cur_day);
                Calendar calStart = cal;
                dpd.getDatePicker().setMinDate(calStart.getTimeInMillis());
                Calendar calEnd = cal;
                calEnd.add(Calendar.MONTH, 4);
                dpd.getDatePicker().setMaxDate(calEnd.getTimeInMillis());
                dpd.show();
            }
        });

        book2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("room", "Conference Room");

                //Set a Date Picker Dialog on booking button, which allows user to pich the date on which meeting room is to be booked
                //First, fetch the current date; which is displayed as default for the date picker
                cal = Calendar.getInstance();
                cur_year = cal.get(Calendar.YEAR);
                cur_day = cal.get(Calendar.DAY_OF_MONTH);
                cur_month = cal.get(Calendar.MONTH);

                //Pick a date from date picker dialog
                DatePickerDialog dpd = new DatePickerDialog(getContext(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        //Fragment newFragment = new Calendar_Slots();  //made on 21june
                        Fragment newFragment = new NewCalendar_Slots();
                        bundle.putInt("picked_day",dayOfMonth);     //assuming the user always picks a date under a month (i.e month from today)
                        bundle.putInt("picked_month", month);
                        bundle.putInt("picked_year", year);
                        newFragment.setArguments(bundle);           //passing the picked date (from date picked dialog) to the next fragment (for booking)
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, newFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }, cur_year, cur_month, cur_day);
                Calendar calStart = cal;
                dpd.getDatePicker().setMinDate(calStart.getTimeInMillis());
                Calendar calEnd = cal;
                calEnd.add(Calendar.MONTH, 4);
                dpd.getDatePicker().setMaxDate(calEnd.getTimeInMillis());
                dpd.show();
            }
        });
    }


//
//        book2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                getData();
//                Map<String, Object> bookedMap= new HashMap<>();
//                bookedMap.put("name","hello" /*name*/);
//
//                //add to firestore
//                mFirestore.collection("booked").add(bookedMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(getContext(), "Data added son!", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        String error = e.getMessage();
//                        Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Book a Hall");
    }


    public void onBackPressed() {
        Fragment newFragment = new NewCalendar_Slots();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}

