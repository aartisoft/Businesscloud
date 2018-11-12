package com.cloud.shangwu.businesscloud.mvp.contract

import com.cloud.shangwu.businesscloud.base.IPresenter
import com.cloud.shangwu.businesscloud.base.IView
import com.cloud.shangwu.businesscloud.mvp.model.bean.LoginData

interface RegisterPersonalContract {

    interface  View :IView{
        fun  registerOK(data: LoginData)
        fun  registerFail()
    }

    interface Persenter :IPresenter<View>{
        fun userRegister(usernme:String
                           ,password:String
                           ,email:String
                           ,invitedCode:String,
                           portrait :String,
                           hobbys :String,
                           label :String)
    }

}