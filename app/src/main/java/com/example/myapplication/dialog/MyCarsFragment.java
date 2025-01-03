package com.example.myapplication.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CarAdapter;
import com.example.myapplication.models.response.Car;
import com.example.myapplication.models.response.CarDetailsResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.ProgressBarUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCarsFragment extends Fragment {

    private RecyclerView carsRecyclerView;
    private CarAdapter carAdapter;
    private View progressOverlay;
    private ProgressBar progressBar;

    private String userId = "123";  // Replace with actual user ID logic

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cars, container, false);

        carsRecyclerView = view.findViewById(R.id.carsRecyclerView);
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progressBar);  // Use 'view.findViewById'
        progressOverlay = view.findViewById(R.id.progressOverlay);  // Use 'view.findViewById'
        fetchCarDetails();

        return view;
    }

    private void fetchCarDetails() {
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

        RetrofitClient.getRetrofitInstance(getContext()).create(ApiService.class)
                .getCarDetailsByUserId(userId)
                .enqueue(new Callback<CarDetailsResponse>() {
                    @Override
                    public void onResponse(Call<CarDetailsResponse> call, Response<CarDetailsResponse> response) {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                        if (response.isSuccessful() && response.body() != null) {
                            CarDetailsResponse carDetailsResponse = response.body();
                            if (carDetailsResponse.isSuccess() && carDetailsResponse.getData() != null && !carDetailsResponse.getData().isEmpty()) {
                                List<CarDetailsResponse.Car> carList = carDetailsResponse.getData();

                                // Set adapter with the list of cars
                                carAdapter = new CarAdapter(carList,getContext());
                                carsRecyclerView.setAdapter(carAdapter);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to load car details", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CarDetailsResponse> call, Throwable t) {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false);
                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateCarList(List<Car> cars) {
    }
}
