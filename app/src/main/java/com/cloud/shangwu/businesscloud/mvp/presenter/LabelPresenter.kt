package com.cloud.shangwu.businesscloud.mvp.presenter

import com.cloud.shangwu.businesscloud.base.BasePresenter
import com.cloud.shangwu.businesscloud.constant.Constant
import com.cloud.shangwu.businesscloud.http.RetrofitHelper
import com.cloud.shangwu.businesscloud.http.exception.ExceptionHandle
import com.cloud.shangwu.businesscloud.http.function.RetryWithDelay
import com.cloud.shangwu.businesscloud.mvp.contract.LabelContract
import com.cloud.shangwu.businesscloud.mvp.model.LabelModel
import com.cloud.shangwu.businesscloud.mvp.rx.SchedulerUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by chenxz on 2018/6/3.
 */
class LabelPresenter : BasePresenter<LabelContract.View>(), LabelContract.Presenter {
    override fun upload(file: File) {
        val descriptionString = "hello, this is description speaking"
        RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        val body1 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, body1)
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
        RetrofitHelper.service.uploadImage(description, body)
                .compose(SchedulerUtils.ioToMain()).subscribe({ results ->
                    mView?.let {
                        if (results.code != Constant.OK) {
                            it.showError(results.message)
                            it.JsonDateErr()
                        } else {
                            it.JsonDateOk("")
                        }
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
    }

    private val labelModel by lazy {
        LabelModel()
    }

    override fun label(countryId  :Int,type: Int,content :String) {

        val disposable = labelModel.label(countryId,type,content)
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.apply {
                        if (results.code != Constant.OK) {
                            showError(results.message)
                            labelFail()
                        } else {
                            labelSuccess(results.data)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }


}