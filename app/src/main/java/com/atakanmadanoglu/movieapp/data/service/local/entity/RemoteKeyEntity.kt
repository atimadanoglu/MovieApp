package com.atakanmadanoglu.movieapp.data.service.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: Int = 0,
    val prevKey: Int?,
    val nextKey: Int?
)

