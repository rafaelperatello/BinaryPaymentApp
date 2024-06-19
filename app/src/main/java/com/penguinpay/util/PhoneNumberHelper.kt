package com.penguinpay.util

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javax.inject.Inject


interface PhoneNumberHelper {

    fun isNumberValid(phone: String, country: Country): Boolean

}

class PhoneNumberHelperImpl @Inject constructor(
    private val phoneNumberUtil: PhoneNumberUtil
) : PhoneNumberHelper {

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