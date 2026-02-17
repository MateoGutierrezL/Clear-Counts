package com.example.clearcounts.data

import com.example.clearcounts.data.database.entities.IncomeEntity

interface IncomeRepository {

    suspend fun insertIncome(incomeEntity: IncomeEntity)

}