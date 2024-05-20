package com.system.lsp.sync;

public interface SyncHandlerListener {
    void onSuccess();
    void onFailure(String msg);
}
