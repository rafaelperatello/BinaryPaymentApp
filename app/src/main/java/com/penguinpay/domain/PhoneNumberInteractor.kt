package com.penguinpay.domain

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javax.inject.Inject


interface PhoneNumberInteractor {

    fun isNumberValid(phone: String, country: Country): Boolean

}

class PhoneNumberInteractorImpl @Inject constructor(
    private val phoneNumberUtil: PhoneNumberUtil
) : PhoneNumberInteractor {

    override fun isNumberValid(phone: String, country: Country): Boolean {
        try {
            val phoneNumber = phoneNumberUtil.parse(phone, country.locale.country)
            return phoneNumberUtil.isValidNumber(phoneNumber)
        } catch (e: NumberParseException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return false
    }
}