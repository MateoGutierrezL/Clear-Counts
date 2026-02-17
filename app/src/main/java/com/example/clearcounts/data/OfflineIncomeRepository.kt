package com.example.clearcounts.data

import com.example.clearcounts.data.database.dao.IncomeDao
import com.example.clearcounts.data.database.entities.IncomeEntity
import javax.inject.Inject

class OfflineIncomeRepository @Inject constructor(

    private val incomeDao: IncomeDao
): IncomeRepository {

    override suspend fun insertIncome(incomeEntity: IncomeEntity) = incomeDao.insert(incomeEntity)

}