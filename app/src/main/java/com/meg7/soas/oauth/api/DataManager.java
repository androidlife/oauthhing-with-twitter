package com.meg7.soas.oauth.api;

/**
 * A singleton class ,that directly calls the necessary classes for
 * data fetch. All the app communicates with this through {@link DataCallback}.
 * So the app doesn't need to know what it implements. Right now {@link retrofit2.Retrofit}
 * is implemented, later this class can be modified to handle any other API libraries
 */
public class DataManager {
    static volatile DataManager dataManager;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (dataManager == null) {
            synchronized (DataManager.class) {
                if (dataManager == null)
                    dataManager = new DataManager();
            }
        }
        return dataManager;
    }
}
