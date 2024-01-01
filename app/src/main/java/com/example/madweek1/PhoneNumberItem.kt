package com.example.madweek1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class PhoneNumberItem(var id: Int, var name: String, var number: String) : Parcelable
