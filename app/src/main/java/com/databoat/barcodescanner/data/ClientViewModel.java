package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClientViewModel extends AndroidViewModel {

    private ClientRepository clientRepository;
    private LiveData<List<Client>> allClient;

    public ClientViewModel(@NonNull Application application) {
        super(application);
        clientRepository = new ClientRepository(application);
        allClient = clientRepository.getAllClient();
    }

    public void insert(Client client) {
        clientRepository.insert(client);
    }

    public LiveData<List<Client>> getAllClient() {
        return  allClient;
    }

    public LiveData<Client> getClientByIdst(String id) {
        return clientRepository.getClientById(id);
    }

    public LiveData<List<Client>> getClientList() {
        return clientRepository.getClientList();
    }

}
