package com.fiknaufalh.snapstory.data.remote.responses

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
