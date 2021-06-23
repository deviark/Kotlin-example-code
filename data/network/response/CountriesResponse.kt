package com.lotteryadviser.data.network.response

import com.lotteryadviser.data.network.model.CountryJson
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CountriesResponse(
    data: List<CountryJson>
): BaseResponse<List<CountryJson>>(data)