package com.example.todo.ui.delayed;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.data.DBHelper;
import com.example.todo.data.dao.AppointmentDAO;
import com.example.todo.data.models.Appointment;
import com.example.todo.databinding.FragmentDelayedEventsBinding;
import com.example.todo.adapter.EventAdapter;


import java.util.ArrayList;
import java.util.List;

public class DelayedEventsFragment extends Fragment {

    private FragmentDelayedEventsBinding binding;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private DelayedEventViewModel DelayedEventViewModel;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Create an instance of the viewmodel using the ViewModelProvider class
        // Pass the fragment as the scope and a factory that provides the AppointmentDAO as a parameter
        DelayedEventViewModel = new ViewModelProvider(this, new DelayedEventsFragment.DelayedTasksViewModelFactory(new AppointmentDAO(getActivity()))).get(DelayedEventViewModel.class);

        binding = FragmentDelayedEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Find the RecyclerView by its id
        recyclerView = root.findViewById(R.id.recycler_view);

        // Set the layout manager to the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));

        // Create a new list of appointments or get it from somewhere else
        List<Appointment> newappointments = new ArrayList<>();
        // Create a new adapter object and set it to the RecyclerView
        adapter = new EventAdapter(getActivity(), newappointments);
        recyclerView.setAdapter(adapter);
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();

        /*DelayedEventViewModel.getAppointments();*/
        // Observe the list of appointments in the viewmodel using the observe method
        // Pass the fragment as the lifecycle owner and a lambda expression as the observer
        DelayedEventViewModel.getDelayedEvents(db).observe(getViewLifecycleOwner(), appointments -> {
            // Check if the list is not null and not empty
            if (appointments != null && !appointments.isEmpty()) {
                // Pass the list to the adapter
                adapter.setAppointments(appointments);
            } else {
                // Show a toast message that there are no appointments
                Toast.makeText(getActivity(), "No Event for today", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        recyclerView = null;
        adapter = null;
        DelayedEventViewModel = null;
        db.close();
        dbHelper.close();
    }

    public static class DelayedTasksViewModelFactory implements ViewModelProvider.Factory {
        // A field to store the AppointmentDAO
        private AppointmentDAO appointmentDAO;

        // A constructor that takes an AppointmentDAO as a parameter and assigns it to the field
        public DelayedTasksViewModelFactory(AppointmentDAO appointmentDAO) {
            this.appointmentDAO = appointmentDAO;
        }

        // Override the create method to return an instance of DelayedEventViewModel with the AppointmentDAO
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DelayedEventViewModel(appointmentDAO);
        }
    }

}

