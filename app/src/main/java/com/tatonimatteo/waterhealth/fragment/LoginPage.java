package com.tatonimatteo.waterhealth.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.configuration.AppConfiguration;
import com.tatonimatteo.waterhealth.configuration.LoginCallback;

public class LoginPage extends Fragment {

    private NavController nav;
    private AppConfiguration configuration;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configuration = AppConfiguration.getInstance();
        nav = Navigation.findNavController(view);
        login();
    }

    private void login() {
        configuration
                .getAuthController()
                .login("waterapp@example.com", "app123", new LoginCallback() {
                    @Override
                    public void onSuccess() {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> next(), 1000);

                    }

                    @Override
                    public void onFailure(String message) {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> configuration
                                .getPopUp()
                                .showErrorPopup(
                                        "Connessione con il Server fallita!",
                                        message,
                                        "Riprova", (dialog, which) -> login(),
                                        "Esci", (dialog, which) -> requireActivity().finishAffinity(),
                                        false
                                ), 500);

                    }
                });
    }

    private void next() {
        if (nav.getCurrentBackStack().getValue().size() > 2) {
            nav.navigateUp();
        } else {
            nav.navigate(R.id.action_loginPage_to_stations);
        }
    }
}