package com.tatonimatteo.waterhealth.repository;

import androidx.lifecycle.LiveData;

public interface Repository {
    LiveData<Throwable> getError();
    LiveData<Boolean> isLoading();

}
