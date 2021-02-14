package com.hajma.apps.mina2.retrofit;

import com.hajma.apps.mina2.C;

public class ApiUtils {

    public static MinaInterface getMinaInterface() {
        return RetrofitClient.getClient(C.BASE_URL)
                .create(MinaInterface.class);
    }


}
