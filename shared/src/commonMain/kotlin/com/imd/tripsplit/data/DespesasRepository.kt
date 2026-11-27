package com.imd.tripsplit.data

import com.imd.tripsplit.model.Despesa
import kotlinx.coroutines.flow.Flow

interface DespesasRepository {
    fun observarDespesas(tripId: String): Flow<List<Despesa>>

    suspend fun buscarPorId(id: String): Despesa?

    suspend fun adicionar(despesa: Despesa)

    suspend fun atualizar(despesa: Despesa)

    suspend fun excluir(id: String)
}
