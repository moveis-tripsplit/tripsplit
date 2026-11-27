package com.imd.tripsplit.data

import com.imd.tripsplit.model.Despesa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object DespesasRepositoryImpl : DespesasRepository {
    private val despesas = MutableStateFlow<List<Despesa>>(emptyList())

    override fun observarDespesas(tripId: String): Flow<List<Despesa>> {
        return despesas.map { lista ->
            lista.filter { it.tripId == tripId }
        }
    }

    override suspend fun buscarPorId(id: String): Despesa? {
        return despesas.value.find { it.id == id }
    }

    override suspend fun adicionar(despesa: Despesa) {
        despesas.update { lista ->
            lista + despesa
        }
    }

    override suspend fun atualizar(despesa: Despesa) {
        despesas.update { lista ->
            lista.map {
                if (it.id == despesa.id) despesa else it
            }
        }
    }

    override suspend fun excluir(id: String) {
        despesas.update { lista ->
            lista.filterNot { it.id == id }
        }
    }
}
