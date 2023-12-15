package com.example.todo.ui.tasks;

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
import com.example.todo.data.dao.AppointmentDAO;
import com.example.todo.data.models.Appointment;

import com.example.todo.databinding.FragmentTasksBinding;
import com.example.todo.ui.AppointmentAdapter;


import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {
    private FragmentTasksBinding binding;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private AppointmentAdapter completedAdapter;
    private AppointmentAdapter cancelledAdapter;
    private TasksViewModel tasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Create an instance of the viewmodel using the ViewModelProvider class
        // Pass the fragment as the scope and a factory that provides the AppointmentDAO as a parameter
        tasksViewModel = new ViewModelProvider(this, new TasksFragment.tasksViewModelFactory(new AppointmentDAO(getActivity()))).get(TasksViewModel.class);

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Find the RecyclerView by its id
        recyclerView1 = root.findViewById(R.id.recycler_view1);

        // Set the layout manager to the RecyclerView
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        // Find the RecyclerView by its id
        recyclerView2 = root.findViewById(R.id.recycler_view2);

        // Set the layout manager to the RecyclerView
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));


        // Create a new list of appointments or get it from somewhere else
        List<Appointment> completedappointments = new ArrayList<>();
        // Create a new adapter object and set it to the RecyclerView
        completedAdapter = new AppointmentAdapter(getActivity(), completedappointments);
        recyclerView1.setAdapter(completedAdapter);
        List<Appointment> canceledappointments = new ArrayList<>();

        cancelledAdapter = new AppointmentAdapter(getActivity(), canceledappointments);
        recyclerView2.setAdapter(cancelledAdapter);



        // Observe the list of appointments in the viewmodel using the observe method
        // Pass the fragment as the lifecycle owner and a lambda expression as the observer
        tasksViewModel.getCompletedAppointments().observe(getViewLifecycleOwner(), appointments1 -> {
            // Check if the list is not null and not empty
            if (appointments1 != null && !appointments1.isEmpty()) {
                // Pass the list to the adapter
                completedAdapter.setAppointments(appointments1);
            } else {
                // Show a toast message that there are no appointments
                Toast.makeText(getActivity(), "No completed events", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe the list of appointments in the viewmodel using the observe method
        // Pass the fragment as the lifecycle owner and a lambda expression as the observer
        tasksViewModel.getCancelledAppointments().observe(getViewLifecycleOwner(), appointments2 -> {
            // Check if the list is not null and not empty
            if (appointments2 != null && !appointments2.isEmpty()) {
                // Pass the list to the adapter
                cancelledAdapter.setAppointments(appointments2);
            } else {
                // Show a toast message that there are no appointments
                Toast.makeText(getActivity(), "No cancelled events", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        recyclerView1 = null;
        recyclerView2 = null;
        completedAdapter = null;
        cancelledAdapter = null;
        tasksViewModel = null;
    }

    public static class tasksViewModelFactory implements ViewModelProvider.Factory {
        // A field to store the AppointmentDAO
        private AppointmentDAO appointmentDAO;

        // A constructor that takes an AppointmentDAO as a parameter and assigns it to the field
        public tasksViewModelFactory(AppointmentDAO appointmentDAO) {
            this.appointmentDAO = appointmentDAO;
        }

        // Override the create method to return an instance of tasksViewModel with the AppointmentDAO
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TasksViewModel(appointmentDAO);
        }
    }
}