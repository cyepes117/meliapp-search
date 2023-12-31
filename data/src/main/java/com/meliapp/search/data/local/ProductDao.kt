package com.meliapp.search.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meliapp.search.data.local.entities.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM product WHERE title LIKE :productName")
    suspend fun getProductList(productName: String): List<ProductEntity>?

    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProductDetails(productId: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProductList(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProductDetails(product: ProductEntity)
}
