package com.bloxbean.cardano.client.backend.ogmios.websocket;

import com.bloxbean.cardano.client.api.exception.ApiRuntimeException;
import com.bloxbean.cardano.client.backend.api.*;
import io.adabox.client.OgmiosWSClient;

import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class Ogmios5BackendService implements BackendService {

    private final OgmiosWSClient wsClient;

    protected Ogmios5BackendService() {
        wsClient = null;
    }

    public Ogmios5BackendService(String url) {
        try {
            wsClient = new OgmiosWSClient(new URI(url));
            if (url.startsWith("wss://")) {
                wsClient.setSocketFactory(SSLSocketFactory.getDefault());
            }
            wsClient.connectBlocking(20, TimeUnit.SECONDS);
        } catch (URISyntaxException e) {
            throw new ApiRuntimeException("Invalid Ogmios URL: ", e);
        } catch (InterruptedException e) {
            throw new ApiRuntimeException(e);
        }
    }

    @Override
    public TransactionService getTransactionService() {
        return new Ogmios5TransactionService(wsClient);
    }

    @Override
    public AssetService getAssetService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public BlockService getBlockService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NetworkInfoService getNetworkInfoService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public PoolService getPoolService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public UtxoService getUtxoService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public AddressService getAddressService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public AccountService getAccountService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public EpochService getEpochService() {
        return new Ogmios5EpochService(wsClient);
    }

    @Override
    public MetadataService getMetadataService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public ScriptService getScriptService() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public GovernanceService getGovernanceService() {
        throw new UnsupportedOperationException("Not supported yet");
    }
}
