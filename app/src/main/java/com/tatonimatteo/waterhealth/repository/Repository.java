package com.tatonimatteo.waterhealth.repository;

import androidx.lifecycle.LiveData;

import com.tatonimatteo.waterhealth.api.exception.DataException;

public interface Repository {
    LiveData<DataException> getError();

    LiveData<Boolean> isLoading();

}
