package com.meliapp.search.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnail: String,
    val price: Double
)
