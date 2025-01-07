package com.example.myapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.request.AddCarRequest;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.models.response.AddCarResponse;
import com.example.myapplication.utils.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private View progressOverlay; // Added overlay for blur effect
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int IMAGE_COUNT = 4;
    private static final int REQUEST_FRONT_PHOTO = 101;
    private static final int REQUEST_BACK_PHOTO = 102;
    private static final int REQUEST_FRONT_PHOTO_DOCUMENT = 103;
    private static final int REQUEST_BACK_PHOTO_DOCUMENT = 104;
    private static final int REQUEST_FRONT_PHOTO_VECHILE = 105;
    private static final int REQUEST_BACK_PHOTO_VECHILE = 106;
    private static final int REQUEST_FRONT_PHOTO_BANK = 107;
    private static final int REQUEST_BACK_PHOTO_BANK= 108;
    private MultipartBody.Part[] selectedImageParts1;
    private MultipartBody.Part[] selectedImageParts2;
    private MultipartBody.Part[] selectedImageParts3;
    private MultipartBody.Part[] selectedImageParts4;

    // Step 1 Fields
    private EditText etCarName, etCarModel, etCarColor, etCarMileagePerHour, etCarDescription, etRegistrationNumber;
    private Spinner spinnerCategory, etSubcategory,spinnerSeatingCapacity,etCarYear, spinnerFuelType, spinnerTransmissionType;
    // Step 3 Fields (Features)
    private CheckBox airConditioning, gps, bluetooth, childSeat;
    private EditText otherFeatures;
    // Step 4 Fields (Location)
    private EditText pickupLocation, dropoffLocation,dailyRentalPrice;
    // Image upload
    private ImageView[] selectedImageViews;
    private MultipartBody.Part[] selectedImageParts;
    private Button[] uploadImageButtons;
    // Navigation Buttons
    private Button nextButton;
    private ImageView backArrow,idfrontPhoto, idbackPhoto, cardocumentfrontPhoto,cardocumentbackPhoto,vechilelicensefrontPhoto,vechileicensebackPhoto,bankpassbookPhoto;
    private Uri idfrontPhotoUri, idbackPhotoUri,cardocumentfrontPhotoUri,cardocumentbackPhotoUri,vechilelicensefrontPhotoUri,vechilelicensebackPhotoUri,bankpassbookPhotoUri;
    // Step Views
    private View step1, step2, step3, step4, step5;
    private TextView stepIndicator;
    private int currentStep = 1;
    private int currentImageIndex = -1;

    // Retrofit API Service
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        // Initialize Retrofit service
        apiService = RetrofitClient.getRetrofitInstance(AddCarActivity.this).create(ApiService.class);
        // Initialize Views for Steps
        initializeViews();
        // Setup Spinners
        setupSpinners();
        // Handle "Next" Button Click
        nextButton.setOnClickListener(v -> {
            switch (currentStep) {
                case 1:
                    if (validateStep1()) {
                        transitionToStep(step1, step2, "Features");
                        currentStep++;
                    }
                    break;
                case 2:
                    if (validateStep2()) {
                        transitionToStep(step2, step3, "Location");
                        currentStep++;
                    }
                    break;
                case 3:
                    if (validateStep3()) {
                        transitionToStep(step3, step4, "Add Your Car Images");
                        currentStep++;
                    }
                    break;
                case 4:
                    if (validateStep4()) {
                        transitionToStep(step4, step5, "Upload Documents");
                        nextButton.setText("Submit");
                        currentStep++;
                    }
                    break;
                case 5:
                    submitForm();
                    break;
            }
        });
        // Handle "Back" Button Click
        backArrow.setOnClickListener(v -> {
            if (currentStep > 1) {
                switch (currentStep) {
                    case 2:
                        transitionToStep(step2, step1, "Add Basic Information");
                        currentStep--;
                        break;
                    case 3:

                        transitionToStep(step3, step2, "Features");
                        currentStep--;
                        break;
                    case 4:
                        transitionToStep(step4, step3, "Location");
                        currentStep--;
                        break;
                    case 5:
                        transitionToStep(step5, step4, "Add Your Car Images");
                        nextButton.setText("Next");
                        currentStep--;
                        break;
                }
            } else {
                finish(); // Navigate back
            }
        });
        // Image upload button click listeners
        for (int i = 0; i < IMAGE_COUNT; i++) {
            int finalI = i;
            uploadImageButtons[i].setOnClickListener(v -> {
                currentImageIndex = finalI;
                openImageGallery();
            });
        }
        idfrontPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_FRONT_PHOTO);
        });

        // Image selection for back photo
        idbackPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_BACK_PHOTO);
        });
        // Image selection for front photo
        cardocumentfrontPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_FRONT_PHOTO_DOCUMENT);
        });

        // Image selection for back photo
        cardocumentbackPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_BACK_PHOTO_DOCUMENT);
        });
        // Image selection for front photo
        vechilelicensefrontPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_FRONT_PHOTO_VECHILE);
        });

        // Image selection for back photo
        vechileicensebackPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_BACK_PHOTO_VECHILE);
        });
        bankpassbookPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_FRONT_PHOTO_BANK);
        });
    }
    // Initialize Views for all steps and buttons
    private void initializeViews() {
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        step5 = findViewById(R.id.step5);
        progressOverlay = findViewById(R.id.progressOverlay); // Overlay view for blur effect
        stepIndicator = findViewById(R.id.stepIndicator);
        nextButton = findViewById(R.id.nextButton);
        backArrow = findViewById(R.id.backArrow);
        progressBar = findViewById(R.id.progressBar);
        // Step 1 Fields
        etCarName = findViewById(R.id.etCarName);
        etCarModel = findViewById(R.id.etCarModel);
        etCarColor = findViewById(R.id.etCarColor);
        etCarYear = findViewById(R.id.etCarYear);
        etCarMileagePerHour = findViewById(R.id.etCarMileagePerHour);
        etCarDescription = findViewById(R.id.etCarDescription);
        etRegistrationNumber = findViewById(R.id.etRegistrationNumber);
        etSubcategory = findViewById(R.id.etSubcategory);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSeatingCapacity = findViewById(R.id.spinnerSeatingCapacity);
        spinnerFuelType = findViewById(R.id.spinnerFuelType);
        spinnerTransmissionType = findViewById(R.id.spinnerTransmissionType);
        // Step 2 Fields (Pricing)
        dailyRentalPrice = findViewById(R.id.dailyRentalPrice);
        // Step 3 Fields (Features)
        airConditioning = findViewById(R.id.airConditioning);
        gps = findViewById(R.id.gps);
        bluetooth = findViewById(R.id.bluetooth);
        childSeat = findViewById(R.id.childSeat);
        otherFeatures = findViewById(R.id.otherFeatures);
        // Step 4 Fields (Location)
        pickupLocation = findViewById(R.id.pickupLocation);
        dropoffLocation = findViewById(R.id.dropoffLocation);
        // Image Upload Fields
        selectedImageViews = new ImageView[IMAGE_COUNT];
        selectedImageParts = new MultipartBody.Part[IMAGE_COUNT];
        selectedImageParts1 = new MultipartBody.Part[2];
        selectedImageParts2 = new MultipartBody.Part[2];
        selectedImageParts3 = new MultipartBody.Part[2];
        selectedImageParts4 = new MultipartBody.Part[2];
        uploadImageButtons = new Button[IMAGE_COUNT];
        selectedImageViews[0] = findViewById(R.id.selectedImageView1);
        selectedImageViews[1] = findViewById(R.id.selectedImageView2);
        selectedImageViews[2] = findViewById(R.id.selectedImageView3);
        selectedImageViews[3] = findViewById(R.id.selectedImageView4);
        uploadImageButtons[0] = findViewById(R.id.uploadImagesButton1);
        uploadImageButtons[1] = findViewById(R.id.uploadImagesButton2);
        uploadImageButtons[2] = findViewById(R.id.uploadImagesButton3);
        uploadImageButtons[3] = findViewById(R.id.uploadImagesButton4);
        idfrontPhoto = findViewById(R.id.idfrontPhoto);
        idbackPhoto = findViewById(R.id.idbackPhoto);
        cardocumentfrontPhoto = findViewById(R.id.cardocumentfrontPhoto);
        cardocumentbackPhoto = findViewById(R.id.cardocumentbackPhoto);
        vechilelicensefrontPhoto = findViewById(R.id.vechilelicensefrontPhoto);
        vechileicensebackPhoto = findViewById(R.id.vechileicensebackPhoto);
        bankpassbookPhoto = findViewById(R.id.bankpassbookPhoto);
    }
    // Transition between steps with indicator update
    private void transitionToStep(View hideStep, View showStep, String stepText) {
        hideStep.setVisibility(View.GONE);
        showStep.setVisibility(View.VISIBLE);
        stepIndicator.setText(stepText);
    }
    // Setup Spinners with predefined values
    private void setupSpinners() {
        setupSpinner(etCarYear, R.array.car_year_array);
        setupSpinner(etSubcategory, R.array.subcategory_array);
        setupSpinner(spinnerCategory, R.array.category_array);
        setupSpinner(spinnerSeatingCapacity, R.array.seating_capacity_array);
        setupSpinner(spinnerFuelType, R.array.fuel_type_array);
        setupSpinner(spinnerTransmissionType, R.array.transmission_type_array);
    }
    private void setupSpinner(Spinner spinner, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    // Validation for Step 1
    private boolean validateStep1() {
        // Check if EditText fields are empty
        if (etCarName.getText().toString().trim().isEmpty()) {
            etCarName.setError("Car Brand is required");
            etCarName.requestFocus();
            return false;
        }

        if (etCarModel.getText().toString().trim().isEmpty()) {
            etCarModel.setError("Car Model is required");
            etCarModel.requestFocus();
            return false;
        }

        if (etCarColor.getText().toString().trim().isEmpty()) {
            etCarColor.setError("Car Color is required");
            etCarColor.requestFocus();
            return false;
        }
        if (etCarYear.getSelectedItemPosition() == 0) { // Assuming the first position is "Select Year"
            Toast.makeText(this, "Please select a car year", Toast.LENGTH_SHORT).show();
            etCarYear.requestFocus();
            return false;
        }
        if (etCarMileagePerHour.getText().toString().trim().isEmpty()) {
            etCarMileagePerHour.setError("Mileage per Hour is required");
            etCarMileagePerHour.requestFocus();
            return false;
        }
// Validate numeric input
        String mileage = etCarMileagePerHour.getText().toString().trim();
        if (!mileage.matches("\\d+")) { // Regular expression to check for digits only
            etCarMileagePerHour.setError("Please enter a valid numeric value");
            etCarMileagePerHour.requestFocus();
            return false;
        }
        if (etRegistrationNumber.getText().toString().trim().isEmpty()) {
            etRegistrationNumber.setError("Registration Number is required");
            etRegistrationNumber.requestFocus();
            return false;
        }

        if (etCarDescription.getText().toString().trim().isEmpty()) {
            etCarDescription.setError("Car Description is required");
            etCarDescription.requestFocus();
            return false;
        }

        // Check if a valid Spinner option is selected
        if (spinnerCategory.getSelectedItemPosition() == 0) { // Assuming the first position is "Select Category"
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            spinnerCategory.requestFocus();
            return false;
        }

        if (etSubcategory.getSelectedItemPosition() == 0) { // Assuming the first position is "Select Subcategory"
            Toast.makeText(this, "Please select a subcategory", Toast.LENGTH_SHORT).show();
            etSubcategory.requestFocus();
            return false;
        }

        if (spinnerSeatingCapacity.getSelectedItemPosition() == 0) { // Assuming the first position is "Select Seating Capacity"
            Toast.makeText(this, "Please select seating capacity", Toast.LENGTH_SHORT).show();
            spinnerSeatingCapacity.requestFocus();
            return false;
        }


        if (spinnerFuelType.getSelectedItemPosition() == 0) { // Assuming the first position is "Select Fuel Type"
            Toast.makeText(this, "Please select a fuel type", Toast.LENGTH_SHORT).show();
            spinnerFuelType.requestFocus();
            return false;
        }

        if (spinnerTransmissionType.getSelectedItemPosition() == 0) { // Assuming the first position is "Select Transmission Type"
            Toast.makeText(this, "Please select a transmission type", Toast.LENGTH_SHORT).show();
            spinnerTransmissionType.requestFocus();
            return false;
        }

        // All fields are valid
        return true;
    }



    // Additional validations for other steps
    private boolean validateStep2() {
        // Check if at least one checkbox is selected
        boolean isAnyCheckboxSelected = airConditioning.isChecked() || gps.isChecked() || bluetooth.isChecked() || childSeat.isChecked();

        // If no checkbox is selected, validate the "Other Features" field
        if (!isAnyCheckboxSelected) {
            String otherFeaturesText = otherFeatures.getText().toString().trim();
            if (otherFeaturesText.isEmpty()) {
                otherFeatures.setError("Please provide other features if no options are selected.");
                return false; // Return false if no checkbox is selected and "Other Features" is empty
            } else {
                otherFeatures.setError(null); // Remove error if "Other Features" is filled
            }
        } else {
            otherFeatures.setError(null); // Remove error if any checkbox is selected
        }

        // If validation passes
        return true;
    }
    private boolean validateStep3() {
        // Get the values entered by the user
        String pickupLocationText = pickupLocation.getText().toString().trim();
        String dropoffLocationText = dropoffLocation.getText().toString().trim();
        String dailyRentalPriceText = dailyRentalPrice.getText().toString().trim();

        // Validate Pickup Location
        if (pickupLocationText.isEmpty()) {
            pickupLocation.setError("Please enter a pickup location.");
            return false; // Stop the process if validation fails
        } else {
            pickupLocation.setError(null); // Remove error if field is valid
        }

        // Validate Dropoff Location
        if (dropoffLocationText.isEmpty()) {
            dropoffLocation.setError("Please enter a dropoff location.");
            return false;
        } else {
            dropoffLocation.setError(null); // Remove error if field is valid
        }

        // Validate Daily Rental Price (should be a valid number)
        if (dailyRentalPriceText.isEmpty()) {
            dailyRentalPrice.setError("Please enter a daily rental price.");
            return false;
        } else {
            try {
                double price = Double.parseDouble(dailyRentalPriceText);
                if (price <= 0) {
                    dailyRentalPrice.setError("Price must be greater than zero.");
                    return false;
                }
            } catch (NumberFormatException e) {
                dailyRentalPrice.setError("Please enter a valid number for the price.");
                return false;
            }
        }

        // If all validations pass
        return true;
    }

    private boolean validateStep4() {
        if (selectedImageParts == null || selectedImageParts.length == 0) {
            Toast.makeText(this, "Please upload all car images before proceeding.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Ensure each image part in the array is valid
        for (MultipartBody.Part imagePart : selectedImageParts) {
            if (imagePart == null) {
                Toast.makeText(this, "Please upload all car images before proceeding.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    // Submit form logic (final step)
    private void submitForm() {
        // Validate that all required images are uploaded
        if (!validateImageUploads()) {
            return;  // Stop the form submission if validation fails
        }
        AddCarRequest request = new AddCarRequest();
        // Set fields from user input...
        request.setCarName(etCarName.getText().toString().trim());
        request.setCarModel(etCarModel.getText().toString().trim());
        request.setCarColor(etCarColor.getText().toString().trim());
        request.setCarYear(Integer.parseInt(etCarYear.getSelectedItem().toString()));

        try {
            request.setCarMileagePerHour(Integer.parseInt(etCarMileagePerHour.getText().toString().trim()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid Car Mileage", Toast.LENGTH_SHORT).show();
            return;
        }

        request.setCarDescription(etCarDescription.getText().toString().trim());
        request.setRegistrationNumber(etRegistrationNumber.getText().toString().trim());
        request.setSubcategory(etSubcategory.getSelectedItem().toString().trim());
        request.setCategory(spinnerCategory.getSelectedItem().toString());
        request.setSeatingCapacity(Integer.parseInt(spinnerSeatingCapacity.getSelectedItem().toString()));
        request.setFuelType(spinnerFuelType.getSelectedItem().toString());
        request.setTransmissionType(spinnerTransmissionType.getSelectedItem().toString());
        request.setDailyRentalPrice(Double.parseDouble(dailyRentalPrice.getText().toString().trim()));

        List<String> features = new ArrayList<>();
        if (airConditioning.isChecked()) features.add("Air Conditioning");
        if (gps.isChecked()) features.add("GPS");
        if (bluetooth.isChecked()) features.add("Bluetooth");
        if (childSeat.isChecked()) features.add("Child Seat");
        if (!otherFeatures.getText().toString().isEmpty()) features.add(otherFeatures.getText().toString());

        request.setFeatures(features);
        request.setPickupLocation(pickupLocation.getText().toString().trim());
        request.setDropoffLocation(dropoffLocation.getText().toString().trim());

        Gson gson = new Gson();
        String carDetailsJson = gson.toJson(request);
        RequestBody carDetailsBody = RequestBody.create(MediaType.parse("application/json"), carDetailsJson);

        // Add all images to the API call
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (MultipartBody.Part imagePart : selectedImageParts) {
            if (imagePart != null) {
                imageParts.add(imagePart);
            }
        }

        if (imageParts.isEmpty()) {
            Toast.makeText(this, "Please upload at least one image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send images in separate lists for each document category
        List<MultipartBody.Part> imageParts1 = new ArrayList<>();
        List<MultipartBody.Part> imageParts2 = new ArrayList<>();
        List<MultipartBody.Part> imageParts3 = new ArrayList<>();
        List<MultipartBody.Part> imageParts4 = new ArrayList<>();

        // Add images to respective lists
        for (MultipartBody.Part imagePart : selectedImageParts1) {
            if (imagePart != null) {
                imageParts1.add(imagePart);
            }
        }

        for (MultipartBody.Part imagePart : selectedImageParts2) {
            if (imagePart != null) {
                imageParts2.add(imagePart);
            }
        }

        for (MultipartBody.Part imagePart : selectedImageParts3) {
            if (imagePart != null) {
                imageParts3.add(imagePart);
            }
        }

        for (MultipartBody.Part imagePart : selectedImageParts4) {
            if (imagePart != null) {
                imageParts4.add(imagePart);
            }
        }
        showProgress(true);

        apiService.addCarWithImages(carDetailsBody, imageParts,imageParts1,imageParts2,imageParts3,imageParts4).enqueue(new Callback<AddCarResponse>() {
            @Override
            public void onResponse(Call<AddCarResponse> call, Response<AddCarResponse> response) {
                showProgress(false);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddCarActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().isSuccess()) {
                        Toast.makeText(AddCarActivity.this, "success", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddCarActivity.this, PartnerDashboardActivity.class);
                        intent.putExtra("tab", "MyCars");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AddCarActivity.this, "Failed to add car", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddCarResponse> call, Throwable t) {
                showProgress(false);
                Log.d("mytag","myerror"+t.getMessage());
                Toast.makeText(AddCarActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open image gallery to pick images
    private void openImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // Check image size
                    Cursor returnCursor = getContentResolver().query(selectedImageUri, null, null, null, null);
                    if (returnCursor != null) {
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        long imageSize = returnCursor.getLong(sizeIndex);
                        returnCursor.close();

                        // Validate image size
                        if (imageSize > 150 * 1024) { // 150KB in bytes
                            Toast.makeText(this, "Image size exceeds 150KB. Please select a smaller image.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    // Decode the selected image to a bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                    // Handle PICK_IMAGE_REQUEST
                    if (requestCode == PICK_IMAGE_REQUEST) {
                        selectedImageViews[currentImageIndex].setImageBitmap(bitmap);
                        selectedImageParts[currentImageIndex] = prepareImageFilePart(selectedImageUri, "image" + currentImageIndex);
                    }

                    // Handle other request codes
                    if (requestCode == REQUEST_FRONT_PHOTO) {
                        idfrontPhoto.setImageBitmap(bitmap);
                        idfrontPhotoUri = selectedImageUri;
                        selectedImageParts1[0] = prepareImageFilePart(selectedImageUri, "idfront");
                    } else if (requestCode == REQUEST_BACK_PHOTO) {
                        idbackPhoto.setImageBitmap(bitmap);
                        idbackPhotoUri = selectedImageUri;
                        selectedImageParts1[1] = prepareImageFilePart(selectedImageUri, "idback");
                    } else if (requestCode == REQUEST_FRONT_PHOTO_DOCUMENT) {
                        cardocumentfrontPhoto.setImageBitmap(bitmap);
                        cardocumentfrontPhotoUri = selectedImageUri;
                        selectedImageParts2[0] = prepareImageFilePart(selectedImageUri, "cardocumentfront");
                    } else if (requestCode == REQUEST_BACK_PHOTO_DOCUMENT) {
                        cardocumentbackPhoto.setImageBitmap(bitmap);
                        cardocumentbackPhotoUri = selectedImageUri;
                        selectedImageParts2[1] = prepareImageFilePart(selectedImageUri, "cardocumentback");
                    } else if (requestCode == REQUEST_FRONT_PHOTO_VECHILE) {
                        vechilelicensefrontPhoto.setImageBitmap(bitmap);
                        vechilelicensefrontPhotoUri = selectedImageUri;
                        selectedImageParts3[0] = prepareImageFilePart(selectedImageUri, "vechilelicensefront");
                    } else if (requestCode == REQUEST_BACK_PHOTO_VECHILE) {
                        vechileicensebackPhoto.setImageBitmap(bitmap);
                        vechilelicensebackPhotoUri = selectedImageUri;
                        selectedImageParts3[1] = prepareImageFilePart(selectedImageUri, "vechilelicenseback");
                    } else if (requestCode == REQUEST_FRONT_PHOTO_BANK) {
                        bankpassbookPhoto.setImageBitmap(bitmap);
                        bankpassbookPhotoUri = selectedImageUri;
                        selectedImageParts4[1] = prepareImageFilePart(selectedImageUri, "bankpassbookphoto");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No image selected. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateImageUploads() {
        // Check if all car images have been uploaded


        // Check if each image has been uploaded
        if (idfrontPhotoUri == null) {
            Toast.makeText(this, "Please upload the front ID photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idbackPhotoUri == null) {
            Toast.makeText(this, "Please upload the back ID photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cardocumentfrontPhotoUri == null) {
            Toast.makeText(this, "Please upload the front car ownership document photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cardocumentbackPhotoUri == null) {
            Toast.makeText(this, "Please upload the back car ownership document photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (vechilelicensefrontPhotoUri == null) {
            Toast.makeText(this, "Please upload the front vehicle license photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (vechilelicensebackPhotoUri == null) {
            Toast.makeText(this, "Please upload the back vehicle license photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bankpassbookPhotoUri == null) {
            Toast.makeText(this, "Please upload the bank passbook photo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Prepare image file part for upload
    private MultipartBody.Part prepareImageFilePart(Uri imageUri, String partName) {
        File file = new File(FileUtils.getPath(this, imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    // Method to handle showing and hiding progress bar with overlay
    private void showProgress(boolean show) {
        if (show) {
            progressOverlay.setVisibility(View.VISIBLE); // Show overlay
            progressBar.setVisibility(View.VISIBLE); // Show progress bar
        } else {
            progressOverlay.setVisibility(View.GONE); // Hide overlay
            progressBar.setVisibility(View.GONE); // Hide progress bar
        }
    }

}