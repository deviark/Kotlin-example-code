package com.lotteryadviser.data.network.response

import com.lotteryadviser.data.network.model.LotteryJson
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LotteriesResponse(
    data: List<LotteryJson>
) : BaseResponse<List<LotteryJson>>(data)