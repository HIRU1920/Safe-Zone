package com.android.safezone.Model

import com.android.safezone.Utility.ItemStatus

data class TimeLineModel(
    val heading: String,
    var text: String,
    var status: ItemStatus
)